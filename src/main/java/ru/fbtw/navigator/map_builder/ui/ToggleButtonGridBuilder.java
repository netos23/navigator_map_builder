package ru.fbtw.navigator.map_builder.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import ru.fbtw.navigator.map_builder.canvas.tools.DrawingTool;
import ru.fbtw.navigator.map_builder.utils.ImageUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

//todo: remove generic usage
public class ToggleButtonGridBuilder <T>{
	private String toString;
	private boolean isStatic;
	private T[] source;
	private EventHandler<ActionEvent> onClick;
	private boolean isUseImage;
	private boolean isUseName;


	public ToggleButtonGridBuilder<T> setToString(String toString) {
		this.toString = toString;
		return this;
	}

	public ToggleButtonGridBuilder<T> setStatic(boolean aStatic) {
		isStatic = aStatic;
		return this;
	}

	public ToggleButtonGridBuilder<T> setSource(T[] source) {
		this.source = source;
		return this;
	}

	public ToggleButtonGridBuilder<T> setOnClick(EventHandler<ActionEvent> onClick) {
		this.onClick = onClick;
		return this;
	}

	public ToggleButtonGridBuilder<T> setUseImage(boolean useImage) {
		isUseImage = useImage;
		return this;
	}

	public ToggleButtonGridBuilder<T> setUseName(boolean useName) {
		isUseName = useName;
		return this;
	}

	public  ArrayList<ToggleButton> build(){
		try {
			ArrayList<ToggleButton> result = new ArrayList<>();

			for (T obj : source) {
				ToggleButton toggleButton;

				Method toStringMethod = obj
						.getClass()
						.getMethod(toString);

				String name = (String) toStringMethod.invoke(
						(isStatic) ? null : obj
				);

				if(isUseName) {
					toggleButton = new ToggleButton(name);
				}else{
					toggleButton = new ToggleButton();
				}

				toggleButton.setOnAction(onClick);


				if(isUseImage && name != null && !name.isEmpty()) {
					toggleButton.setGraphic(
							ImageUtils.loadImage("image/buttons/" + name + ".png")
					);
				}

				result.add(toggleButton);
			}
			return result;
		}catch (ReflectiveOperationException | IOException ex){
			ex.printStackTrace();

		}
		return null;
	}
}
