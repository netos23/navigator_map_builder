package ru.fbtw.navigator.map_builder.connection_editor.node;

import javafx.scene.shape.Line;
import ru.fbtw.navigator.map_builder.core.navigation.LevelConnection;
import ru.fbtw.navigator.map_builder.utils.common.LinePoints;

public class LevelConnectionEntity {

	private final Line line;
	private final LevelEntity beginEntity;
	private final String beginSocketId;
	private final LevelEntity endEntity;
	private final String endSocketId;
	private final LevelConnection connection;


	private LinePoints editPosition;

	public LevelConnectionEntity(
			Line line,
			LevelEntity beginEntity,
			String beginSocketId,
			LevelEntity endEntity,
			String endSocketId,
			LevelConnection connection
	) {
		this.line = line;
		this.beginEntity = beginEntity;
		this.beginSocketId = beginSocketId;
		this.endEntity = endEntity;
		this.endSocketId = endSocketId;
		this.connection = connection;
	}

	public void beginResize(LevelEntity levelEntity) {
		editPosition = levelEntity == beginEntity
				? LinePoints.START
				: LinePoints.END;
	}


	public void resize(double x, double y) {
		if (editPosition == LinePoints.START) {
			line.setStartX(x);
			line.setStartY(y);
		} else {
			line.setEndX(x);
			line.setEndY(y);
		}
	}




	public void endResize(double x, double y) {
		resize(x, y);
	}

	public String getEditSocket(){
		return editPosition == LinePoints.START
				? beginSocketId
				: endSocketId;
	}


	public boolean contains(double x, double y) {
		return line.contains(x, y);
	}

	public Line getLine() {
		return line;
	}


	public LevelEntity getBeginEntity() {
		return beginEntity;
	}

	public LevelEntity getEndEntity() {
		return endEntity;
	}


	public String getBeginSocketId() {
		return beginSocketId;
	}

	public String getEndSocketId() {
		return endSocketId;
	}

	public LevelConnection getConnection() {
		return connection;
	}
}
