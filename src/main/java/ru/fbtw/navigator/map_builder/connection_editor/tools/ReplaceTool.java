package ru.fbtw.navigator.map_builder.connection_editor.tools;

import ru.fbtw.navigator.map_builder.connection_editor.node.LevelEntity;
import ru.fbtw.navigator.map_builder.connection_editor.node.LevelEntityManager;
import ru.fbtw.navigator.map_builder.utils.common.ScreenTool;

public class ReplaceTool implements ScreenTool {
	private LevelEntityManager manager;
	private LevelEntity entity;

	public ReplaceTool(LevelEntityManager manager) {
		this.manager = manager;
	}

	@Override
	public void onPressed(double x, double y) {
		entity = manager.select(x, y);
		if(entity != null){
			entity.beginReplace(x, y);
		}
	}

	@Override
	public void onDragged(double x, double y) {
		if(entity != null){
			entity.replace(x, y);
		}
	}

	@Override
	public void onReleased(double x, double y) {
		if(entity != null){
			entity.endReplace(x, y);
		}
	}
}
