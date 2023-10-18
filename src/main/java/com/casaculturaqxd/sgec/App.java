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
    private static Stack<Parent> lastVisitedPages = new Stack<Parent>();
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

    public static void setRoot(Parent objVisualizacao) throws IOException {
        lastVisitedPages.add(objVisualizacao);
        scene.setRoot(objVisualizacao);
    }

     public static void setRoot(String fxml) throws IOException {
        lastVisitedPages.add(loadFXML(fxml));
        System.out.println(lastVisitedPages);
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}