package ru.fbtw.navigator.map_builder.core.navigation;

import ru.fbtw.navigator.map_builder.math.GraphNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Node implements GraphNode<Node> {
	private static int nameCount = 0;
	private static String namePref = "Node %d";
	private static HashSet<String> names = new HashSet<>();


	private int x;
	private int y;

	private String name;
	private String description;

	private NodeType type;
	private ArrayList<Node> connections;

	public Node(int x, int y, String name, String description, NodeType type) {
		this.x = x;
		this.y = y;
		this.description = description;
		this.type = type;

		this.name = name == null || name.isEmpty()
				? getNextName()
				: name;

		connections = new ArrayList<>();

	}
	public Node(double x, double y){
		this((int) x, (int) y, null,"",NodeType.DESTINATION);
	}

	public static void makeConnection(Node o1, Node o2) {
		if(!o1.equals(o2)) {
			o1.getNeighbors().add(o2);
			o2.getNeighbors().add(o1);
		}
	}

	public static void breakConnection(Node o1, Node o2) {
		o1.getNeighbors().remove(o2);
		o2.getNeighbors().remove(o1);
	}

	private static String getNextName() {
		String name;
		do {
			name = String.format(namePref, nameCount++);
		} while (names.contains(name));
		names.add(name);
		return name;
	}

	public static boolean validateName(String name){
		return !(name == null || name.isEmpty() || names.contains(name));
	}

	public static Node getEmptyNode() {
		return new Node(0, 0, getNextName(), null, null);
	}

	public static void removeName(String name) {
		names.remove(name);
	}

	public boolean hasConnection(Node other) {
		return connections.contains(other);
	}

	@Deprecated
	public ArrayList<Node> getConnections() {
		return connections;
	}

	public void setConnections(ArrayList<Node> connections) {
		this.connections = connections;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public boolean setName(String name) {
		if(validateName(name)){
			names.remove(this.name);
			this.name = name;
			names.add(name);
			return true;
		}else {
			return false;
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	@Override
	public List<Node> getNeighbors() {
		return connections;
	}
}
