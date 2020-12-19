package ru.fbtw.navigator.map_builder.core.navigation;

import ru.fbtw.navigator.map_builder.connection_editor.node.LevelEntity;
import ru.fbtw.navigator.map_builder.utils.StringUtils;
import ru.fbtw.navigator.map_builder.utils.common.Disposable;

import java.util.HashSet;

public class LevelConnection implements Disposable {
	private LevelNode nodeA;
	private LevelNode nodeB;
	private Node socketA;
	private Node socketB;

	private HashSet<LevelConnection> storage;

	private int h = 0;

	public LevelConnection(
			LevelNode nodeA,
			LevelNode nodeB,
			Node socketA,
			Node socketB,
			HashSet<LevelConnection> storage
	) throws ConnectionFormatException {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.socketA = socketA;
		this.socketB = socketB;
		this.storage = storage;

		if(nodeA.equals(nodeB)){
			throw new ConnectionFormatException();
		}
	}

	public LevelConnection(
			LevelEntity nodeAEntity,
			LevelEntity nodeBEntity,
			String socketAName,
			String socketBName,
			HashSet<LevelConnection> storage
	) throws ConnectionFormatException {
		this.storage = storage;

		nodeA = nodeAEntity.getNode();
		nodeB = nodeBEntity.getNode();

		socketA = nodeA.getSocketByHashName(socketAName);
		socketB = nodeB.getSocketByHashName(socketBName);

		if(nodeA.equals(nodeB)){
			throw new ConnectionFormatException();
		}
	}


	public Node getSocketA() {
		return socketA;
	}

	public Node getSocketB() {
		return socketB;
	}


	public LevelNode getNodeA() {
		return nodeA;
	}

	public LevelNode getNodeB() {
		return nodeB;
	}

	@Override
	public int hashCode() {
		String socketAName = StringUtils
				.getOrDefault(socketA.getHashName(), "");
		String socketBName = StringUtils
				.getOrDefault(socketB.getHashName(), "");

		if (h == 0 && !socketAName.isEmpty() && !socketBName.isEmpty()) {
			String hashVal;

			if (socketAName.length() == socketBName.length()) {
				hashVal = socketAName.compareTo(socketBName) > 0
						? socketAName + socketBName
						: socketBName + socketAName;
			} else {
				hashVal = socketAName.length() > socketBName.length()
						? socketAName + socketBName
						: socketBName + socketAName;
			}

			return (h = hashVal.hashCode());
		}

		return h;
	}

	@Override
	public boolean equals(Object obj) {
		return hashCode() == obj.hashCode();
	}

	@Override
	public void dispose() {
		socketA.getLevelConnections()
				.remove(this);
		socketB.getLevelConnections()
				.remove(this);

		storage.remove(this);
	}
}
