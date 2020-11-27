package ru.fbtw.navigator.map_builder;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.ui.control.LvlEditScreen;
import ru.fbtw.navigator.map_builder.ui.control.Navigator;
import ru.fbtw.navigator.map_builder.ui.control.Screen;


public class App extends Application {

	@Override
	public void init() throws Exception {
		super.init();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Screen firstScreen = new LvlEditScreen(new Project());
		Navigator.init(primaryStage,firstScreen);
	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
