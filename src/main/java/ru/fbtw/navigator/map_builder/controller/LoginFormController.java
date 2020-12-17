package ru.fbtw.navigator.map_builder.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import ru.fbtw.navigator.map_builder.controller.response.AuthResponse;

import java.io.IOException;
import java.util.Objects;

public class LoginFormController implements Controller {
    private static final String URL = "http://localhost:8080/auth";

    private String login;
    private String password;
    private OkHttpClient client;
    private String token;

    private static final LoginFormController instance = new LoginFormController();
    private final AuthResponse EMPTY_RESPONSE;

    public static LoginFormController getInstance() {
        return instance;
    }

    private LoginFormController() {
        client = new OkHttpClient().newBuilder()
                .build();

        EMPTY_RESPONSE = new AuthResponse();
        EMPTY_RESPONSE.setSuccess(false);
        EMPTY_RESPONSE.setMessage("Fields should not be empty");

    }
    public LoginFormController setCreditLines(String login, String password){
        this.login = login;
        this.password = password;

        return this;
    }

    public AuthResponse execute() {
        if(login != null && password != null) {
            String body = RequestUtil.parseBody(login, password);
            return getToken(body);
        }

        return EMPTY_RESPONSE;
    }

    private AuthResponse getToken(String body) {
        AuthResponse authResponse = new AuthResponse();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(body, mediaType);

            Request request = new Request.Builder()
                    .url(URL)
                    .method("POST", requestBody)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            Objects.requireNonNull(response);
            String token = parseBody(response);
            authResponse.setSuccess(true);
            authResponse.setToken(token);

        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
            authResponse.setSuccess(false);
            authResponse.setMessage("Error while executing auth");
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
            authResponse.setSuccess(false);
            authResponse.setMessage("Invalid username and password combination");
        }
        return authResponse;
    }

    private String parseBody(Response response) throws IOException, PermissionDeniedException {
        ResponseBody body = response.body();

        Objects.requireNonNull(body);
        String stringBody = body.string();

        if(stringBody.isEmpty()){
            throw new IOException();
        }

        JsonObject responseJson = JsonParser.parseString(stringBody).getAsJsonObject();

        if(responseJson.has("status")){
            throw new PermissionDeniedException();
        }


        return responseJson.get("token").getAsString();
    }

    public boolean isCorrect() {
        return true;
    }

}
