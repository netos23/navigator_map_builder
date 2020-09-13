package ru.fbtw.navigator.map_builder.canvas;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.tools.*;


public class CanvasController{
	private Pane[] layers;
	private Pane inputLayer;
	//private int currentTool, toolGroup;

	private CanvasProperties properties;

	public static DrawingTool[] tools = new DrawingTool[]{
			new LineTool(),
			new RectangleTool(),
			new EllipseTool(),
			new CircleTool(),
	};

	public CanvasController(CanvasProperties properties) {
		this.properties = properties;

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
			switch (ToolGroup.getToolGroupById(properties.getTool())){
				case 0:
					Shape tmp = tools[properties.getTool()].onPressed(event,properties);
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
			switch (ToolGroup.getToolGroupById(properties.getTool())){
				case 0:
					tools[properties.getTool()].onDragged(event);
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
			switch (ToolGroup.getToolGroupById(properties.getTool())){
				case 0:
					tools[properties.getTool()].onReleased(event);
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



	private void useDecoration(){

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

}
