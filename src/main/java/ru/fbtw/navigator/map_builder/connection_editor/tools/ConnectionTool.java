package ru.fbtw.navigator.map_builder.connection_editor.tools;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import ru.fbtw.navigator.map_builder.connection_editor.LayersName;
import ru.fbtw.navigator.map_builder.connection_editor.node.LevelConnection;
import ru.fbtw.navigator.map_builder.connection_editor.node.LevelEntity;
import ru.fbtw.navigator.map_builder.connection_editor.node.LevelEntityManager;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;
import ru.fbtw.navigator.map_builder.utils.common.ScreenTool;

public class ConnectionTool implements ScreenTool {
	private static final double WIDTH = 10;
	private LevelEntityManager manager;
	private Line tmp;
	private LevelEntity beginEntity;
	private int beginSocketId;

	public ConnectionTool(LevelEntityManager manager) {
		this.manager = manager;
	}

	@Override
	public void onPressed(double x, double y) {
		beginEntity = manager.select(x, y);
		if (beginEntity != null) {
			beginSocketId = beginEntity.socketContains(x, y);
			Circle beginSocket = beginEntity.getSocket(beginSocketId);
			if (beginSocket != null) {
				//System.out.printf("On lvl: %s socId: %d\n",beginEntity.getNode().getName(),beginSocketId);

				tmp = new Line(beginSocket.getCenterX(), beginSocket.getCenterY(),
						beginSocket.getCenterX(), beginSocket.getCenterY());
				tmp.setStrokeWidth(WIDTH);

				manager.getLayers()[LayersName.CONNECTIONS].getChildren().add(tmp);
			}
		}
	}

	@Override
	public void onDragged(double x, double y) {
		if (tmp != null) {
			tmp.setEndX(x);
			tmp.setEndY(y);
		}
	}

	@Override
	public void onReleased(double x, double y) {
		LevelEntity endEntity = manager.select(x, y);
		int endSocketId;

		if (beginEntity != null && tmp != null
				&& endEntity != null
				&& (endSocketId = endEntity.socketContains(x, y)) > -1
				&& beginEntity != endEntity
				&& (!beginEntity.getNode().hasConnection(endEntity.getNode(), beginSocketId)
				|| !endEntity.getNode().hasConnection(beginEntity.getNode(),endSocketId)
		)
		) {
			//System.out.printf("On lvl: %s socId: %d\n",endEntity.getNode().getName(),endSocketId);
			LevelNode.connect(beginEntity.getNode(), beginSocketId,
					endEntity.getNode(), endSocketId);

			LevelConnection connection
					= new LevelConnection(tmp, beginEntity, beginSocketId, endEntity, endSocketId);

			beginEntity.addConnection(connection);
			endEntity.addConnection(connection);

			Circle endSocket = endEntity.getSocket(endSocketId);
			tmp.setEndX(endSocket.getCenterX());
			tmp.setEndY(endSocket.getCenterY());

			manager.push(connection);
		} else {
			manager.getLayers()[LayersName.CONNECTIONS].getChildren()
					.remove(tmp);
		}
	}
}
