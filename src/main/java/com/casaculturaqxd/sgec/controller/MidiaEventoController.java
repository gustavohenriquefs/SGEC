package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.net.URLConnection;
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
import javafx.collections.ObservableMap;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

    private DatabasePostgres db = DatabasePostgres.getInstance("URL",
            "USER_NAME", "PASSWORD");
    private ServiceFileDAO serviceFileDAO;
    private Evento evento;
    @FXML
    // map contendo todos os service files de um evento como chave e os FXMLLoaders
    // de preview arquivo como valores
    private ObservableMap<ServiceFile, FXMLLoader> mapArquivos = FXCollections.observableHashMap();
    // lista contendo os controllers de todos os arquivos
    private ObservableList<PreviewArquivoController> controllersPreviews = FXCollections.observableArrayList();
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

    public void selectNovoArquivo() {
        FileChooser fileChooser = new FileChooser();
        adicionarArquivo(new ServiceFile(fileChooser.showOpenDialog(stage)));
    }

    // filtra os resultados atuais de acordo com o nome pesquisado
    public void pesquisarArquivo() {
        Predicate<PreviewArquivoController> lambdaPreviewCarregado = entry -> filesContainer
                .getChildren().contains(entry.getRoot());
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

    // mostra somente os arquivos do tipo especificado no predicado,
    // chama o metodo load caso o controller do fxmlloader seja nulo
    private void filterArquivos(Predicate<ServiceFile> predicate) {
        filesContainer.getChildren().clear();
        for (ServiceFile serviceFile : mapArquivos.keySet()) {
            if (predicate.test(serviceFile)) {
                FXMLLoader loader = mapArquivos.get(serviceFile);
                PreviewArquivoController controller = loader.getController();
                try {
                    if (controller == null) {
                        loader.load();
                        controller = loader.getController();
                        controllersPreviews.add(controller);
                    }
                    controller.setServiceFile(serviceFile);
                    controller.setParentController(this);
                    filesContainer.getChildren().add(controller.getRoot());

                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        return !isImage(serviceFile) && !isVideo(serviceFile) &&
                !isAudio(serviceFile) && !isDocument(serviceFile);
    }

    // seta o evento da pagina e adiciona os arquivos dele ao mapa cd arquivos
    public void setEvento(Evento evento) {
        this.evento = evento;
        for (ServiceFile serviceFile : serviceFileDAO.listarArquivosEvento(evento)) {
            mapArquivos.putIfAbsent(serviceFile,
                    new FXMLLoader(App.class.getResource("view/preview/previewArquivo.fxml")));
        }
        swapToImagens();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void adicionarArquivo(ServiceFile serviceFile) {
        try {
            serviceFileDAO.inserirArquivo(serviceFile);
            serviceFileDAO.vincularArquivo(serviceFile.getServiceFileId(), evento.getIdEvento());

            mapArquivos.putIfAbsent(serviceFile,
                    new FXMLLoader(App.class.getResource("view/preview/previewArquivo.fxml")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removerArquivo(ServiceFile serviceFile) {
        try {
            serviceFileDAO.desvincularArquivo(serviceFile.getServiceFileId(), evento.getIdEvento());
        } catch (Exception e) {
            e.printStackTrace();
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
