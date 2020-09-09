package ru.fbtw.navigator.map_builder.core;


import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.util.Callback;
import ru.fbtw.navigator.map_builder.canvas.CanvasController;

public class Level{
	public static final String DEFAULT_NAME = "Level ";
	private Image background;
	private boolean isBackground;
	private boolean isTmpNodes;
	private StringProperty name;

	private CanvasController controller;

	private static Callback<Level, Observable[]> callback = param -> new Observable[]{param.name};

	public Level(int id) {
		this.isBackground = false;
		this.isTmpNodes = true;
		this.name = new SimpleStringProperty(DEFAULT_NAME +id);
		this.background = null;

		controller = new CanvasController();

	}

	public Image getBackground() {
		return background;
	}

	public void setBackground(Image background) {
		this.background = background;
		controller.setBackground(background);
	}

	public boolean isBackground() {
		return isBackground;
	}

	public void setUseBackground(boolean background) {
		isBackground = background;
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

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public CanvasController getController() {
		return controller;
	}

	public static Callback<Level, Observable[]> getCallback() {
		return callback;
	}

	@Override
	public String toString() {
		return name.get();
	}

}
