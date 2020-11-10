package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.shapes.LineHolder;
import ru.fbtw.navigator.map_builder.probe.Probe;


public class LineTool extends DrawingTool {
	private Line curShape;
	private Probe start, end;

	@Override
	public Shape onPressed(Probe start, CanvasProperties properties) {
		this.start = start;
		curShape = new Line();

		curShape.setStartX(start.getX());
		curShape.setStartY(start.getY());

		curShape.setEndX(start.getX());
		curShape.setEndY(start.getY());


		return super.onPressed(properties,curShape);
	}

	@Override
	public void onDragged(double x, double y) {
		if(curShape!=null) {
			curShape.setEndX(x);
			curShape.setEndY(y);
		}
	}

	@Override
	public LineHolder onReleased(Probe end) {
		this.end = end;

		onDragged(end.getX(),end.getY());

		/*curShape = null;
		start = null;
		end = null;*/
		return new LineHolder(curShape, start,end);
	}





	@Override
	public String toString() {
		return "Line";
	}
}
