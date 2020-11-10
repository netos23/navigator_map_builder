package ru.fbtw.navigator.map_builder.canvas.shapes;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.probe.Probe;

import java.util.ArrayList;

public class LineHolder extends Holder{
	public static final int ADDITIONAL_WIDTH = 5;
	private Line decoration;
	private Line hitBox;

	public LineHolder(Line line, Probe start, Probe end) {
		this.decoration = line;
		this.probes = new ArrayList<>();

		hitBox = new Line(line.getStartX(),line.getStartY(),
				line.getEndX(),line.getEndY());
		hitBox.setStroke(Color.TRANSPARENT);
		hitBox.setStrokeWidth(hitBox.getStrokeWidth() + ADDITIONAL_WIDTH);


		start.getAttachedShapes().add(line);
		end.getAttachedShapes().add(line);

		probes = new ArrayList<>();
		probes.add(start);
		probes.add(end);


	}



	@Override
	public void splitLayers(Pane[] layers) {
		///layers[LayersName.DECORATION].getChildren().add(decoration);
		layers[LayersName.INPUT_LAYER].getChildren().add(hitBox);
		/*for(Probe probe : probes){
			if(probe != null) {
				layers[LayersName.INPUT_LAYER].getChildren().add(probe.getHitBox());
			}
		}*/
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
