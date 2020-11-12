package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;

public class CircleHolder  extends Holder{

	private Circle decoration;
	private Circle hitBoxExternal, hitBoxInner;
	private Probe tmpStart, tmpEnd;


	public CircleHolder(Circle circle, Probe start, Probe end) {
		this.decoration = circle;
		this.tmpStart = start;
		this.tmpEnd = end;

		double temp = ADDITIONAL_WIDTH / 2.0;
		hitBoxExternal = new Circle(
				decoration.getCenterX() - temp,
				decoration.getCenterY() - temp,
				decoration.getRadius() + ADDITIONAL_WIDTH

		);
		hitBoxExternal.setFill(Color.TRANSPARENT);
		hitBoxExternal.setStroke(Color.TRANSPARENT);

		double innerRadius = (decoration.getCenterX() >= ADDITIONAL_WIDTH)
				? decoration.getCenterX() - ADDITIONAL_WIDTH : 0;

		hitBoxInner = new Circle(
				decoration.getCenterX() + temp,
				decoration.getCenterY() + temp,
				innerRadius
		);

		hitBoxInner.setFill(Color.TRANSPARENT);
		hitBoxInner.setStroke(Color.TRANSPARENT);

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

		manager.removeProbe(tmpStart);
		manager.removeProbe(tmpEnd);
	}

	@Override
	public void remove() {

	}

	@Override
	public void replace() {

	}

	@Override
	public void resize() {

	}

	@Override
	public void getInfo() {

	}

	@Override
	public Shape getShape() {
		return null;
	}
}
