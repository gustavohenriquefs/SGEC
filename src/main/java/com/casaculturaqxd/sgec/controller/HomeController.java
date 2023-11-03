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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class HomeController {

  @FXML
  private VBox root;

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private GridPane gridMetas;

  @FXML
  private FlowPane secaoUltimosEventos;

  @FXML
  private RowConstraints titulo1;

  @FXML
  public void initialize() throws IOException {
    this.loadMenu();
    this.initGridUltimosEventos();
  }

  private void initGridUltimosEventos() {
    ArrayList<Evento> ultimosEventos = getUltimosEventos();

    if (ultimosEventos != null) {
      for (Evento evento : ultimosEventos) {
        this.adicionarEventoEmGrid(evento);
      }
    }
  }

  private void adicionarEventoEmGrid(Evento evento) {
    try {

      FXMLLoader childLoader = obterFXMLPreviewEventoExistenteLoader();

      VBox childNode = childLoader.load();

      PreviewEventoController childController = childLoader.getController();

      childController.setEvento(evento);

      secaoUltimosEventos.getChildren().add(childNode);
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

  private void loadMenu() throws IOException {
    FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
    root.getChildren().add(0, carregarMenu.load());
  }
}
