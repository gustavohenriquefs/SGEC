package com.casaculturaqxd.sgec.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.DAO.GrupoEventosDAO;
import com.casaculturaqxd.sgec.controller.preview.PreviewEventoController;
import com.casaculturaqxd.sgec.controller.preview.PreviewGrupoEventoController;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.FlowPane;
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
  private FlowPane secaoGrupoEventos;

  @FXML
  private FlowPane secaoUltimosEventos;

  @FXML
  private RowConstraints titulo1;

  @FXML
  public void initialize() throws IOException {
    this.loadMenu();
    this.initGridUltimosEventos();
    this.initGruposEventos();
  }

  private void initGridUltimosEventos() {
    ArrayList<Evento> ultimosEventos = getUltimosEventos();

    if (ultimosEventos != null) {
      for (Evento evento : ultimosEventos) {
        this.adicionarEventoEmGrid(evento);
      }
    }
  }

  private void initGruposEventos() {
    ArrayList<GrupoEventos> ultimosGrupoEventos = getGruposEventos();

    if (ultimosGrupoEventos != null) {
      for (GrupoEventos grupoEventos : ultimosGrupoEventos) {
        this.adicionarGrupoEventoEmGrid(grupoEventos);
      }
    }
  }

  private void adicionarGrupoEventoEmGrid(GrupoEventos grupoEventos) {
    try {

      FXMLLoader childLoader = obterFXMLPreviewGrupoEventoLoader();

      VBox childNode = childLoader.load();

      PreviewGrupoEventoController childController = childLoader.getController();

      childController.setGrupoEventos(grupoEventos);

      this.secaoGrupoEventos.getChildren().add(childNode);
    } catch (RuntimeException | IOException e) {
      e.printStackTrace();
    }
  }

  private FXMLLoader obterFXMLPreviewGrupoEventoLoader() {
    return new FXMLLoader(App.class.getResource("view/preview/previewGrupoEvento.fxml"));
  }

  private void adicionarEventoEmGrid(Evento evento) {
    try {

      FXMLLoader childLoader = obterFXMLPreviewEventoExistenteLoader();

      VBox childNode = childLoader.load();

      PreviewEventoController childController = childLoader.getController();

      childController.setEvento(evento);

      secaoUltimosEventos.getChildren().add(childNode);
    } catch (RuntimeException | IOException e) {
      e.printStackTrace();
    }

  }

  private FXMLLoader obterFXMLPreviewEventoExistenteLoader() {

    return new FXMLLoader(App.class.getResource("view/preview/previewEventoExistente.fxml"));
  }

  private ArrayList<Evento> getUltimosEventos() {
    EventoDAO eventoDAO = new EventoDAO();
    DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");

    eventoDAO.setConnection(db.getConnection());

    try {
      return eventoDAO.listarUltimosEventos();
    } catch (SQLException e) {
      return new ArrayList<>();
    }
  }

  private ArrayList<GrupoEventos> getGruposEventos() {
    DatabasePostgres db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    GrupoEventosDAO grupoEventosDAO = new GrupoEventosDAO(db.getConnection());

    try {
      return grupoEventosDAO.listUltimosGrupoEventos();
    } catch (SQLException e) {
      return new ArrayList<>();
    }
  }

  private void loadMenu() throws IOException {
    FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
    root.getChildren().add(0, carregarMenu.load());
  }
}
