package ru.fbtw.navigator.map_builder.canvas;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;
import ru.fbtw.navigator.map_builder.canvas.tools.*;
import ru.fbtw.navigator.map_builder.navigation.Node;
import ru.fbtw.navigator.map_builder.utils.StringUtils;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;


public class CanvasController {
	public static String[] drawingToolsNames = new String[]{
			StringUtils.toolToString(LineTool.class),
			StringUtils.toolToString(ReplaceTool.class),
			StringUtils.toolToString(Ellipse.class),
			StringUtils.toolToString(Circle.class)
	};
	public static String[] settingsToolNames = new String[]{
			StringUtils.toolToString(RemoveTool.class),
			StringUtils.toolToString(ResizeTool.class),
			StringUtils.toolToString(ReplaceTool.class),
			StringUtils.toolToString(StrokeTool.class),
			StringUtils.toolToString(WidthTool.class),
			StringUtils.toolToString(FillTool.class),
			StringUtils.toolToString(InfoTool.class),
	};

	public Tool[] tools;
	ArrayList<Node> nodeSystem;
	private Pane[] layers;
	private Pane inputLayer;
	private ProbeManager probeManager;
	private HolderManager holderManager;
	private CanvasProperties properties;

	public CanvasController(CanvasProperties properties, ArrayList<Node> nodeSystem) {
		this.properties = properties;
		this.nodeSystem = nodeSystem;
		this.properties.setSource(this);


		layers = new Pane[5];
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new Pane();
		}
		inputLayer = layers[4];

		probeManager = new ProbeManager(this);
		holderManager = new HolderManager(probeManager, properties);

		tools = new Tool[]{
				new LineTool(holderManager),
				new RectangleTool(holderManager),
				new EllipseTool(holderManager),
				new CircleTool(holderManager),
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

		inputLayer.setOnMousePressed(event ->
				tools[properties.getTool()].onPressed(event.getX(), event.getY())
		);

		inputLayer.setOnMouseDragged(event -> {
			Vector2 curPos = probeManager.getPosOfExistingTempPoint(event);
			tools[properties.getTool()].onDragged(curPos.getX(), curPos.getY());

		});

		inputLayer.setOnMouseReleased(event ->
				tools[properties.getTool()].onReleased(event.getX(), event.getY())
		);


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


	public ProbeManager getProbeManager() {
		return probeManager;
	}
}
