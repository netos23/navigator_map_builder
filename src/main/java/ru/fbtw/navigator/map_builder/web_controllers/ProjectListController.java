package ru.fbtw.navigator.map_builder.web_controllers;

import com.google.gson.*;
import okhttp3.*;
import ru.fbtw.navigator.map_builder.auth.UserData;
import ru.fbtw.navigator.map_builder.web_controllers.response.BaseResponse;
import ru.fbtw.navigator.map_builder.web_controllers.response.ListResponse;
import ru.fbtw.navigator.map_builder.web_controllers.response.Response;
import ru.fbtw.navigator.map_builder.core.ProjectModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectListController implements Controller {
    private static final String URL = "http://localhost:8080/api/";
    public static final String REMOVE = "remove_project";
    public static final String LIST = "project_list";

    private static final ProjectListController instance = new ProjectListController();

    private OkHttpClient client;
    private Gson gson;

    private String method;
    private String request_method;
    private ProjectModel target;

    private ProjectListController() {
        client = new OkHttpClient().newBuilder()
                .build();
        gson = new Gson();
        method = "";
        request_method = "GET";
    }

    public static ProjectListController getInstance() {
        return instance;
    }

    private RequestBody buildBody() {
        if (method.equals(REMOVE)) {
            try {
                MediaType mediaType = MediaType.parse("application/json");
                String body = RequestUtil.parseBody(target);

                return RequestBody.create(body, mediaType);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public BaseResponse execute() {
        Response response = new Response();
        try {
            Request request = new Request.Builder()
                    .url(URL + method)
                    .method(request_method, buildBody())
                    .addHeader("Authorization", "Bearer " + UserData.getToken())
                    .build();

            ResponseBody responseBody = client.newCall(request).execute().body();
            Objects.requireNonNull(responseBody);

            return method.equals(LIST)
                    ? parseListBody(responseBody)
                    : parseRemoveBody(responseBody);

        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error while executing response");
        }
        return response;
    }

    private BaseResponse parseRemoveBody(ResponseBody responseBody) throws IOException {
        JsonObject object = JsonParser.parseString(responseBody.string())
                .getAsJsonObject();

        String message = object.get("message").getAsString();
        boolean status = object.get("status").getAsInt() == 200;
        return new Response(message, status);
    }

    private ListResponse<ProjectModel> parseListBody(ResponseBody body) throws IOException {
        JsonArray array = JsonParser.parseString(body.string())
                .getAsJsonArray();
        List<ProjectModel> models = new ArrayList<>();
        for (JsonElement element : array) {
            ProjectModel model = gson.fromJson(element, ProjectModel.class);
            models.add(model);
        }

        return new ListResponse<>(models, "", true);
    }

    public String getMethod() {
        return method;
    }

    public ProjectListController setMethod(String method) {
        this.method = method;
        if(method.equals(REMOVE)){
            request_method = "POST";
        }
        if(method.equals(LIST)){
            request_method = "GET";
        }
        return this;
    }

    public ProjectModel getTarget() {
        return target;
    }

    public ProjectListController setTarget(ProjectModel target) {
        this.target = target;
        return this;
    }
}
