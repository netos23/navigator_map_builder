package ru.fbtw.navigator.map_builder.core.navigation;

import ru.fbtw.navigator.map_builder.core.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class LevelNode {
	private HashMap<String, ArrayList<LevelNode>> sockets;
	private HashMap<Integer, String> hashKeys;
	private String name;
	private Level level;

	public LevelNode(Level level) {
		this.level = level;

		sockets = new HashMap<>();
		hashKeys = new HashMap<>();
		name = level.getName();

		updateSockets();
	}

	public void updateSockets() {

		int count = 0;
		//todo : чистка удаленных уровней
		for (Node baseSocket : getBasicSockets()) {

			final String name = baseSocket.getName();

			if(!sockets.containsKey(name)){
				ArrayList<LevelNode> socket = new ArrayList<>();
				sockets.put(name, socket);
				hashKeys.put(count, name);
				count++;
			}
		}
	}

	public static void connect(LevelNode a, int socketA, LevelNode b, int socketB) {
		if(!a.equals(b)) {
			final String socketKeyA = a.hashKeys.get(socketA);
			a.sockets.get(socketKeyA).add(b);

			final String socketKeyB = b.hashKeys.get(socketB);
			b.sockets.get(socketKeyB).add(a);
		}
	}

	public static void breakConnection(LevelNode a, int socketA, LevelNode b, int socketB) {
		if(!a.equals(b)) {
			final String socketKeyA = a.hashKeys.get(socketA);
			a.sockets.get(socketKeyA).remove(b);

			final String socketKeyB = b.hashKeys.get(socketB);
			b.sockets.get(socketKeyB).remove(a);
		}
	}

	private ArrayList<Node> getBasicSockets() {
		return level.getNodeSystem()
				.stream()
				.filter(e -> e.getType() == NodeType.ZONE_CONNECTION)
				.collect(Collectors.toCollection(ArrayList::new));
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, ArrayList<LevelNode>> getSockets() {
		return sockets;
	}

	public boolean hasConnection(LevelNode node, int beginSocketId) {
		String socketKey = hashKeys.get(beginSocketId);
		return sockets.get(socketKey)
				.contains(node);
	}
}
