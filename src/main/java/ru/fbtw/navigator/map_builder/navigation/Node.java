package ru.fbtw.navigator.map_builder.navigation;

import java.util.ArrayList;

public class Node {
	private int x;
	private int y;

	private String name;
	private String description;

	private NodeType type;
	private ArrayList<Node> connections;

	public Node(int x, int y, String name, String description, NodeType type) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.description = description;
		this.type = type;
	}

	public static void makeConnection(Node o1, Node o2){
		o1.getConnections().add(o2);
		o2.getConnections().add(o1);
	}

	public static void breakConnection(Node o1, Node o2){
		o1.getConnections().remove(o2);
		o2.getConnections().remove(o1);
	}



	public boolean hasConnection(Node other){
		return connections.contains(other);
	}

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

	public void setName(String name) {
		this.name = name;
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
}
