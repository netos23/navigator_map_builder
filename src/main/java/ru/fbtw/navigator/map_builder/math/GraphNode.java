package ru.fbtw.navigator.map_builder.math;

import java.util.List;

public interface GraphNode<T> {
	List<T> getNeighbors();
}
