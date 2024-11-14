package com.example.kursovaya;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChanger {
    public static void changeScene(String fxmlName, String title, int v, int v1) {
        Parent root = null;
        try {
            root = FXMLLoader.load(SceneChanger.class.getResource(fxmlName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.getIcons().add(new Image(SceneChanger.class.getResourceAsStream("/files/cutlery.png")));
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, v, v1));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
