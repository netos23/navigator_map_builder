package ru.fbtw.navigator.map_builder.canvas.tools;


import ru.fbtw.navigator.map_builder.utils.Vector2;

public interface Positionable {

	default Vector2 getNormalizedOrigin(Vector2 o1, Vector2 o2) {
		if (o1.getX() > o2.getX()) {
			return o1.getY() > o2.getY() ? o2 : new Vector2(o2.getX(), o1.getY());
		} else {
			return o1.getY() > o2.getY() ? new Vector2(o1.getX(), o2.getY()) : o1;
		}
	}
}
