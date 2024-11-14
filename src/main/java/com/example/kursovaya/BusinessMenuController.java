package com.example.kursovaya;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BusinessMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToMenuBtn;
    @FXML
    private Button blSaladsBtn;
    @FXML
    private Button blSoupsBtn;

    private static String blPosition;

    public static String getBlPosition() {
        return blPosition;
    }

    public static void setBlPosition(String bL){
        blPosition = bL;
    }
    CategoriesPageController categoriesPageController;




    @FXML
    void backToMenuBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        backToMenuBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("menu.fxml", "Меню", 1000, 700);

    }


    @FXML
    void nextPageBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        Object source = actionEvent.getSource();
        String source1 = source.toString();
        String[] parts = source1.split("]");
        String[] nextParts = parts[1].split("'");
        setBlPosition(nextParts[1]);
        categoriesPageController.setMenuCategory(nextParts[1]);
        categoriesPageController.setGildiya(0);
        blSoupsBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("positionPage.fxml", "ЗАКАЗ", 1336, 700);

    }

    @FXML
    void initialize() {

    }

}
