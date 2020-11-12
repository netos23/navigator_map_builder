package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;

public class RectangleHolder extends Holder {

	private Rectangle decoration;
	private Rectangle hitBoxExternal, hitBoxInner;


	public RectangleHolder(Rectangle rectangle, Probe start, Probe end) {
		this.decoration = rectangle;

		double temp = ADDITIONAL_WIDTH / 2.0;
		hitBoxExternal = new Rectangle(
				decoration.getX() - temp,
				decoration.getY() - temp,
				decoration.getWidth() + ADDITIONAL_WIDTH,
				decoration.getHeight() + ADDITIONAL_WIDTH
		);
		hitBoxExternal.setFill(Color.TRANSPARENT);
		hitBoxExternal.setStroke(Color.TRANSPARENT);

		double innerWidth = (decoration.getWidth() >= ADDITIONAL_WIDTH)
				? decoration.getWidth() - ADDITIONAL_WIDTH : 0;
		double innerHeight = (decoration.getHeight() >= ADDITIONAL_WIDTH)
				? decoration.getHeight() - ADDITIONAL_WIDTH : 0;

		hitBoxInner = new Rectangle(
			decoration.getX() + temp,
			decoration.getY() + temp,
				innerWidth, innerHeight
		);

		hitBoxInner.setFill(Color.TRANSPARENT);
		hitBoxInner.setStroke(Color.TRANSPARENT);

		initProbes(rectangle, start, end);
	}


	@Override
	protected void initProbes(Shape shape, Probe... probes) {
		Probe tmp1 = new Probe(probes[0].getX(), probes[1].getY());
		Probe tmp2 = new Probe(probes[1].getX(), probes[0].getY());

		super.initProbes(shape, tmp1, tmp2, probes[0], probes[1]);
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
	public Rectangle getShape() {
		return decoration;
	}


}
