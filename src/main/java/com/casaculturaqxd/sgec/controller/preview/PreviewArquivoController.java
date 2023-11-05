package com.casaculturaqxd.sgec.controller.preview;

import com.casaculturaqxd.sgec.controller.ControllerServiceFile;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PreviewArquivoController {
    private ServiceFile serviceFile;
    private ControllerServiceFile ParentController;
    @FXML
    private Parent root;
    @FXML
    private ImageView imagemPreview;
    @FXML
    private Label fileKey, date, fileType, tamanho;

    public void initialize() {

    }

    public void download() {

    }

    public void deletar() {

    }

    public ServiceFile getServiceFile() {
        return serviceFile;
    }

    public void setServiceFile(ServiceFile serviceFile) {
        imagemPreview.setImage(null);
        this.serviceFile = serviceFile;
    }

    public ControllerServiceFile getParentController() {
        return ParentController;
    }

    public void setParentController(ControllerServiceFile parentController) {
        ParentController = parentController;
    }

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

}
