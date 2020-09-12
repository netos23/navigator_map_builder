package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;

public abstract class DrawingTool {


	Shape onPressed(CanvasProperties properties, Shape shape){
		setProperties(shape,properties);
		return shape;
	}
	public abstract Shape onPressed(MouseEvent event, CanvasProperties properties);
	public abstract void onDragged(MouseEvent event);
	public abstract void onReleased(MouseEvent event);

	private void setProperties(Shape shape, CanvasProperties properties){
		shape.setStroke(properties.getColor());
		shape.setStrokeWidth(properties.getLineWidth());
		shape.setSmooth(true);
	}


}

