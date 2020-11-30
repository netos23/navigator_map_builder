package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.utils.common.ScreenTool;

import java.util.ArrayList;

public abstract class Tool implements ScreenTool {
	protected HolderManager manager;
	protected ArrayList<Holder> holders;

	public Tool(HolderManager manager) {
		this.manager = manager;
	}

}
