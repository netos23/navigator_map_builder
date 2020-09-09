package ru.fbtw.navigator.map_builder.canvas;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;



public class CanvasController{
	private Pane[] layers;
	private Pane inputLayer;

	public CanvasController() {
		layers = new Pane[5];
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new Pane();
		}
		inputLayer = layers[1];
	}

	public void setBackground(Image bg){
		enableBackground(true);

		ImageView imageView = new ImageView(bg);
		layers[0].getChildren().add(imageView);
	}

	public void enableBackground(boolean value){
		layers[0].setVisible(value);
	}

	public Pane[] getLayers() {
		return layers;
	}

}
