package ru.fbtw.navigator.map_builder.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;
import ru.fbtw.navigator.map_builder.core.navigation.Node;

import java.util.*;

public class JsonSerializer {
	private static JsonSerializer ourInstance = new JsonSerializer();
	private Gson gson;

	private JsonSerializer() {
		gson = new Gson();
	}

	public static JsonSerializer getInstance() {
		return ourInstance;
	}

	public JsonElement nodeToJsonEl(Node node) {
		JsonObject root = new JsonObject();

		root.addProperty("name", node.getName());
		root.addProperty("description", node.getDescription());
		root.addProperty("x", node.getX());
		root.addProperty("y", node.getY());
		root.addProperty("type", node.getType().ordinal());
		return root;
	}

	public JsonArray nodesToJsonEl(ArrayList<Node> nodes) {
		JsonArray nodesArray = new JsonArray(nodes.size());

		for (Node node : nodes) {
			nodesArray.add(nodeToJsonEl(node));
		}

		return nodesArray;
	}

	public JsonArray nodeConnectionsToJson(ArrayList<Node> nodes) {
		HashSet<String> writenObjects = new HashSet<>();
		JsonArray connectionsArray = new JsonArray();

		for (Node node : nodes) {
			for (Node attached : node.getConnections()) {
				String nodeA = node.getName();
				String nodeB = attached.getName();

				String hash = getHash(nodeA,nodeB);

				if (!(writenObjects.contains(hash))) {
					writenObjects.add(hash);

					JsonObject connectionJson = new JsonObject();
					connectionJson.addProperty("a", nodeA);
					connectionJson.addProperty("b", nodeB);

					connectionsArray.add(connectionJson);
				}
			}
		}

		return connectionsArray;
	}

	public JsonElement nodeSystemToJson(ArrayList<Node> nodes) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("nodes", nodesToJsonEl(nodes));
		jsonObject.add("connections", nodeConnectionsToJson(nodes));

		return jsonObject;
	}

	public JsonElement levelToJson(LevelNode level) {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("name", level.getName());

		byte[] imageBytes = level.getLevel().getImage();
		String base64Image = Base64.encodeBase64String(imageBytes);
		jsonObject.addProperty("image", base64Image);

		jsonObject.add("node_system",
				nodeSystemToJson(level.getLevel().getNodeSystem()));

		return jsonObject;
	}

	public JsonArray levelNodesToJson(List<LevelNode> nodes) {
		JsonArray levelsArray = new JsonArray(nodes.size());
		for (LevelNode node : nodes) {
			levelsArray.add(levelToJson(node));
		}

		return levelsArray;
	}

	public JsonArray levelConnectionsToJson(List<LevelNode> nodes) {
		JsonArray connectionsArray = new JsonArray(nodes.size());

		for (LevelNode node : nodes) {
			JsonObject level = new JsonObject();
			level.addProperty("name", node.getName());

			JsonArray sockets = new JsonArray();
			for (Map.Entry<String, ArrayList<LevelNode>> entry : node.getSockets().entrySet()) {
				JsonObject socket = new JsonObject();
				socket.addProperty("name", entry.getKey());

				JsonArray socketConnections = new JsonArray();
				for (LevelNode attachedToSocket : entry.getValue()) {
					socketConnections.add(attachedToSocket.getName());
				}

				socket.add("connections", socketConnections);
				sockets.add(socket);
			}

			level.add("connections", sockets);
			connectionsArray.add(level);
		}

		return connectionsArray;
	}


	public JsonElement projectToJson(Project project) {
		JsonObject element = new JsonObject();

		element.add("levels", levelNodesToJson(project.getNodeSystem()));
		element.add("connections", levelConnectionsToJson(project.getNodeSystem()));

		return element;
	}


	public String extractProject(Project project){
		JsonElement root = projectToJson(project);

		return gson.toJson(root);
	}

	// todo: ускорить hash
	public String getHash(String a, String b){
		if(a.length() == b.length()){
			if(a.compareTo(b)>0){
				return a + b;
			}else{
				return b + a;
			}
		}else{
			if(a.length() > b.length()){
				return a + b;
			}else{
				return b + a;
			}
		}
	}


}
