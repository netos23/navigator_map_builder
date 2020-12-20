package ru.fbtw.navigator.map_builder.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.core.navigation.LevelConnection;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;
import ru.fbtw.navigator.map_builder.core.navigation.Node;
import ru.fbtw.navigator.map_builder.core.navigation.NodeType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GraphJsonSerializer {
	private static GraphJsonSerializer ourInstance = new GraphJsonSerializer();
	private Gson gson;

	private GraphJsonSerializer() {
		gson = new Gson();
	}

	public static GraphJsonSerializer getInstance() {
		return ourInstance;
	}

	public JsonElement nodeToJsonEl(Node node) {
		JsonObject root = new JsonObject();

		root.addProperty("name", node.getName());
		root.addProperty("description", node.getDescription());
		root.addProperty("x", node.getX());
		root.addProperty("y", node.getY());
		root.addProperty("type", node.getType().ordinal());
		if (node.getType() == NodeType.ZONE_CONNECTION) {
			root.addProperty("isPrime", node.isPrime());
		}
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
			for (Node attached : node.getNeighbors()) {
				String nodeA = node.getName();
				String nodeB = attached.getName();

				String hash = getStringHash(nodeA, nodeB);

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


	public JsonArray levelSystemToJson(HashSet<LevelConnection> connections) {
		JsonArray connectionsArray = new JsonArray();

		for (LevelConnection connection : connections) {
			JsonObject object = connectionToJson(connection);
			connectionsArray.add(object);
		}

		return connectionsArray;
	}

	public JsonObject connectionToJson(LevelConnection connection) {
		JsonObject object = new JsonObject();

		object.addProperty("nodeA", connection.getNodeA().getName());
		object.addProperty("nodeB", connection.getNodeB().getName());
		object.addProperty("socketA", connection.getSocketA().getName());
		object.addProperty("socketB", connection.getSocketB().getName());
		return object;
	}


	public JsonElement projectToJson(Project project) {
		JsonObject element = new JsonObject();

		element.addProperty("id",project.getModel().getId());
		element.add("levels", levelNodesToJson(project.getNodeSystem()));
		element.add("connections", levelSystemToJson(project.getConnections()));

		return element;
	}


	public String extractProject(Project project) {
		JsonElement root = projectToJson(project);

		return gson.toJson(root);
	}

	private String getStringHash(String a, String b) {
		if (a.length() == b.length()) {
			if (a.compareTo(b) > 0) {
				return a + b;
			} else {
				return b + a;
			}
		} else {
			if (a.length() > b.length()) {
				return a + b;
			} else {
				return b + a;
			}
		}
	}

	@Deprecated
	private int getIntHash(String a, String b) {
		return getStringHash(a, b).hashCode();
	}

}
