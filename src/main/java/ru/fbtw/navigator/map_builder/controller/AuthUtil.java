package ru.fbtw.navigator.map_builder.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AuthUtil {
    private static Gson gson = new Gson();

    public static String parseBody(String login, String password){
        JsonObject request = new JsonObject();
        request.addProperty("login",login);
        request.addProperty("password",password);

        return gson.toJson(request);
    }
}
