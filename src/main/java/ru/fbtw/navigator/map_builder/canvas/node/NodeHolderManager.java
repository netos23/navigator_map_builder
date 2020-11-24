package ru.fbtw.navigator.map_builder.canvas.node;

import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.navigation.Node;

import java.util.ArrayList;
@Deprecated
public class NodeHolderManager {
	private ArrayList<Node> nodeSystem;

	private HolderManager manager;

	public NodeHolderManager() {
		nodeSystem = new ArrayList<>();
	}

}
