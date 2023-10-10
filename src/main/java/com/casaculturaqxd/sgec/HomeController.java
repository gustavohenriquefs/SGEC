package com.casaculturaqxd.sgec;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.controller.preview.PreviewEventoController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane gridMetas;

    @FXML
    private Pane header;

    @FXML
    private HBox listaEventos;

    @FXML
    private RowConstraints titulo1;

    @FXML
    private HBox menuContainer;

    @FXML
    public void initialize() throws IOException {

      FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
      
      VBox menu = carregarMenu.load();

      this.menuContainer.getChildren().add(menu);

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
      try {

        FXMLLoader childLoader = obterFXMLPreviewEventoExistenteLoader();

        VBox childNode = childLoader.load();
        
        PreviewEventoController childController = childLoader.getController();

        childController.setEvento(evento);

        listaEventos.getChildren().add(childNode);
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
