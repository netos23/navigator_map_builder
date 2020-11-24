package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.holder.LineHolder;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.KeyManager;
import ru.fbtw.navigator.map_builder.utils.Vector2;


public class LineTool extends DrawingTool {
	private Line curShape;
	private Probe start;
	private Vector2 originPoint;

	public LineTool(HolderManager manager) {
		super(manager);
	}

	@Override
	public Shape onPressed(Probe start, CanvasProperties properties) {
		this.start = start;
		curShape = new Line();
		originPoint = new Vector2(start.getX(),start.getY());

		curShape.setStartX(start.getX());
		curShape.setStartY(start.getY());

		curShape.setEndX(start.getX());
		curShape.setEndY(start.getY());


		return super.onPressed(properties,curShape);
	}

	@Override
	public void onDragged(double x, double y) {
		if(curShape!=null) {
			Vector2 endPoint = new Vector2(x,y);

			if(KeyManager.isKeyPressed(KeyCode.SHIFT)){
				Vector2.normalizeLine(originPoint,endPoint);
			}

			curShape.setEndX(endPoint.getX());
			curShape.setEndY(endPoint.getY());
		}
	}

	@Override
	public LineHolder onReleased(Probe end) {


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
