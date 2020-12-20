package ru.fbtw.navigator.map_builder.web_controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.fbtw.navigator.map_builder.core.ProjectModel;

public class RequestUtil {
    private static Gson gson = new Gson();

    public static String parseBody(String login, String password){
        JsonObject request = new JsonObject();
        request.addProperty("login",login);
        request.addProperty("password",password);

        return gson.toJson(request);
    }

    public static String parseBody(ProjectModel model){
        return gson.toJson(model);
    }
}
