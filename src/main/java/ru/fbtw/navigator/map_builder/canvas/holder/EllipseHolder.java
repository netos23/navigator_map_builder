package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class EllipseHolder extends Holder {

	private Ellipse decoration;
	private Ellipse hitBoxExternal, hitBoxInner;
	private Probe tmpStart, tmpEnd;

	private Vector2 origin;
	private double rx, ry;

	public EllipseHolder(Ellipse ellipse, Probe start, Probe end) {
		this.decoration = ellipse;
		this.tmpStart = start;
		this.tmpEnd = end;


		hitBoxExternal = new Ellipse();
		hitBoxExternal.setFill(Color.TRANSPARENT);
		//hitBoxExternal.setStroke(Color.TRANSPARENT);
		hitBoxExternal.setStroke(Color.GREEN);



		hitBoxInner = new Ellipse();

		hitBoxInner.setFill(Color.TRANSPARENT);
		//hitBoxInner.setStroke(Color.TRANSPARENT);
		hitBoxInner.setStroke(Color.RED);

		reBuildHitBoxes();

		initProbes(ellipse, start, end);
	}


	@Override
	protected void initProbes(Shape shape, Probe... probes) {
		Ellipse ellipse = (Ellipse)shape;
		Probe probe = new Probe(ellipse.getCenterX(),ellipse.getCenterY());

		super.initProbes(shape,probe);
	}

	@Override
	public void splitLayers(Pane[] layers) {
		layers[LayersName.INPUT_LAYER]
				.getChildren()
				.addAll(hitBoxExternal, hitBoxInner);
	}

	@Override
	public void extractProbes(ProbeManager manager) {
		for (Probe pr : probes) {
			manager.push(pr);
		}

		manager.removeEmptyProbe(tmpStart);
		manager.removeEmptyProbe(tmpEnd);
	}

	@Override
	public void remove(Pane[] layers) {
		super.remove(layers,decoration,hitBoxExternal,hitBoxInner);
	}

	@Override
	public void beginReplace(double x, double y) {
		origin = new Vector2(x, y);
	}

	@Override
	public void replace(double x, double y) {
		Vector2 currentPosition = new Vector2(x, y);
		Vector2 delta = origin.subtract(currentPosition);

		decoration.setCenterX(decoration.getCenterX() + delta.getX());
		decoration.setCenterY(decoration.getCenterY() + delta.getY());

		reBuildHitBoxes();
	}

	@Override
	public void endReplace(double x, double y, ProbeManager manager) {
		replace(x, y);
		reBuildProbes(manager);
	}


	@Override
	public void beginResize(double x, double y) {
		origin = new Vector2(x, y);
		rx = decoration.getRadiusX();
		ry = decoration.getRadiusY();
	}

	@Override
	public void resize(double x, double y) {
		Vector2 currentPosition = new Vector2(x, y);
		Vector2 delta = origin.subtract(currentPosition);

		double addY = origin.getY() > decoration.getCenterY() ? delta.getY() : -delta.getY();
		double addX = origin.getX() > decoration.getCenterX() ? delta.getX() : -delta.getX();

		decoration.setRadiusX(Math.abs(rx + addX));
		decoration.setRadiusY(Math.abs(ry + addY));

		reBuildHitBoxes();
	}

	@Override
	public void endResize(double x, double y, ProbeManager manager) {
		resize(x, y);
	}

	@Override
	public void reBuildProbes(ProbeManager manager) {
		manager.remove(decoration);

		Probe tmp = manager.getPosOfExistingPoint(
				decoration.getCenterX(),decoration.getCenterY());

		replace(tmp.getX(),tmp.getY());
		initProbes(decoration,tmp);
	}

	@Override
	public void setStrokeWidth(double width) {
		decoration.setStrokeWidth(width);
		reBuildHitBoxes();
	}

	@Override
	public void setStroke(Paint color) {
		decoration.setStroke(color);
	}

	@Override
	public void setFill(Paint color) {
		decoration.setFill(color);
	}

	@Override
	public void getInfo() {

	}

	@Override
	public void reBuildHitBoxes() {
		double tmp = (ADDITIONAL_WIDTH + decoration.getStrokeWidth()) / 2.0;

		double innerWidth = (decoration.getRadiusX() >= tmp)
				? decoration.getRadiusX() - tmp : 0;
		double innerHeight = (decoration.getRadiusY() >= tmp)
				? decoration.getRadiusY() - tmp : 0;

		hitBoxInner.setRadiusX(innerWidth);
		hitBoxInner.setRadiusY(innerHeight);

		hitBoxExternal.setRadiusX(
				decoration.getRadiusX() + tmp);
		hitBoxExternal.setRadiusY(
				decoration.getRadiusY() + tmp);

		hitBoxInner.setCenterX(decoration.getCenterX());
		hitBoxInner.setCenterY(decoration.getCenterY());
		hitBoxExternal.setCenterX(decoration.getCenterX());
		hitBoxExternal.setCenterY(decoration.getCenterY());
	}

	@Override
	public Shape getShape() {
		return null;
	}

	@Override
	public boolean contains(double x, double y) {
		return hitBoxExternal.contains(x,y) && !hitBoxInner.contains(x,y);
	}

	@Override
	public boolean containsInner(double x, double y) {
		return hitBoxInner.contains(x,y);
	}
}
