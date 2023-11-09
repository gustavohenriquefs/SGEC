package com.casaculturaqxd.sgec.controller.preview;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

import com.casaculturaqxd.sgec.controller.ControllerServiceFile;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class PreviewArquivoController {
    private ServiceFile serviceFile;
    private ControllerServiceFile parentController;
    private Alert alertaArquivo;
    private ButtonType showFileOption, closeOption;
    private Stage stage;
    @FXML
    private Parent root;
    @FXML
    private ImageView imagemPreview;
    @FXML
    private Label fileKey, date, fileType, tamanho;
    @FXML
    private MenuItem downloadItem;

    public void initialize() {
        loadAlertDialog();
    }

    public void download() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar arquivo");
        fileChooser.getExtensionFilters()
                .add(new ExtensionFilter("Tipo de arquivo", "*" + serviceFile.getSuffix()));
        File downloadFile = fileChooser.showSaveDialog(stage);
        if (!writeFileContent(downloadFile)) {
            // mudando tipo de alerta e removendo opcao de visualizacao em caso de falha
            alertaArquivo.setAlertType(AlertType.WARNING);
            alertaArquivo.setContentText("Erro ao baixar arquivo");
            alertaArquivo.getButtonTypes().remove(showFileOption);
        }

        executeCloseDialogAction(alertaArquivo.showAndWait(), downloadFile);

    }

    private void executeCloseDialogAction(Optional<ButtonType> chosenButton, File result)
            throws IOException {
        if (chosenButton.isPresent()) {
            if (chosenButton.get() == showFileOption) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(result.getParentFile());
            }
        }

    }

    private boolean writeFileContent(File file) {
        try {
            InputStream readFromContent = new FileInputStream(serviceFile.getContent());
            OutputStream writeToFile = new FileOutputStream(file);
            readFromContent.transferTo(writeToFile);
            return true;
        } catch (FileNotFoundException exception) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private void loadAlertDialog() {
        // inicializando dialog de confirmacao com opcao de ver o arquivo usando o file
        // system do
        // sistema ou fechar o aviso
        showFileOption = new ButtonType("Visualizar", ButtonData.OK_DONE);
        closeOption = new ButtonType("Fechar", ButtonData.CANCEL_CLOSE);
        alertaArquivo = new Alert(AlertType.CONFIRMATION);
        alertaArquivo.setContentText("Arquivo baixado com sucesso");
        alertaArquivo.getButtonTypes().clear();
        alertaArquivo.getButtonTypes().addAll(showFileOption, closeOption);
    }

    private void loadContent() {
        if (serviceFile != null) {
            String dataArquivo = serviceFile.getUltimaModificacao() == null ? " "
                    : String.valueOf(serviceFile.getUltimaModificacao());
            fileKey.setText(serviceFile.getFileKey());
            tamanho.setText(formatFileSize(serviceFile.getContent().length()));
            date.setText(dataArquivo);
            fileType.setText(serviceFile.getSuffix());
        }
    }

    public void deletar() {
        parentController.removerArquivo(serviceFile);
    }

    public ServiceFile getServiceFile() {
        return serviceFile;
    }

    public void setServiceFile(ServiceFile serviceFile) {
        // opcao de download apenas para arquivos ja registrados
        if (serviceFile.getServiceFileId() == null) {
            downloadItem.setVisible(false);
            downloadItem.setDisable(true);
        }
        imagemPreview.setImage(null);
        this.serviceFile = serviceFile;
        loadContent();
    }

    public ControllerServiceFile getParentController() {
        return parentController;
    }

    public void setParentController(ControllerServiceFile parentController) {
        this.parentController = parentController;
    }

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    // recebe um tamanho de arquivo em bytes e retorna em KB, para tamanhos menores
    // que 1024 KB, se
    // nao retorna em MB
    private String formatFileSize(long fileSize) {
        long sizeInKB = fileSize / 1024;
        long sizeInMB = sizeInKB / 1024;
        return sizeInMB < 1 ? sizeInKB + " KB" : sizeInMB + " MB";
    }
}
