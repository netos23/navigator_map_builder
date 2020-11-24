package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.holder.RectangleHolder;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class RectangleTool extends DrawingTool implements Positionable{
	private Rectangle curShape;
	private Probe start;

	public RectangleTool(HolderManager manager) {
		super(manager);
	}


	@Override
	public Shape onPressed(Probe start, CanvasProperties properties) {
		this.start = start;
		curShape = new Rectangle();

		curShape.setX(start.getX());
		curShape.setY(start.getY());


		curShape.setFill(properties.isUseFill()
				? properties.getFillColor()
				: Color.TRANSPARENT
		);

		return super.onPressed(properties,curShape);

	}

	@Override
	public void onDragged(double x, double y) {
		if(curShape != null) {
			Vector2 curPos = new Vector2(x,y);
			Vector2 origin = new Vector2(start.getX(),start.getY());

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
	public Holder onReleased(Probe end) {
		onDragged(end.getX(),end.getY());

		return new RectangleHolder(curShape, start,end);
	}

	@Override
	public String toString() {
		return "Rectangle";
	}


}
