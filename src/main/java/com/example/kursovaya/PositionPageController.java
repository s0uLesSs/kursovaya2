package com.example.kursovaya;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PositionPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button saveOrderBtn;

    @FXML
    private URL location;

    @FXML
    private Button backToMenuBtn;

    @FXML
    private Button kassirBtn;

    @FXML
    private GridPane positionsGrid;
    @FXML
    private Button onePageBtn;
    @FXML
    private Button twoPageBtn;
    @FXML
    private Label fioLabel;
    @FXML
    private Label tableLabel;
    @FXML
    public GridPane orderPane;
    @FXML
    private Label summaLabel;

    public int countPositions;

    public List<Button> buttonList = new ArrayList<>();
    public int pages = 0;
    public List<Integer> chislaDown = new ArrayList<>();  // числа нижней четверки
    public List<Integer> chislaUp = new ArrayList<>(); // числа верхней четверки
    public List<Integer> chislaPages = new ArrayList<>(); // для страниц ( каждая страница хранит свой chislaDown, chislaUp)
    public int count = 0;  // диапазон для страниц
    public int selectGild;

    public int stolbec1 = 0; // для ордерПеин, где столбец - название позиции
    public int stolbec2 = 1; // для ордерПеин, где столбец - цена
    public int stolbec3 = 2; // для ордерПеин, где столбец - количество
    public int stolbec4 = 3; // для ордерПеин, где столбец - итоговая цена ( количество * цену позиции )
    public int positiyaVZakaze = 1; // счетчик строк ордерПеина, начинаем с 1, так как первая строка занята label'ами

    DBHandler dbHandler = new DBHandler();
    BusinessMenuController businessMenuController;
    CategoriesPageController categoriesPageController;
    TablesController tablesController;
    OrdersController ordersController;
    DBUsers dbUsers;
    List<String> firstColumnString = new ArrayList<>();
    List<String> secondColumnString = new ArrayList<>();
    List<String> thirdColumnString = new ArrayList<>();
    List<String> fourthColumnStringAllClass = new ArrayList<>();
    private static List<Label> firstColumnForClasses = new ArrayList<>();
    private static List<Label> secondColumnForClasses = new ArrayList<>();
    private static List<Label> thirdColumnForClasses = new ArrayList<>();
    private static List<Label> fourthColumnForClasses = new ArrayList<>();
    private static Label summaZakazaForClasses = new Label();
    private static boolean haveOrderPain = false;
    private static int tries = 0;
    private static int unChangeableRow = 0;
    private static int Role = 0;
    private static int kartaViruchka = 0;
    private static int nalichkaViruchka = 0;


    public boolean have = false;

    // firstColumn, secondColumn, thirdColumn, fourthColumn, summaLabel
    public static List<Label> getFirstColumnForClasses() {
        return firstColumnForClasses;
    }

    public static void setFirstColumnForClasses(List<Label> labelsForClass) {
        firstColumnForClasses = labelsForClass;
    }

    public static List<Label> getSecondColumnForClasses() {
        return secondColumnForClasses;
    }

    public static void setSecondColumnForClasses(List<Label> labelsForClass) {
        secondColumnForClasses = labelsForClass;
    }

    public static List<Label> getThirdColumnForClasses() {
        return thirdColumnForClasses;
    }

    public static void setThirdColumnForClasses(List<Label> labelsForClass) {
        thirdColumnForClasses = labelsForClass;
    }

    public static List<Label> getFourthColumnForClasses() {
        return fourthColumnForClasses;
    }

    public static void setFourthColumnForClasses(List<Label> labelsForClass) {
        fourthColumnForClasses = labelsForClass;
    }

    public static Label getSummaZakazaForClasses() {
        return summaZakazaForClasses;
    }

    public static void setSummaZakazaForClasses(Label labelForClass) {
        summaZakazaForClasses = labelForClass;
    }

    public static boolean getHaveOrderPain() {
        return haveOrderPain;
    }

    public static void setHaveOrderPain(boolean bool) {
        haveOrderPain = bool;
    }

    public static int getTries() {
        return tries;
    }

    public static void setTries(int bool) {
        tries = bool;
    }

    public static int getUnChangeableRow() {
        return unChangeableRow;
    }

    public static void setUnChangeableRow(int unChangeableRows) {
        unChangeableRow = unChangeableRows;
    }

    public static int getRole() {
        return Role;
    }

    public static void setRole(int role) {
        Role = role;
    }

    public static int getNalichkaViruchka() {
        return nalichkaViruchka;
    }

    public static void setNalichkaViruchka(int nalichkaViruchka) {
        PositionPageController.nalichkaViruchka = nalichkaViruchka;
    }

    public static int getKartaViruchka() {
        return kartaViruchka;
    }

    public static void setKartaViruchka(int kartaViruchka) {
        PositionPageController.kartaViruchka = kartaViruchka;
    }

    @FXML
    void kassirButton(ActionEvent actionEvent) throws  SQLException, IOException, ClassNotFoundException{
        int idTable = dbHandler.getTablesByTitle(tablesController.getTableNumber());
        int idUser = dbUsers.getUserId();
        if (getRole()!=0){
            idUser = getRole();
        }
        setRole(idUser);
        int haveOrder = dbHandler.getIdOrdersByIdUsersAndIdTable(idUser, idTable);
        if(haveOrder>0){
            dbHandler.dropOrderInfo(haveOrder);
        }
        int idOrder = dbHandler.addOrder(idUser, idTable);
        setFirstColumnForClasses(null);
        setSecondColumnForClasses(null);
        setThirdColumnForClasses(null);
        setFourthColumnForClasses(null);
        setHaveOrderPain(false);
        setTries(0);
        setRole(0);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("ЗАКРЫТЬ");
        Label label = new Label("Закрыть стол на наличные или на карту?");
        label.setTextFill(Color.RED);
        label.setStyle("-fx-font-weight: bold");
        label.setFont(new Font(17));
        Button yesButton = new Button("Карта");
        yesButton.setOnAction(actionEvent1 -> {
            int summa = Integer.parseInt(getSummaZakazaForClasses().getText());
            setKartaViruchka(getKartaViruchka() + summa);
            try {
                dbHandler.dropOrderInfo(idOrder);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            kassirBtn.getScene().getWindow().hide();
            SceneChanger.changeScene("mainMenu.fxml", "Меню", 1000, 700);
        });
        Button noButton = new Button("Наличка!");
        noButton.setOnAction(actionEvent1 -> {
            int summa = Integer.parseInt(getSummaZakazaForClasses().getText());
            setNalichkaViruchka(getNalichkaViruchka() + summa);
            try {
                dbHandler.dropOrderInfo(idOrder);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            kassirBtn.getScene().getWindow().hide();
            SceneChanger.changeScene("mainMenu.fxml", "Меню", 1000, 700);
        });
        VBox layot = new VBox(10);
        layot.getChildren().addAll(label, yesButton, noButton);
        layot.setAlignment(Pos.CENTER);
        layot.setStyle("-fx-background-color: #171717");
        Scene scene = new Scene(layot,500,250);
        stage.setScene(scene);
        stage.showAndWait();
    }
    @FXML
    void backToMenuBtn(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

        if(getFirstColumnForClasses().size()<=1){
            setTries(0);
        }


        List<Label> firstColumn = new ArrayList<>();
        List<Label> secondColumn = new ArrayList<>();
        List<Label> thirdColumn = new ArrayList<>();
        List<Label> fourthColumn = new ArrayList<>();
        List<String> fourthColumnString = new ArrayList<>();
        firstColumn.clear();
        secondColumn.clear();
        thirdColumn.clear();
        fourthColumn.clear();
        fourthColumnString.clear();
        for (Node child : orderPane.getChildren()) {
            if (orderPane.getColumnIndex(child) == 0 && child instanceof Label) {
                firstColumn.add((Label) child);
            }
        }
        for (int a = 1; a <= firstColumn.size() - 1; a++) {
            String first = firstColumn.get(a).toString();
            String[] partsLabelCena = first.split("]");
            String[] nextPartsLabelCena = partsLabelCena[1].split("'");
            if (!nextPartsLabelCena[1].equals("Итог")) {
                firstColumnString.add(nextPartsLabelCena[1]);
            }
        }

        for (Node child : orderPane.getChildren()) {
            if (orderPane.getColumnIndex(child) == 1 && child instanceof Label) {
                secondColumn.add((Label) child);
            }
        }
        for (int a = 1; a <= secondColumn.size() - 1; a++) {
            String second = secondColumn.get(a).toString();
            String[] partsLabelCena = second.split("]");
            String[] nextPartsLabelCena = partsLabelCena[1].split("'");
            if (!nextPartsLabelCena[1].equals("Итог")) {
                secondColumnString.add(nextPartsLabelCena[1]);
            }
        }
        for (Node child : orderPane.getChildren()) {
            if (orderPane.getColumnIndex(child) == 2 && child instanceof Label) {
                thirdColumn.add((Label) child);
            }
        }
        for (int a = 1; a <= thirdColumn.size() - 1; a++) {
            String third = thirdColumn.get(a).toString();
            String[] partsLabelCena = third.split("]");
            String[] nextPartsLabelCena = partsLabelCena[1].split("'");
            if (!nextPartsLabelCena[1].equals("Итог")) {
                thirdColumnString.add(nextPartsLabelCena[1]);
            }
        }
        for (Node child : orderPane.getChildren()) {
            if (orderPane.getColumnIndex(child) == 3 && child instanceof Label) {
                fourthColumn.add((Label) child);
            }
        }
        for (int a = 1; a <= fourthColumn.size() - 1; a++) {
            String fourth = fourthColumn.get(a).toString();
            String[] partsLabelCena = fourth.split("]");
            String[] nextPartsLabelCena = partsLabelCena[1].split("'");
            if (!nextPartsLabelCena[1].equals("Итог")) {
                fourthColumnString.add(nextPartsLabelCena[1]);
            }
        }
        int summa = 0;
        for (int i = 0; i<fourthColumnString.size(); i++){
            int chislo = Integer.parseInt(fourthColumnString.get(i));
            summa = summa + chislo;
        }
        summaLabel.setText(String.valueOf(summa));
        setFirstColumnForClasses(firstColumn);
        setSecondColumnForClasses(secondColumn);
        setThirdColumnForClasses(thirdColumn);
        setFourthColumnForClasses(fourthColumn);
        setSummaZakazaForClasses(summaLabel);
        setHaveOrderPain(true);
        setTries(getTries() + 1);
        fourthColumnStringAllClass = fourthColumnString;


        if (categoriesPageController.getGildiya() == 0) {
            backToMenuBtn.getScene().getWindow().hide();
            SceneChanger.changeScene("businessMenu.fxml", "Бизнес-Меню", 1000, 700);
        } else {
            backToMenuBtn.getScene().getWindow().hide();
            SceneChanger.changeScene("categoriesPage.fxml", "Заказ", 1000, 700);
        }

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
    void saveOrderButton(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException{

        if(getFirstColumnForClasses().size()<=unChangeableRow && ordersController.getManager() == false){
            Alert loginError = new Alert(Alert.AlertType.ERROR);
            loginError.setTitle("Ошибка");
            loginError.setHeaderText("Пустой заказ");
            loginError.setContentText("Вы ничего не добавили");
            loginError.show();
        }else{
            int idTable = dbHandler.getTablesByTitle(tablesController.getTableNumber());
            int idUser = dbUsers.getUserId();
            if (getRole()!=0){
                idUser = getRole();
            }
            setRole(idUser);
            int haveOrder = dbHandler.getIdOrdersByIdUsersAndIdTable(idUser, idTable);
            if(haveOrder>0){
                dbHandler.dropOrderInfo(haveOrder);
            }
            int idOrder = dbHandler.addOrder(idUser, idTable);
            firstColumnString.clear();
            secondColumnString.clear();
            thirdColumnString.clear();
            fourthColumnStringAllClass.clear();
            for (int b = 1; b<getFirstColumnForClasses().size(); b++){
                String first = getFirstColumnForClasses().get(b).toString();
                String[] partsLabelFirst = first.split("]");
                String[] nextPartsLabelFirst = partsLabelFirst[1].split("'");
                if (!nextPartsLabelFirst[1].equals("Позиция")) {
                    firstColumnString.add(nextPartsLabelFirst[1]);
                }
                String second = getSecondColumnForClasses().get(b).toString();
                String[] partsLabelSecond = second.split("]");
                String[] nextPartsLabelSecond = partsLabelSecond[1].split("'");
                if (!nextPartsLabelSecond[1].equals("Цена")) {
                    secondColumnString.add(nextPartsLabelSecond[1]);
                }
                String third = getThirdColumnForClasses().get(b).toString();
                String[] partsLabelThird = third.split("]");
                String[] nextPartsLabelThird = partsLabelThird[1].split("'");
                if (!nextPartsLabelThird[1].equals("Кол-во")) {
                    thirdColumnString.add(nextPartsLabelThird[1]);
                }
                String fourth = getFourthColumnForClasses().get(b).toString();
                String[] partsLabelFourth = fourth.split("]");
                String[] nextPartsLabelFourth = partsLabelFourth[1].split("'");
                if (!nextPartsLabelFourth[1].equals("Итог")) {
                    fourthColumnStringAllClass.add(nextPartsLabelFourth[1]);
                }
            }
            int summa = 0;
            for (int i = 0; i<fourthColumnStringAllClass.size(); i++){
                int chislo = Integer.parseInt(fourthColumnStringAllClass.get(i));
                summa = summa + chislo;
            }

            for (int i = 0; i<firstColumnString.size(); i++){
                for (int j = 0; j<firstColumnString.size(); j++){
                    if (firstColumnString.get(i).equals(firstColumnString.get(j)) && i != j){
                        int newKolvo = Integer.parseInt(thirdColumnString.get(j));
                        int oldKolvo = Integer.parseInt(thirdColumnString.get(i));
                        thirdColumnString.set(i,String.valueOf(oldKolvo+newKolvo));
                        firstColumnString.remove(j);
                        secondColumnString.remove(j);
                        thirdColumnString.remove(j);

                    }
                }
            }
            for (int c = 0; c<=firstColumnString.size()-1; c++){
                int price = Integer.parseInt(secondColumnString.get(c));
                int quantity = Integer.parseInt(thirdColumnString.get(c));
                dbHandler.addOrderInfo(idOrder,dbHandler.getIdPositionByPositionName(firstColumnString.get(c), price),quantity);
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Создан заказ");
            Label label = new Label("Заказ успешно создан.");
            label.setTextFill(Color.RED);
            label.setStyle("-fx-font-weight: bold");
            label.setFont(new Font(17));
            Button yesButton = new Button("Ок");
            yesButton.setOnAction(actionEvent1 -> {
                setFirstColumnForClasses(null);
                setSecondColumnForClasses(null);
                setThirdColumnForClasses(null);
                setFourthColumnForClasses(null);
                setHaveOrderPain(false);
                setTries(0);
                setRole(0);
                if (getSummaZakazaForClasses().getText().equals("0")){
                    try {
                        dbHandler.dropOrderInfo(idOrder);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                stage.close();
                saveOrderBtn.getScene().getWindow().hide();
                ordersController.setManager(false);
                ordersController.setKassir(false);
                SceneChanger.changeScene("mainMenu.fxml", "МЕНЮ", 1000, 700);
            });
            VBox layot = new VBox(10);
            layot.getChildren().addAll(label, yesButton);
            layot.setAlignment(Pos.CENTER);
            layot.setStyle("-fx-background-color: #171717");
            Scene scene = new Scene(layot,500,250);
            stage.setScene(scene);
            stage.showAndWait();
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

        if (ordersController.getKassir()==false){
            kassirBtn.setVisible(false);
        }


        ScrollPane scrollPane = new ScrollPane(orderPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        try {

            dbHandler.getDbConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (getTries() == 0) {
            Label label = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            Label label3 = new Label();
            label.setText("Позиция");
            label.setTextFill(Color.RED);
            label.setStyle("-fx-font-weight: bold");
            label.setFont(new Font(17));
            orderPane.add(label, 0, 0);
            orderPane.setHalignment(label, HPos.CENTER);
            label1.setText("Цена");
            label1.setTextFill(Color.RED);
            label1.setStyle("-fx-font-weight: bold");
            label1.setFont(new Font(17));
            orderPane.add(label1, 1, 0);
            orderPane.setHalignment(label1, HPos.CENTER);
            label2.setText("Кол-во");
            label2.setTextFill(Color.RED);
            label2.setStyle("-fx-font-weight: bold");
            label2.setFont(new Font(17));
            orderPane.add(label2, 2, 0);
            orderPane.setHalignment(label2, HPos.CENTER);
            label3.setText("Итог");
            label3.setTextFill(Color.RED);
            label3.setStyle("-fx-font-weight: bold");
            label3.setFont(new Font(17));
            orderPane.add(label3, 3, 0);
            orderPane.setHalignment(label3, HPos.CENTER);
        }
        if (getHaveOrderPain() == true) {
            if (getTries() == 0) {
                Label label = new Label();
                Label label1 = new Label();
                Label label2 = new Label();
                Label label3 = new Label();
                label.setText("Позиция");
                label.setTextFill(Color.RED);
                label.setStyle("-fx-font-weight: bold");
                label.setFont(new Font(17));
                orderPane.add(label, 0, 0);
                orderPane.setHalignment(label, HPos.CENTER);
                label1.setText("Цена");
                label1.setTextFill(Color.RED);
                label1.setStyle("-fx-font-weight: bold");
                label1.setFont(new Font(17));
                orderPane.add(label1, 1, 0);
                orderPane.setHalignment(label1, HPos.CENTER);
                label2.setText("Кол-во");
                label2.setTextFill(Color.RED);
                label2.setStyle("-fx-font-weight: bold");
                label2.setFont(new Font(17));
                orderPane.add(label2, 2, 0);
                orderPane.setHalignment(label2, HPos.CENTER);
                label3.setText("Итог");
                label3.setTextFill(Color.RED);
                label3.setStyle("-fx-font-weight: bold");
                label3.setFont(new Font(17));
                orderPane.add(label3, 3, 0);
                orderPane.setHalignment(label3, HPos.CENTER);
                for (int q = 0; q <= getFirstColumnForClasses().size()-1; q++) {
                    if (q >= 1) {
                        RowConstraints row = new RowConstraints(100);
                        orderPane.add(getFirstColumnForClasses().get(q), stolbec1, q+1);
                        orderPane.add(getSecondColumnForClasses().get(q), stolbec2, q+1);
                        orderPane.add(getThirdColumnForClasses().get(q), stolbec3, q+1);
                        orderPane.add(getFourthColumnForClasses().get(q), stolbec4, q+1);
                        orderPane.getRowConstraints().add(row);
                    } else {
                        orderPane.add(getFirstColumnForClasses().get(q), stolbec1, q+1);
                        orderPane.add(getSecondColumnForClasses().get(q), stolbec2, q+1);
                        orderPane.add(getThirdColumnForClasses().get(q), stolbec3, q+1);
                        orderPane.add(getFourthColumnForClasses().get(q), stolbec4, q+1);
                    }

                }
                summaLabel.setText(getSummaZakazaForClasses().getText());
            }else {
                for (int q = 0; q <= getFirstColumnForClasses().size() - 1; q++) {
                    if (q >= 1) {
                        RowConstraints row = new RowConstraints(100);
                        orderPane.add(getFirstColumnForClasses().get(q), stolbec1, q);
                        orderPane.add(getSecondColumnForClasses().get(q), stolbec2, q);
                        orderPane.add(getThirdColumnForClasses().get(q), stolbec3, q);
                        orderPane.add(getFourthColumnForClasses().get(q), stolbec4, q);
                        orderPane.getRowConstraints().add(row);
                    } else {
                        orderPane.add(getFirstColumnForClasses().get(q), stolbec1, q);
                        orderPane.add(getSecondColumnForClasses().get(q), stolbec2, q);
                        orderPane.add(getThirdColumnForClasses().get(q), stolbec3, q);
                        orderPane.add(getFourthColumnForClasses().get(q), stolbec4, q);
                    }

                }
                summaLabel.setText(getSummaZakazaForClasses().getText());
            }
        }
        if (ordersController.getHaveOrder()){
            // листы Label для заполнения GridPane и дальнейшей работы
            List<Label> firstColumn = new ArrayList<>();
            List<Label> secondColumn = new ArrayList<>();
            List<Label> thirdColumn = new ArrayList<>();
            List<Label> fourthColumn = new ArrayList<>();
            // листы String для конвертирования их в Label
            List<String> firstColumnString = new ArrayList<>();
            List<String> secondColumnString = new ArrayList<>();
            List<String> thirdColumnString = new ArrayList<>();
            List<String> fourthColumnString = new ArrayList<>();
            // перед определнием листов String подготавливаем нужные данные
            int idTables = dbHandler.getIdTableByTableNumber(ordersController.getTableNumber());
            int idUser = dbHandler.getOwnerOrderByIdTables(idTables);
            setRole(idUser);
            int idOrder = dbHandler.getIdOrdersByIdUsersAndIdTable(idUser, idTables);
            List<Integer> idPositions = new ArrayList<>();
            idPositions = dbHandler.getIdPositionsByOrder(idOrder); // айди Позиций в листе Integer
            List<Integer> positionsPrice = new ArrayList<>();
            List<Integer> quantityPositions = new ArrayList<>();
            quantityPositions = dbHandler.getQuantityPositionsByOrder(idOrder); // количество каждой позиции в заказе в листе Integer
            for (int i = 0; i < idPositions.size(); i++){
                positionsPrice.add(dbHandler.getPositionPriceByIdPosition(idPositions.get(i))); // цена Позиций в листе Integer
            }
            List<String> positionsName = new ArrayList<>();
            // все конвертируем в листы String, лист цены мы посчитаем сами
            for (int i = 0; i < idPositions.size(); i++){
                firstColumnString.add(dbHandler.getPositionNameById(idPositions.get(i)));
                secondColumnString.add(String.valueOf(positionsPrice.get(i)));
                thirdColumnString.add(String.valueOf(quantityPositions.get(i)));
                fourthColumnString.add(String.valueOf(quantityPositions.get(i) * positionsPrice.get(i)));
            }

            for (int i = 0; i < firstColumnString.size(); i++){
                Label position = new Label();
                Label pricePosition = new Label();
                Label quantity = new Label();
                Label itogovayaCena = new Label();
                pricePosition.setText(secondColumnString.get(i));
                pricePosition.setTextFill(Color.WHITE);
                position.setText(firstColumnString.get(i));
                position.setTextFill(Color.WHITE);
                quantity.setText(thirdColumnString.get(i));
                quantity.setTextFill(Color.WHITE);
                itogovayaCena.setTextFill(Color.WHITE);
                itogovayaCena.setText(fourthColumnString.get(i));
                firstColumn.add(position);
                secondColumn.add(pricePosition);
                thirdColumn.add(quantity);
                fourthColumn.add(itogovayaCena);
            }

            int summa = 0;
            for (int i = 0; i<fourthColumnString.size(); i++){
                int chislo = Integer.parseInt(fourthColumnString.get(i));
                summa = summa + chislo;
            }

            summaLabel.setText(String.valueOf(summa));
            setFirstColumnForClasses(firstColumn);
            setSecondColumnForClasses(secondColumn);
            setThirdColumnForClasses(thirdColumn);
            setFourthColumnForClasses(fourthColumn);
            fourthColumnStringAllClass = fourthColumnString;
            setSummaZakazaForClasses(summaLabel);
            setHaveOrderPain(true);
            setUnChangeableRow(firstColumn.size());

            for (int q = 0; q <= getFirstColumnForClasses().size() - 1; q++) {
                if (q >= 0) {
                    RowConstraints row = new RowConstraints(100);
                    orderPane.add(getFirstColumnForClasses().get(q), stolbec1, q+1);
                    orderPane.add(getSecondColumnForClasses().get(q), stolbec2, q+1);
                    orderPane.add(getThirdColumnForClasses().get(q), stolbec3, q+1);
                    orderPane.add(getFourthColumnForClasses().get(q), stolbec4, q+1);
                    orderPane.getRowConstraints().add(row);
                } else {
                    orderPane.add(getFirstColumnForClasses().get(q), stolbec1, q+1);
                    orderPane.add(getSecondColumnForClasses().get(q), stolbec2, q+1);
                    orderPane.add(getThirdColumnForClasses().get(q), stolbec3, q+1);
                    orderPane.add(getFourthColumnForClasses().get(q), stolbec4, q+1);
                }

            }
            ordersController.setHaveOrder(false);
            setHaveOrderPain(true);
        }
        if (getHaveOrderPain() == false) {
            summaLabel.setText("0");
        }

        tableLabel.setText(tablesController.getTableNumber());
        fioLabel.setText(dbUsers.getUserSurname() + " " + dbUsers.getUserName());


        int idCategories = 0;
        if (businessMenuController.getBlPosition() != "") {
            idCategories = dbHandler.getCategoriesByBtn(businessMenuController.getBlPosition());
        }
        if (categoriesPageController.getMenuCategories() != "") {
            idCategories = dbHandler.getCategoriesByBtn(categoriesPageController.getMenuCategories());
        }


        positionsGrid.setHalignment(onePageBtn, HPos.CENTER);
        positionsGrid.setHalignment(twoPageBtn, HPos.CENTER);


        List<String> positions = new ArrayList<>();
        positions = dbHandler.getPositionsById(idCategories);

        countPositions = dbHandler.getCountPositions(idCategories);
        int z = 1;
        int z1 = 0;
        for (int i = 0; i < countPositions; i++) {
            // создаем кнопку каждый раз, настраиваем и создаем ему Action
            Button button = new Button();
            button.setMinWidth(142.0);
            button.setMinHeight(37.0);
            button.setText(positions.get(i));
            button.setId(positions.get(i));
            button.setStyle("-fx-background-color: #ffc800; -fx-background-radius: 20;");
            int finalIdCategories = idCategories;
            button.setOnAction(actionEvent -> {
                List<String> fourthColumnString = new ArrayList<>();
                fourthColumnString.clear();
                int summaZakaza = 0;
                have = false;
                int itogCena = 0;
                int maxKolvo = 0;
                Object source = actionEvent.getSource();
                String source1 = source.toString();
                String[] parts = source1.split("]");
                String[] nextParts = parts[1].split("'");
                try {
                    maxKolvo = dbHandler.getQuantityInStockByPositionName(nextParts[1]);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                int price = 0;
                int quantityNow = 1;
                try {
                    price = dbHandler.getPriceByPositionNameAndCategories(nextParts[1], finalIdCategories);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                int quantityInStock = 1;
                try {
                    quantityInStock = dbHandler.getQuantityInStockByPositionNameAndCategories(nextParts[1], finalIdCategories);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Label position = new Label();
                Label pricePosition = new Label();
                Label quantity = new Label();
                Label itogovayaCena = new Label();
                pricePosition.setText(String.valueOf(price));
                pricePosition.setTextFill(Color.YELLOW);
                position.setText(nextParts[1]);
                position.setTextFill(Color.YELLOW);
                quantity.setText(String.valueOf(quantityNow));
                quantity.setTextFill(Color.YELLOW);
                itogovayaCena.setTextFill(Color.YELLOW);

                List<Label> firstColumn = new ArrayList<>();
                List<Label> secondColumn = new ArrayList<>();
                List<Label> thirdColumn = new ArrayList<>();
                List<Label> fourthColumn = new ArrayList<>();
                fourthColumn.clear();


                String searchText = nextParts[1]; // ищем во всем гридпейн такое же название
                String searchText2 = String.valueOf(price); // ищем во всем гридпейн такую же цену
                int columnIndex = 2; // индекс столбца для замены ( в нашем случае количество )
                int columnIndex2 = 3; // для итоговой цены
                for (Node node : orderPane.getChildren()) {
                    if (node instanceof Label && orderPane.getRowIndex(node) != null && orderPane.getRowIndex(node) > getUnChangeableRow()) {
                        Label labelKolva = (Label) node;
                        if (labelKolva.getText().equals(searchText) && orderPane.getColumnIndex(node) == 0) {
                            Label otherLabel = (Label) orderPane.getChildren().filtered(n -> orderPane.getRowIndex(n) == orderPane.getRowIndex(node) && orderPane.getColumnIndex(n) == 1).get(0);
                            if (otherLabel.getText().equals(searchText2)) {
                                Label thirdLabel = (Label) orderPane.getChildren().filtered(n -> orderPane.getRowIndex(n) == orderPane.getRowIndex(node) && orderPane.getColumnIndex(n) == columnIndex).get(0);
                                Label fourthLabel = (Label) orderPane.getChildren().filtered(n -> orderPane.getRowIndex(n) == orderPane.getRowIndex(node) && orderPane.getColumnIndex(n) == columnIndex2).get(0);
                                String kolvo = thirdLabel.getText();
                                int kolichetsvo = Integer.parseInt(kolvo);
                                kolichetsvo = kolichetsvo + 1;
                                if (kolichetsvo > maxKolvo){
                                    Alert loginError = new Alert(Alert.AlertType.ERROR);
                                    loginError.setTitle("Ошибка!");
                                    loginError.setHeaderText("Максимальное количество");
                                    loginError.setContentText("Достигнуто максимальное количество");
                                    loginError.show();
                                    kolichetsvo = kolichetsvo-1;
                                    thirdLabel.setText(String.valueOf(kolichetsvo));
                                }
                                thirdLabel.setText(String.valueOf(kolichetsvo));
                                String cenna = otherLabel.getText();
                                int cena = Integer.parseInt(cenna);
                                itogCena = cena * kolichetsvo;
                                fourthLabel.setText(String.valueOf(itogCena));
                                have = true;
                                for (Node child : orderPane.getChildren()) {
                                    if (orderPane.getColumnIndex(child) == columnIndex2 && child instanceof Label) {
                                        fourthColumn.add((Label) child);
                                    }
                                }
                                for (int a = 1; a <= fourthColumn.size() - 1; a++) {
                                    String fourth = fourthColumn.get(a).toString();
                                    String[] partsLabelCena = fourth.split("]");
                                    String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                                    if (!nextPartsLabelCena[1].equals("Итог")) {
                                        fourthColumnString.add(nextPartsLabelCena[1]);
                                    }
                                }
                                for (int g = 0; g <= fourthColumnString.size() - 1; g++) {
                                    String chislo = fourthColumnString.get(g);
                                    int chisloInt = Integer.parseInt(chislo);
                                    summaZakaza = summaZakaza + chisloInt;
                                    summaLabel.setText(String.valueOf(summaZakaza));
                                }
                                fourthColumnStringAllClass = fourthColumnString;
                            }
                        }
                    }
                }


                if (have == false) {
                    RowConstraints row = new RowConstraints(100);
                    itogovayaCena.setText(pricePosition.getText());
                    if(quantityNow > maxKolvo){
                        Alert loginError = new Alert(Alert.AlertType.ERROR);
                        loginError.setTitle("Ошибка!");
                        loginError.setHeaderText("Максимальное количество");
                        loginError.setContentText("Достигнуто максимальное количество");
                        loginError.show();
                        quantityNow = 0;
                        quantity.setText(String.valueOf(quantityNow));
                        itogCena = 0;
                        itogovayaCena.setText(String.valueOf(itogCena));
                    }
                    orderPane.getRowConstraints().add(row);
                    orderPane.add(position, stolbec1, orderPane.getRowCount() - 1);
                    orderPane.add(pricePosition, stolbec2, orderPane.getRowCount() - 1);
                    orderPane.add(quantity, stolbec3, orderPane.getRowCount() - 1);
                    orderPane.add(itogovayaCena, stolbec4, orderPane.getRowCount() - 1);
                    orderPane.setHalignment(position, HPos.CENTER);
                    orderPane.setHalignment(pricePosition, HPos.CENTER);
                    orderPane.setHalignment(quantity, HPos.CENTER);
                    orderPane.setHalignment(itogovayaCena, HPos.CENTER);
                    for (Node child : orderPane.getChildren()) {
                        if (orderPane.getColumnIndex(child) == columnIndex2 && child instanceof Label) {
                            fourthColumn.add((Label) child);
                        }
                    }
                    for (int a = 1; a <= fourthColumn.size() - 1; a++) {
                        String fourth = fourthColumn.get(a).toString();
                        String[] partsLabelCena = fourth.split("]");
                        String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                        if (!nextPartsLabelCena[1].equals("Итог")) {
                            fourthColumnString.add(nextPartsLabelCena[1]);
                        }
                    }
                    for (int g = 0; g <= fourthColumnString.size() - 1; g++) {
                        String chislo = fourthColumnString.get(g);
                        int chisloInt = Integer.parseInt(chislo);
                        summaZakaza = summaZakaza + chisloInt;
                        summaLabel.setText(String.valueOf(summaZakaza));
                    }
                }
                firstColumn.clear();
                secondColumn.clear();
                thirdColumn.clear();
                fourthColumn.clear();
                for (Node child : orderPane.getChildren()) {
                    if (orderPane.getColumnIndex(child) == 0 && child instanceof Label) {
                        firstColumn.add((Label) child);
                    }
                }

                for (Node child : orderPane.getChildren()) {
                    if (orderPane.getColumnIndex(child) == 1 && child instanceof Label) {
                        secondColumn.add((Label) child);
                    }
                }
                for (Node child : orderPane.getChildren()) {
                    if (orderPane.getColumnIndex(child) == 2 && child instanceof Label) {
                        thirdColumn.add((Label) child);
                    }
                }
                for (Node child : orderPane.getChildren()) {
                    if (orderPane.getColumnIndex(child) == 3 && child instanceof Label) {
                        fourthColumn.add((Label) child);
                    }
                }
                for (int a = 1; a <= firstColumn.size() - 1; a++) {
                    String first = firstColumn.get(a).toString();
                    String[] partsLabelCena = first.split("]");
                    String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                    if (!nextPartsLabelCena[1].equals("Итог")) {
                        firstColumnString.add(nextPartsLabelCena[1]);
                    }
                }
                for (int a = 1; a <= secondColumn.size() - 1; a++) {
                    String second = secondColumn.get(a).toString();
                    String[] partsLabelCena = second.split("]");
                    String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                    if (!nextPartsLabelCena[1].equals("Итог")) {
                        secondColumnString.add(nextPartsLabelCena[1]);
                    }
                }
                for (int a = 1; a <= thirdColumn.size() - 1; a++) {
                    String third = thirdColumn.get(a).toString();
                    String[] partsLabelCena = third.split("]");
                    String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                    if (!nextPartsLabelCena[1].equals("Итог")) {
                        thirdColumnString.add(nextPartsLabelCena[1]);
                    }
                }
                for (int a = 1; a <= fourthColumn.size() - 1; a++) {
                    String fourth = fourthColumn.get(a).toString();
                    String[] partsLabelCena = fourth.split("]");
                    String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                    if (!nextPartsLabelCena[1].equals("Итог")) {
                        fourthColumnString.add(nextPartsLabelCena[1]);
                    }
                }
                setFirstColumnForClasses(firstColumn);
                setSecondColumnForClasses(secondColumn);
                setThirdColumnForClasses(thirdColumn);
                setFourthColumnForClasses(fourthColumn);
                fourthColumnStringAllClass = fourthColumnString;
                setSummaZakazaForClasses(summaLabel);
                setHaveOrderPain(true);
                setTries(getTries() + 1);
            });
            buttonList.add(button);
            if (z == 1 && z1 == 0) {
                positionsGrid.add(button, z, z1);
                positionsGrid.setHalignment(button, HPos.CENTER);
                z1++;
                continue;
            }
            if (z == 1 && z1 == 1) {
                positionsGrid.add(button, z, z1);
                positionsGrid.setHalignment(button, HPos.CENTER);
                z = 0;
                z1 = 1;
                continue;
            }
            if (z == 0 && z1 == 1) {
                positionsGrid.add(button, z, z1);
                positionsGrid.setHalignment(button, HPos.CENTER);
                z1++;
                continue;
            }
            if (z == 0 && z1 == 2) {
                positionsGrid.add(button, z, z1);
                positionsGrid.setHalignment(button, HPos.CENTER);
                z = 1;
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

        if (countPositions > 4) {
            pages = (countPositions / 4) + 1;
            int a = countPositions / 4; // сколько раз мы можем заполнить 4-мя позициями
            for (int i = 1; i <= a; i++) {
                chislaUp.add(chisloUp);
                chisloUp = chisloUp + 4;
            }
            if ((countPositions % 4) != 0) {
                chislaUp.add(buttonList.size() - 1);
            }
        } else {
            pages = 1;
            int a = 1;
            chislaUp.add(buttonList.size() - 1);
        }

        for (
                int i = 1;
                i <= pages; i++) {
            chislaDown.add(chisloDown);
            chisloDown = chisloDown + 4;
        }
        for (
                int i = 0;
                i < pages; i++) {
            chislaPages.add(i);
        }
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Удалить");
        orderPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                if (contextMenu.isShowing()){
                    contextMenu.hide();
                }
                int rowIndex = (int) event.getY() / 100;
                deleteItem.setOnAction(e -> {
                    List<Label> firstColumn = new ArrayList<>();
                    List<Label> secondColumn = new ArrayList<>();
                    List<Label> thirdColumn = new ArrayList<>();
                    List<Label> fourthColumn = new ArrayList<>();
                    List<String> fourthColumnString = new ArrayList<>();
                    int columnIndex2 = 3;
                    int summaZakaza = 0;
                    String role = "";
                    if(ordersController.getManager()){
                        role = "Менеджер";
                    }
                    if (rowIndex>=orderPane.getRowCount()){
                        Alert loginError = new Alert(Alert.AlertType.ERROR);
                        loginError.setTitle("Ошибка");
                        loginError.setHeaderText("Пустое место");
                        loginError.setContentText("Вы не выделили строку");
                        loginError.show();
                        return;
                    }else if(rowIndex<=getUnChangeableRow() && !role.equals("Менеджер")){
                        Alert loginError = new Alert(Alert.AlertType.ERROR);
                        loginError.setTitle("Ошибка");
                        loginError.setHeaderText("Позиция уже сохранена");
                        loginError.setContentText("Обратитесь к менеджеру");
                        loginError.show();
                        return;
                    }else if(rowIndex==0){
                        Alert loginError = new Alert(Alert.AlertType.ERROR);
                        loginError.setTitle("Ошибка");
                        loginError.setHeaderText("Эту строчку нельзя удалить");
                        loginError.setContentText("Строку нельзя удалить, попробуйте еще раз");
                        loginError.show();
                    }else{
                        orderPane.getChildren().removeIf(node -> orderPane.getRowIndex(node) == rowIndex);
                        orderPane.getRowConstraints().remove(rowIndex);
                        orderPane.getChildren().forEach(node -> {
                            Integer nodeRowIndex = orderPane.getRowIndex(node);
                            if (nodeRowIndex != null && nodeRowIndex > rowIndex) {
                                orderPane.setRowIndex(node, nodeRowIndex - 1);
                            }
                            fourthColumn.clear();
                            fourthColumnString.clear();
                        });
                        fourthColumn.clear();
                        fourthColumnString.clear();
                        for (Node child : orderPane.getChildren()) {
                            if (orderPane.getColumnIndex(child) == columnIndex2 && child instanceof Label) {
                                fourthColumn.add((Label) child);
                            }
                        }
                        for (int a = 1; a <= fourthColumn.size() - 1; a++) {
                            String fourth = fourthColumn.get(a).toString();
                            String[] partsLabelCena = fourth.split("]");
                            String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                            if (!nextPartsLabelCena[1].equals("Итог")) {
                                fourthColumnString.add(nextPartsLabelCena[1]);
                            }
                        }
                        for (int g = 0; g <= fourthColumnString.size() - 1; g++) {
                            String chislo = fourthColumnString.get(g);
                            int chisloInt = Integer.parseInt(chislo);
                            summaZakaza = summaZakaza + chisloInt;
                            summaLabel.setText(String.valueOf(summaZakaza));
                        }
                        if (fourthColumnString.size() == 0) {
                            summaLabel.setText(("0"));
                        }
                        firstColumn.clear();
                        secondColumn.clear();
                        thirdColumn.clear();
                        fourthColumn.clear();
                        for (Node child : orderPane.getChildren()) {
                            if (orderPane.getColumnIndex(child) == 0 && child instanceof Label) {
                                firstColumn.add((Label) child);
                            }
                        }
                        for (int a = 1; a <= firstColumn.size() - 1; a++) {
                            String first = firstColumn.get(a).toString();
                            String[] partsLabelCena = first.split("]");
                            String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                            if (!nextPartsLabelCena[1].equals("Итог")) {
                                firstColumnString.add(nextPartsLabelCena[1]);
                            }
                        }

                        for (Node child : orderPane.getChildren()) {
                            if (orderPane.getColumnIndex(child) == 1 && child instanceof Label) {
                                secondColumn.add((Label) child);
                            }
                        }
                        for (int a = 1; a <= secondColumn.size() - 1; a++) {
                            String second = secondColumn.get(a).toString();
                            String[] partsLabelCena = second.split("]");
                            String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                            if (!nextPartsLabelCena[1].equals("Итог")) {
                                secondColumnString.add(nextPartsLabelCena[1]);
                            }
                        }
                        for (Node child : orderPane.getChildren()) {
                            if (orderPane.getColumnIndex(child) == 2 && child instanceof Label) {
                                thirdColumn.add((Label) child);
                            }
                        }
                        for (int a = 1; a <= thirdColumn.size() - 1; a++) {
                            String third = thirdColumn.get(a).toString();
                            String[] partsLabelCena = third.split("]");
                            String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                            if (!nextPartsLabelCena[1].equals("Итог")) {
                                thirdColumnString.add(nextPartsLabelCena[1]);
                            }
                        }
                        for (Node child : orderPane.getChildren()) {
                            if (orderPane.getColumnIndex(child) == 3 && child instanceof Label) {
                                fourthColumn.add((Label) child);
                            }
                        }
                        for (int a = 1; a <= fourthColumn.size() - 1; a++) {
                            String fourth = fourthColumn.get(a).toString();
                            String[] partsLabelCena = fourth.split("]");
                            String[] nextPartsLabelCena = partsLabelCena[1].split("'");
                            if (!nextPartsLabelCena[1].equals("Итог")) {
                                fourthColumnString.add(nextPartsLabelCena[1]);
                            }
                        }
                        setFirstColumnForClasses(firstColumn);
                        setSecondColumnForClasses(secondColumn);
                        setThirdColumnForClasses(thirdColumn);
                        setFourthColumnForClasses(fourthColumn);
                        setSummaZakazaForClasses(summaLabel);
                        setHaveOrderPain(true);
                        setTries(getTries() + 1);
                        fourthColumnStringAllClass = fourthColumnString;
                    }
                });
                contextMenu.getItems().add(deleteItem);
                contextMenu.show(orderPane, event.getScreenX(), event.getScreenY());

            }
        });
    }

}
