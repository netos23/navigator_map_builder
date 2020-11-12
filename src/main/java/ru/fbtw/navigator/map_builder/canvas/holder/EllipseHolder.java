package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;

public class EllipseHolder extends Holder {

	private Ellipse decoration;
	private Ellipse hitBoxExternal, hitBoxInner;
	private Probe tmpStart, tmpEnd;

	public EllipseHolder(Ellipse ellipse, Probe start, Probe end) {
		this.decoration = ellipse;
		this.tmpStart = start;
		this.tmpEnd = end;

		double temp = ADDITIONAL_WIDTH / 2.0;
		hitBoxExternal = new Ellipse(
				decoration.getCenterX() - temp,
				decoration.getCenterY() - temp,
				decoration.getRadiusX() + ADDITIONAL_WIDTH,
				decoration.getRadiusY() + ADDITIONAL_WIDTH
		);
		hitBoxExternal.setFill(Color.TRANSPARENT);
		hitBoxExternal.setStroke(Color.TRANSPARENT);

		double innerWidth = (decoration.getCenterX() >= ADDITIONAL_WIDTH)
				? decoration.getCenterX() - ADDITIONAL_WIDTH : 0;
		double innerHeight = (decoration.getCenterY() >= ADDITIONAL_WIDTH)
				? decoration.getCenterY() - ADDITIONAL_WIDTH : 0;

		hitBoxInner = new Ellipse(
				decoration.getCenterX() + temp,
				decoration.getCenterY() + temp,
				innerWidth, innerHeight
		);

		hitBoxInner.setFill(Color.TRANSPARENT);
		hitBoxInner.setStroke(Color.TRANSPARENT);

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
