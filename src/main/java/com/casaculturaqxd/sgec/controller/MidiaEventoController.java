package com.casaculturaqxd.sgec.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.comparator.ComparatorServiceFileContext;
import com.casaculturaqxd.sgec.comparator.ComparatorServiceFileDate;
import com.casaculturaqxd.sgec.comparator.ComparatorServiceFileKey;
import com.casaculturaqxd.sgec.comparator.ComparatorServiceFileSize;
import com.casaculturaqxd.sgec.comparator.ComparatorServiceFileStrategy;
import com.casaculturaqxd.sgec.controller.preview.PreviewArquivoController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MidiaEventoController implements ControllerServiceFile {
    private Stage stage;
    @FXML
    private VBox root;
    @FXML
    private FlowPane filesContainer;
    @FXML
    private ImageView arrowNome, arrowData, arrowTamanho;
    @FXML
    private Button imagens;
    @FXML
    private TextField filtroNomeArquivo;

    private DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    private ServiceFileDAO serviceFileDAO;
    private File ultimoDiretorioAcessado;
    private Evento evento;
    // lista contendo os controllers de todos os arquivos
    private ObservableList<PreviewArquivoController> controllersPreviews = FXCollections.observableArrayList();
    // Context que realiza ordenacoes usando diferentes strategies
    private ComparatorServiceFileContext comparatorContext;
    // propriedades que definem se a ordenacao eh reversa ou nao
    private BooleanProperty nomeProperty, dataCriacaoProperty, tamanhoProperty;

    public void initialize() throws IOException {
        nomeProperty = new SimpleBooleanProperty();
        dataCriacaoProperty = new SimpleBooleanProperty();
        tamanhoProperty = new SimpleBooleanProperty();
        serviceFileDAO = new ServiceFileDAO(db.getConnection());
        comparatorContext = new ComparatorServiceFileContext();
        loadMenu();
        bindArrows();
    }

    public void loadMenu() throws IOException {
        FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    /**
     * abre o explorador de arquivos no diretorio raiz do usuario, em uma segunda
     * chamada abre o ultimo diretorio aberto pelo usuario, chama a operacao de
     * adicionar o arquivo arquivoSelecionado
     */
    public void selectNovoArquivo() throws IOException {
        FileChooser fileChooser = new FileChooser();
        if (ultimoDiretorioAcessado != null) {
            fileChooser.setInitialDirectory(ultimoDiretorioAcessado);
        }
        File arquivoSelecionado = fileChooser.showOpenDialog(stage);
        if (arquivoSelecionado.getParentFile().isDirectory()) {
            ultimoDiretorioAcessado = arquivoSelecionado.getParentFile();
        }
        adicionarArquivo(new ServiceFile(arquivoSelecionado));
    }

    // filtra os resultados mostrados atualmente na tela de acordo com o nome
    // pesquisado
    public void pesquisarArquivo() {
        Predicate<PreviewArquivoController> lambdaPreviewCarregado = entry -> filesContainer.getChildren()
                .contains(entry.getRoot());
        Predicate<PreviewArquivoController> lambdaNomePesquisado = entry -> entry.getServiceFile().getFileKey()
                .contains(filtroNomeArquivo.getText());

        ObservableList<Parent> filteredByName = FXCollections.observableArrayList();
        for (PreviewArquivoController controller : controllersPreviews
                .filtered(lambdaPreviewCarregado.and(lambdaNomePesquisado))) {
            filteredByName.add(controller.getRoot());
        }

        filesContainer.getChildren().setAll(filteredByName);
    }

    public void orderByNome() {
        applyOrdering(new ComparatorServiceFileKey(), nomeProperty);
    }

    public void orderByDataCriacao() {
        applyOrdering(new ComparatorServiceFileDate(), dataCriacaoProperty);
    }

    public void orderByTamanho() {
        applyOrdering(new ComparatorServiceFileSize(), tamanhoProperty);
    }

    public void swapToImagens() {
        filterArquivos(serviceFile -> isImage(serviceFile));
    }

    public void swapToVideos() {
        filterArquivos(serviceFile -> isVideo(serviceFile));
    }

    public void swapToAudios() {
        filterArquivos(serviceFile -> isAudio(serviceFile));
    }

    public void swapToDocumentos() {
        filterArquivos(serviceFile -> isDocument(serviceFile));
    }

    public void swapToOutros() {
        filterArquivos(serviceFile -> isOutros(serviceFile));
    }

    /**
     * carrega a secao especifica na tela dependendo do tipo do arquivo passado,
     * chama os mesmos metodos swapTo que sao usados pelos buttons da interface
     */
    private void goToSecaoArquivo(ServiceFile serviceFile) {
        if (isImage(serviceFile)) {
            swapToImagens();
        } else if (isVideo(serviceFile)) {
            swapToVideos();
        } else if (isAudio(serviceFile)) {
            swapToAudios();
        } else if (isDocument(serviceFile)) {
            swapToDocumentos();
        } else if (isOutros(serviceFile)) {
            swapToOutros();
        }
    }

    /**
     * mostra somente os arquivos do tipo especificado no predicado
     */
    private void filterArquivos(Predicate<ServiceFile> predicate) {
        filesContainer.getChildren().clear();
        for (PreviewArquivoController controllerPreview : controllersPreviews) {
            if (predicate.test(controllerPreview.getServiceFile())) {
                filesContainer.getChildren().add(controllerPreview.getRoot());
            }
        }
    }

    // ordena os previews presentes na tela seguindo alguma das strategies
    // existentes, a propriedade vinculada define se a ordem eh reversa
    private void applyOrdering(ComparatorServiceFileStrategy comparator, BooleanProperty booleanProperty) {
        comparatorContext.setStrategy(comparator);
        booleanProperty.set(booleanProperty.get() == false);
        // filtrando entre todos os controllers apenas os que tem sua root na tela,
        // depois ordena usando a estrategia passada
        SortedList<PreviewArquivoController> orderedControllers = controllersPreviews
                .filtered(controller -> filesContainer.getChildren().contains(controller.getRoot()))
                .sorted((oneController, otherController) -> {
                    return comparatorContext.compare(oneController.getServiceFile(), otherController.getServiceFile(),
                            booleanProperty.get());
                });

        ObservableList<Parent> orderedPreviews = orderedControllers.stream().map(PreviewArquivoController::getRoot)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        filesContainer.getChildren().setAll(orderedPreviews);
    }

    private boolean isImage(ServiceFile serviceFile) {
        String mimeType = URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
        return mimeType != null ? mimeType.startsWith("image") : false;
    }

    private boolean isVideo(ServiceFile serviceFile) {
        String mimeType = URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
        return mimeType != null ? mimeType.startsWith("video") : false;
    }

    private boolean isAudio(ServiceFile serviceFile) {
        String mimeType = URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
        return mimeType != null ? mimeType.startsWith("audio") : false;
    }

    private boolean isDocument(ServiceFile serviceFile) {
        String mimeType = URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
        return mimeType != null ? mimeType.startsWith("application/pdf") : false;
    }

    private boolean isOutros(ServiceFile serviceFile) {
        return !isImage(serviceFile) && !isVideo(serviceFile) && !isAudio(serviceFile) && !isDocument(serviceFile);
    }

    // seta o evento da pagina e adiciona um novo controller contendo o preview e o
    // arquivo na lista
    public void setEvento(Evento evento) throws IOException, SQLException {
        this.evento = evento;
        for (ServiceFile serviceFile : serviceFileDAO.listarArquivosEvento(evento)) {
            try {
                addPreviewArquivo(serviceFile);
            } catch (IOException e) {
                throw new IOException("falha ao carregar preview:", e);
            }
        }
        swapToImagens();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void adicionarArquivo(ServiceFile serviceFile) throws IOException {
        try {
            Optional<ServiceFile> checkArquivoJaExiste = serviceFileDAO.getArquivo(serviceFile.getFileKey());
            if (checkArquivoJaExiste.isPresent()) {
                serviceFile = checkArquivoJaExiste.get();
            } else {
                serviceFileDAO.inserirArquivo(serviceFile);
            }
            serviceFileDAO.vincularArquivo(serviceFile.getServiceFileId(), evento.getIdEvento());
            addPreviewArquivo(serviceFile);
            goToSecaoArquivo(serviceFile);
        } catch (SQLException | IllegalArgumentException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), new ButtonType("Ok", ButtonData.CANCEL_CLOSE));
            alert.showAndWait();
        }
    }

    @Override
    public void removerArquivo(ServiceFile serviceFile) {
        try {
            serviceFileDAO.desvincularArquivo(serviceFile.getServiceFileId(), evento.getIdEvento());
            Iterator<PreviewArquivoController> iteratorPreviews = controllersPreviews.iterator();
            while (iteratorPreviews.hasNext()) {
                PreviewArquivoController controllerArquivoRemovido = iteratorPreviews.next();
                if (controllerArquivoRemovido.getServiceFile().equals(serviceFile)) {
                    iteratorPreviews.remove();
                    filesContainer.getChildren().remove(controllerArquivoRemovido.getRoot());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // adiciona o controller do novo arquivo a lista de controllers
    // levanta uma excecao caso ja exista um arquivo com o mesmo nome
    private PreviewArquivoController addPreviewArquivo(ServiceFile serviceFile) throws IOException, SQLException {
        FXMLLoader loaderPreviewArquivo = new FXMLLoader(App.class.getResource("view/preview/previewArquivo.fxml"));
        loaderPreviewArquivo.load();
        PreviewArquivoController previewArquivoController = loaderPreviewArquivo.getController();
        previewArquivoController.setParentController(this);
        previewArquivoController.setServiceFile(serviceFile);
        if (controllersPreviews.stream()
                .anyMatch(controller -> controller.getServiceFile().getFileKey().equals(serviceFile.getFileKey()))) {
            throw new IllegalArgumentException("Arquivo ja esta vinculado ao evento: " + serviceFile.getFileKey());
        } else {
            controllersPreviews.add(previewArquivoController);
            return previewArquivoController;
        }
    }

    // liga o sentido das setas nos campos de ordenacao com as propriedades
    // booleanas
    private void bindArrows() {
        rotateOnChange(nomeProperty, arrowNome);
        rotateOnChange(dataCriacaoProperty, arrowData);
        rotateOnChange(tamanhoProperty, arrowTamanho);
    }

    private void rotateOnChange(BooleanProperty booleanProperty, ImageView imageView) {
        booleanProperty.addListener((obs, oldValue, newValue) -> {
            imageView.setRotate(imageView.getRotate() + 180);
        });
    }
}
