package ru.fbtw.navigator.map_builder.connection_editor.node;

import javafx.scene.shape.Line;
import ru.fbtw.navigator.map_builder.utils.common.LinePoints;

public class LevelConnection {

	private final Line line;
	private final LevelEntity beginEntity;
	private final int beginSocketId;
	private final LevelEntity endEntity;
	private final int endSocketId;


	private LinePoints editPosition;

	public LevelConnection(
			Line line,
			LevelEntity beginEntity,
			int beginSocketId,
			LevelEntity endEntity,
			int endSocketId

	) {
		this.line = line;
		this.beginEntity = beginEntity;
		this.beginSocketId = beginSocketId;
		this.endEntity = endEntity;
		this.endSocketId = endSocketId;
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

	public int getEditSocket(){
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


	public int getBeginSocketId() {
		return beginSocketId;
	}

	public int getEndSocketId() {
		return endSocketId;
	}
}
