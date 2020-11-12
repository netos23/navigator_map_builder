package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.holder.EllipseHolder;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class EllipseTool extends DrawingTool implements Positionable {
	private Ellipse curShape;
	private Probe start, end;




	@Override
	public Shape onPressed(Probe start, CanvasProperties properties) {
		this.start = start;
		curShape = new Ellipse();

		curShape.setCenterX(start.getX());
		curShape.setCenterY(start.getY());

		curShape.setFill(properties.isUseFill()
				? properties.getFillColor()
				: Color.TRANSPARENT
		);

		return super.onPressed(properties, curShape);
	}

	@Override
	public void onDragged(double x, double y) {
		if (curShape != null) {
			Vector2 curPos = new Vector2(x, y);
			Vector2 origin = new Vector2(start.getX(),start.getY());

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
	public EllipseHolder onReleased(Probe end) {
		onDragged(end.getX(),end.getY());
		return new EllipseHolder(curShape,start,end);
	}

	@Override
	public String toString() {
		return "Ellipse";
	}

}
