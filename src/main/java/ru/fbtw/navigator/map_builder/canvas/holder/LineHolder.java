package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.DoublePropertyEventHandler;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.InfoToolDialogLayoutBuilder;
import ru.fbtw.navigator.map_builder.utils.MathUtils;
import ru.fbtw.navigator.map_builder.utils.Vector2;
import ru.fbtw.navigator.map_builder.utils.common.LinePoints;

import java.util.ArrayList;

public class LineHolder extends Holder {
	private static final double e = 10.5;

	private Line decoration;
	private Line hitBox;

	// Temporary variables for resize
	private LinePoints editPoint;
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
		editPoint = LinePoints.ALL;

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
		
		final boolean equalsX = MathUtils.doubleEquals(probes.get(0).getX(), decoration.getStartX(), e);
		final boolean equalsY = MathUtils.doubleEquals(probes.get(0).getY(), decoration.getStartY(), e);

		if(probes.size() != 1) {
			if (equalsX && equalsY) {
				start = probes.get(0);
				end = probes.get(1);
			} else {
				start = probes.get(1);
				end = probes.get(0);
			}
			if (start.getDistanceToPoint(x, y) >= end.getDistanceToPoint(x, y)) {
				editPoint = LinePoints.END;
				editProbe = end;
			} else {
				editPoint = LinePoints.START;
				editProbe = start;
			}
		}else{
			editPoint = LinePoints.SINGLETON;

		}

	}

	@Override
	public void resize(double x, double y) {
		if (editPoint != null) {
			switch (editPoint) {
				case SINGLETON:
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
		if (editPoint != LinePoints.ALL && editPoint != LinePoints.SINGLETON) {
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

			// todo: мало тестировалось
			editPoint = LinePoints.START;
			resize(startProbe.getX(), startProbe.getY());

			editPoint = LinePoints.END;
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

	private void setPosition(double x, double y, LinePoints target, ProbeManager manager) {
		if (target == LinePoints.START) {
			beginResize(decoration.getStartX(), decoration.getStartY());
		} else {
			beginResize(decoration.getEndX(), decoration.getEndY());
		}
		endResize(x, y, manager);
	}

	@Override
	public ScrollPane getInfo(ProbeManager manager) {


		DoublePropertyEventHandler onStartX = value -> {
			if (value != null) {
				setPosition(value, decoration.getStartY(), LinePoints.START, manager);
			}
			return decoration.getStartX();
		};

		DoublePropertyEventHandler onStartY = value -> {
			if (value != null) {
				setPosition(decoration.getStartX(), value, LinePoints.START, manager);
			}
			return decoration.getStartY();
		};

		DoublePropertyEventHandler onEndX = value -> {
			if (value != null) {
				setPosition(value, decoration.getEndY(), LinePoints.END, manager);
			}
			return decoration.getEndX();
		};

		DoublePropertyEventHandler onEndY = value -> {
			if (value != null) {
				setPosition(decoration.getEndX(), value, LinePoints.END, manager);
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

		return new InfoToolDialogLayoutBuilder()
				.addDoubleProperty("Start X", decoration.getStartX(), onStartX)
				.addDoubleProperty("Start Y", decoration.getStartY(), onStartY)
				.addDoubleProperty("End X", decoration.getEndX(), onEndX)
				.addDoubleProperty("End Y", decoration.getEndY(), onEndY)
				.addDoubleProperty("Width", decoration.getStrokeWidth(), onWidth)
				.addColorProperty("Color", decoration.getStroke(), onColor)
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


}


