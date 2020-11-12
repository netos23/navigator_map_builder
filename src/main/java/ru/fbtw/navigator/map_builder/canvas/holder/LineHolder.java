package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;

import java.util.ArrayList;

public class LineHolder extends Holder {

	private Line decoration;
	private Line hitBox;

	public LineHolder(Line line, Probe start, Probe end) {
		this.decoration = line;
		this.probes = new ArrayList<>();

		hitBox = new Line(line.getStartX(), line.getStartY(),
				line.getEndX(), line.getEndY());
		hitBox.setStroke(Color.TRANSPARENT);

		double hiBoxWidth =
				(hitBox.getStrokeWidth() >= 10) ? hitBox.getStrokeWidth() : 2 * ADDITIONAL_WIDTH;
		hitBox.setStrokeWidth(hiBoxWidth);

		initProbes(line, start, end);
	}


	@Override
	public void splitLayers(Pane[] layers) {
		layers[LayersName.INPUT_LAYER]
				.getChildren()
				.add(hitBox);
	}

	@Override
	public void extractProbes(ProbeManager manager) {
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
	public Line getShape() {
		return decoration;
	}


}
