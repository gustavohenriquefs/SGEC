package com.casaculturaqxd.sgec.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Button;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.ServiceFileDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MidiaEventoController {
    private Stage stage;
    @FXML
    private VBox root;
    @FXML
    private FlowPane filesContainer;
    @FXML
    private Button imagens;
    @FXML
    private TextField filtroNomeArquivo;

    private DatabasePostgres db = DatabasePostgres.getInstance("URL_TEST",
            "USER_NAME_TEST", "PASSWORD_TEST");
    private ServiceFileDAO serviceFileDAO = new ServiceFileDAO(db.getConnection());
    private Evento evento;
@FXML
// map contendo todos os service files de um evento como chave e os
FXMLLoaders
// de preview arquivo como valores
private ObservableMap<ServiceFile, FXMLLoader> mapArquivos =
FXCollections.observableHashMap();
    // map usado para mostrar apenas os tipos de arquivos filtrados, os filtros
sao
// aplicados tanto na pesquisa pelo nome do arquivo quanto pelo tipo
selecionado
    // na secoes do lado esquerdo da tela
    private ObservableMap<ServiceFile, FXMLLoader> filteredMap = FXCollections.observableHashMap();

    public void initialize() throws IOException {
        loadMenu();
        addListenerArquivo(filteredMap);
        // Imagens são carregadas inicialmente
        filterArquivos(arquivo -> isImage(arquivo));
    }

    public void loadMenu() throws IOException {
        FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        root.getChildren().add(0, carregarMenu.load());
    }

    public void adicionarArquivo(File arquivoSelecionado) {
        try {
            ServiceFileDAO serviceFileDAO = new ServiceFileDAO();
            serviceFileDAO.inserirArquivo(new ServiceFile(arquivoSelecionado, null));
            eventoDAO.vincularArquivo();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void selectNovoArquivo() {
        FileChooser fileChooser = new FileChooser();
        adicionarArquivo(fileChooser.showOpenDialog(stage));
    }

    public void swapToImagens() {
        filteredMap = filterArquivos(arquivo -> isImage(arquivo));
    }

    public void swapToVideos() {
        filteredMap = filterArquivos(arquivo -> isVideo(arquivo));
    }

    public void swapToAudios() {
        filteredMap = filterArquivos(arquivo -> isAudio(arquivo));
    }

    public void swapToDocumentos() {
        filteredMap = filterArquivos(arquivo -> isDocument(arquivo));
    }

    // retorna um novo map utilizando o filtro passado no map de arquivos
    private ObservableMap<ServiceFile, FXMLLoader> filterArquivos(Predicate<ServiceFile> predicate) {
        return FXCollections
                .observableMap(mapArquivos.entrySet().stream().filter(entry -> predicate.test(entry.getKey()))
                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
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
String mimeType =a
URLConnection.guessContentTypeFromName(serviceFile.getFileKey());
return mimeType.startsWith("application/pdf");
}

public void setEvento(Evento evento) {
this.evento = evento;
for(ServiceFile serviceFile : serviceFileDAO.listarArquivosEvento()){
mapArquivos.put(serviceFile, new
FXMLLoader(App.class.getResource("view/preview/cardArquivo.fxml")))
}
}

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addListenerArquivo(ObservableMap<ServiceFile, FXMLLoader> observablemap) {
        observablemap.addListener(new MapChangeListener<ServiceFile, FXMLLoader>() {
            @Override
            public void onChanged(
                    MapChangeListener.Change<? extends ServiceFile, ? extends FXMLLoader> change) {
                if (change.wasAdded()) {
                    ServiceFile addedKey = change.getKey();

                    try {
                        Parent previewServiceFile = change.getValueAdded().load();
                        PreviewArquivoController controller = change.getValueAdded().getController();
                        controller.setServiceFile(addedKey);
                        filesContainer.getChildren().add(previewServiceFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (change.wasRemoved()) {
                    PreviewArquivoController removedController = change.getValueRemoved().getController();
                    filesContainer.getChildren().remove(removedController.getContainer());
                }
            }
        });
    }
}
