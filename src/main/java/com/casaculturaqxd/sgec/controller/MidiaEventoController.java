package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.comparator.ComparatorServiceFileContext;
import com.casaculturaqxd.sgec.comparator.ComparatorServiceFileDate;
import com.casaculturaqxd.sgec.comparator.ComparatorServiceFileKey;
import com.casaculturaqxd.sgec.comparator.ComparatorServiceFileSize;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import com.casaculturaqxd.sgec.controller.preview.PreviewArquivoController;

import javafx.animation.RotateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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
    // map contendo o conteudo carregado que e acessado a partir do seu controller
    private ObservableMap<PreviewArquivoController, Parent> mapPreviews = FXCollections.observableHashMap();
    private ComparatorServiceFileContext comparatorContext;
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

    }

    public void orderByNome() {
        comparatorContext.setStrategy(new ComparatorServiceFileKey());
        nomeProperty.set(nomeProperty.get() == false);
        ;
        ArrayList<Map.Entry<PreviewArquivoController, Parent>> entries = new ArrayList<>(mapPreviews.entrySet().stream()
                .filter(entry -> filesContainer.getChildren().contains(entry.getValue())).collect(Collectors.toList()));
        entries.sort((entry, otherEntry) -> {
            PreviewArquivoController controller = entry.getKey(), otherController = otherEntry.getKey();
            return comparatorContext.compare(controller.getServiceFile(), otherController.getServiceFile(),
                    nomeProperty.get());
        });

        ObservableList<Parent> sortedChildren = FXCollections.observableArrayList();
        for (Map.Entry<PreviewArquivoController, Parent> entry : entries) {
            sortedChildren.add(entry.getValue());
        }

        filesContainer.getChildren().setAll(sortedChildren);
    }

    public void orderByDataCriacao() {
        comparatorContext.setStrategy(new ComparatorServiceFileDate());
        dataCriacaoProperty.set(dataCriacaoProperty.get() == false);
        ArrayList<Map.Entry<PreviewArquivoController, Parent>> entries = new ArrayList<>(mapPreviews.entrySet().stream()
                .filter(entry -> filesContainer.getChildren().contains(entry.getValue())).collect(Collectors.toList()));
        entries.sort((entry, otherEntry) -> {
            PreviewArquivoController controller = entry.getKey(), otherController = otherEntry.getKey();
            return comparatorContext.compare(controller.getServiceFile(), otherController.getServiceFile(),
                    dataCriacaoProperty.get());
        });

        ObservableList<Parent> sortedChildren = FXCollections.observableArrayList();
        for (Map.Entry<PreviewArquivoController, Parent> entry : entries) {
            sortedChildren.add(entry.getValue());
        }

        filesContainer.getChildren().setAll(sortedChildren);
    }

    public void orderByTamanho() {
        comparatorContext.setStrategy(new ComparatorServiceFileSize());
        tamanhoProperty.set(tamanhoProperty.get() == false);
        ArrayList<Map.Entry<PreviewArquivoController, Parent>> entries = new ArrayList<>(mapPreviews.entrySet().stream()
                .filter(entry -> filesContainer.getChildren().contains(entry.getValue())).collect(Collectors.toList()));
        entries.sort((entry, otherEntry) -> {
            PreviewArquivoController controller = entry.getKey(), otherController = otherEntry.getKey();
            return comparatorContext.compare(controller.getServiceFile(), otherController.getServiceFile(),
                    tamanhoProperty.get());
        });

        ObservableList<Parent> sortedChildren = FXCollections.observableArrayList();
        for (Map.Entry<PreviewArquivoController, Parent> entry : entries) {
            sortedChildren.add(entry.getValue());
        }

        filesContainer.getChildren().setAll(sortedChildren);
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
    // se um fxml ja foi carregado
    private void filterArquivos(Predicate<ServiceFile> predicate) {
        filesContainer.getChildren().clear();
        for (ServiceFile serviceFile : mapArquivos.keySet()) {
            if (predicate.test(serviceFile)) {
                FXMLLoader loader = mapArquivos.get(serviceFile);
                PreviewArquivoController controller = loader.getController();
                try {
                    if (controller == null) {
                        Parent preview = loader.load();
                        controller = loader.getController();
                        mapPreviews.put(loader.getController(), preview);
                        controller = mapArquivos.get(serviceFile).getController();
                    }
                    controller.setServiceFile(serviceFile);
                    controller.setParentController(this);
                    filesContainer.getChildren().add(mapPreviews.get(controller));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isImage(ServiceFile serviceFile) {
        String mimeType = URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
        return mimeType.startsWith("image");
    }

    private boolean isVideo(ServiceFile serviceFile) {
        String mimeType = URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
        return mimeType.startsWith("video");
    }

    private boolean isAudio(ServiceFile serviceFile) {
        String mimeType = URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
        return mimeType.startsWith("audio");
    }

    private boolean isDocument(ServiceFile serviceFile) {
        String mimeType = URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
        URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
        return mimeType.startsWith("application/pdf");
    }

    private boolean isOutros(ServiceFile serviceFile) {
        return !isImage(serviceFile) && !isVideo(serviceFile) &&
                !isAudio(serviceFile) && !isDocument(serviceFile);
    }

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
