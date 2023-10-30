package com.casaculturaqxd.sgec;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

import com.casaculturaqxd.sgec.models.User;

/**
 * JavaFX App
 */
public class App extends Application {
    public static Stack<Parent> lastVisitedPages = new Stack<Parent>();
    private static Scene scene;
    private static User usuarioLogado;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("view/login"), 640, 480);
        Image image = new Image(App.class.getResourceAsStream("imagens/logo_cego_aderaldo.png"));
        stage.getIcons().add(image);
        stage.setTitle("SGEC");
        stage.setScene(scene);

        stage.setMaximized(true);
        stage.show();
    }

    public static void setUsuario(User novoUsuario) {
        if (novoUsuario != null) {
            usuarioLogado = novoUsuario;
        }
    }

    public static User getUsuario() {
        return usuarioLogado;
    }

    public static Stack<Parent> getLastVisitedPages() {
        return lastVisitedPages;
    }

    public static Parent getRoot() {
        return scene.getRoot();
    }

    public static void backLastScreen() {
        scene = new Scene(lastVisitedPages.pop());
        scene.getStylesheets();
    }

    public static void setRoot(Parent objVisualizacao) throws IOException {
        if (lastVisitedPages.empty() == true
                && !objVisualizacao.getId().equals(loadFXML("view/login").getId())) {

            lastVisitedPages.add(getRoot());
            scene.setRoot(objVisualizacao);
        } else if (!lastVisitedPages.lastElement().getId().equals(objVisualizacao.getId())
                && !objVisualizacao.getId().equals(loadFXML("view/login").getId())) {

            lastVisitedPages.add(getRoot());
            scene.setRoot(objVisualizacao);
        }
    }

    public static void setRoot(String fxml) throws IOException {
        if (lastVisitedPages.empty() == true
                && getRoot().getId().equals(loadFXML("view/login").getId())) {

            scene.setRoot(loadFXML(fxml));
        } else if (!getRoot().getId().equals(loadFXML(fxml).getId())
                && !loadFXML(fxml).getId().equals(loadFXML("view/login").getId())) {

            lastVisitedPages.add(getRoot());
            scene.setRoot(loadFXML(fxml));
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
