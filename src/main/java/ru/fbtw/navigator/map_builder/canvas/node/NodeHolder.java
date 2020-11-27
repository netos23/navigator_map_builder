package ru.fbtw.navigator.map_builder.canvas.node;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.navigation.Node;
import ru.fbtw.navigator.map_builder.navigation.NodeType;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.DoublePropertyEventHandler;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.EnumPropertyEventHandler;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.InfoToolDialogLayoutBuilder;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.StringPropertyEventHandler;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeHolder extends Holder {

	private final static Color STROKE_COLOR = Color.BLACK;
	private final static double WIDTH = 15;
	private static HashMap<NodeType, Color> fillColor;

	static {
		fillColor = new HashMap<>();
		fillColor.put(NodeType.DESTINATION, Color.GREEN);
		fillColor.put(NodeType.TEMP, Color.CORAL);
		fillColor.put(NodeType.ZONE_CONNECTION, Color.BLUE);
		fillColor.put(null, Color.RED);
	}

	private Circle decoration;
	private Label name;
	private Node target;
	private Vector2 origin;
	private Vector2 startPos;
	private ArrayList<NodeConnectionHolder> attachedConnections;

	public NodeHolder(Node node) {
		target = node;

		decoration = new Circle();
		decoration.setRadius(WIDTH);
		decoration.setStroke(STROKE_COLOR);
		setFillColor();

		name = new Label(target.getName());



		setPosition(target.getX(), target.getY());
		setTmpNode();

		attachedConnections = new ArrayList<>();
	}


	private void setPosition(double x, double y) {
		origin = new Vector2(x, y);
		target.setX((int) x);
		target.setY((int) y);

		decoration.setCenterX(x);
		decoration.setCenterY(y);

		double nameX = target.getX() - getNameWidth();
		double nameY = target.getY() + WIDTH;
		name.setLayoutX(nameX);
		name.setLayoutY(nameY);

	}



	private void setFillColor() {
		final Color value = fillColor.get(target.getType());
		decoration.setFill(value);
	}

	private void setTmpNode() {
		boolean isTmp = target.getType() == NodeType.TEMP;
		name.setVisible(!isTmp);
	}


	private double getNameWidth() {
		return name.getText().length() * 3.5;
	}

	@Override
	public void splitLayers(Pane[] layers) {
		layers[LayersName.INPUT_LAYER].getChildren()
				.addAll(decoration,name);
	}

	@Override
	public void extractProbes(ProbeManager manager) {
		// do nothing
	}

	@Override
	public void remove(Pane[] layers) {
		layers[LayersName.INPUT_LAYER].getChildren()
				.removeAll(decoration, name);

		Node.removeName(target.getName());
	}

	@Override
	public void beginReplace(double x, double y) {
		origin = new Vector2(decoration.getCenterX(), decoration.getCenterY());
		startPos = new Vector2(x, y);
		name.setVisible(false);

		for (NodeConnectionHolder h : attachedConnections) {
			h.beginResize(this);
		}
	}

	@Override
	public void replace(double x, double y) {
		Vector2 curPos = new Vector2(x, y);
		Vector2 delta = startPos.subtract(curPos);
		double dx = delta.getX();
		double dy = delta.getY();


		decoration.setCenterX(origin.getX() + dx);
		decoration.setCenterY(origin.getY() + dy);

		for (NodeConnectionHolder h : attachedConnections) {
			h.resize(new Vector2(decoration.getCenterX(),decoration.getCenterY()));
		}
	}

	@Override
	public void endReplace(double x, double y, ProbeManager manager) {
		replace(x, y);
		setPosition(decoration.getCenterX(), decoration.getCenterY());
		name.setVisible(true);
	}

	@Override
	public void beginResize(double x, double y) {
		// do nothing
	}

	@Override
	public void resize(double x, double y) {
		// do nothing
	}

	@Override
	public void endResize(double x, double y, ProbeManager manager) {
		// do nothing
	}

	@Override
	public void reBuildProbes(ProbeManager manager) {
		// do nothing
	}

	@Override
	public void setStrokeWidth(double width) {
		// do nothing
	}

	@Override
	public void setStroke(Paint color) {
		// do nothing
	}

	@Override
	public void setFill(Paint color) {
		// do nothing
	}

	@Override
	public ScrollPane getInfo(ProbeManager manager) {
		DoublePropertyEventHandler setX = e -> {
			if (e != null) {
				setPosition(e, target.getY());
			}
			return target.getX();
		};
		DoublePropertyEventHandler setY = e -> {
			if (e != null) {
				setPosition(target.getX(), e);
			}
			return target.getY();
		};

		StringPropertyEventHandler setName = e -> {
			target.setName(e);
			name.setText(e);
			setPosition(decoration.getCenterX(), decoration.getCenterY());
			return target.getName();

		};
		StringPropertyEventHandler setDescription = e -> {
			if (e == null) e = "";
			target.setDescription(e);
			return target.getDescription();
		};
		EnumPropertyEventHandler<NodeType> setType = e -> {
			target.setType(e);
			setFillColor();
			setTmpNode();
		};
		return new InfoToolDialogLayoutBuilder()
				.addDoubleProperty("x", target.getX(), setX)
				.addDoubleProperty("y", target.getY(), setY)
				.addEnumProperty("Node type", target.getType(), setType)
				.addStringProperty("Name", target.getName(), setName)
				.addMultiLineProperty("Description", target.getDescription(), setDescription)
				.build();
	}

	@Override
	public void reBuildHitBoxes() {
		// do nothing
	}

	@Override
	public Shape getShape() {
		return decoration;
	}

	public Node getTarget() {
		return target;
	}

	public double getX(){
		return decoration.getCenterX();
	}

	public double getY(){
		return decoration.getCenterY();
	}

	@Override
	public boolean contains(double x, double y) {
		return decoration.contains(x, y);
	}

	@Override
	public boolean containsInner(double x, double y) {
		return contains(x, y);
	}

	public ArrayList<NodeConnectionHolder> getAttachedConnections() {
		return attachedConnections;
	}

	@Override
	public String toString() {
		return String.format("Node: \"%s\"", target.getName());
	}
}
