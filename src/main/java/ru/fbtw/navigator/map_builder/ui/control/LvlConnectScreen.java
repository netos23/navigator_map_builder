package ru.fbtw.navigator.map_builder.ui.control;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.fbtw.navigator.map_builder.core.Project;

public class LvlConnectScreen implements Screen {

	private BorderPane mainLayout;
	private Scene scene;

	private Project project;

	public LvlConnectScreen(Project project) {
		this.project = project;

		mainLayout = new BorderPane();

		Button button = new Button("back");
		button.setOnAction(event -> Navigator.pop());
		mainLayout.setCenter(button);
	}

	@Override
	public void start(Stage primaryStage) {

		scene = new Scene(mainLayout);
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
