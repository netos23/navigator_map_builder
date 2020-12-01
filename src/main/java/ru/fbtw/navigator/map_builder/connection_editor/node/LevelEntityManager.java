package ru.fbtw.navigator.map_builder.connection_editor.node;

import javafx.scene.layout.Pane;
import ru.fbtw.navigator.map_builder.connection_editor.ConnectionEditorProperties;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;

import java.util.ArrayList;

public class LevelEntityManager {
	private static final int offset = 200;

	private ArrayList<LevelEntity> levelEntities;
	private ArrayList<LevelConnection> levelConnections;
	private ConnectionEditorProperties properties;
	private Pane[] layers;

	private Project project;

	public LevelEntityManager(
			Project project,
			ConnectionEditorProperties properties
	) {
		this.project = project;
		this.properties = properties;

		levelEntities = new ArrayList<>();
		levelConnections = new ArrayList<>();
		layers = properties.getSource().getLayers();

		initLevelEntities();
	}

	private void initLevelEntities() {
		ArrayList<LevelNode> nodeSystem = project.getNodeSystem();
		for (LevelNode level : nodeSystem) {
			levelEntities.add(new LevelEntity(level));
		}

		setDefaultPositions();

	}


	private void setDefaultPositions() {
		final int size = levelEntities.size();
		int count = (int) Math.round(Math.sqrt(size));

		for (int i = 0; i < size; i++) {
			LevelEntity node = levelEntities.get(i);

			final double baseOffset = offset + node.getR();

			double x = (i % count == 0)
					? baseOffset
					: levelEntities.get(i-1).getX() + baseOffset + levelEntities.get(i - 1).getR();

			double y = (i < count)
					? baseOffset
					: levelEntities.get(i-count).getY() + baseOffset + levelEntities.get(i - count).getR();

			node.setPosition(x,y);
			node.splitLayers(layers);
		}
	}


	public LevelEntity select(double x, double y) {
		return levelEntities.stream()
				.filter(lvl -> lvl.contains(x, y))
				.findFirst()
				.orElse(null);
	}


}