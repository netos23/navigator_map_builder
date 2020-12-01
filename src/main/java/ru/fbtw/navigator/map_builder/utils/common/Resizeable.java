package ru.fbtw.navigator.map_builder.utils.common;

public interface Resizeable {
	void beginResize(double x, double y);
	void resize(double x, double y);
	void endResize(double x, double y);
}
