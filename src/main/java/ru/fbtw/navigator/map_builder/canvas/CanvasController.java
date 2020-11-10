package ru.fbtw.navigator.map_builder.canvas;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.shapes.Holder;
import ru.fbtw.navigator.map_builder.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.tools.*;
import ru.fbtw.navigator.map_builder.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;


public class CanvasController{
	private Pane[] layers;
	private Pane inputLayer;
	private ProbeManager manager;
	//private int currentTool, toolGroup;

	private CanvasProperties properties;

	public static DrawingTool[] tools = new DrawingTool[]{
			new LineTool(),
			/*new RectangleTool(),
			new EllipseTool(),
			new CircleTool(),*/
	};

	public CanvasController(CanvasProperties properties) {
		this.properties = properties;
		this.properties.setSource(this);

		manager = new ProbeManager(this);

		layers = new Pane[5];
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new Pane();
		}
		inputLayer = layers[4];


		/*currentTool = properties.getTool();
		toolGroup = ToolGroup.getToolGroupById(currentTool);*/

		setOnClicks();
	}

	private void setOnClicks() {

		inputLayer.setOnMousePressed(event -> {
			Probe curPos = manager.getPosOfExistingPoint(event);

			switch (ToolGroup.getToolGroupById(properties.getTool())){
				case 0:
					Shape tmp = tools[properties.getTool()].onPressed(curPos, properties);
					layers[1].getChildren().add(tmp);
					break;
				case 1:
					useNode();
					break;
				case 2:

					break;
				case 3:
					useOther();
					break;
			}
		});



		inputLayer.setOnMouseDragged(event -> {
			Vector2 curPos = manager.getPosOfExistingTempPoint(event);
			switch (ToolGroup.getToolGroupById(properties.getTool())){
				case 0:
					tools[properties.getTool()].onDragged(curPos.getX(),curPos.getY());
					break;
				case 1:
					useNode();
					break;
				case 2:

					break;
				case 3:
					useOther();
					break;
			}
		});

		inputLayer.setOnMouseReleased(event -> {
			Probe curPos = manager.getPosOfExistingPoint(event);
			switch (ToolGroup.getToolGroupById(properties.getTool())){
				case 0:
					Holder tmp = tools[properties.getTool()].onReleased(curPos);
					tmp.splitLayers(layers);
					break;
				case 1:
					useNode();
					break;
				case 2:

					break;
				case 3:
					useOther();
					break;
			}
		});
	}




	private void useNode(){

	}

	private void useOther(){

	}

	public void setBackground(Image bg){
		enableBackground(true);

		ImageView imageView = new ImageView(bg);
		layers[0].getChildren().add(imageView);
		for(Pane layer : layers){
			layer.setMinSize(imageView.getX(),imageView.getY());
		}
	}

	public void enableBackground(boolean value){
		layers[0].setVisible(value);
	}

	public Pane[] getLayers() {
		return layers;
	}

	public DrawingTool[] getTools() {
		return tools;
	}

	public ProbeManager getManager() {
		return manager;
	}
}
