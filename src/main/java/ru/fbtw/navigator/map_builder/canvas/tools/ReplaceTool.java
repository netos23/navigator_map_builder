package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;

public class ReplaceTool extends SettingsTool {
	private ArrayList<Holder> target;

	public ReplaceTool(HolderManager manager) {
		super(manager);
	}

	@Override
	public void onPressed(Probe probe) {
		target = manager.selectAll(probe);
		for(Holder h : target){
			h.beginResize(probe.getX(),probe.getY());
		}
	}

	@Override
	public void onPressed(double x, double y) {
		Probe tmp = manager.getManager().select(x,y);
		if(tmp != null){
			onPressed(tmp);
		}else{
			target = manager.selectAll(x,y);
			for(Holder h : target){
				h.beginResize(x,y);
			}
		}
	}

	@Override
	public void onPressed(Vector2 pos) {
		onPressed(pos.getX(),pos.getY());
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
