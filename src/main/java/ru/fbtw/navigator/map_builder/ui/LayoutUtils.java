package ru.fbtw.navigator.map_builder.ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ru.fbtw.navigator.map_builder.utils.ImageUtils;

import java.io.IOException;

public class LayoutUtils {

	private static String filenameFormat = "common/buttons/%s.png";

	public static void setSize(Button node, double w, double h) {
		setSize((Region) node,w,h);

		Node content = node.getGraphic();
		if(content != null) {
			try {
				ImageView graphic = (ImageView) content;
				graphic.setFitHeight(h);
				graphic.setFitWidth(w);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Wrong type of button graphic");
			}
		}

	}

	public static void setSize(Region region,double w, double h){
		region.setMaxSize(w, h);
		region.setPrefSize(w, h);
		region.setMinSize(w, h);

		VBox.setVgrow(region, Priority.ALWAYS);
		HBox.setHgrow(region, Priority.ALWAYS);
	}

	public static Button getImageButton(String name, double w, double h){
		Button button = getImageButton(name);
		setSize(button,w,h);

		return button;
	}

	public static Button getImageButton(String name){
		Button button = new Button();
		Tooltip tooltip = new Tooltip(name);
		button.setTooltip(tooltip);

		try {
			String filename = String.format(filenameFormat,name);
			ImageView imageView = ImageUtils.loadImage(filename);
			button.setGraphic(imageView);
		} catch (IOException e) {
			e.printStackTrace();
			button.setText(name);
		}

		return button;
	}
}
