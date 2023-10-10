package com.casaculturaqxd.sgec;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class HomeController {

    @FXML
    private GridPane gridMetas;

    @FXML
    private GridPane gridUltimosEventos;

    @FXML
    private Pane header;

    @FXML
    private RowConstraints titulo1;

    @FXML
    private RowConstraints titulo2;

    @FXML
    public void initialize() {
      this.initGridUltimosEventos();
    }

    private void initGridUltimosEventos() {
      ArrayList<Evento> ultimosEventos = getUltimosEventos();

      if(ultimosEventos != null) {
        for (int idxEvento = 0; idxEvento < ultimosEventos.size(); idxEvento++) {
          this.adicionarEventoEmGrid(ultimosEventos.get(idxEvento), idxEvento);
        }
      }
    }

    private void adicionarEventoEmGrid(Evento evento, int idxEvento) {
      int idxColuna = idxEvento % 3;
      int idxLinha = idxEvento / 3;

      try {

        FXMLLoader childLoader = obterFXMLPreviewEventoExistenteLoader();

        VBox childNode = childLoader.load();
        
        PreviewEventoExistenteController childController = childLoader.getController();

        childController.setEvento(evento);

        gridUltimosEventos.add(childNode, idxColuna, idxLinha);

      } catch (IOException e) {
          e.printStackTrace();
      }
    }

    private FXMLLoader obterFXMLPreviewEventoExistenteLoader() {
      URL fxmlUrl = getClass().getResource("view/preview/previewEventoExistente.fxml");

      return new FXMLLoader(fxmlUrl);
    }

    private ArrayList<Evento> getUltimosEventos() {
      EventoDAO eventoDAO = new EventoDAO();
      DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
      
      eventoDAO.setConnection(db.getConnection());
      
      return eventoDAO.listarUltimosEventos();
    }
  
}
