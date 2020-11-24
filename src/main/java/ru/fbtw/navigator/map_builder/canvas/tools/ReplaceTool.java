package ru.fbtw.navigator.map_builder.canvas.tools;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;

public class ReplaceTool extends Tool {


	public ReplaceTool(HolderManager manager) {
		super(manager);
	}


	@Override
	public void onPressed(double x, double y) {
		holders = manager.selectAll(x, y);

		for (Holder h : holders) {
			h.beginReplace(x, y);
		}

	}

	@Override
	public void onDragged(double x, double y) {
		if (holders != null) {
			for (Holder h : holders) {
				h.replace(x, y);
			}
		}
	}

	@Override
	public void onReleased(double x, double y) {
		if (holders != null) {
			for (Holder h : holders) {
				h.endReplace(x, y, manager.getManager());
			}
		}
	}
}
