package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class EllipseTool extends DrawingTool implements Positionable {
	private Ellipse curShape;
	private Vector2 origin;

	@Override
	public Shape onPressed(MouseEvent event, CanvasProperties properties) {
		curShape = new Ellipse();
		origin = new Vector2(event.getX(), event.getY());
		curShape.setCenterX(origin.getX());
		curShape.setCenterY(origin.getY());

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
			curShape.setRadiusX(radiusX);
			curShape.setRadiusY(radiusY);

			Vector2 normalizedOrigin = getNormalizedOrigin(origin, curPos);

			curShape.setCenterX(normalizedOrigin.getX() + radiusX);
			curShape.setCenterY(normalizedOrigin.getY() + radiusY);

		}
	}

	@Override
	public void onReleased(MouseEvent event) {
		onDragged(event);
		curShape = null;
	}

	@Override
	public String toString() {
		return "Ellipse";
	}
}
