package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class RectangleTool extends DrawingTool implements Positionable{
	private Rectangle curShape;
	private Vector2 origin;
	@Override
	public Shape onPressed(MouseEvent event, CanvasProperties properties) {
		curShape = new Rectangle();
		origin = new Vector2(event.getX(),event.getY());
		curShape.setX(origin.getX());
		curShape.setY(origin.getY());

		if(properties.isUseFill()){
			curShape.setFill(properties.getFillColor());
		}else{
			curShape.setFill(Color.TRANSPARENT);
		}
		return super.onPressed(properties,curShape);
	}

	@Override
	public void onDragged(MouseEvent event) {
		if(curShape != null) {
			Vector2 curPos = new Vector2(event.getX(), event.getY());

			double width = Math.abs(origin.getX() - curPos.getX());
			double height = Math.abs(origin.getY() - curPos.getY());

			Vector2 normalizedOrigin = getNormalizedOrigin(origin, curPos);

			curShape.setX(normalizedOrigin.getX());
			curShape.setY(normalizedOrigin.getY());
			curShape.setWidth(width);
			curShape.setHeight(height);
		}
	}

	@Override
	public void onReleased(MouseEvent event) {
		onDragged(event);
		curShape = null;
	}

	@Override
	public String toString() {
		return "Rectangle";
	}
}
