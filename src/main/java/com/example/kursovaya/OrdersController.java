package com.example.kursovaya;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class OrdersController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label fioLabel;

    @FXML
    private GridPane tablesPane;
    @FXML
    private Button backBtn;
    DBHandler dbHandler = new DBHandler();
    DBUsers dbUsers;

    private static String tableNumber;

    public static boolean getHaveOrder() {
        return haveOrder;
    }

    public static void setHaveOrder(boolean haveOrder) {
        OrdersController.haveOrder = haveOrder;
    }

    public static String getTableNumber() {
        return tableNumber;
    }

    public static boolean getManager() {
        return manager;
    }

    public static void setManager(boolean manager) {
        OrdersController.manager = manager;
    }

    public static boolean getKassir() {
        return kassir;
    }

    public static void setKassir(boolean kassir) {
        OrdersController.kassir = kassir;
    }

    public static boolean getDirector() {
        return director;
    }

    public static void setDirector(boolean director) {
        OrdersController.director = director;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }
    private static boolean haveOrder;
    private static boolean manager;
    private static boolean director;
    private static boolean kassir;


    @FXML
    void backBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        if(getDirector()){
            backBtn.getScene().getWindow().hide();
            SceneChanger.changeScene("directorMenu.fxml", "МЕНЮ ДИРЕКТОРА", 1000, 700);
            return;
        }

        backBtn.getScene().getWindow().hide();
        setHaveOrder(false);
        setKassir(false);
        setManager(false);
        SceneChanger.changeScene("mainMenu.fxml", "Меню", 1000, 700);

    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        try {
            dbHandler.getDbConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        fioLabel.setText(dbUsers.getUserSurname() + " " + dbUsers.getUserName());
        int countOrders = dbHandler.getCountOrders(dbUsers.getUserId());
        List<String> tables = new ArrayList<>();
        tables = dbHandler.getTablesNumberByUser(dbUsers.getUserId());
        String role = dbHandler.roleById(dbUsers.getUserRole());
        if(getManager()){
            tables = dbHandler.getAllIdTablesInOrders();
            countOrders = tables.size();
            setManager(true);
        }
        if (getKassir()){
            tables = dbHandler.getAllIdTablesInOrders();
            countOrders = tables.size();
            setKassir(true);
        }
        int z = 0; // координата строки гридпейна
        int z1 = 0; // координата столбца грид пейна
        for (int i = 0; i < countOrders; i++){
            Button button = new Button();
            int tablesIdForSelect = Integer.parseInt(tables.get(i));
            button.setText(String.valueOf(dbHandler.getTablesNumberById(tablesIdForSelect)));
            button.setId(String.valueOf(i));
            button.setMinHeight(75);
            button.setMinWidth(75);
            button.setPrefHeight(75);
            button.setPrefWidth(75);
            button.setMaxHeight(75);
            button.setMaxWidth(75);
            button.setStyle("-fx-background-color: #ffc800; -fx-background-radius: 20;");
            button.setFont(new Font(20));
            button.setOnAction(actionEvent -> {
                setTableNumber(button.getText());
                setHaveOrder(true);
                button.getScene().getWindow().hide();
                SceneChanger.changeScene("menu.fxml", "МЕНЮ", 1000, 700);
            });
            tablesPane.add(button,z1, z);
            tablesPane.setHalignment(button, HPos.CENTER);
            z1++;
            if(z1 == 5){
                z1 = 0;
                z++;
            }
        }
    }

}
