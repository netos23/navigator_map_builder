package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;

public abstract class SettingsTool {
	protected HolderManager manager;
	protected ArrayList<Holder> holders;

	public SettingsTool(HolderManager manager) {
		this.manager = manager;
	}

	public abstract void onPressed(Probe probe);

	public abstract void onPressed(double x, double y);

	// todo: вынести селект из методов

	@Deprecated
	public  void onPressed(Vector2 pos){
		Probe tmp;
		if((tmp = manager.getManager().select(pos.getX(),pos.getY())) !=  null){
			onPressed(tmp);
		}else{
			onPressed(pos.getX(),pos.getY());
		}
	}

	public abstract void onDragged(double x, double y);

	public abstract void onReleased(Probe probe);

	public abstract void onReleased(double x, double y);
}
