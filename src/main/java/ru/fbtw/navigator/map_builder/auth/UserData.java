package ru.fbtw.navigator.map_builder.auth;

public class UserData {
    private String token;
    private String userName;

    public UserData(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }
}
