package ru.fbtw.navigator.map_builder.web_controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import okhttp3.*;
import ru.fbtw.navigator.map_builder.auth.UserData;
import ru.fbtw.navigator.map_builder.core.Level;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.io.GraphJsonSerializer;
import ru.fbtw.navigator.map_builder.utils.common.Action;
import ru.fbtw.navigator.map_builder.web_controllers.response.BaseResponse;
import ru.fbtw.navigator.map_builder.web_controllers.response.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ProjectPublishController implements Controller{
	private static final String URL = "http://localhost:8080/api/publish_project";
	private static final ProjectPublishController instance = new ProjectPublishController();
	private OkHttpClient client;
	private Project project;
	private Map<Level, byte[]> levelMap;

	public ProjectPublishController() {
		client = new OkHttpClient().newBuilder()
				.build();
	}

	public static ProjectPublishController getInstance() {
		return instance;
	}

	@Override
	public BaseResponse execute() {
		Response response = new Response();
		try {
			Request request = new Request.Builder()
					.url(URL)
					.method("POST", buildBody())
					.addHeader("Authorization", "Bearer " + UserData.getToken())
					.build();

			ResponseBody responseBody = client.newCall(request).execute().body();
			Objects.requireNonNull(responseBody);

			return parseResponse(responseBody);

		} catch (IOException | NullPointerException | JsonSyntaxException ex) {
			ex.printStackTrace();
			response.setSuccess(false);
			response.setMessage("Error while executing response");
		}catch (IllegalStateException ex){
			ex.printStackTrace();
			response.setSuccess(false);
			response.setMessage("Wrong err");
		}
		return response;
	}

	private BaseResponse parseResponse(ResponseBody body) throws IOException,JsonSyntaxException {
		Response response = new Response();
		System.out.println(body.string());
		JsonObject jsonResponse = JsonParser.parseString(body.string()).getAsJsonObject();
		response.setSuccess(jsonResponse.get("status").getAsInt() == 200);
		response.setMessage(jsonResponse.get("message").getAsString());
		return response;
	}

	private RequestBody buildBody() {

		if(project != null && levelMap!= null){
			GraphJsonSerializer graphJsonSerializer = GraphJsonSerializer.getInstance();
			String body = graphJsonSerializer.extractProject(project,levelMap);
			MediaType mediaType = MediaType.parse("application/json");

			return   RequestBody.create(body, mediaType);
		}else if(levelMap == null){
			GraphJsonSerializer graphJsonSerializer = GraphJsonSerializer.getInstance();
			String body = graphJsonSerializer.extractProject(project);
			MediaType mediaType = MediaType.parse("application/json");

			return   RequestBody.create(body, mediaType);
		}

		return null;
	}

	public ProjectPublishController setProject(Project project) {
		this.project = project;
		return this;
	}

	public ProjectPublishController setProject(Project project, Map<Level, byte[]> levelMap) {
		this.levelMap = levelMap;
		return setProject(project);
	}
}
