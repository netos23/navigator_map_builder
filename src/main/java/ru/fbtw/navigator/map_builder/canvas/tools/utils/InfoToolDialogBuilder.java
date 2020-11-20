package ru.fbtw.navigator.map_builder.canvas.tools.utils;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class InfoToolDialogBuilder {
	private GridPane mainLayout;
	private int rowCount;


	public InfoToolDialogBuilder() {
		mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.getColumnConstraints().add(0, new ColumnConstraints(100));
		mainLayout.getColumnConstraints().add(1, new ColumnConstraints(100));
		mainLayout.setVgap(5);


	}

	public InfoToolDialogBuilder addDoubleProperty(String name, double value, DoublePropertyEventHandler onclick) {

		Label label = new Label(name);
		TextField input = new TextField(Double.toString(value));
		input.setOnAction(getBaseDoubleOnClick(onclick));

		addRow(label, input);
		return this;
	}

	private void addRow(Label label, Node input) {
		mainLayout.add(label,0,rowCount);
		mainLayout.add(input,1,rowCount);
		rowCount++;
	}

	public InfoToolDialogBuilder addColorProperty(String name, Paint value, ChangeListener<Color> onclick) {

		Label label = new Label(name);
		ColorPicker input = new ColorPicker();


		input.valueProperty().setValue((Color) value);
		input.valueProperty()
				.addListener(onclick);

		addRow(label,input);
		return this;
	}




	/**
	 * That method attach double validator to the property onClick
	 * */
	private EventHandler<ActionEvent> getBaseDoubleOnClick(DoublePropertyEventHandler handler){
		return event -> {
			TextField textField =  (TextField) event.getSource();
			double newValue;
			try {
				double value = Double.parseDouble(textField.getText());
				newValue = handler.onValueChanged(value);

			} catch (NumberFormatException ex) {
				newValue = handler.onValueChanged(null);
			}

			textField.setText(Double.toString(newValue));
		};
	}

	public GridPane build() {
		return mainLayout;
	}
}
