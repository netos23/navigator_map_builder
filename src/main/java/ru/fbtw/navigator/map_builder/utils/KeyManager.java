package ru.fbtw.navigator.map_builder.utils;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;

public class KeyManager {
	private static HashSet<KeyCode> keyStore;

	static  {
		keyStore = new HashSet<>();
	}

	public static void push(KeyCode keyCode){

		keyStore.add(keyCode);
	}

	public static void remove(KeyCode keyCode){
		keyStore.remove(keyCode);
	}


	public static boolean isKeyPressed(KeyCode keyCode){
		return keyStore.contains(keyCode);
	}

	public static void attachKeyManagerToScene(Scene target){
		target.setOnKeyPressed(event -> KeyManager.push(event.getCode()));
		target.setOnKeyReleased(event -> KeyManager.remove(event.getCode()));
	}
}
