package com.casaculturaqxd.sgec.controller.preview;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
        
        this.dataHora.setText(evento.getDataFinal().toString() + "\t" +evento.getHorario());
        this.titulo.setText(evento.getNome());
    }

    public void setEvento(Evento evento){
        this.evento = dao.buscarEvento(evento).get();
    }

    public void verDetalhes(){
        FXMLLoader loaderVisualizacao = new FXMLLoader(App.class.getResource("view/visualizarEvento"));
        //VisualizarEventoController controller= loaderVisualizacao.getController();
        //controller.setEvento(this.evento);
        //App.setRoot("view/visualizarEvento");
    }
}
