package ru.fbtw.navigator.map_builder.utils.common;

public interface ScreenTool {
	void onPressed(double x, double y);

	void onDragged(double x, double y);

	void onReleased(double x, double y);
}
