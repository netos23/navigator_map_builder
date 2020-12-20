package ru.fbtw.navigator.map_builder.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import ru.fbtw.navigator.map_builder.canvas.holder.*;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.core.Level;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.core.navigation.Node;
import ru.fbtw.navigator.map_builder.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Serializer {
	private Gson gson;
	private JsonObject levelRoot;
	private GraphJsonSerializer graphJsonSerializer;
	private JsonArray levels;


	public Serializer() {
		gson = new Gson();
		levelRoot = new JsonObject();
		levels = new JsonArray();
		graphJsonSerializer = GraphJsonSerializer.getInstance();
	}

	private JsonObject write(Probe probe) {
		JsonObject root = new JsonObject();

		root.addProperty("x", probe.getX());
		root.addProperty("y", probe.getY());
		root.addProperty("id", probe.getId());

		return root;
	}

	private JsonObject write(LineHolder holder) {
		JsonObject root = new JsonObject();

		Line shape = holder.getShape();

		root.addProperty("type", HolderSerializeType.LINE);
		root.addProperty("startX", shape.getStartX());
		root.addProperty("startY", shape.getStartY());
		root.addProperty("endX", shape.getEndX());
		root.addProperty("endY", shape.getEndY());
		writeStroke(root, shape);

		root.add("probes", writeProbesId(holder.getProbes()));

		return root;
	}

	private JsonElement writeProbesId(List<Probe> probes) {
		JsonArray array = new JsonArray();

		for (Probe probe : probes) {
			array.add(probe.getId());
		}

		return array;
	}

	private JsonObject write(RectangleHolder holder) {
		JsonObject root = new JsonObject();

		Rectangle shape = holder.getShape();

		root.addProperty("type", HolderSerializeType.RECTANGLE);
		root.addProperty("x", shape.getX());
		root.addProperty("y", shape.getY());
		root.addProperty("width", shape.getWidth());
		root.addProperty("height", shape.getHeight());
		root.addProperty("fill", shape.getFill().toString());
		writeStroke(root, shape);

		root.add("probes", writeProbesId(holder.getProbes()));

		return root;
	}


	private JsonObject write(CircleHolder holder) {
		JsonObject root = new JsonObject();

		Circle shape = holder.getShape();

		root.addProperty("type", HolderSerializeType.CIRCLE);
		root.addProperty("x", shape.getCenterX());
		root.addProperty("y", shape.getCenterY());
		root.addProperty("r", shape.getRadius());
		root.addProperty("fill", shape.getFill().toString());
		writeStroke(root, shape);

		root.add("probes", writeProbesId(holder.getProbes()));
		return root;
	}

	private JsonObject write(EllipseHolder holder) {
		JsonObject root = new JsonObject();

		Ellipse shape = holder.getShape();

		root.addProperty("type", HolderSerializeType.ELLIPSE);
		root.addProperty("x", shape.getCenterX());
		root.addProperty("y", shape.getCenterY());
		root.addProperty("rX", shape.getRadiusX());
		root.addProperty("rY", shape.getRadiusY());
		root.addProperty("fill", shape.getFill().toString());
		writeStroke(root, shape);

		root.add("probes", writeProbesId(holder.getProbes()));
		return root;
	}

	private void writeStroke(JsonObject root, Shape shape) {
		root.addProperty("stroke", shape.getStroke().toString());
		root.addProperty("strokeWidth", shape.getStrokeWidth());
	}

	private JsonObject write(Holder holder) {
		if (holder instanceof LineHolder) {
			return write((LineHolder) holder);
		}

		if (holder instanceof RectangleHolder) {
			return write((RectangleHolder) holder);
		}

		if (holder instanceof CircleHolder) {
			return write((CircleHolder) holder);
		}

		if (holder instanceof EllipseHolder) {
			return write((EllipseHolder) holder);
		}

		return null;
	}

	private Serializer writeProbes(List<Probe> probes) {
		JsonArray array = new JsonArray();

		for (Probe probe : probes) {
			array.add(write(probe));
		}

		levelRoot.add("probes", array);

		return this;
	}

	private Serializer writeHolders(List<Holder> holders) {
		JsonArray array = new JsonArray();

		for (Holder holder : holders) {
			array.add(write(holder));
		}

		levelRoot.add("holders", array);

		return this;
	}

	private Serializer writeBg(Image bg) {
		ImageView view = new ImageView(bg);
		int width = (int) bg.getWidth();
		int height = (int) bg.getHeight();

		byte[] image = ImageUtils.getImageFromNode(view, width, height, true);

		return writeBg(image);
	}

	private Serializer writeBg(byte[] image) {
		if (image != null) {
			String base64Image = Base64.encodeBase64String(image);
			levelRoot.addProperty("image", base64Image);
		}
		return this;
	}

	public String writeProject(Project project) {
		JsonObject projJson = new JsonObject();
		for (Level level : project.getLevels()) {
			levelRoot = new JsonObject();

			levelRoot.addProperty("name", level.getName());
			writeBg(level.getBackground());
			writeHolders(level.getController().getHolders());
			writeProbes(level.getController().getProbes());
			writeNodeSystem(level.getNodeSystem());

			levels.add(levelRoot);
		}

		projJson.add("levels", levels);
		JsonArray connections = graphJsonSerializer.levelSystemToJson(project.getConnections());
		projJson.add("connections", connections);

		return gson.toJson(projJson);
	}

	private void writeNodeSystem(ArrayList<Node> nodeSystem) {
		JsonElement element = graphJsonSerializer.nodeSystemToJson(nodeSystem);
		levelRoot.add("nodeSystem", element);
	}


	public String writeProject(Project project, Map<Level, byte[]> map) {
		JsonObject projJson = new JsonObject();
		for (Level level : project.getLevels()) {
			levelRoot = new JsonObject();

			levelRoot.addProperty("name", level.getName());
			writeBg(map.get(level));
			writeHolders(level.getController().getHolders());
			writeProbes(level.getController().getProbes());
			writeNodeSystem(level.getNodeSystem());

			levels.add(levelRoot);
		}

		projJson.add("levels", levels);
		JsonArray connections = graphJsonSerializer.levelSystemToJson(project.getConnections());
		projJson.add("connections", connections);

		return gson.toJson(projJson);
	}
}
