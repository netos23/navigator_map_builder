package ru.fbtw.navigator.map_builder.canvas;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CanvasProperties {
	private Paint color;
	private Paint fillColor;
	private boolean useFill;
	private double lineWidth;

	private int tool;
	private int toolGroup;
	private CanvasController source;

	public CanvasProperties(Paint color, Paint fillColor, boolean useFill, double lineWidth) {
		this.color = color;
		this.fillColor = fillColor;
		this.useFill = useFill;
		this.lineWidth = lineWidth;
		this.source = null;
	}

	public Paint getColor() {
		return color;
	}

	public void setColor(Paint color) {
		this.color = color;
	}

	public int getToolGroup() {
		return toolGroup;
	}

	public CanvasProperties setToolGroup(int toolGroup) {
		this.toolGroup = toolGroup;
		return this;
	}

	public Paint getFillColor() {
		return fillColor;
	}

	public void setFillColor(Paint fillColor) {
		this.fillColor = fillColor;
	}

	public boolean isUseFill() {
		return useFill;
	}

	public void setUseFill(boolean useFill) {
		this.useFill = useFill;
	}

	public double getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getTool() {
		return tool;
	}

	public void setTool(int tool) {
		this.tool = tool;
	}

	public CanvasController getSource() {
		return source;
	}

	public void setSource(CanvasController source) {
		this.source = source;
	}

	public static final CanvasProperties DEFAULT_PROPERTIES =
			new CanvasProperties(Color.BLACK,Color.WHITE,false,5);
}
