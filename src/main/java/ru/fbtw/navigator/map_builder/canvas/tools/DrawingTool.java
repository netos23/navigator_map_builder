package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;

import java.util.ArrayList;

public abstract class DrawingTool {

	protected  ArrayList<Probe> probes;

	Shape onPressed(CanvasProperties properties, Shape shape){
		setProperties(shape,properties);
		return shape;
	}
	public abstract Shape onPressed(Probe start, CanvasProperties properties);
	public abstract void onDragged(double x,double y);
	public abstract Holder onReleased(Probe end);

	/**
	 * Sets the current color and width values, as well as some default values
	 */
	private void setProperties(Shape shape, CanvasProperties properties){
		shape.setStroke(properties.getColor());
		shape.setStrokeWidth(properties.getLineWidth());
		shape.setSmooth(true);
	}

	@Deprecated
	boolean setExistProbe(Probe probe, MouseEvent event){
		for(Probe existProbe : probes){
			if(probe.isContainsPoint(event.getX(), event.getY())){
				probe = existProbe;
				return true;
			}
		}
		return false;
	}


}

