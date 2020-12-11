package ru.fbtw.navigator.map_builder;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.io.ProjectLoader;
import ru.fbtw.navigator.map_builder.io.Serializer;
import ru.fbtw.navigator.map_builder.ui.screens.LoginPage;
import ru.fbtw.navigator.map_builder.ui.screens.LvlEditScreen;
import ru.fbtw.navigator.map_builder.ui.control.Navigator;
import ru.fbtw.navigator.map_builder.ui.control.Screen;

import java.io.File;


public class App extends Application {

	@Override
	public void init() throws Exception {
		super.init();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ProjectLoader loader = new ProjectLoader(new File("saves/471355bfa0a04f7f.json"));
		Screen firstScreen = new LvlEditScreen(loader.load());
		//Screen firstScreen = new LvlEditScreen(new Project());
		//Screen firstScreen = new LoginPage();
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
