package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;

public class LineTool extends DrawingTool {
	private Line curShape;

	@Override
	public Shape onPressed(MouseEvent event, CanvasProperties properties) {
		curShape = new Line();

		curShape.setStartX(event.getX());
		curShape.setStartY(event.getY());
		onDragged(event);

		return super.onPressed(properties,curShape);
	}

	@Override
	public void onDragged(MouseEvent event) {
		if(curShape!=null) {
			curShape.setEndX(event.getX());
			curShape.setEndY(event.getY());
		}
	}

	@Override
	public void onReleased(MouseEvent event) {
		onDragged(event);
		curShape = null;
	}

	@Override
	public String toString() {
		return "Line";
	}
}
