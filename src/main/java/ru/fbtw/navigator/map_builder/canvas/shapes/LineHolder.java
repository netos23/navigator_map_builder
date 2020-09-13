package ru.fbtw.navigator.map_builder.canvas.shapes;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;

public class LineHolder extends Holder{
	public static final int ADDITIONAL_WIDTH = 5;
	private Line decoration;
	private Line hitBox;

	public LineHolder(Line line) {
		this.decoration = line;

		hitBox = new Line(line.getStartX(),line.getStartY(),
				line.getEndX(),line.getEndY());
		hitBox.setStroke(Color.TRANSPARENT);
		hitBox.setStrokeWidth(hitBox.getStrokeWidth() + ADDITIONAL_WIDTH);

		probes.add(new Probe(line.getStartX(),line.getStartY()));
		probes.add(new Probe(line.getEndX(),line.getEndY()));

	}

	@Override
	public void splitLayers(Pane[] layers) {
		layers[LayersName.DECORATION].getChildren().add(decoration);
		layers[LayersName.INPUT_LAYER].getChildren().add(hitBox);
		layers[LayersName.INPUT_LAYER].getChildren().addAll();
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
