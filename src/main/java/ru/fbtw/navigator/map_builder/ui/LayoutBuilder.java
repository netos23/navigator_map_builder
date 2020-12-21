package ru.fbtw.navigator.map_builder.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public  class LayoutBuilder {
	private VBox layout;
	private StyleBuilder styleBuilder;
	private ToggleGroup tools;
	private boolean isScroll;

	public LayoutBuilder(double spacing) {
		layout = new VBox(spacing);
		layout.getStyleClass().add("hbox-group");
		layout.setPadding(new Insets(5));
		styleBuilder = new StyleBuilder();
		tools = new ToggleGroup();

	}

	public  LayoutBuilder setTitle(String title){
		Label titleWidget = buildTitle(title);

		layout.getChildren().add(titleWidget);
		return this;
	}

	private Label buildTitle(String title) {
		Label titleWidget = new Label(title);
		titleWidget.getStyleClass().add("label-head");
		return titleWidget;
	}

	public LayoutBuilder setOptionalTitle(String title, CheckBox isUse){
		Label optionalTitle = buildTitle(title);
		HBox localLayout = new HBox(10,optionalTitle,isUse);
		layout.getChildren().add(localLayout);
		return this;
	}

	public  LayoutBuilder addContent(Node content){
		VBox.setVgrow(content, Priority.ALWAYS);
		layout.getChildren().add(content);
		return this;
	}

	public LayoutBuilder addButtonsGrid(int colCount, ArrayList<ToggleButton> buttons, boolean isDefault) {
		GridPane localLayout = new GridPane();
		localLayout.setAlignment(Pos.CENTER);
		localLayout.setHgap(5);
		localLayout.setVgap(10);



		for (int i = 0, row = 0, col =0; i < buttons.size(); i++, row= i/colCount, col=i%colCount) {
			ToggleButton button = buttons.get(i);
			//button.setMinWidth(90);
			button.setToggleGroup(tools);
			button.setCursor(Cursor.HAND);
			//styleBuilder.setAccentBgStyle().apply(button);
			localLayout.add(button,col,row);
		}

		if(isDefault) {
			buttons.get(0).fire();
		}

		layout.getChildren().add(localLayout);
		return this;
	}


	public LayoutBuilder addHorizontalButtonsPanel(boolean fitWidth, double spacing, Button ... buttons){
		for(Button button : buttons){
			if(fitWidth) {
				button.setMinWidth(90);
			}
			button.setCursor(Cursor.HAND);
			//button.setTextFill(Color.WHITE);
			//styleBuilder.setAccentBgStyle().apply(button);
		}
		layout.getChildren().add(new HBox(spacing,buttons));
		return this;
	}


	@Deprecated
	public LayoutBuilder addList(ListView listView){
		layout.getChildren().add(listView);
		return this;
	}

	public LayoutBuilder wrapWithScrolView(){
		isScroll = true;
		return this;
	}

	public Node build(){
		if(isScroll) {
			ScrollPane safeLayout = new ScrollPane(layout);
			safeLayout.setPannable(true);
			safeLayout.setFitToHeight(true);
			safeLayout.setFitToWidth(true);
			safeLayout.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			return safeLayout;
		}else {
			return layout;
		}
	}


}
