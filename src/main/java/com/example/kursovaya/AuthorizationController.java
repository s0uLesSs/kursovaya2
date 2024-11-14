package com.example.kursovaya;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class AuthorizationController {
    DBHandler db = new DBHandler();
    private static int tries = 0;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField codeField;

    @FXML
    private Button authBtn;

    OrdersController ordersController;

    @FXML
    void checkauth(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        String code = codeField.getText().trim();
        if ((code != null || !code.isEmpty())) {
            try {
                int id = db.userExist(code);
                if (id != -1) {
                    db.loadUser(id);
                    authBtn.getScene().getWindow().hide();
                    ordersController.setHaveOrder(false);
                    SceneChanger.changeScene("mainMenu.fxml", "Меню", 1000, 700);
                } else {
                    Alert loginError = new Alert(Alert.AlertType.ERROR);
                    loginError.setTitle("Error!");
                    loginError.setHeaderText("Error authorization");
                    loginError.setContentText("Something went wrong");
                    loginError.show();
                    tries++;
                }
            } catch (SQLException sqlException){

            }
        }
    }
    @FXML
    void initialize() {

    }

}

