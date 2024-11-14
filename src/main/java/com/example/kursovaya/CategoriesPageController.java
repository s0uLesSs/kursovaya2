package com.example.kursovaya;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class CategoriesPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToMenuBtn;

    @FXML
    private GridPane categoriesGrid;
    @FXML
    private Button onePageBtn;
    @FXML
    private Button twoPageBtn;

    DBHandler dbHandler = new DBHandler();
    public int countCategories;

    public List<Button> buttonList = new ArrayList<>();
    public int pages = 0;
    public List<Integer> chislaDown = new ArrayList<>();  // числа нижней четверки
    public List<Integer> chislaUp = new ArrayList<>(); // числа верхней четверки
    public List<Integer> chislaPages = new ArrayList<>(); // для страниц ( каждая страница хранит свой chislaDown, chislaUp)
    public int count = 0;  // диапазон для страниц

    MenuController menuController;
    private static String menuCategory;
    private static int idGildiya;
    public static String getMenuCategories() {
        return menuCategory;
    }

    public static void setMenuCategory(String mC){
        menuCategory= mC;
    }
    public static int getGildiya() {
        return idGildiya;
    }

    public static void setGildiya(int gild){
        idGildiya = gild;
    }
    public int idGild = 0;

    BusinessMenuController businessMenuController;

    @FXML
    void backToMenuBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        backToMenuBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("menu.fxml", "Меню", 1000, 700);

    }

    @FXML
    void onePageBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        if (count != 0) {
            for (int i = chislaDown.get(chislaPages.get(count)); i <= chislaUp.get(chislaPages.get(count)); i++) {
                buttonList.get(i).setVisible(false);
            }
            count--;
            for (int i = chislaDown.get(chislaPages.get(count)); i <= chislaUp.get(chislaPages.get(count)); i++) {
                buttonList.get(i).setVisible(true);
            }
        } else {
            Alert loginError = new Alert(Alert.AlertType.ERROR);
            loginError.setTitle("Min!");
            loginError.setHeaderText("Min pages!");
            loginError.setContentText("Min pages!");
            loginError.show();
        }

    }

    @FXML
    void twoPageBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        if (count != chislaPages.size() - 1) {
            for (int i = chislaDown.get(chislaPages.get(count)); i <= chislaUp.get(chislaPages.get(count)); i++) {
                buttonList.get(i).setVisible(false);
            }
            count++;
            for (int i = chislaDown.get(chislaPages.get(count)); i <= chislaUp.get(chislaPages.get(count)); i++) {
                buttonList.get(i).setVisible(true);
            }
        } else {
            Alert loginError = new Alert(Alert.AlertType.ERROR);
            loginError.setTitle("Max!");
            loginError.setHeaderText("Max pages!");
            loginError.setContentText("Max pages!");
            loginError.show();
        }
    }


    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        try {

            dbHandler.getDbConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (getGildiya() == 0){
            idGild = dbHandler.getGildByTitle(menuController.getMenuCategories());
            setGildiya(idGild);
        }else{
            idGild = getGildiya();
        }


        List<String> categories = new ArrayList<>();
        categories = dbHandler.getCategoriesByGild(idGild);

        countCategories = dbHandler.getCountCategories(idGild);
        int z = 0;
        int z1 = 0;
        for (int i = 0; i < countCategories; i++) {
            Button button = new Button();
            button.setPrefWidth(142.0);
            button.setPrefHeight(37.0);
            button.setText(categories.get(i));
            button.setId(categories.get(i));
            button.setStyle("-fx-background-color: #ffc800; -fx-background-radius: 20;");
            int finalIdGild = idGild;
            button.setOnAction(actionEvent -> {
                Object source = actionEvent.getSource();
                String source1 = source.toString();
                String[] parts = source1.split("]");
                String[] nextParts = parts[1].split("'");
                businessMenuController.setBlPosition("");
                CategoriesPageController.setGildiya(finalIdGild);
                CategoriesPageController.setMenuCategory(nextParts[1]);
                backToMenuBtn.getScene().getWindow().hide();
                SceneChanger.changeScene("positionPage.fxml", "ЗАКАЗ", 1336, 700);
            });
            buttonList.add(button);

            if (z == 0 && z1 == 0) {
                categoriesGrid.add(button, z, z1);
                categoriesGrid.setHalignment(button, HPos.CENTER);
                z1++;
                continue;
            }
            if (z == 0 && z1 == 1) {
                categoriesGrid.add(button, z, z1);
                categoriesGrid.setHalignment(button, HPos.CENTER);
                z = 1;
                z1 = 0;
                continue;
            }
            if (z == 1 && z1 == 0) {
                categoriesGrid.add(button, z, z1);
                categoriesGrid.setHalignment(button, HPos.CENTER);
                z1++;
                continue;
            }
            if (z == 1 && z1 == 1) {
                categoriesGrid.add(button, z, z1);
                categoriesGrid.setHalignment(button, HPos.CENTER);
                z = 0;
                z1 = 0;
            }

        }
        if (buttonList.size() >= 5) {
            for (int c = 4; c < buttonList.size(); c++) {
                buttonList.get(c).setVisible(false);
            }
        }

        int chisloDown = 0;
        int chisloUp = 3;

        if (countCategories > 4) {
            pages = (countCategories / 4) + 1;
            int a = countCategories / 4; // сколько раз мы можем заполнить 4-мя позициями
            for (int i = 1; i <= a; i++) {
                chislaUp.add(chisloUp);
                chisloUp = chisloUp + 4;
            }
            if ((countCategories % 4) != 0) {
                chislaUp.add(buttonList.size() - 1);
            }
        } else {
            pages = 1;
            int a = 1;
            chislaUp.add(buttonList.size() - 1);
        }

        for (int i = 1; i <= pages; i++) {
            chislaDown.add(chisloDown);
            chisloDown = chisloDown + 4;
        }
        for (int i = 0; i < pages; i++) {
            chislaPages.add(i);
        }
    }

}
