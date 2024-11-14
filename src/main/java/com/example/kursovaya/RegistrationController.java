package com.example.kursovaya;

import java.io.IOException;
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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField codeField;

    @FXML
    private TextField familyField;

    @FXML
    private TextField nameFIeld;

    @FXML
    private ComboBox<String> rolesCombo;
    @FXML
    private Button regBtn;


    @FXML
    private
    DBHandler dbHandler = new DBHandler();
    public String selectedRole = "";

    @FXML
    void backButton(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException{
        regBtn.getScene().getWindow().hide();
        SceneChanger.changeScene("directorMenu.fxml", "МЕНЮ ДИРЕКТОРА", 1000, 700);
    }

    @FXML
    void regButton(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        String name = nameFIeld.getText();
        if(name.equals("")){
            Alert loginError = new Alert(Alert.AlertType.ERROR);
            loginError.setTitle("Ошибка");
            loginError.setHeaderText("Пустое имя");
            loginError.setContentText("Вы не ввели имя");
            loginError.show();
        }else{
            String surName = familyField.getText();
            if(surName.equals("")){
                Alert loginError = new Alert(Alert.AlertType.ERROR);
                loginError.setTitle("Ошибка");
                loginError.setHeaderText("Пустая фамилия");
                loginError.setContentText("Вы не ввели фамилию");
                loginError.show();
            }else{
                String code = codeField.getText();
                if(code.equals("")){
                    Alert loginError = new Alert(Alert.AlertType.ERROR);
                    loginError.setTitle("Ошибка");
                    loginError.setHeaderText("Пустой код");
                    loginError.setContentText("Вы не ввели код");
                    loginError.show();
                }else{
                    try{
                        int cod = Integer.parseInt(code);
                        if(selectedRole.equals("")){
                            Alert loginError = new Alert(Alert.AlertType.ERROR);
                            loginError.setTitle("Ошибка");
                            loginError.setHeaderText("Не выбрана роль");
                            loginError.setContentText("Выберите роль сотрудника");
                            loginError.show();
                        }else{
                            int idRole = 0;
                            idRole = dbHandler.getIdRoleByRoleName(selectedRole);
                            String result = "";
                            try {
                                result = dbHandler.insertUser(code, name, surName, idRole);
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setTitle("ВЫВОД");
                                Label label = new Label(result);
                                label.setTextFill(Color.RED);
                                label.setStyle("-fx-font-weight: bold");
                                label.setFont(new Font(17));
                                Button yesButton = new Button("Ок");
                                yesButton.setOnAction(actionEvent1 -> {
                                    regBtn.getScene().getWindow().hide();
                                    SceneChanger.changeScene("directorMenu.fxml", "МЕНЮ ДИРЕКТОРА", 1000, 700);
                                });
                            VBox layot = new VBox(10);
                            layot.getChildren().addAll(label, yesButton);
                            layot.setAlignment(Pos.CENTER);
                            layot.setStyle("-fx-background-color: #171717");
                            Scene scene = new Scene(layot,500,250);
                            stage.setScene(scene);
                            stage.showAndWait();
                            }
                            catch (SQLException e){
                                Alert loginError = new Alert(Alert.AlertType.ERROR);
                                loginError.setTitle("Ошибка");
                                loginError.setHeaderText("Код занят");
                                loginError.setContentText("Сотрудник с таким кодом уже существует");
                                loginError.show();
                            }
                        }
                    }
                    catch (NumberFormatException e){
                        Alert loginError = new Alert(Alert.AlertType.ERROR);
                        loginError.setTitle("Ошибка");
                        loginError.setHeaderText("В коде обнаружены символы");
                        loginError.setContentText("Код должен быть числом");
                        loginError.show();
                    }
                }

            }
        }
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

        List<String> roleNames = new ArrayList<>();
        try {
            roleNames = dbHandler.getRoleNames();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> roles = FXCollections.observableArrayList(roleNames);
        rolesCombo.setItems(roles);
        rolesCombo.setOnAction(e -> {
            String selected = rolesCombo.getValue();
            selectedRole = selected;
        });
    }
}
