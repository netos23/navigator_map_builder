package ru.fbtw.navigator.map_builder.connection_editor.node;

import javafx.scene.layout.Pane;
import ru.fbtw.navigator.map_builder.connection_editor.ConnectionEditorProperties;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.core.navigation.LevelConnection;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;

import java.util.ArrayList;
import java.util.HashSet;

public class LevelEntityManager {
	private static final int offset = 200;

	private ArrayList<LevelEntity> levelEntities;
	private HashSet<LevelConnectionEntity> levelConnections;
	private ConnectionEditorProperties properties;
	private Pane[] layers;

	private Project project;
	private HashSet<LevelConnection> connectionsManager;

	public LevelEntityManager(
			Project project,
			ConnectionEditorProperties properties
	) {
		this.project = project;
		this.properties = properties;

		levelEntities = new ArrayList<>();
		levelConnections = new HashSet<>();
		layers = properties.getSource().getLayers();
		connectionsManager = project.getConnections();

		initLevelEntities();
	}

	private void initLevelEntities() {
		ArrayList<LevelNode> nodeSystem = project.getNodeSystem();
		for (LevelNode level : nodeSystem) {
			levelEntities.add(new LevelEntity(level));
		}

		setDefaultPositions();
		reBuildConnections();
	}

	private void reBuildConnections(){

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




	public Pane[] getLayers() {
		return layers;
	}

	public LevelEntity select(double x, double y) {
		return levelEntities.stream()
				.filter(lvl -> lvl.contains(x, y))
				.findFirst()
				.orElse(null);
	}

	public LevelConnectionEntity selectConnection(double x, double y){
		return levelConnections.stream()
				.filter(connection -> connection.contains(x, y))
				.findFirst()
				.orElse(null);
	}


	public void push(LevelConnectionEntity connection) {
		levelConnections.add(connection);
	}

	public void remove(LevelConnectionEntity connection) {
		levelConnections.remove(connection);
	}

	public HashSet<LevelConnection> getConnectionsManager() {
		return connectionsManager;
	}
}
