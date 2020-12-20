package ru.fbtw.navigator.map_builder.core;


import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import ru.fbtw.navigator.map_builder.canvas.CanvasController;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.core.navigation.Node;
import ru.fbtw.navigator.map_builder.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Level {
	private static final String DEFAULT_NAME = "Level ";
	private static Callback<Level, Observable[]> callback = param -> new Observable[]{param.name};
	private Image background;
	private boolean isBackground;
	private boolean isTmpNodes;
	private StringProperty name;
	private CanvasController controller;
	private CanvasProperties properties;
	private ArrayList<Node> nodeSystem;

	public Level(int id, CanvasProperties properties) {
		this.properties = properties;
		this.isBackground = false;
		this.isTmpNodes = true;
		this.name = new SimpleStringProperty(DEFAULT_NAME + id);
		this.background = null;

		nodeSystem = new ArrayList<>();
		controller = new CanvasController(properties, nodeSystem);

	}

	public static Callback<Level, Observable[]> getCallback() {
		return callback;
	}

	public CanvasProperties getProperties() {
		return properties;
	}

	public Image getBackground() {
		return background;
	}

	public boolean isBackground() {
		return isBackground;
	}

	public void setBackground(Image background) {
		this.background = background;
		controller.setBackground(background);
	}

	public void setUseBackground(boolean background) {
		isBackground = background;
		controller.enableBackground(background);
	}

	public boolean isTmpNodes() {
		return isTmpNodes;
	}

	public void setTmpNodes(boolean tmpNodes) {
		isTmpNodes = tmpNodes;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public CanvasController getController() {
		return controller;
	}

	public ArrayList<Node> getNodeSystem() {
		return nodeSystem;
	}

	@Override
	public String toString() {
		return name.get();
	}


	public byte[] getImage() {
		int width = (int) background.getWidth();
		int height = (int) background.getHeight();
		javafx.scene.Node node;

		if(isBackground){
			Pane[] layers = controller.getLayers();
			node = new StackPane(
				layers[LayersName.BACKGROUND],
				layers[LayersName.DECORATION]
			);

		}else {
			 node = controller.getLayers()[LayersName.DECORATION];
		}
		return ImageUtils.getImageFromNode(node, width, height,true);
	}

	public void setNodeSystem(Collection<Node> nodes) {
		nodeSystem.addAll(nodes);
	}
}
