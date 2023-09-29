package com.casaculturaqxd.sgec.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import com.casaculturaqxd.sgec.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class SubSceneLoader {
    FXMLLoader sceneLoader;
    public Pane getPage(String sceneName) throws IOException{
        System.out.println(SubSceneLoader.class);
        try{
            URL pagePath = App.class.getResource("view/" + sceneName +".fxml");
            if(pagePath == null){
                throw new FileNotFoundException(sceneName+".fxml nao encontrado");
            }
            sceneLoader = new FXMLLoader(pagePath);
        }   
        catch(FileNotFoundException erro){
            System.out.println("arquivo "+sceneName+" nao encontrado");
        }
        return sceneLoader.load();
    }
}
