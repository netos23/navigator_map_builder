package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

public abstract class SettingsTool extends Tool{

	public SettingsTool(HolderManager manager) {
		super(manager);
	}

	public abstract void onPressed(Probe probe);

	@Deprecated
	public  void onPressed(Vector2 pos){
		Probe tmp;
		if((tmp = manager.getManager().select(pos.getX(),pos.getY())) !=  null){
			onPressed(tmp);
		}else{
			onPressed(pos.getX(),pos.getY());
		}
	}

	public abstract void onReleased(Probe probe);

}
