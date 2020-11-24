package ru.fbtw.navigator.map_builder.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import ru.fbtw.navigator.map_builder.utils.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;


public class ToggleButtonGridBuilder {
	private String[] source;
	private EventHandler<ActionEvent> onClick;
	private boolean isUseImage;
	private boolean isUseName;

	public ToggleButtonGridBuilder setSource(String[] source) {
		this.source = source;
		return this;
	}

	public ToggleButtonGridBuilder setOnClick(EventHandler<ActionEvent> onClick) {
		this.onClick = onClick;
		return this;
	}

	public ToggleButtonGridBuilder setUseImage(boolean useImage) {
		isUseImage = useImage;
		return this;
	}

	public ToggleButtonGridBuilder setUseName(boolean useName) {
		isUseName = useName;
		return this;
	}

	public ArrayList<ToggleButton> build() throws IOException {

		ArrayList<ToggleButton> result = new ArrayList<>();

		for (String name : source) {
			ToggleButton toggleButton = isUseName
					? new ToggleButton(name)
					: new ToggleButton();

			toggleButton.setOnAction(onClick);


			if (isUseImage && name != null && !name.isEmpty()) {
				toggleButton.setGraphic(
						ImageUtils.loadImage("image/buttons/" + name + ".png")
				);
			}

			result.add(toggleButton);
		}
		return result;

	}
}
