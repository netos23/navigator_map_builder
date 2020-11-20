package ru.fbtw.navigator.map_builder.canvas;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.canvas.tools.*;
import ru.fbtw.navigator.map_builder.utils.StringUtils;
import ru.fbtw.navigator.map_builder.utils.Vector2;


public class CanvasController {
	public static DrawingTool[] drawingTools = new DrawingTool[]{
			new LineTool(),
			new RectangleTool(),
			new EllipseTool(),
			new CircleTool()
	};
	public static String[] settingsToolNames = new String[]{
			StringUtils.toolToString(RemoveTool.class),
			StringUtils.toolToString(ResizeTool.class),
			StringUtils.toolToString(ReplaceTool.class),
			StringUtils.toolToString(StrokeTool.class),
			StringUtils.toolToString(WidthTool.class),
			StringUtils.toolToString(FillTool.class),
			StringUtils.toolToString(InfoTool.class)
	};
	public SettingsTool[] settingsTools;
	private Pane[] layers;
	//private int currentTool, toolGroup;
	private Pane inputLayer;
	private ProbeManager probeManager;
	private HolderManager holderManager;
	private CanvasProperties properties;


	public CanvasController(CanvasProperties properties) {
		this.properties = properties;
		this.properties.setSource(this);

		layers = new Pane[5];
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new Pane();
		}
		inputLayer = layers[4];

		probeManager = new ProbeManager(this);
		holderManager = new HolderManager(probeManager, properties);

		settingsTools = new SettingsTool[]{
				new RemoveTool(holderManager),
				new ResizeTool(holderManager),
				new ReplaceTool(holderManager),
				new StrokeTool(holderManager),
				new WidthTool(holderManager),
				new FillTool(holderManager),
				new InfoTool(holderManager)
		};

		setOnClicks();
	}

	private void setOnClicks() {

		inputLayer.setOnMousePressed(event -> {


			switch (properties.getToolGroup()) {
				case 0:
					Probe curPos = probeManager.getPosOfExistingPoint(event);
					Shape tmp = drawingTools[properties.getTool()].onPressed(curPos, properties);
					layers[1].getChildren().add(tmp);
					break;
				case 1:
					settingsTools[properties.getTool()].onPressed(event.getX(), event.getY());
					break;
				case 2:

					break;
				case 3:

					break;
			}
		});


		inputLayer.setOnMouseDragged(event -> {
			Vector2 curPos = probeManager.getPosOfExistingTempPoint(event);
			switch (properties.getToolGroup()) {
				case 0:
					drawingTools[properties.getTool()].onDragged(curPos.getX(), curPos.getY());
					break;
				case 1:
					settingsTools[properties.getTool()].onDragged(curPos.getX(), curPos.getY());
					break;
				case 2:

					break;
				case 3:

					break;
			}
		});

		inputLayer.setOnMouseReleased(event -> {

			switch (properties.getToolGroup()) {
				case 0:
					Probe curPos = probeManager.getPosOfExistingPoint(event);
					Holder tmp = drawingTools[properties.getTool()].onReleased(curPos);
					holderManager.push(tmp);
					break;
				case 1:
					settingsTools[properties.getTool()].onReleased(event.getX(), event.getY());
					break;
				case 2:

					break;
				case 3:

					break;
			}
		});


	}


	public void setBackground(Image bg) {
		enableBackground(true);

		ImageView imageView = new ImageView(bg);
		layers[0].getChildren().add(imageView);
		for (Pane layer : layers) {
			layer.setMinSize(imageView.getX(), imageView.getY());
		}
	}

	public void enableBackground(boolean value) {
		layers[0].setVisible(value);
	}

	public Pane[] getLayers() {
		return layers;
	}

	public DrawingTool[] getTools() {
		return drawingTools;
	}

	public ProbeManager getProbeManager() {
		return probeManager;
	}
}
