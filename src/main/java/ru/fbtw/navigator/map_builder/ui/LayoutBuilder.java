package ru.fbtw.navigator.map_builder.ui;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public  class LayoutBuilder {
	private VBox layout;
	private StyleBuilder styleBuilder;

	public LayoutBuilder(double spacing) {
		layout = new VBox(spacing);
		layout.setPadding(new Insets(5));
		styleBuilder = new StyleBuilder();

		styleBuilder.setMenuBgStyle().apply(layout);
	}

	public  LayoutBuilder setTitle(String title){
		Label titleWidget = new Label(title);

		FontStyler.setHeaderStyle(titleWidget,25);

		layout.getChildren().add(titleWidget);
		return this;
	}
	public  LayoutBuilder addContent(Node content){
		layout.getChildren().add(content);
		return this;
	}

	public LayoutBuilder addButtonsGrid(int colCount, ArrayList<ToggleButton> buttons) {
		GridPane localLayout = new GridPane();
		localLayout.setHgap(5);
		localLayout.setVgap(10);
		ToggleGroup tools = new ToggleGroup();


		for (int i = 0, row = 0, col =0; i < buttons.size(); i++, row= i/colCount, col=i%colCount) {
			ToggleButton button = buttons.get(i);
			button.setMinWidth(90);
			button.setToggleGroup(tools);
			button.setCursor(Cursor.HAND);
			//styleBuilder.setAccentBgStyle().apply(button);
			localLayout.add(button,col,row);
		}

		buttons.get(0).fire();

		layout.getChildren().add(localLayout);
		return this;
	}

	public LayoutBuilder addHorizontalButtonsPanel(Button ... buttons){
		for(Button button : buttons){
			button.setMinWidth(90);
			button.setCursor(Cursor.HAND);
			button.setTextFill(Color.WHITE);
			styleBuilder.setAccentBgStyle().apply(button);
		}
		layout.getChildren().add(new HBox(5,buttons));
		return this;
	}

	@Deprecated
	public LayoutBuilder addList(ListView listView){
		layout.getChildren().add(listView);
		return this;
	}

	public Node build(){
		/*ScrollPane safeLayout = new ScrollPane(layout);
		safeLayout.setPannable(true);
		safeLayout.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		return safeLayout;*/
		return layout;
	}
}
