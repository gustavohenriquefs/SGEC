package com.casaculturaqxd.sgec.controller.preview;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.controller.ControllerEvento;
import com.casaculturaqxd.sgec.controller.VisualizarEventoController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PreviewEventoController {
    private EventoDAO dao = new EventoDAO();
    private DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    private Evento evento;
    private ControllerEvento parentController;
    @FXML
    private Parent container; // pane raiz do fxml
    @FXML
    Label dataHora, titulo;
    @FXML
    Button detalhes, remover;

    public void initialize() {
        dao.setConnection(db.getConnection());
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
        this.setarInformacoesEvento();

    }

    public Evento getEvento() {
      return evento;
    }

    private void setarInformacoesEvento() {
        if (evento.getHorario() == null) {
            this.dataHora.setText(evento.getDataInicial().toString() + "\t" + " ");
            this.titulo.setText(evento.getNome());
        } else {
            this.dataHora.setText(evento.getDataInicial().toString() + "\t" + evento.getHorario());
            this.titulo.setText(evento.getNome());
        }
    }

    @FXML
    public void verDetalhes(ActionEvent event) throws IOException, SQLException {
        try {
            URL url = App.class.getResource("view/visualizarEvento.fxml");
            FXMLLoader loaderVisualizacao = new FXMLLoader(url);
            Parent objVisualizacao = loaderVisualizacao.load();

            VisualizarEventoController controller = loaderVisualizacao.getController();

            controller.setEvento(dao.getEvento(evento).get());
            App.setRoot(objVisualizacao);

        } catch (NoSuchElementException e) {
            Alert erroLoading = new Alert(AlertType.WARNING, "Falha ao carregar o evento");
            erroLoading.show();
        }
    }

    public ControllerEvento getParentController() {
      return parentController;
    }

    public void setParentController(ControllerEvento parentController) {
        this.parentController = parentController;
        habilitaBotao();
    }

    void habilitaBotao(){
        if(getParentController()!=null){
            remover.setDisable(false);
            remover.setVisible(true);
        }
    }

    public Parent getContainer() {
        return container;
    }

    public void setContainer(Parent container) {
        this.container = container;
    }

    public void remover() {
        parentController.removerEvento(getEvento());
    }
}
