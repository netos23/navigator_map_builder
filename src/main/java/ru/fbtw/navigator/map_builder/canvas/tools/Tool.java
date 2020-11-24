package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;

import java.util.ArrayList;

public abstract class Tool {
	protected HolderManager manager;
	protected ArrayList<Holder> holders;

	public Tool(HolderManager manager) {
		this.manager = manager;
	}


	public abstract void onPressed(double x, double y);

	public abstract void onDragged(double x, double y);

	public abstract void onReleased(double x, double y);
}
