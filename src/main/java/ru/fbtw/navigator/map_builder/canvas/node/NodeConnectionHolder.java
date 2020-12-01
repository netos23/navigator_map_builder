package ru.fbtw.navigator.map_builder.canvas.node;


import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.LinePoints;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.core.navigation.Node;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class NodeConnectionHolder extends Holder {
	public static final double WIDTH = 10;
	private Line decoration;
	private NodeHolder a, b;

	private LinePoints editPoint;

	public NodeConnectionHolder(Line decoration, NodeHolder a, NodeHolder b) {
		this.a = a;
		this.b = b;

		this.decoration = decoration;
		this.decoration.setStrokeWidth(WIDTH);

		a.getAttachedConnections()
				.add(this);
		b.getAttachedConnections()
				.add(this);
	}

	@Override
	public void splitLayers(Pane[] layers) {
		//do nothing
	}

	@Override
	public void extractProbes(ProbeManager manager) {
		//do nothing
	}

	@Override
	public void remove(Pane[] layers) {
		layers[LayersName.NODE_CONNECTIONS]
				.getChildren()
				.remove(decoration);

		a.getAttachedConnections()
				.remove(this);
		b.getAttachedConnections()
				.remove(this);

		Node.breakConnection(a.getTarget(), b.getTarget());

	}


	@Override
	public void beginReplace(double x, double y) {
		//do nothing
	}

	@Override
	public void replace(double x, double y) {
		//do nothing
	}

	@Override
	public void endReplace(double x, double y, ProbeManager manager) {
		//do nothing
	}

	void beginResize(NodeHolder target) {
		if (target.equals(a)) {
			editPoint = LinePoints.START;
		} else {
			editPoint = LinePoints.END;
		}
	}

	@Override
	public void beginResize(double x, double y) {
		//do nothing
	}

	@Override
	public void resize(double x, double y) {
		//do nothing
	}

	void resize(Vector2 pos) {
		if(editPoint == LinePoints.START){
			decoration.setStartX(pos.getX());
			decoration.setStartY(pos.getY());
		}else{
			decoration.setEndX(pos.getX());
			decoration.setEndY(pos.getY());
		}
	}

	@Override
	public void endResize(double x, double y, ProbeManager manager) {
		//do nothing
	}

	@Override
	public void reBuildProbes(ProbeManager manager) {
		//do nothing
	}

	@Override
	public void setStrokeWidth(double width) {
		//do nothing
	}

	@Override
	public void setStroke(Paint color) {
		//do nothing
	}

	@Override
	public void setFill(Paint color) {
		//do nothing
	}

	@Override
	public ScrollPane getInfo(ProbeManager manager) {
		return null;
	}

	@Override
	public void reBuildHitBoxes() {
		//do nothing
	}

	@Override
	public Shape getShape() {
		return decoration;
	}

	@Override
	public boolean contains(double x, double y) {
		return decoration.contains(x, y);
	}

	@Override
	public boolean containsInner(double x, double y) {
		return false;
	}
}
