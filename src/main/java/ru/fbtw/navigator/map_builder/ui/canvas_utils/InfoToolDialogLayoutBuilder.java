package ru.fbtw.navigator.map_builder.ui.canvas_utils;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ru.fbtw.navigator.map_builder.core.navigation.NodeType;
import ru.fbtw.navigator.map_builder.utils.KeyManager;

import java.lang.reflect.InvocationTargetException;


public class InfoToolDialogLayoutBuilder {
	private GridPane mainLayout;
	private int rowCount;


	public InfoToolDialogLayoutBuilder() {
		mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.getColumnConstraints().add(0, new ColumnConstraints(100));
		mainLayout.getColumnConstraints().add(1, new ColumnConstraints(100));
		mainLayout.setVgap(5);


	}

	public InfoToolDialogLayoutBuilder addDoubleProperty(String name, double value, DoublePropertyEventHandler onclick) {

		Label label = new Label(name);
		TextField input = new TextField(Double.toString(value));
		input.setOnAction(getBaseDoubleOnClick(onclick));

		addRow(label, input);
		return this;
	}


	public InfoToolDialogLayoutBuilder addColorProperty(String name, Paint value, ChangeListener<Color> onclick) {

		Label label = new Label(name);
		ColorPicker input = new ColorPicker();


		input.valueProperty().setValue((Color) value);
		input.valueProperty().addListener(onclick);

		addRow(label, input);
		return this;
	}

	public InfoToolDialogLayoutBuilder addMultiLineProperty(
			String description,
			String initVal,
			StringPropertyEventHandler setDescription
	) {
		Label label = new Label(description);

		TextArea input = new TextArea(initVal);
		input.setOnKeyTyped(getBaseMultiStringOnClick(setDescription));

		addRow(label, input);
		return this;
	}

	public <T extends Enum> InfoToolDialogLayoutBuilder addEnumProperty(
			String name,
			T initVal,
			EnumPropertyEventHandler<T> onClick
	) {
		try {
			Label label = new Label(name);

			T[] values = (T[]) NodeType.DESTINATION
					.getClass()
					.getMethod("values")
					.invoke(null);
			ObservableList<T> valuesList = FXCollections.observableArrayList(values);
			ChoiceBox<T> input = new ChoiceBox<>(valuesList);
			input.setValue(initVal);
			input.setOnAction(getBaseEnumOnClick(onClick));

			addRow(label, input);
		} catch (IllegalAccessException
				| InvocationTargetException
				| NoSuchMethodException
				| ClassCastException e
		) {
			e.printStackTrace();
		}

		return this;
	}

	public InfoToolDialogLayoutBuilder addStringProperty(
			String name,
			String initVal,
			StringPropertyEventHandler onClick
	) {
		Label label = new Label(name);
		TextField input = new TextField(initVal);

		input.setOnAction(getBaseStringOnClick(onClick));

		addRow(label, input);
		return this;
	}

	private <T extends Enum> EventHandler<ActionEvent> getBaseEnumOnClick(EnumPropertyEventHandler<T> handler)
			throws ClassCastException {
		return event -> {
			ChoiceBox<T> choiceBox = (ChoiceBox<T>) event.getSource();
			handler.onValueChanged(choiceBox.getValue());
		};
	}

	/**
	 * This method wraps property {@param handler} to the onClick
	 *
	 * @return - returns the collected method for onAction
	 */
	private EventHandler<ActionEvent> getBaseStringOnClick(StringPropertyEventHandler handler) {
		return event -> {
			TextField textField = (TextField) event.getSource();
			String val = handler.onValueChanged(textField.getText());
			textField.setText(val);
		};
	}

	/**
	 * This method checks the form submission condition for target TextArea
	 *
	 * @param handler - on submit action
	 * @return - returns the collected method for onAction
	 */
	private EventHandler<KeyEvent> getBaseMultiStringOnClick(StringPropertyEventHandler handler) {
		return event -> {
			if (KeyManager.isKeyPressed(KeyCode.ENTER)
					&& KeyManager.isKeyPressed(KeyCode.SHIFT)) {
				TextArea textArea = (TextArea) event.getSource();

				String text = textArea.getText();
				text = text.substring(0, text.length() - 1);

				String val = handler.onValueChanged(text);
				textArea.setText(val);
			}
		};
	}


	/**
	 * This method attach  double validator {@param handler} to the property onClick
	 *
	 * @return - returns the collected method for onAction
	 */
	private EventHandler<ActionEvent> getBaseDoubleOnClick(DoublePropertyEventHandler handler) {
		return event -> {
			TextField textField = (TextField) event.getSource();
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

	private void addRow(Label label, Node input) {
		mainLayout.add(label, 0, rowCount);
		mainLayout.add(input, 1, rowCount);
		rowCount++;
	}

	public ScrollPane build() {
		return new ScrollPane(mainLayout);
	}


}
