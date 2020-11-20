package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;

public class WidthTool extends SettingsTool {

	public WidthTool(HolderManager manager) {
		super(manager);
	}

	@Override
	public void onPressed(Probe probe) {

	}

	@Override
	public void onPressed(Vector2 pos) {

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
	public void onReleased(Probe probe) {

	}

	@Override
	public void onReleased(double x, double y) {

	}
}
