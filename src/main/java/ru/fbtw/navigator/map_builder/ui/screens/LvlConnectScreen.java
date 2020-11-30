package ru.fbtw.navigator.map_builder.ui.screens;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.ui.LayoutBuilder;
import ru.fbtw.navigator.map_builder.ui.control.Screen;

public class LvlConnectScreen implements Screen {

	private BorderPane mainLayout;
	private StackPane rootCanvas;
	private Scene scene;

	private Button clear, undo, redo;
	private Button save, load, push;

	private Project project;

	public LvlConnectScreen(Project project) {
		this.project = project;

		mainLayout = new BorderPane();
		rootCanvas = new StackPane();

		clear = new Button("Clear");
		undo = new Button("<-");
		redo = new Button("->");

		save = new Button("Save");
		load = new Button("Load");
		push = new Button("Push");

		initScene();
	}

	@Override
	public void initScene() {
		javafx.scene.Node  rightMenu = new LayoutBuilder(10)
				.setTitle("Tools")
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
