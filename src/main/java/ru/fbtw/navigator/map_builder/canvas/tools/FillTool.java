package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;

public class FillTool extends Tool {

	public FillTool(HolderManager manager) {
		super(manager);
	}

	@Override
	public void onPressed(double x, double y) {
		holders = manager.selectAllByInsideContains(x, y);

		for (Holder h : holders) {
			Paint color = manager.getCanvasProperties().isUseFill()
					? manager.getCanvasProperties().getFillColor()
					: Color.TRANSPARENT;

			h.setFill(color);
		}
	}

	@Override
	public void onDragged(double x, double y) {

	}

	@Override
	public void onReleased(double x, double y) {

	}
}
