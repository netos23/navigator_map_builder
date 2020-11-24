package ru.fbtw.navigator.map_builder.canvas.node;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.InfoToolDialogBuilder;
import ru.fbtw.navigator.map_builder.navigation.Node;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class NodeHolder extends Holder {

	private final static Color fillColor = Color.LIME;
	private final static Color strokeColor = Color.BLACK;
	private final static double width = 5;

	private Circle decoration;
	private Node target;

	private Vector2 origin;

	public NodeHolder(Node node) {
		target = node;
		decoration = new Circle();
		decoration.setRadius(width);
		decoration.setStroke(strokeColor);
		decoration.setStroke(fillColor);

		setPosition(target.getX(), target.getY());

	}


	private void setPosition(double x, double y) {
		origin = new Vector2(x, y);
		target.setX((int) x);
		target.setY((int) y);

		decoration.setCenterX(x);
		decoration.setCenterY(y);

	}

	@Override
	public void splitLayers(Pane[] layers) {
		layers[LayersName.INPUT_LAYER].getChildren()
				.add(decoration);
	}

	@Override
	public void extractProbes(ProbeManager manager) {
		// do nothing
	}

	@Override
	public void remove(Pane[] layers) {
		layers[LayersName.INPUT_LAYER].getChildren()
				.add(decoration);
	}

	@Override
	public void beginReplace(double x, double y) {
		origin = new Vector2(x, y);
	}

	@Override
	public void replace(double x, double y) {
		Vector2 curPos = new Vector2(x, y);
		Vector2 delta = curPos.subtract(origin);
		double dx = delta.getX();
		double dy = delta.getY();

		setPosition(origin.getX() + dx, origin.getY() + dy);
	}

	@Override
	public void endReplace(double x, double y, ProbeManager manager) {
		replace(x, y);
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
	public GridPane getInfo(ProbeManager manager) {

		return new InfoToolDialogBuilder()
				//.addDoubleProperty("x")
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

	@Override
	public boolean contains(double x, double y) {
		return decoration.contains(x, y);
	}

	@Override
	public boolean containsInner(double x, double y) {
		return contains(x, y);
	}
}
