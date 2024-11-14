package com.example.kursovaya;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MainMenuController {

    @FXML
    private ResourceBundle resources;

    DBHandler db;
    DBUsers dbusers;

    @FXML
    private URL location;

    @FXML
    private Label userRole;

    @FXML
    private Label userName;

    @FXML
    private Button backBtn;

    @FXML
    private Button managerBtn;

    @FXML
    private Button directorBtn;

    @FXML
    private Button cassirBtn;

    @FXML
    private Button createBtn;
    @FXML
    private Button redactBtn;

    OrdersController ordersController;

    @FXML
    void initialize() {
        db = new DBHandler();

        try {

            db.getDbConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        userName.setText(dbusers.getUserSurname() + " " + dbusers.getUserName());

        try {
            userRole.setText(db.roleById(dbusers.getUserRole()));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        String role = userRole.getText();
        switch (role){
            case "Официант":
                managerBtn.setVisible(false);
                directorBtn.setVisible(false);
                cassirBtn.setVisible(false);
                break;
            case "Кассир":
                managerBtn.setVisible(false);
                directorBtn.setVisible(false);
                break;
            case "Менеджер":
                directorBtn.setVisible(false);
                cassirBtn.setVisible(false);
                break;
            case "Директор":
                managerBtn.setVisible(false);
                cassirBtn.setVisible(false);
                break;
        }
    }

    @FXML
    void backBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        backBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("authorization.fxml", "Авторизация", 1000, 700);

    }

    @FXML
    void redaktirovanieBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        redactBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("orders.fxml", "Заказы", 1000, 700);

    }
    @FXML
    void createBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        createBtn.getScene().getWindow().hide();
        ordersController.setHaveOrder(false);
        SceneChanger.changeScene("tables.fxml", "Столы", 1000, 700);

    }

    @FXML
    void managersBtn(ActionEvent actionEvent) throws  SQLException, IOException, ClassNotFoundException{

        managerBtn.getScene().getWindow().hide();
        ordersController.setManager(true);
        SceneChanger.changeScene("orders.fxml", "Заказы", 1000, 700);
    }

    @FXML
    void kassirBtn(ActionEvent actionEvent) throws  SQLException, IOException, ClassNotFoundException{
        ordersController.setKassir(true);
        cassirBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("orders.fxml", "Заказы", 1000, 700);
    }

    @FXML
    void directorBtn(ActionEvent actionEvent) throws  SQLException, IOException, ClassNotFoundException{
        directorBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("directorMenu.fxml", "МЕНЮ ДИРЕКТОРА", 1000, 700);
    }

}
