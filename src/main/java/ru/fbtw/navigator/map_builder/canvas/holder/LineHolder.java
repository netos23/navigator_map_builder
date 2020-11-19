package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;

public class LineHolder extends Holder {

	private Line decoration;
	private Line hitBox;

	// Temporary variables for resize
	private Points editPoint;
	private Probe editProbe;

	// Temporary variable for replace
	private Vector2 origin;

	public LineHolder(Line line, Probe start, Probe end) {
		this.decoration = line;
		this.probes = new ArrayList<>();

		hitBox = new Line();
		hitBox.setStroke(Color.TRANSPARENT);
		//hitBox.setStroke(Color.GREEN);
		reBuildHitBoxes();

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
	public void beginReplace(double x, double y) {
		origin = new Vector2(x,y);
		editPoint = Points.ALL;
	}

	@Override
	public void replace(double x, double y) {
		Vector2 currentPosition = new Vector2(x, y);
		Vector2 delta = origin.subtract(currentPosition);

		decoration.setStartX(decoration.getStartX() + delta.getX());
		decoration.setStartY(decoration.getStartY() + delta.getY());
		decoration.setEndX(decoration.getEndX() + delta.getX());
		decoration.setEndY(decoration.getEndY() + delta.getY());

		reBuildHitBoxes();
	}

	@Override
	public void endReplace(double x, double y, ProbeManager manager) {
		replace(x, y);
		reBuildProbes(manager);
		//reBuildHitBoxes();
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
		reBuildProbes(manager);
	}

	@Override
	public void reBuildProbes(ProbeManager manager) {
		if(editPoint != Points.ALL) {
			editProbe.getAttachedShapes().remove(decoration);
			manager.removeEmptyProbe(editProbe);
			probes.remove(editProbe);

			switch (editPoint) {
				case END:
					editProbe = manager.getPosOfExistingPoint(decoration.getEndX(), decoration.getEndY());
					break;
				case START:
					editProbe = manager.getPosOfExistingPoint(decoration.getStartX(), decoration.getStartY());
					break;
			}

			resize(editProbe.getX(), editProbe.getY());
			editProbe.getAttachedShapes().add(decoration);
			probes.add(editProbe);
		}else{
			Probe startProbe = manager
					.getPosOfExistingPoint(decoration.getStartX(),decoration.getStartY());
			Probe endProbe = manager
					.getPosOfExistingPoint(decoration.getEndX(),decoration.getEndY());

			beginResize(decoration.getStartX(),decoration.getStartY());
			endResize(startProbe.getX(),startProbe.getY(),manager);

			beginResize(decoration.getEndX(),decoration.getEndY());
			endResize(endProbe.getX(),endProbe.getY(),manager);
		}

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
	}

	@Override
	public void getInfo() {

	}

	@Override
	public void reBuildHitBoxes() {
		hitBox.setStartX(decoration.getStartX());
		hitBox.setStartY(decoration.getStartY());

		hitBox.setEndX(decoration.getEndX());
		hitBox.setEndY(decoration.getEndY());

		double hiBoxWidth =
				(decoration.getStrokeWidth() >= 10) ? decoration.getStrokeWidth() : 2 * ADDITIONAL_WIDTH;

		hitBox.setStrokeWidth(hiBoxWidth);
	}

	@Override
	public Line getShape() {
		return decoration;
	}

	@Override
	public boolean contains(double x, double y) {
		return hitBox.contains(x,y);
	}

	@Override
	public boolean containsInner(double x, double y) {
		return false;
	}

	private enum Points{
		START,
		END,
		ALL
	}

}


