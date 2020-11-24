package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.DoublePropertyEventHandler;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.InfoToolDialogBuilder;
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
	private Vector2 startPos, endPos;

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
		super.remove(layers, decoration, hitBox);
	}

	@Override
	public void beginReplace(double x, double y) {
		origin = new Vector2(x, y);
		editPoint = Points.ALL;

		startPos = new Vector2(decoration.getStartX(), decoration.getStartY());
		endPos = new Vector2(decoration.getEndX(), decoration.getEndY());
	}

	@Override
	public void replace(double x, double y) {
		Vector2 currentPosition = new Vector2(x, y);
		Vector2 delta = origin.subtract(currentPosition);

		decoration.setStartX(startPos.getX() + delta.getX());
		decoration.setStartY(startPos.getY() + delta.getY());
		decoration.setEndX(endPos.getX() + delta.getX());
		decoration.setEndY(endPos.getY() + delta.getY());

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

		if (probes.get(0).getX() == decoration.getStartX()
				&& probes.get(0).getY() == decoration.getStartY()) {
			start = probes.get(0);
			end = probes.get(1);
		} else {
			start = probes.get(1);
			end = probes.get(0);
		}

		if (start.getDistanceToPoint(x, y) >= end.getDistanceToPoint(x, y)) {
			editPoint = Points.END;
			editProbe = end;
		} else {
			editPoint = Points.START;
			editProbe = start;
		}
	}

	@Override
	public void resize(double x, double y) {
		if (editPoint != null) {
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
		resize(x, y);
		reBuildProbes(manager);
	}

	@Override
	public void reBuildProbes(ProbeManager manager) {
		if (editPoint != Points.ALL) {
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
		} else {
			Probe startProbe = manager
					.getPosOfExistingPoint(decoration.getStartX(), decoration.getStartY());
			Probe endProbe = manager
					.getPosOfExistingPoint(decoration.getEndX(), decoration.getEndY());

			beginResize(decoration.getStartX(), decoration.getStartY());
			resize(startProbe.getX(), startProbe.getY());

			beginResize(decoration.getEndX(), decoration.getEndY());
			resize(endProbe.getX(), endProbe.getY());

			manager.remove(decoration);
			manager.push(startProbe);
			manager.push(endProbe);
			initProbes(decoration, startProbe, endProbe);
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

	private void setPosition(double x, double y, Points target, ProbeManager manager) {
		if(target == Points.START) {
			beginResize(decoration.getStartX(), decoration.getStartY());
		}else{
			beginResize(decoration.getEndX(),decoration.getEndY());
		}
		endResize(x, y, manager);
	}

	@Override
	public GridPane getInfo(ProbeManager manager) {


		DoublePropertyEventHandler onStartX = value -> {
			if (value != null) {
				setPosition(value, decoration.getStartY(),Points.START, manager);
			}
			return decoration.getStartX();
		};

		DoublePropertyEventHandler onStartY = value -> {
			if (value != null) {
				setPosition(decoration.getStartX(), value,Points.START, manager);
			}
			return decoration.getStartY();
		};

		DoublePropertyEventHandler onEndX = value -> {
			if (value != null) {
				setPosition(value, decoration.getEndY(),Points.END, manager);
			}
			return decoration.getEndX();
		};

		DoublePropertyEventHandler onEndY = value -> {
			if (value != null) {
				setPosition(decoration.getEndX(), value,Points.END, manager);
			}
			return decoration.getEndY();
		};


		DoublePropertyEventHandler onWidth = value -> {
			if (value != null) {
				setStrokeWidth(value);
			}
			return decoration.getStrokeWidth();
		};


		ChangeListener<Color> onColor = (observable, oldValue, newValue) -> setStroke(newValue);

		return new InfoToolDialogBuilder()
				.addDoubleProperty("Start X", decoration.getStartX(), onStartX)
				.addDoubleProperty("Start Y", decoration.getStartY(), onStartY)
				.addDoubleProperty("End X", decoration.getEndX(), onEndX)
				.addDoubleProperty("End Y", decoration.getEndY(), onEndY)
				.addDoubleProperty("Width",decoration.getStrokeWidth(),onWidth)
				.addColorProperty("Color",decoration.getStroke(),onColor)
				.build();

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
		return hitBox.contains(x, y);
	}

	@Override
	public boolean containsInner(double x, double y) {
		return false;
	}

	private enum Points {
		START,
		END,
		ALL
	}

}


