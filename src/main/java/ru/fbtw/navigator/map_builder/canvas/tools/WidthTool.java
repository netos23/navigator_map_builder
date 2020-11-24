package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;

public class WidthTool extends Tool {

	public WidthTool(HolderManager manager) {
		super(manager);
	}

	@Override
	public void onPressed(double x, double y) {
		holders = manager.selectAll(x,y);
		for(Holder h : holders){
			h.setStrokeWidth(manager.getCanvasProperties().getLineWidth());
		}
	}

	@Override
	public void onDragged(double x, double y) {

	}


	@Override
	public void onReleased(double x, double y) {

	}
}
