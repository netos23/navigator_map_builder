package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class RectangleHolder extends Holder {

	private Rectangle decoration;
	private Rectangle hitBoxExternal, hitBoxInner;

	private EditTarget editTarget;
	private Vector2 editPos, editBounds;

	public RectangleHolder(Rectangle rectangle, Probe start, Probe end) {
		this.decoration = rectangle;

		double temp = (ADDITIONAL_WIDTH + decoration.getStrokeWidth());
		hitBoxExternal = new Rectangle(
				decoration.getX() - temp / 2.0,
				decoration.getY() - temp / 2.0,
				decoration.getWidth() + temp,
				decoration.getHeight() + temp
		);
		hitBoxExternal.setFill(Color.TRANSPARENT);
		//hitBoxExternal.setStroke(Color.TRANSPARENT);
		hitBoxExternal.setStroke(Color.GREEN);

		double innerWidth = (decoration.getWidth() >= temp)
				? decoration.getWidth() - temp : 0;
		double innerHeight = (decoration.getHeight() >= temp)
				? decoration.getHeight() - temp : 0;

		hitBoxInner = new Rectangle(
				decoration.getX() + temp / 2,
				decoration.getY() + temp / 2,
				innerWidth, innerHeight
		);

		hitBoxInner.setFill(Color.TRANSPARENT);
		//hitBoxInner.setStroke(Color.TRANSPARENT);
		hitBoxInner.setStroke(Color.RED);

		initProbes(rectangle, start, end);
	}


	@Override
	protected void initProbes(Shape shape, Probe... probes) {
		Probe tmp1 = new Probe(probes[0].getX(), probes[1].getY());
		Probe tmp2 = new Probe(probes[1].getX(), probes[0].getY());
		//fixme дубликаты проб
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
	public void replace() {

	}

	@Override
	public void beginResize(double x, double y) {
		editPos = new Vector2(decoration.getX(), decoration.getY());
		editBounds = new Vector2(decoration.getWidth(), decoration.getHeight());

		boolean isCorner = false;
		Probe target = probes.get(0);
		double minDest = target.getDistanceToPoint(x, y);

		for (Probe probe : probes) {
			if (probe.isContainsPoint(x, y)) {
				isCorner = true;
				target = probe;
				break;
			} else {
				double distanceToPoint = probe.getDistanceToPoint(x, y);
				if (minDest > distanceToPoint) {
					target = probe;
					minDest = distanceToPoint;
				}
			}
		}


		if (target.getX() == decoration.getX()) {
			if (target.getY() == decoration.getY()) {
				editTarget = EditTarget.L_T_CORNER;
			} else {
				editTarget = EditTarget.L_B_CORNER;
			}
		} else {
			if (target.getY() == decoration.getY()) {
				editTarget = EditTarget.R_T_CORNER;
			} else {
				editTarget = EditTarget.R_B_CORNER;
			}
		}

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
		}

	}

	@Override
	public void resize(double x, double y) {
		// fixme баг со стики холдерами

		// Change left side
		if (editTarget == EditTarget.L_SIDE
				|| editTarget == EditTarget.L_T_CORNER
				|| editTarget == EditTarget.L_B_CORNER) {
			double newWidth = editPos.getX() - x + editBounds.getX();

			if(newWidth > 0){
				decoration.setX(x);
				decoration.setWidth(newWidth);
			}else{
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
			if(newWidth > 0){
				decoration.setWidth(newWidth);
			}else{
				newWidth = Math.abs(newWidth);
				decoration.setX(editPos.getX()-newWidth);
				decoration.setWidth(newWidth);
			}

		}

		// Change top side
		if (editTarget == EditTarget.T_SIDE
				|| editTarget == EditTarget.R_T_CORNER
				|| editTarget == EditTarget.L_T_CORNER) {


			double newHeight = editPos.getY() - y + editBounds.getY();

			if(newHeight > 0) {
				decoration.setY(y);
				decoration.setHeight(newHeight);
			}else {
				newHeight = Math.abs(newHeight);
				decoration.setY(editPos.getY()+editBounds.getY());
				decoration.setHeight(newHeight);
			}
		}

		// Change bottom side
		if (editTarget == EditTarget.B_SIDE
				|| editTarget == EditTarget.R_B_CORNER
				|| editTarget == EditTarget.L_B_CORNER) {

			double newHeight = y - editPos.getY();

			if(newHeight > 0 ) {
				decoration.setHeight(newHeight);
			}else{
				newHeight = Math.abs(newHeight);
				decoration.setY(editPos.getY() - newHeight);
				decoration.setHeight(newHeight);
			}
		}


		// update hitBoxes
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
	public void endResize(double x, double y, ProbeManager manager) {
		resize(x,y);
		rebuildProbes(manager);
	}

	@Override
	public void rebuildProbes(ProbeManager manager) {
		manager.remove(decoration);

		Probe probe0 = manager.getPosOfExistingPoint(
				decoration.getX(),decoration.getY()
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

		super.initProbes(decoration,probe0,probe1,probe2,probe3);
	}

	@Override
	public void getInfo() {

	}

	@Override
	public Rectangle getShape() {
		return decoration;
	}

	@Override
	public boolean contains(double x, double y) {
		return hitBoxExternal.contains(x, y) && !hitBoxInner.contains(x, y);
	}


	public boolean containsInner(double x, double y) {
		return hitBoxExternal.contains(x, y);
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
