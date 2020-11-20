package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;

public class ReplaceTool extends SettingsTool {


	public ReplaceTool(HolderManager manager) {
		super(manager);
	}

	@Override
	public void onPressed(Probe probe) {
		holders = manager.selectAll(probe);
		for(Holder h : holders){
			h.beginReplace(probe.getX(),probe.getY());
		}
	}

	@Override
	public void onPressed(double x, double y) {
		Probe tmp = manager.getManager().select(x,y);
		if(tmp != null){
			onPressed(tmp);
		}else{
			holders = manager.selectAll(x,y);
			for(Holder h : holders){
				h.beginReplace(x,y);
			}
		}
	}

	@Override
	public void onPressed(Vector2 pos) {
		onPressed(pos.getX(),pos.getY());
	}

	@Override
	public void onDragged(double x, double y) {
		if(holders != null){
			for(Holder h : holders){
				h.replace(x,y);
			}
		}
	}

	@Deprecated
	@Override
	public void onReleased(Probe probe) {
		onReleased(probe.getX(),probe.getY());
	}

	@Override
	public void onReleased(double x, double y) {
		if(holders != null) {
			for (Holder h : holders) {
				h.endReplace(x,y,manager.getManager());
			}
		}
	}
}
