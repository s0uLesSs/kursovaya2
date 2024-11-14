package com.example.kursovaya;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToMenuBtn;

    @FXML
    private Button barBtn;

    @FXML
    private Button businessBtn;

    @FXML
    private ImageView homeImg;

    @FXML
    private Button kitchenBtn;
    private static String menuCategory;

    @FXML
    private Label tableNumber;

    TablesController tables;
    CategoriesPageController categoriesPageController;
    PositionPageController positionPageController;
    OrdersController ordersController;


    public static String getMenuCategories() {
        return menuCategory;
    }

    public static void setMenuCategory(String mC){
        menuCategory= mC;
    }

    @FXML
    void backToMenuBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        if (positionPageController.getHaveOrderPain() == true){
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Уверены ли Вы?");
            Label label = new Label("У вас есть незакрытые столы. Вы хотите выйти?");
            label.setTextFill(Color.RED);
            label.setStyle("-fx-font-weight: bold");
            label.setFont(new Font(17));
            Button yesButton = new Button("Да!");
            yesButton.setOnAction(actionEvent1 -> {
                positionPageController.setFirstColumnForClasses(null);
                positionPageController.setSecondColumnForClasses(null);
                positionPageController.setThirdColumnForClasses(null);
                positionPageController.setFourthColumnForClasses(null);
                positionPageController.setHaveOrderPain(false);
                positionPageController.setTries(0);
                stage.close();
                backToMenuBtn.getScene().getWindow().hide();
                ordersController.setManager(false);
                ordersController.setKassir(false);
                SceneChanger.changeScene("tables.fxml", "Столы", 1000, 700);

            });
            Button noButton = new Button("Нет!");
            noButton.setOnAction(actionEvent1 -> {
                stage.close();
            });
            VBox layot = new VBox(10);
            layot.getChildren().addAll(label, yesButton, noButton);
            layot.setAlignment(Pos.CENTER);
            layot.setStyle("-fx-background-color: #171717");
            Scene scene = new Scene(layot,500,250);
            stage.setScene(scene);
            stage.showAndWait();
        }else{
            backToMenuBtn.getScene().getWindow().hide();
            SceneChanger.changeScene("tables.fxml", "Столы", 1000, 700);
        }

    }

    @FXML
    void homeImage(MouseEvent mouseEvent) throws SQLException, IOException, ClassNotFoundException {

        homeImg.getScene().getWindow().hide();
        ordersController.setManager(false);
        ordersController.setKassir(false);
        SceneChanger.changeScene("authorization.fxml", "Авторизация", 1000, 700);

    }


    @FXML
    void businessBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        businessBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("businessMenu.fxml", "Бизнес-Ланч", 1000, 700);

    }

    @FXML
    void nextPageBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        Object source = actionEvent.getSource();
        String source1 = source.toString();
        String[] parts = source1.split("]");
        String[] nextParts = parts[1].split("'");
        setMenuCategory(nextParts[1]);
        categoriesPageController.setGildiya(0);
        barBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("categoriesPage.fxml", "ЗАКАЗ", 1000, 700);

    }

    @FXML
    void initialize() {
        tableNumber.setText(tables.getTableNumber());
        if(ordersController.getHaveOrder()){
            tableNumber.setText(ordersController.getTableNumber());
            tables.setTableNumber(ordersController.getTableNumber());
        }
    }

}
