package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.DoublePropertyEventHandler;
import ru.fbtw.navigator.map_builder.ui.canvas_utils.InfoToolDialogBuilder;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class RectangleHolder extends Holder {

	private static final double e = 10.5;
	private Rectangle decoration;
	private Rectangle hitBoxExternal, hitBoxInner;

	// Temporary variable for replace and resize
	private EditTarget editTarget;
	private Vector2 editPos, editBounds;
	private Vector2 origin;

	public RectangleHolder(Rectangle rectangle, Probe start, Probe end) {
		this.decoration = rectangle;


		hitBoxExternal = new Rectangle();
		hitBoxExternal.setFill(Color.TRANSPARENT);
		//hitBoxExternal.setStroke(Color.TRANSPARENT);
		hitBoxExternal.setStroke(Color.GREEN);

		hitBoxInner = new Rectangle();
		hitBoxInner.setFill(Color.TRANSPARENT);
		//hitBoxInner.setStroke(Color.TRANSPARENT);
		hitBoxInner.setStroke(Color.RED);

		reBuildHitBoxes();

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
	public void remove(Pane[] layers) {
		super.remove(layers, decoration, hitBoxExternal, hitBoxInner);
	}

	@Override
	public void beginReplace(double x, double y) {
		origin = new Vector2(x, y);
		editPos = new Vector2(decoration.getX(),decoration.getY());
	}

	@Override
	public void replace(double x, double y) {
		Vector2 currentPosition = new Vector2(x, y);
		Vector2 delta = origin.subtract(currentPosition);

		decoration.setX(editPos.getX()+delta.getX());
		decoration.setY(editPos.getY()+delta.getY());

		reBuildHitBoxes();
	}

	@Override
	public void endReplace(double x, double y, ProbeManager manager) {
		replace(x, y);
		reBuildProbes(manager);
	}


	// fixme баг со стики холдерами
	@Override
	public void beginResize(double x, double y) {
		/*System.out.printf("begin begin resize obj: %s pos %f %f point %f %f \n",
				toString(),
				decoration.getX(),
				decoration.getY(),
				x,y
		);*/

		editPos = new Vector2(decoration.getX(), decoration.getY());
		editBounds = new Vector2(decoration.getWidth(), decoration.getHeight());

		boolean isCorner = false;
		Probe target = probes.get(0);
		double minDest = target.getDistanceToPoint(x, y);

		for (Probe probe : probes) {
			//System.out.printf("try probe obj: %s pos: %f %f ", probe.toString(),probe.getX(),probe.getY() );
			if (probe.isContainsPoint(x, y)) {
				//System.out.printf("contains %f %f\n",x,y);
				isCorner = true;
				target = probe;
				break;
			} else {
				double distanceToPoint = probe.getDistanceToPoint(x, y);
				//System.out.printf("distance to %f",distanceToPoint);
				if (minDest > distanceToPoint) {
					//	System.out.print(" distance is min");
					target = probe;
					minDest = distanceToPoint;
				}
				//System.out.println();
			}
		}


		if (Math.abs(target.getX() - decoration.getX()) <= e) {
			if (Math.abs(target.getY() - decoration.getY()) <= e) {
				editTarget = EditTarget.L_T_CORNER;
			} else {
				editTarget = EditTarget.L_B_CORNER;
			}
		} else {
			if (Math.abs(target.getY() - decoration.getY()) <= e) {
				editTarget = EditTarget.R_T_CORNER;
			} else {
				editTarget = EditTarget.R_B_CORNER;
			}
		}
		//System.out.printf("set corner %s\n",editTarget);

		if (!isCorner) {
			Vector2 curPos = new Vector2(target.getX(), target.getY());
			Vector2 tmp = curPos.subtract(new Vector2(x, y));
			switch (editTarget) {
				case L_B_CORNER:
					if (Math.abs(tmp.getX()) > Math.abs(tmp.getY())) {
						editTarget = EditTarget.B_SIDE;
					} else {
						editTarget = EditTarget.L_SIDE;
					}
					break;
				case L_T_CORNER:
					if (Math.abs(tmp.getX()) > Math.abs(tmp.getY())) {
						editTarget = EditTarget.T_SIDE;
					} else {
						editTarget = EditTarget.L_SIDE;
					}
					break;
				case R_B_CORNER:
					if (Math.abs(tmp.getX()) > Math.abs(tmp.getY())) {
						editTarget = EditTarget.B_SIDE;
					} else {
						editTarget = EditTarget.R_SIDE;
					}
					break;
				case R_T_CORNER:
					if (Math.abs(tmp.getX()) > Math.abs(tmp.getY())) {
						editTarget = EditTarget.T_SIDE;
					} else {
						editTarget = EditTarget.R_SIDE;
					}
					break;

			}
			//System.out.printf("set side %s\n",editTarget);
		}

		/*System.out.printf("end begin resize obj: %s with start: %s \n",toString(),editTarget);
		System.out.println();
		System.out.println();*/

	}

	@Override
	public void resize(double x, double y) {


		// Change left side
		if (editTarget == EditTarget.L_SIDE
				|| editTarget == EditTarget.L_T_CORNER
				|| editTarget == EditTarget.L_B_CORNER) {
			double newWidth = editPos.getX() - x + editBounds.getX();

			if (newWidth > 0) {
				decoration.setX(x);
				decoration.setWidth(newWidth);
			} else {
				newWidth = Math.abs(newWidth);
				decoration.setX(editPos.getX() + editBounds.getX());
				decoration.setWidth(newWidth);
			}
		}


		// Change right side
		if (editTarget == EditTarget.R_SIDE
				|| editTarget == EditTarget.R_T_CORNER
				|| editTarget == EditTarget.R_B_CORNER) {

			double newWidth = x - editPos.getX();
			if (newWidth > 0) {
				decoration.setWidth(newWidth);
			} else {
				newWidth = Math.abs(newWidth);
				decoration.setX(editPos.getX() - newWidth);
				decoration.setWidth(newWidth);
			}

		}

		// Change top side
		if (editTarget == EditTarget.T_SIDE
				|| editTarget == EditTarget.R_T_CORNER
				|| editTarget == EditTarget.L_T_CORNER) {


			double newHeight = editPos.getY() - y + editBounds.getY();

			if (newHeight > 0) {
				decoration.setY(y);
				decoration.setHeight(newHeight);
			} else {
				newHeight = Math.abs(newHeight);
				decoration.setY(editPos.getY() + editBounds.getY());
				decoration.setHeight(newHeight);
			}
		}

		// Change bottom side
		if (editTarget == EditTarget.B_SIDE
				|| editTarget == EditTarget.R_B_CORNER
				|| editTarget == EditTarget.L_B_CORNER) {

			double newHeight = y - editPos.getY();

			if (newHeight > 0) {
				decoration.setHeight(newHeight);
			} else {
				newHeight = Math.abs(newHeight);
				decoration.setY(editPos.getY() - newHeight);
				decoration.setHeight(newHeight);
			}
		}


		// update hitBoxes
		reBuildHitBoxes();

		/*
		System.out.printf("in resize obj: %s begin (%f %f %f %f) -> (%f %f %f %f)\n",
				toString(),
				editPos.getX(),
				editPos.getY(),
				editBounds.getX(),
				editBounds.getY(),
				decoration.getX(),
				decoration.getY(),
				decoration.getWidth(),
				decoration.getHeight()
				);*/
	}

	@Override
	public void endResize(double x, double y, ProbeManager manager) {
		resize(x, y);
		reBuildProbes(manager);
		/*System.out.printf("end resize obj: %s begin (%f %f %f %f) -> (%f %f %f %f)\n\n",
				toString(),
				editPos.getX(),
				editPos.getY(),
				editBounds.getX(),
				editBounds.getY(),
				decoration.getX(),
				decoration.getY(),
				decoration.getWidth(),
				decoration.getHeight()
		);*/
	}

	@Override
	public void reBuildProbes(ProbeManager manager) {
		manager.remove(decoration);

		Probe probe0 = manager.getPosOfExistingPoint(
				decoration.getX(), decoration.getY()
		);
		Probe probe1 = manager.getPosOfExistingPoint(
				decoration.getX() + decoration.getWidth(),
				decoration.getY()
		);
		Probe probe2 = manager.getPosOfExistingPoint(
				decoration.getX(),
				decoration.getY() + decoration.getHeight()
		);
		Probe probe3 = manager.getPosOfExistingPoint(
				decoration.getX() + decoration.getWidth(),
				decoration.getY() + decoration.getHeight()
		);

		super.initProbes(decoration, probe0, probe1, probe2, probe3);
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
		decoration.setFill(color);
	}

	@Override
	public GridPane getInfo(ProbeManager manager) {
		DoublePropertyEventHandler onX = value -> {
			if(value != null){
				beginReplace(decoration.getX(),decoration.getY());
				endReplace(value,decoration.getY(),manager);
			}
			return decoration.getX();
		};

		DoublePropertyEventHandler onY = value -> {
			if(value != null){
				beginReplace(decoration.getX(),decoration.getY());
				endReplace(decoration.getX(),value,manager);
			}
			return decoration.getY();
		};

		DoublePropertyEventHandler onWidth = value -> {
			if(value != null){
				value = Math.abs(value);
				decoration.setWidth(value);
				reBuildProbes(manager);
				reBuildHitBoxes();
			}
			return decoration.getWidth();
		};

		DoublePropertyEventHandler onHeight = value -> {
			if(value != null){
				value = Math.abs(value);
				decoration.setHeight(value);
				reBuildProbes(manager);
				reBuildHitBoxes();
			}
			return decoration.getHeight();
		};
		DoublePropertyEventHandler onLineWidth = value -> {
			if(value != null){
				setStrokeWidth(value);
			}
			return decoration.getStrokeWidth();
		};

		ChangeListener<Color> onColor = (observable, oldValue, newValue) -> setStroke(newValue);

		ChangeListener<Color> onFillColor = (observable, oldValue, newValue) -> setFill(newValue);
		return new InfoToolDialogBuilder()
				.addDoubleProperty("X",decoration.getX(),onX)
				.addDoubleProperty("Y",decoration.getY(),onY)
				.addDoubleProperty("Width", decoration.getWidth(),onWidth)
				.addDoubleProperty("Height", decoration.getHeight(),onHeight)
				.addDoubleProperty("Line width",decoration.getStrokeWidth(),onLineWidth)
				.addColorProperty("Color",decoration.getStroke(),onColor)
				.addColorProperty("Fill color",decoration.getFill(),onFillColor)
				.build();
	}

	@Override
	public void reBuildHitBoxes() {
		double temp = (ADDITIONAL_WIDTH + decoration.getStrokeWidth());
		hitBoxExternal.setX(decoration.getX() - temp / 2.0);
		hitBoxExternal.setY(decoration.getY() - temp / 2.0);
		hitBoxExternal.setWidth(decoration.getWidth() + temp);
		hitBoxExternal.setHeight(decoration.getHeight() + temp);

		double innerWidth = (decoration.getWidth() >= temp)
				? decoration.getWidth() - temp : 0;
		double innerHeight = (decoration.getHeight() >= temp)
				? decoration.getHeight() - temp : 0;

		hitBoxInner.setX(decoration.getX() + temp / 2.0);
		hitBoxInner.setY(decoration.getY() + temp / 2.0);
		hitBoxInner.setWidth(innerWidth);
		hitBoxInner.setHeight(innerHeight);
	}

	@Override
	public Rectangle getShape() {
		return decoration;
	}

	@Override
	public boolean contains(double x, double y) {
		return hitBoxExternal.contains(x, y) && !hitBoxInner.contains(x, y);
	}


	@Override
	public boolean containsInner(double x, double y) {
		return hitBoxExternal.contains(x, y);
	}

	@Override
	public String toString() {
		String string = super.toString();
		return string.substring(string.lastIndexOf('.') + 1);
	}

	private enum EditTarget {
		//todo: сократить количество элементов до 4
		L_SIDE,
		T_SIDE,
		R_SIDE,
		B_SIDE,
		L_T_CORNER,
		L_B_CORNER,
		R_T_CORNER,
		R_B_CORNER
	}
}
