package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;

import java.util.ArrayList;

public abstract class DrawingTool extends Tool {

	protected ArrayList<Probe> probes;

	public DrawingTool(HolderManager manager) {
		super(manager);
	}

	Shape onPressed(CanvasProperties properties, Shape shape) {
		setProperties(shape, properties);
		return shape;
	}


	@Override
	public void onPressed(double x, double y) {

		Probe startPosition = manager.getManager()
				.getPosOfExistingPoint(x, y);

		Shape tempShape = onPressed(startPosition, manager.getCanvasProperties());

		manager.getCanvasProperties()
				.getSource()
				.getLayers()[LayersName.DECORATION]
				.getChildren()
				.add(tempShape);

	}


	public abstract Shape onPressed(Probe start, CanvasProperties properties);

	@Override
	public void onReleased(double x, double y) {
		Probe endPosition = manager.getManager()
				.getPosOfExistingPoint(x, y);

		Holder editHolder = onReleased(endPosition);

		manager.push(editHolder);
	}

	public abstract Holder onReleased(Probe end);


	/**
	 * Sets the current color and WIDTH values, as well as some default values
	 */
	private void setProperties(Shape shape, CanvasProperties properties) {
		shape.setStroke(properties.getColor());
		shape.setStrokeWidth(properties.getLineWidth());
		shape.setSmooth(true);
	}


}

