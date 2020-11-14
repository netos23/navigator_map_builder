package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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


		double tmp = (ADDITIONAL_WIDTH + decoration.getStrokeWidth()) / 2.0;

		hitBoxExternal = new Ellipse(
				decoration.getCenterX() ,
				decoration.getCenterY(),
				decoration.getRadiusX() + tmp,
				decoration.getRadiusY() + tmp
		);
		hitBoxExternal.setFill(Color.TRANSPARENT);
		//hitBoxExternal.setStroke(Color.TRANSPARENT);
		hitBoxExternal.setStroke(Color.GREEN);


		double innerWidth = (decoration.getRadiusX() >= tmp)
				? decoration.getRadiusX() - tmp : 0;
		double innerHeight = (decoration.getRadiusY() >= tmp)
				? decoration.getRadiusY() - tmp : 0;

		hitBoxInner = new Ellipse(
				decoration.getCenterX(),
				decoration.getCenterY(),
				innerWidth, innerHeight
		);

		hitBoxInner.setFill(Color.TRANSPARENT);
		//hitBoxInner.setStroke(Color.TRANSPARENT);
		hitBoxInner.setStroke(Color.RED);

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
	public void replace() {

	}

	@Override
	public void beginResize(double x, double y) {
		origin = new Vector2(x, y);
		rx = decoration.getRadiusX();
		ry = decoration.getRadiusY();
	}

	@Override
	public void resize(double x, double y) {
		Vector2 curPos = origin.subtract(new Vector2(x,y));

		double addY = origin.getY() > decoration.getCenterY() ? curPos.getY() : -curPos.getY();
		double addX = origin.getX() > decoration.getCenterX() ? curPos.getX() : -curPos.getX();

		decoration.setRadiusX(Math.abs(rx + addX));
		decoration.setRadiusY(Math.abs(ry + addY));


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
	}

	@Override
	public void endResize(double x, double y, ProbeManager manager) {

	}

	@Override
	public void rebuildProbes(ProbeManager manager) {

	}

	@Override
	public void getInfo() {

	}

	@Override
	public Shape getShape() {
		return null;
	}

	@Override
	public boolean contains(double x, double y) {
		return hitBoxExternal.contains(x,y) && !hitBoxInner.contains(x,y);
	}
}
