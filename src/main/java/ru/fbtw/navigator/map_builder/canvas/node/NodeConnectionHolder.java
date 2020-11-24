package ru.fbtw.navigator.map_builder.canvas.node;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;

public class NodeConnectionHolder extends Holder {
	@Override
	public void splitLayers(Pane[] layers) {

	}

	@Override
	public void extractProbes(ProbeManager manager) {

	}

	@Override
	public void remove(Pane[] layers) {

	}

	@Override
	public void beginReplace(double x, double y) {

	}

	@Override
	public void replace(double x, double y) {

	}

	@Override
	public void endReplace(double x, double y, ProbeManager manager) {

	}

	@Override
	public void beginResize(double x, double y) {

	}

	@Override
	public void resize(double x, double y) {

	}

	@Override
	public void endResize(double x, double y, ProbeManager manager) {

	}

	@Override
	public void reBuildProbes(ProbeManager manager) {

	}

	@Override
	public void setStrokeWidth(double width) {

	}

	@Override
	public void setStroke(Paint color) {

	}

	@Override
	public void setFill(Paint color) {

	}

	@Override
	public GridPane getInfo(ProbeManager manager) {
		return null;
	}

	@Override
	public void reBuildHitBoxes() {

	}

	@Override
	public Shape getShape() {
		return null;
	}

	@Override
	public boolean contains(double x, double y) {
		return false;
	}

	@Override
	public boolean containsInner(double x, double y) {
		return false;
	}
}
