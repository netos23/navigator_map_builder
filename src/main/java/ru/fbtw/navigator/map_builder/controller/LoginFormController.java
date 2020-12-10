package ru.fbtw.navigator.map_builder.controller;

import okhttp3.OkHttpClient;
import ru.fbtw.navigator.map_builder.auth.UserData;

public class LoginFormController {
    private String login;
    private String password;
    private OkHttpClient client;
    private String token;

    private static final LoginFormController instance = new LoginFormController();

    public static LoginFormController getInstance(){
        return instance;
    }

    private LoginFormController() {
        client = new OkHttpClient().newBuilder()
                .build();
    }

    public void execute(String login, String password){

    }

    public boolean isCorrect(){
        return true;
    }



    public UserData getUserData() {
        return new UserData("ddfdsfdsf","Netos23");
    }
}
