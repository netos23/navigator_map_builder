package ru.fbtw.navigator.map_builder.auth;

public class UserData {
    private static String token;
    public static String getToken(){
        return token;
    }

    public static void setToken(String token) {
        UserData.token = token;
    }
}
