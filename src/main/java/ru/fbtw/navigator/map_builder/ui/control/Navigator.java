package ru.fbtw.navigator.map_builder.ui.control;

import javafx.stage.Stage;

import java.util.Stack;

public class Navigator {
	private static Stack<Screen> screens;
	private static Stage primaryStage;


	public static void init(Stage primaryStage, Screen startScreen){
		Navigator.primaryStage = primaryStage;
		screens = new Stack<>();

		push(startScreen );
	}

	public static void push(Screen screen) {
		screens.push(screen);
		showFirstTime(screen);
	}

	private static void showFirstTime(Screen screen) {
		screen.start(primaryStage);
		reShow(screen);
	}

	private static void reShow(Screen screen) {
		primaryStage.setTitle(screen.getTitle());
		primaryStage.setScene(screen.getScene());
		primaryStage.show();
	}

	public static void pop(){
		screens.pop();
		reShow(screens.peek());
	}

	public static void replace(Screen screen){
		screens.pop();
		push(screen);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}
}
