package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;

public class RemoveTool extends SettingsTool {

	public RemoveTool(HolderManager manager) {
		super(manager);
	}

	@Override
	public void onPressed(Probe probe) {
		ArrayList<Holder> holders = manager.selectAll(probe);

		if(holders.size() == 1){
			manager.remove(holders.get(0));
		}else if(holders.size() == 0){
			manager.getManager().removeProbeCompletely(probe);
		}
	}

	@Override
	public void onPressed(Vector2 pos) {
		onPressed(pos.getX(),pos.getY());
	}

	@Override
	public void onPressed(double x, double y) {
		Holder holder = manager.selectFirst(x,y);
		if(holder != null) {
			manager.remove(holder);
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
