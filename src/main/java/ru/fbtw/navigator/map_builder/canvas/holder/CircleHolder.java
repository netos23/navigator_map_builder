package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class CircleHolder  extends Holder{

	private Circle decoration;
	private Circle hitBoxExternal, hitBoxInner;
	private Probe tmpStart, tmpEnd;

	private Vector2 origin;
	private Vector2 editPos;

	public CircleHolder(Circle circle, Probe start, Probe end) {
		this.decoration = circle;
		this.tmpStart = start;
		this.tmpEnd = end;


		hitBoxExternal = new Circle();
		hitBoxExternal.setFill(Color.TRANSPARENT);
		//hitBoxExternal.setStroke(Color.TRANSPARENT);
		hitBoxExternal.setStroke(Color.GREEN);

		hitBoxInner = new Circle();

		hitBoxInner.setFill(Color.TRANSPARENT);
		//hitBoxInner.setStroke(Color.TRANSPARENT);
		hitBoxInner.setStroke(Color.RED);

		origin = new Vector2(decoration.getCenterX(),decoration.getCenterY());

		reBuildHitBoxes();

		initProbes(circle, start, end);
	}

	@Override
	protected void initProbes(Shape shape, Probe... probes) {
		Circle circle = (Circle) shape;
		Probe probe = new Probe(circle.getCenterX(),circle.getCenterY());

		super.initProbes(shape,probe);
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

		manager.removeEmptyProbe(tmpStart);
		manager.removeEmptyProbe(tmpEnd);
	}

	@Override
	public void remove(Pane[] layers) {
		super.remove(layers,decoration,hitBoxExternal,hitBoxInner);
	}

	//fixme:  разрулить origin и editPos
	@Override
	public void beginReplace(double x, double y) {
		origin = new Vector2(x, y);
		editPos = new Vector2(decoration.getCenterX(),decoration.getCenterY());
	}

	@Override
	public void replace(double x, double y) {
		Vector2 currentPosition = new Vector2(x, y);
		Vector2 delta = origin.subtract(currentPosition);

		decoration.setCenterX(editPos.getX() + delta.getX());
		decoration.setCenterY(editPos.getY() + delta.getY());

		reBuildHitBoxes();
	}

	@Override
	public void endReplace(double x, double y, ProbeManager manager) {
		replace(x, y);
		reBuildProbes(manager);
	}


	@Override
	public void beginResize(double x, double y) {}

	@Override
	public void resize(double x, double y) {
		Vector2 tmp = new Vector2(x,y);
		decoration.setRadius(origin.subtract(tmp).sqrMaginitude());

		reBuildHitBoxes();

	}

	@Override
	public void endResize(double x, double y, ProbeManager manager) {
		resize(x, y);
	}

	@Override
	public void reBuildProbes(ProbeManager manager) {
		manager.remove(decoration);

		Probe tmp = manager.getPosOfExistingPoint(
				decoration.getCenterX(),decoration.getCenterY());

		replace(tmp.getX(),tmp.getY());
		initProbes(decoration,tmp);
		origin = new Vector2(decoration.getCenterX(),decoration.getCenterY());
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
	public void getInfo() {

	}

	@Override
	public void reBuildHitBoxes() {
		double tmpWidth = (ADDITIONAL_WIDTH + decoration.getStrokeWidth()) / 2.0;

		double innerRadius = (decoration.getRadius() >= tmpWidth)
				? decoration.getRadius() - tmpWidth : 0;

		hitBoxInner.setRadius(innerRadius);
		hitBoxExternal.setRadius(decoration.getRadius() + tmpWidth);

		hitBoxInner.setCenterX(decoration.getCenterX());
		hitBoxInner.setCenterY(decoration.getCenterY());
		hitBoxExternal.setCenterX(decoration.getCenterX());
		hitBoxExternal.setCenterY(decoration.getCenterY());
	}

	@Override
	public Shape getShape() {
		return null;
	}

	@Override
	public boolean contains(double x, double y) {
		return hitBoxExternal.contains(x,y) && !hitBoxInner.contains(x,y);
	}

	@Override
	public boolean containsInner(double x, double y) {
		return false;
	}
}
