package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class CircleHolder  extends Holder{

	private Circle decoration;
	private Circle hitBoxExternal, hitBoxInner;
	private Probe tmpStart, tmpEnd;

	private Vector2 origin;

	public CircleHolder(Circle circle, Probe start, Probe end) {
		this.decoration = circle;
		this.tmpStart = start;
		this.tmpEnd = end;

		double tmp = (ADDITIONAL_WIDTH + decoration.getStrokeWidth()) / 2.0;
		hitBoxExternal = new Circle(
				decoration.getCenterX(),
				decoration.getCenterY(),
				decoration.getRadius() + tmp

		);
		hitBoxExternal.setFill(Color.TRANSPARENT);
		//hitBoxExternal.setStroke(Color.TRANSPARENT);
		hitBoxExternal.setStroke(Color.GREEN);

		double innerRadius = (decoration.getRadius() >= tmp)
				? decoration.getRadius() - tmp : 0;

		hitBoxInner = new Circle(
				decoration.getCenterX(),
				decoration.getCenterY(),
				innerRadius
		);
		// TODO: 13.11.2020 пересчитать хитбоксы для элипса и круга
		hitBoxInner.setFill(Color.TRANSPARENT);
		//hitBoxInner.setStroke(Color.TRANSPARENT);
		hitBoxInner.setStroke(Color.RED);

		origin = new Vector2(decoration.getCenterX(),decoration.getCenterY());

		initProbes(circle, start, end);
	}

	@Override
	protected void initProbes(Shape shape, Probe... probes) {
		Circle circle = (Circle) shape;
		Probe probe = new Probe(circle.getCenterX(),circle.getCenterY());

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

	}

	@Override
	public void resize(double x, double y) {
		Vector2 tmp = new Vector2(x,y);
		decoration.setRadius(origin.subtract(tmp).sqrMaginitude());

		double tmpWidth = (ADDITIONAL_WIDTH + decoration.getStrokeWidth()) / 2.0;

		double innerRadius = (decoration.getRadius() >= tmpWidth)
				? decoration.getRadius() - tmpWidth : 0;

		hitBoxInner.setRadius(innerRadius);
		hitBoxExternal.setRadius(decoration.getRadius() + tmpWidth);

	}

	@Override
	public void endResize(double x, double y, ProbeManager manager) {
		resize(x, y);
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
