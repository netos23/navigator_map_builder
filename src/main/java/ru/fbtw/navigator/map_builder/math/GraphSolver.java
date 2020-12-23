package ru.fbtw.navigator.map_builder.math;

import ru.fbtw.navigator.map_builder.core.navigation.LevelConnection;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class GraphSolver {


	public static <T extends GraphNode> boolean testNodeAvailabilityByDs(List<T> nodeSystem) {
		try {
			final Method getNeighbors = GraphNode.class.getMethod("getNeighbors");

			return availabilityWideSearch(nodeSystem, getNeighbors);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean testLevelAvailability(HashSet<LevelConnection> connections, List<LevelNode> levels) {
		ArrayList<LevelNodeGraphEntry> entries
				= levels.stream()
				.map(LevelNodeGraphEntry::new)
				.collect(Collectors.toCollection(ArrayList::new));

		for (LevelConnection connection : connections) {
			LevelNodeGraphEntry entryA = entries.get(levels.indexOf(connection.getNodeA()));
			LevelNodeGraphEntry entryB = entries.get(levels.indexOf(connection.getNodeB()));

			LevelNodeGraphEntry.connect(entryA, entryB);
		}

		return testNodeAvailabilityByDs(entries);
	}

	@Deprecated
	private static <T> boolean availabilityWideSearch(List<T> graph, Method neighborsGetter) {
		HashSet<T> unchecked = new HashSet<>(graph);

		LinkedList<T> queue = new LinkedList<>();
		queue.addLast(getFirstFormSet(unchecked));

		while (!queue.isEmpty()) {
			T element = queue.pollFirst();

			if (element != null && unchecked.remove(element)) {

				try {

					queue.addAll((Collection<? extends T>) neighborsGetter.invoke(element));

				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return unchecked.size() == 0;
	}


	private static <T> T getFirstFormSet(Set<T> collection) {
		return collection.iterator().next();
	}


	private static class LevelNodeGraphEntry implements GraphNode<LevelNodeGraphEntry> {
		private LevelNode node;
		private ArrayList<LevelNodeGraphEntry> nodes;

		LevelNodeGraphEntry(LevelNode node) {
			this.node = node;
			nodes = new ArrayList<>();
		}

		static void connect(LevelNodeGraphEntry a, LevelNodeGraphEntry b) {
			a.nodes.add(b);
			b.nodes.add(a);
		}

		@Override
		public List<LevelNodeGraphEntry> getNeighbors() {
			return nodes;
		}

		@Override
		public String getHashName() {
			return null;
		}

		@Override
		public int hashCode() {
			return node.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return node.equals(obj);
		}
	}
}
