package ru.fbtw.navigator.map_builder.ui.screens;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.fbtw.navigator.map_builder.connection_editor.ConnectionEditorController;
import ru.fbtw.navigator.map_builder.connection_editor.ConnectionEditorProperties;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.ui.LayoutBuilder;
import ru.fbtw.navigator.map_builder.ui.ToggleButtonGridBuilder;
import ru.fbtw.navigator.map_builder.ui.control.Screen;

import java.util.ArrayList;

public class LvlConnectScreen implements Screen {

	private BorderPane mainLayout;
	private StackPane rootCanvas;
	private Scene scene;

	private ArrayList<ToggleButton> tools;

	private Button clear, undo, redo;
	private Button save, load, push;

	private Project project;
	private ConnectionEditorProperties properties;
	private ConnectionEditorController controller;

	public LvlConnectScreen(Project project) {
		this.project = project;
		project.update();

		properties = new ConnectionEditorProperties(0);
		controller = new ConnectionEditorController(properties, project);


		mainLayout = new BorderPane();
		rootCanvas = new StackPane();

		tools = new ToggleButtonGridBuilder()
				.setSource(ConnectionEditorController.TOOLS_NAME)
				.setUseName(true)
				.setOnClick(this::onToolChanged)
				.build();


		clear = new Button("Clear");
		undo = new Button("<-");
		redo = new Button("->");

		save = new Button("Save");
		load = new Button("Load");
		push = new Button("Push");

		initScene();
		setController(controller);
	}

	private void onToolChanged(ActionEvent event) {
		ToggleButton target = (ToggleButton) event.getSource();
		if (target.isSelected()) {
			properties.setTool(tools.indexOf(target));
		} else {
			tools.get(0).fire();
		}
	}


	private void setController(ConnectionEditorController controller){
		rootCanvas.getChildren().clear();
		rootCanvas.getChildren().addAll(controller.getLayers());
	}

	@Override
	public void initScene() {
		javafx.scene.Node rightMenu = new LayoutBuilder(10)
				.setTitle("Tools")
				.addButtonsGrid(1, tools, true)
				.addHorizontalButtonsPanel(clear, undo, redo)
				.addContent(save)
				.addContent(load)
				.addContent(push)
				.wrapWithScrolView()
				.build();


		rootCanvas.setStyle("-fx-background-color: #666666;");
		ScrollPane scrollPane = new ScrollPane(rootCanvas);
		scrollPane.setMinSize(800.0, 600);
		scrollPane.setPadding(new Insets(5));

		mainLayout.setCenter(scrollPane);
		mainLayout.setRight(rightMenu);

		scene = new Scene(mainLayout);
	}


	@Override
	public void start(Stage primaryStage) {
		setOnClicks(primaryStage);
	}

	private void setOnClicks(Stage primaryStage) {

	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public String getTitle() {
		return "Mapper";
	}

	@Override
	public void dispose() {

	}
}
