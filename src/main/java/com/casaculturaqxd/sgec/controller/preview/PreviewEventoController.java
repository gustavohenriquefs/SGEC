package com.casaculturaqxd.sgec.controller.preview;

import java.io.IOException;
import java.net.URL;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.controller.VisualizarEventoController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class PreviewEventoController {
    private EventoDAO dao = new EventoDAO();
    private DatabasePostgres db = DatabasePostgres.getInstance("URL","USER_NAME","PASSWORD");
    private Evento evento;
    @FXML
    Label dataHora, titulo;
    @FXML
    Button detalhes;


    public void initialize(){
        dao.setConnection(db.getConnection());
    }

    public void setEvento(Evento evento){
        this.evento = dao.buscarEvento(evento).get();

        this.setarInformacoesEvento();
    }

    private void setarInformacoesEvento() {
        this.dataHora.setText(evento.getDataFinal().toString() + "\t" + evento.getHorario());
        this.titulo.setText(evento.getNome());
    }
    
    @FXML
    public void verDetalhes(ActionEvent event) throws IOException {

        URL url = App.class.getResource("view/visualizarEvento.fxml");

        FXMLLoader loaderVisualizacao = new FXMLLoader(url);

        Parent objVisualizacao = loaderVisualizacao.load();
        
        VisualizarEventoController controller = loaderVisualizacao.getController();
        
        controller.setEvento(this.evento);

        App.setRoot(objVisualizacao);
    }
}
