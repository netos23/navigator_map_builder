package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public class FillTool extends SettingsTool{

	public FillTool(HolderManager manager) {
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
		holders = manager.selectAllByInsideContains(x, y);

		for(Holder h : holders){
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
	public void onReleased(Probe probe) {

	}

	@Override
	public void onReleased(double x, double y) {

	}
}
