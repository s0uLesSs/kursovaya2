package com.example.kursovaya;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TablesController  {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToMenuBtn;

    @FXML
    private ImageView homeImg;

    @FXML
    private Button tablesButton1, tablesButton2, tableButton3, tablesButton4, tablesButton5, tablesButton6, tablesButton7, tablesButton8, tablesButton9, tablesButton10, tablesButton11, tablesButton12, tablesButton13, tablesButton14, tablesButton15, tablesButton16;



    DBUsers dbUsers;
    DBHandler dbHandler = new DBHandler();
    OrdersController ordersController;

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

    private static String tableNumber;

    public static String getTableNumber() {
        return tableNumber;
    }

    public static void setTableNumber(String tN){
        tableNumber = tN;
    }

    @FXML
    void homeImage(MouseEvent mouseEvent) throws SQLException, IOException, ClassNotFoundException {

        homeImg.getScene().getWindow().hide();
        SceneChanger.changeScene("authorization.fxml", "Авторизация", 1000, 700);

    }

    @FXML
    void backToMenuBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        backToMenuBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("mainMenu.fxml", "Меню", 1000, 700);

    }

    @FXML
    void setTables(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        List<String> tables = new ArrayList<>();
        tables = dbHandler.getTablesNumberByUser(dbUsers.getUserId());

        List<String> table1 = new ArrayList<>();
        for (int i = 0; i < tables.size(); i++){
            int idTable = Integer.parseInt(tables.get(i));
            table1.add(dbHandler.getTablesNumberById(idTable));
        }

        Object source = actionEvent.getSource();
        String source1 = source.toString();
        String[] parts = source1.split("]");
        String[] nextParts = parts[1].split("'");
        boolean haveOrderOnTable = false;
        for (int i = 0; i < table1.size(); i++){
            if(table1.get(i).equals(nextParts[1])){
                haveOrderOnTable = true;
                Alert loginError = new Alert(Alert.AlertType.ERROR);
                loginError.setTitle("Ошибка");
                loginError.setHeaderText("Стол занят");
                loginError.setContentText("Стол уже занят");
                loginError.show();
            }
        }
        if(haveOrderOnTable == false){
            setTableNumber(nextParts[1]);
            backToMenuBtn.getScene().getWindow().hide();
            SceneChanger.changeScene("menu.fxml", "ЗАКАЗ", 1000, 700);
        }
    }
}
