package ru.fbtw.navigator.map_builder.math;

import java.util.ArrayList;
import java.util.HashSet;

public interface MultiGraphNode<T> {
	ArrayList<ArrayList<T>> getSockets();

	default HashSet<T> getUniqueNeighbors(){
		HashSet<T> valueSet = new HashSet<>();

		for (ArrayList<T> socket : getSockets()) {
			valueSet.addAll(socket);
		}

		return valueSet;
	}
}
