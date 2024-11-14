package com.example.kursovaya;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHandler {
    Connection dbConnection;
    DBUsers dbusers = new DBUsers();

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection("jdbc:mysql://10.207.144.159:3306/user089_db1?useUnicode=true&serverTimezone=UTC&characterEncoding=UTF-8", "user089_user1", "m_kie2Ph");
        return dbConnection;
    }


    public int getQuantityInStock(int id) throws ClassNotFoundException, SQLException {
        int quantity = -1;
        String query = "SELECT quantityInStock FROM position WHERE idPosition = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            quantity = rs.getInt("quantityInStock");
        }
        return quantity;
    }

    public void addOrderInfo(int idOrder, int idPosition, int quantity) throws ClassNotFoundException, SQLException{

        String insert = "INSERT INTO orders_has_position(Orders_idOrders, Position_idPosition, quantityPosition) VALUES (?,?,?)";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
        preparedStatement.setInt(1, idOrder);
        preparedStatement.setInt(2, idPosition);
        preparedStatement.setInt(3, quantity);
        preparedStatement.executeUpdate();
        preparedStatement.close();

        int newQuantity = getQuantityInStock(idPosition) - quantity;
        String update = "UPDATE position SET quantityInStock = ? WHERE idPosition = ?";
        PreparedStatement preparedStatement1 = getDbConnection().prepareStatement(update);
        preparedStatement1.setInt(1, newQuantity);
        preparedStatement1.setInt(2, idPosition);
        preparedStatement1.executeUpdate();
        preparedStatement1.close();
    }

    public void dropOrderInfo(int idOrder) throws ClassNotFoundException, SQLException{

        String drop1 = "delete from orders_has_position where Orders_idOrders = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(drop1);
        preparedStatement.setInt(1, idOrder);
        preparedStatement.executeUpdate();
        preparedStatement.close();

        String drop2 = "delete from orders where idOrders = ?";
        PreparedStatement preparedStatement1 = getDbConnection().prepareStatement(drop2);
        preparedStatement1.setInt(1, idOrder);
        preparedStatement1.executeUpdate();
        preparedStatement1.close();

    }



    public int getCountBusinessSalads() throws ClassNotFoundException, SQLException{
        int id = 0;
        String query = "SELECT count(*) from position where idCategories = 15";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            id = (rs.getInt("count(*)"));
        }
        rs.close();
        return id;
    }

    public int addOrder(int idUser, int idTables) throws ClassNotFoundException, SQLException {
        int orderId = -1;
        String status = "Новый";
        String query = "SELECT max(idOrders) a FROM orders";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            orderId = rs.getInt("a") + 1;
        }
        String insert = "INSERT INTO orders(idOrders, statusOrder, idUsers, idTables) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement1 = getDbConnection().prepareStatement(insert);
        preparedStatement1.setInt(1, orderId);
        preparedStatement1.setString(2, status);
        preparedStatement1.setInt(3, idUser);
        preparedStatement1.setInt(4, idTables);
        preparedStatement1.executeUpdate();
        preparedStatement1.close();
        return orderId;
    }

    public int userExist(String code) throws ClassNotFoundException, SQLException {
        int check = -1;
        String query = "SELECT idUsers FROM users WHERE userCode = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, code);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            check = rs.getInt("idUsers");
        }
        rs.close();
        return check;
    }

    public void loadUser(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE idUsers = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            dbusers.getUserInfo(id, rs.getString("userSurname"), rs.getString("userName"), rs.getString("userCode"), rs.getString("userRole"));
        }
    }



    public String roleById(String role) throws ClassNotFoundException, SQLException{
        int i = Integer.parseInt(role);
        String roles = "";
        String query = "SELECT roleName from roles WHERE idRoles = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, i);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            roles = rs.getString("roleName");
        }
        rs.close();
        return roles;
    }

    public int getCategoriesByBtn(String title) throws ClassNotFoundException, SQLException{
        int id = 0;
        String query = "SELECT idCategories from categories where title = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, title);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            id = (rs.getInt("idCategories"));
        }
        rs.close();

        return id;
    }

    public List<String> getPositionsById(int id) throws ClassNotFoundException, SQLException{
        List<String> info = new ArrayList<>();
        String query = "SELECT positionName from position where idCategories = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            info.add(rs.getString("positionName"));
        }
        rs.close();

        return info;
    }

    public int getCountPositions(int id) throws ClassNotFoundException, SQLException{
        int count = 0;
        String query = "SELECT count(*) from position where idCategories = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            count = (rs.getInt("count(*)"));
        }
        rs.close();
        return count;
    }

    public List<String> getCategoriesByGild(int id) throws ClassNotFoundException, SQLException{
        List<String> info = new ArrayList<>();
        String query = "SELECT title from categories where idGild= ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            info.add(rs.getString("title"));
        }
        rs.close();

        return info;
    }

    public int getCountCategories(int id) throws ClassNotFoundException, SQLException{
        int count = 0;
        String query = "SELECT count(*) from categories where idGild = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            count = (rs.getInt("count(*)"));
        }
        rs.close();
        return count;
    }

    public int getGildByTitle(String title) throws ClassNotFoundException, SQLException{
        int gild = 0;
        String query = "SELECT idGild from gild where title = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, title);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            gild = (rs.getInt("idGIld"));
        }
        rs.close();
        return gild;
    }

    public int getGildByCategory(String title) throws ClassNotFoundException, SQLException{
        int gild = 0;
        String query = "SELECT idGild from categories where title = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, title);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            gild = (rs.getInt("idGIld"));
        }
        rs.close();
        return gild;
    }

    public int getPriceByPositionNameAndCategories(String title, int idCategories) throws ClassNotFoundException, SQLException{
        int gild = 0;
        String query = "SELECT positionPrice from position where positionName = ? and idCategories = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, title);
        preparedStatement.setInt(2, idCategories);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            gild = (rs.getInt("positionPrice"));
        }
        rs.close();
        return gild;
    }

    public int getQuantityInStockByPositionNameAndCategories(String title, int idCategories) throws ClassNotFoundException, SQLException{
        int gild = 0;
        String query = "SELECT quantityInStock from position where positionName = ? and idCategories = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, title);
        preparedStatement.setInt(2, idCategories);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            gild = (rs.getInt("quantityInStock"));
        }
        rs.close();
        return gild;
    }

    public int getQuantityInStockByPositionName(String title) throws ClassNotFoundException, SQLException {
        int quantity = -1;
        String query = "SELECT quantityInStock FROM position WHERE positionName = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, title);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            quantity = rs.getInt("quantityInStock");
        }
        return quantity;
    }

    public int getTablesByTitle(String title) throws ClassNotFoundException, SQLException {
        int quantity = -1;
        String query = "SELECT idTables FROM tables WHERE tableNumber = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, title);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            quantity = rs.getInt("idTables");
        }
        return quantity;
    }

    public int getIdPositionByPositionName(String title, int price) throws ClassNotFoundException, SQLException {
        int quantity = -1;
        String query = "SELECT idPosition FROM position WHERE positionName = ? and positionPrice = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, title);
        preparedStatement.setInt(2, price);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            quantity = rs.getInt("idPosition");
        }
        return quantity;
    }

    public int getCountOrders(int idUser) throws ClassNotFoundException, SQLException {
        int quantity = -1;
        String query = "select count(idOrders) from orders where idUsers = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idUser);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            quantity = rs.getInt("count(idOrders)");
        }
        return quantity;
    }

    public List<String> getTablesNumberByUser(int idUser) throws ClassNotFoundException, SQLException {
        List<String> quantity = new ArrayList<>();
        String query = "select idTables from orders where idUsers = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idUser);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            quantity.add(String.valueOf(rs.getInt("idTables")));
        }
        rs.close();
        return quantity;
    }

    public String getTablesNumberById(int idTables) throws ClassNotFoundException, SQLException {
        String tablesNumber = "";
        String query = "select tableNumber from tables where idTables = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idTables);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            tablesNumber = rs.getString("tableNumber");
        }
        rs.close();
        return tablesNumber;
    }
    public int getIdRoleByRoleName(String roleName) throws ClassNotFoundException, SQLException {
        int idRole = 0;
        String query = "select idRoles from roles where roleName = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, roleName);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            idRole = rs.getInt("idRoles");
        }
        rs.close();
        return idRole;
    }


    public int getIdOrdersByIdUsersAndIdTable(int idUser, int idTables) throws ClassNotFoundException, SQLException {
        int idOrders = 0;
        String query = "select idOrders from orders where idUsers = ? and idTables = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idUser);
        preparedStatement.setInt(2, idTables);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            idOrders = rs.getInt("idOrders");
        }
        rs.close();
        return idOrders;
    }

    public int getIdTableByTableNumber(String idTables) throws ClassNotFoundException, SQLException {
        int idTable = 0;
        String query = "select idTables from tables where tableNumber = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, idTables);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            idTable = rs.getInt("idTables");
        }
        rs.close();
        return idTable;
    }

    public List<Integer> getIdPositionsByOrder(int idOrder) throws ClassNotFoundException, SQLException {
        List<Integer> idPositions = new ArrayList<>();
        String query = "select Position_idPosition from orders_has_position where Orders_idOrders = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idOrder);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            idPositions.add(rs.getInt("Position_idPosition"));
        }
        rs.close();
        return idPositions;
    }
    public List<Integer> getQuantityPositionsByOrder(int idOrder) throws ClassNotFoundException, SQLException {
        List<Integer> quantityPositions = new ArrayList<>();
        String query = "select quantityPosition from orders_has_position where Orders_idOrders = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idOrder);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            quantityPositions.add(rs.getInt("quantityPosition"));
        }
        rs.close();
        return quantityPositions;
    }

    public int getPositionPriceByIdPosition(int idPosition) throws ClassNotFoundException, SQLException {
        int pricePosition = 0;
        String query = "select positionPrice from position where idPosition = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idPosition);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            pricePosition = rs.getInt("positionPrice");
        }
        rs.close();
        return pricePosition;
    }

    public String getPositionNameById(int idPosition) throws ClassNotFoundException, SQLException {
        String positionName = "";
        String query = "select positionName from position where idPosition = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idPosition);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            positionName = rs.getString("positionName");
        }
        rs.close();
        return positionName;
    }

    public List<String> getAllIdTablesInOrders() throws ClassNotFoundException, SQLException {
        List<String> idTables = new ArrayList<>();
        String query = "select idTables from orders";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
             idTables.add(rs.getString("idTables"));
        }
        rs.close();
        return idTables;
    }

    public int getOwnerOrderByIdTables(int idTable) throws ClassNotFoundException, SQLException {
        int owner = 0;
        String query = "select idUsers from orders where idTables = ?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, idTable);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            owner = rs.getInt("idUsers");
        }
        rs.close();
        return owner;
    }

    public List<String> getRoleNames() throws ClassNotFoundException, SQLException {
        List<String> roleNames = new ArrayList<>();
        String query = "SELECT roleName FROM roles where roleName not like 'Директор'";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            roleNames.add(rs.getString("roleName"));
        }
        rs.close();
        return roleNames;
    }

    public  String insertUser(String usCode, String usName, String usSurname, int usRole) throws  SQLException, ClassNotFoundException{
        String vivod ="";
        CallableStatement proc = getDbConnection().prepareCall("CALL AddUser(?, ?, ?, ?)");
        proc.setString(1, usCode);
        proc.setString(2, usName);
        proc.setString(3, usSurname);
        proc.setInt(4, usRole);

        ResultSet res =proc.executeQuery();
        while (res.next()){
            vivod = res.getString("Success") ;
        }
        return  vivod;
    }

}
