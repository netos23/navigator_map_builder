package ru.fbtw.navigator.map_builder.math;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class GraphSolver {


	public static <T extends GraphNode> boolean testNodeAvailabilityByDs(List<T> nodeSystem) {
		try {
			final Method getNeighbors = GraphNode.class.getMethod("getNeighbors");

			return availabilityDeepSearch(nodeSystem, getNeighbors);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}
		/*HashSet<T> checked = new HashSet<>(nodeSystem);

		LinkedList<T> queue = new LinkedList<>();
		queue.addLast(getFirstFormSet(checked));

		while (!queue.isEmpty()) {
			T element = queue.pollFirst();

			if (element != null) {
				checked.remove(element);
				queue.addAll(element.getNeighbors());
			}
		}
		return checked.size() == 0;*/
	}

	public static <T extends MultiGraphNode> boolean testLevelAvailabilityByDs(List<T> levelNodeSystem) {
		try {
			final Method getUniqueNeighbors = MultiGraphNode.class.getMethod("getUniqueNeighbors");

			return availabilityDeepSearch(levelNodeSystem, getUniqueNeighbors);
		} catch (NoSuchMethodException e) {
			return false;
		}
	}


	private static <T> boolean availabilityDeepSearch(List<T> graph, Method neighborsGetter) {
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
}
