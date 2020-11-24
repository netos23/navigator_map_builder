package ru.fbtw.navigator.map_builder.canvas.node;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.navigation.Node;

import java.util.ArrayList;

public class NodeHolderManager {
	private ArrayList<Node> nodeSystem;
	private ArrayList<NodeHolder> nodeHolders;

	public NodeHolderManager(ArrayList<Node> nodeSystem) {
		this.nodeSystem = nodeSystem;
		nodeHolders = new ArrayList<>();
	}


	public Holder select(double x, double y) {
		return nodeHolders.stream()
				.filter(v -> v.contains(x, y))
				.findFirst()
				.orElse(null);
	}
}
