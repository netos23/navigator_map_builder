package ru.fbtw.navigator.map_builder.ui.control;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface Screen {
	void start(Stage primaryStage);

	Scene getScene();

	String getTitle();

	void dispose();


}
