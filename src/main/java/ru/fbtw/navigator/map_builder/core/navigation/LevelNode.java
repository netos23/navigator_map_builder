package ru.fbtw.navigator.map_builder.core.navigation;

import ru.fbtw.navigator.map_builder.core.Level;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class LevelNode  {
	private ArrayList<String> socketsNames;
	private String name;
	private Level level;


	public LevelNode(Level level) {
		this.level = level;

		name = level.getName();

		updateBasicSockets();
	}

	public ArrayList<String> updateBasicSockets() {
		return socketsNames = level.getNodeSystem()
				.stream()
				.filter(e -> e.getType() == NodeType.ZONE_CONNECTION)
				.map(Node::getHashName)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public Level getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getSocketsHashNames() {
		return socketsNames;
	}

	public Node getSocketByHashName(String socketHashName) {
		return level.getNodeSystem()
				.stream()
				.filter(e -> e.getHashName().equals(socketHashName))
				.findFirst()
				.orElse(null);
	}
}
