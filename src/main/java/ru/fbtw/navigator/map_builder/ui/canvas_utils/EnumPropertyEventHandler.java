package ru.fbtw.navigator.map_builder.ui.canvas_utils;

public interface EnumPropertyEventHandler<T extends Enum> {
	void onValueChanged(T value);
}
