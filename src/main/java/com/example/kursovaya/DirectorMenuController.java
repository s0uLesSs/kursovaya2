package com.example.kursovaya;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DirectorMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button categoriesBtn;

    @FXML
    private Button ordersBtn;

    @FXML
    private Button positionBtn;

    @FXML
    private Button regBtn;

    @FXML
    private Button viruchkaBtn;
    @FXML
    private Button backBtn;
    OrdersController ordersController;
    PositionPageController positionPageController;
    DBHandler dbHandler = new DBHandler();

    @FXML
    void backButton(ActionEvent event) {
        backBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("mainMenu.fxml", "Меню", 1000, 700);
    }

    @FXML
    void ordersButton(ActionEvent event){
        ordersBtn.getScene().getWindow().hide();
        ordersController.setManager(true);
        ordersController.setDirector(true);
        SceneChanger.changeScene("orders.fxml", "Заказы", 1000, 700);
    }

    @FXML
    void regButton(ActionEvent event){
        regBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("registration.fxml","Регистрация", 1000, 700);
    }
    @FXML
    void viruchkaButton(ActionEvent event){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("ВЫРУЧКА");
        Label label = new Label("Карта: " + positionPageController.getKartaViruchka());
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-weight: bold");
        label.setFont(new Font(17));
        Label label1 = new Label("Наличка: " + positionPageController.getNalichkaViruchka());
        label1.setTextFill(Color.WHITE);
        label1.setStyle("-fx-font-weight: bold");
        label1.setFont(new Font(17));
        Button yesButton = new Button("Ок");
        yesButton.setOnAction(actionEvent1 -> {
            stage.close();
        });
        VBox layot = new VBox(10);
        layot.getChildren().addAll(label, label1, yesButton);
        layot.setAlignment(Pos.CENTER);
        layot.setStyle("-fx-background-color: #171717");
        Scene scene = new Scene(layot,500,250);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    void initialize() {
        try {
            dbHandler.getDbConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
