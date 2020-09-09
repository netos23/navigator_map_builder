package ru.fbtw.navigator.map_builder.ui;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FontStyler {
	public static void setFontSize(Label label, int fontSize){
		label.setFont(Font.font("Arial",fontSize));
	}

	public static void setHeaderStyle(Label header, int fontSize){
		header.setFont(Font.font("Arial", FontWeight.BOLD,fontSize));
	}
}
