package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.holder.CircleHolder;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class CircleTool extends DrawingTool implements Positionable {
	private Circle curShape;
	private Probe start;

	public CircleTool(HolderManager manager) {
		super(manager);
	}


	@Override
	public Shape onPressed(Probe start, CanvasProperties properties) {
		this.start = start;
		curShape = new Circle();

		curShape.setCenterX(start.getX());
		curShape.setCenterY(start.getY());

		curShape.setFill(properties.isUseFill()
				? properties.getFillColor()
				: Color.TRANSPARENT);

		return super.onPressed(properties, curShape);
	}

	@Override
	public void onDragged(double x, double y) {
		if (curShape != null) {
			Vector2 curPos = new Vector2(x, y);
			Vector2 origin = new Vector2(start.getX(), start.getY());

			double radiusX = Math.abs(origin.getX() - curPos.getX()) / 2;
			double radiusY = Math.abs(origin.getY() - curPos.getY()) / 2;
			curShape.setRadius(Math.max(radiusX, radiusY));


			Vector2 normalizedOrigin = getNormalizedOrigin(origin, curPos);

			curShape.setCenterX(normalizedOrigin.getX() + curShape.getRadius());
			curShape.setCenterY(normalizedOrigin.getY() + curShape.getRadius());
		}
	}

	@Override
	public Holder onReleased(Probe end) {
		onDragged(end.getX(), end.getY());
		return new CircleHolder(curShape, start, end);
	}


	@Override
	public String toString() {
		return "Circle";
	}

}
