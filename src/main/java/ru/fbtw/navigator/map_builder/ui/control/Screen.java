package ru.fbtw.navigator.map_builder.ui.control;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface Screen {
	void initScene();

	void start(Stage primaryStage);

	void dispose();

	Scene getScene();

	String getTitle();
}
