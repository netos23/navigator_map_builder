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

	private Points editPoint;
	private Probe editProbe;

	public LineHolder(Line line, Probe start, Probe end) {
		this.decoration = line;
		this.probes = new ArrayList<>();

		hitBox = new Line(line.getStartX(), line.getStartY(),
				line.getEndX(), line.getEndY());
		hitBox.setStroke(Color.TRANSPARENT);
		//hitBox.setStroke(Color.GREEN);

		double hiBoxWidth =
				(decoration.getStrokeWidth() >= 10) ? decoration.getStrokeWidth() : 2 * ADDITIONAL_WIDTH;
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
	public void remove(Pane[] layers) {
		super.remove(layers,decoration,hitBox);
	}

	@Override
	public void replace() {

	}

	@Override
	public void beginResize(double x, double y) {
		Probe start, end;

		if(probes.get(0).getX() == decoration.getStartX()
				&& probes.get(0).getY() == decoration.getStartY()){
			start = probes.get(0);
			end = probes.get(1);
		}else{
			start = probes.get(1);
			end = probes.get(0);
		}

		if(start.getDistanceToPoint(x, y) >= end.getDistanceToPoint(x, y)){
			editPoint = Points.END;
			editProbe = end;
		}else{
			editPoint = Points.START;
			editProbe = start;
		}
	}

	@Override
	public void resize(double x, double y) {
		if(editPoint != null) {
			switch (editPoint) {
				case START:
					decoration.setStartX(x);
					decoration.setStartY(y);

					hitBox.setStartX(x);
					hitBox.setStartY(y);
					break;
				case END:
					decoration.setEndX(x);
					decoration.setEndY(y);

					hitBox.setEndX(x);
					hitBox.setEndY(y);
					break;
			}
		}
	}


	@Override
	public void endResize(double x, double y, ProbeManager manager) {
		resize(x,y);
		rebuildProbes(manager);
	}

	@Override
	public void rebuildProbes(ProbeManager manager) {
		editProbe.getAttachedShapes().remove(decoration);
		manager.removeEmptyProbe(editProbe);
		probes.remove(editProbe);

		switch (editPoint){
			case END:
				editProbe = manager.getPosOfExistingPoint(decoration.getEndX(),decoration.getEndY());
				break;
			case START:
				editProbe = manager.getPosOfExistingPoint(decoration.getStartX(),decoration.getStartY());
				break;
		}

		resize(editProbe.getX(),editProbe.getY());
		editProbe.getAttachedShapes().add(decoration);
		probes.add(editProbe);

	}

	@Override
	public void getInfo() {

	}

	@Override
	public Line getShape() {
		return decoration;
	}

	@Override
	public boolean contains(double x, double y) {
		return hitBox.contains(x,y);
	}

	private enum Points{
		START,
		END
	}

}


