package com.example.kursovaya;

public class DBUsers {
    private static int Users_ID;
    private static String Users_Code;
    private static String Users_Name;
    private static String Users_Surname;
    private static String Users_Role;

    public static void getUserInfo(int id, String surname, String name, String code, String role){
        Users_ID = id;
        Users_Code = code;
        Users_Surname = surname;
        Users_Name = name;
        Users_Role = role;
    }

    public static int getUserId() {
        return Users_ID;
    }

    public static String getUserSurname() {
        return Users_Surname;
    }

    public static String getUserName() {
        return Users_Name;
    }

    public static String getUserCode(){
        return Users_Code;
    }

    public static String getUserRole() {
        return Users_Role;
    }


}
