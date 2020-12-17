package ru.fbtw.navigator.map_builder.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import ru.fbtw.navigator.map_builder.auth.UserData;
import ru.fbtw.navigator.map_builder.controller.response.AuthResponse;
import ru.fbtw.navigator.map_builder.controller.response.BaseResponse;
import ru.fbtw.navigator.map_builder.controller.response.Response;
import ru.fbtw.navigator.map_builder.core.ProjectModel;

import java.io.IOException;
import java.util.Objects;

public class ProjectUpdateController implements Controller{

    private static final String URL = "http://localhost:8080/api/create_project";
    private ProjectModel projectModel;
    private OkHttpClient client;

    private static final ProjectUpdateController instance = new ProjectUpdateController();


    public static ProjectUpdateController getInstance() {
        return instance;
    }


    private ProjectUpdateController(){
        client = new OkHttpClient().newBuilder()
                .build();
    }

    public ProjectUpdateController setCreditLines(ProjectModel projectModel){
        this.projectModel = projectModel;

        return this;
    }

    @Override
    public BaseResponse execute() {
        if(projectModel != null){
            String requestBody = RequestUtil.parseBody(projectModel);

            return createProject(requestBody);
        }else{
            return null;
        }
    }

    private BaseResponse createProject(String body) {

        BaseResponse response = new Response();
        try {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(body, mediaType);

            Request request = new Request.Builder()
                    .url(URL)
                    .method("POST", requestBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + UserData.getToken())
                    .build();

            ResponseBody responseBody = client.newCall(request).execute().body();

            Objects.requireNonNull(responseBody);
            return parseBody(responseBody);


        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error while executing response");
        }
        return response;
    }

    private Response parseBody(ResponseBody body) throws  IOException {
        Response response = new Response();
        JsonObject jsonResponse = JsonParser.parseString(body.string()).getAsJsonObject();
        response.setSuccess(jsonResponse.get("status").getAsInt() == 200);
        response.setMessage(jsonResponse.get("message").getAsString());
        return response;
    }
}
