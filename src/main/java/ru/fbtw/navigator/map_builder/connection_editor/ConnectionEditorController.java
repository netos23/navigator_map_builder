package ru.fbtw.navigator.map_builder.connection_editor;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ru.fbtw.navigator.map_builder.connection_editor.node.LevelEntityManager;
import ru.fbtw.navigator.map_builder.connection_editor.tools.ConnectionTool;
import ru.fbtw.navigator.map_builder.connection_editor.tools.RemoveTool;
import ru.fbtw.navigator.map_builder.connection_editor.tools.ReplaceTool;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.utils.StringUtils;
import ru.fbtw.navigator.map_builder.utils.common.ScreenTool;

public class ConnectionEditorController {
	private ConnectionEditorProperties properties;

	public static final String[] TOOLS_NAME = {
			StringUtils.toolToString(ConnectionTool.class),
			StringUtils.toolToString(RemoveTool.class),
			StringUtils.toolToString(ReplaceTool.class),
	};

	private ScreenTool[] tools;

	private Pane[] layers;
	private Pane inputLayer;

	public ConnectionEditorController(ConnectionEditorProperties properties, Project project) {
		this.properties = properties;
		this.properties.setSource(this);

		layers = new Pane[LayersName.LAYERS_COUNT];
		for(int i =0; i < layers.length; i++){
			layers[i] = new Pane();
			layers[i].setMinWidth(1000);
			layers[i].setMinHeight(1000);

		}
		layers[LayersName.BACKGROUND]
				.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));

		inputLayer = layers[LayersName.INPUT];

		LevelEntityManager manager = new LevelEntityManager(project,properties);

		tools = new ScreenTool[]{
				new ConnectionTool(manager),
				new RemoveTool(manager),
				new ReplaceTool(manager)
		};

		setOnClicks();
	}
	private void setOnClicks() {
		inputLayer.setOnMousePressed(event ->
				tools[properties.getTool()].onPressed(event.getX(),event.getY()));

		inputLayer.setOnMouseDragged(event ->
				tools[properties.getTool()].onDragged(event.getX(),event.getY()));

		inputLayer.setOnMouseReleased(event ->
				tools[properties.getTool()].onPressed(event.getX(),event.getY()));
	}
	public Pane[] getLayers() {
		return layers;
	}


}
