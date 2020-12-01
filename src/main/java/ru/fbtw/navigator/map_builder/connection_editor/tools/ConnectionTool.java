package ru.fbtw.navigator.map_builder.connection_editor.tools;

import ru.fbtw.navigator.map_builder.connection_editor.node.LevelEntity;
import ru.fbtw.navigator.map_builder.connection_editor.node.LevelEntityManager;
import ru.fbtw.navigator.map_builder.utils.common.ScreenTool;

public class ConnectionTool implements ScreenTool {
	private  LevelEntityManager manager;
	public ConnectionTool(LevelEntityManager manager) {
		this.manager = manager;
	}

	@Override
	public void onPressed(double x, double y) {
		LevelEntity entity = manager.select(x,y);
	}

	@Override
	public void onDragged(double x, double y) {

	}

	@Override
	public void onReleased(double x, double y) {

	}
}
