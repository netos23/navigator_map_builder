/*
package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.shapes.Holder;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class CircleTool extends DrawingTool implements Positionable {
	private Circle curShape;
	private Vector2 origin;

	@Override
	public Holder onPressed(MouseEvent event, CanvasProperties properties) {
		curShape = new Circle();
		origin = new Vector2(event.getX(), event.getY());
		curShape.setCenterX(origin.getX());
		curShape.setCenterX(origin.getY());

		curShape.setFill(properties.isUseFill()
				? properties.getFillColor()
				: Color.TRANSPARENT);

		return super.onPressed(properties, curShape);
	}

	@Override
	public void onDragged(MouseEvent event) {
		if (curShape != null) {
			Vector2 curPos = new Vector2(event.getX(), event.getY());

			double radiusX = Math.abs(origin.getX() - curPos.getX()) / 2;
			double radiusY = Math.abs(origin.getY() - curPos.getY()) / 2;
			curShape.setRadius(Math.max(radiusX,radiusY));


			Vector2 normalizedOrigin = getNormalizedOrigin(origin, curPos);

			curShape.setCenterX(normalizedOrigin.getX() + curShape.getRadius());
			curShape.setCenterY(normalizedOrigin.getY() + curShape.getRadius());

		}
	}

	@Override
	public void onReleased(MouseEvent event) {
		onDragged(event);
		curShape = null;
	}

	@Override
	public String toString() {
		return "Circle";
	}
}
*/
