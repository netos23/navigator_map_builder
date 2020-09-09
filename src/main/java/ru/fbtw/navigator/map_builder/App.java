package ru.fbtw.navigator.map_builder;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.*;

import ru.fbtw.navigator.map_builder.canvas.CanvasController;
import ru.fbtw.navigator.map_builder.core.Level;
import ru.fbtw.navigator.map_builder.ui.FontStyler;
import ru.fbtw.navigator.map_builder.ui.LayoutBuilder;
import ru.fbtw.navigator.map_builder.utils.EmptyImageMaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class App extends Application {


	private Button imageChooserBtn,emptyBtn;
	private ListView<Level> levelListWidget;
	private CheckBox isBg, isTmpNodes;
	private TextField levelName;
	private Button nameUpdater;
	private FileChooser imageChooser;

	private ArrayList<ToggleButton> btns;
	private ColorPicker mainColor, fillColor;
	private Slider widthPicker;
	private Button clear,undo,redo;
	private Button save,load,push;

	private ObservableList<Level> levels;
	private int selectedLevel;
	private StackPane rootCanvas;


	@Override
	public void init() throws Exception {
		super.init();

		imageChooserBtn = new Button("Add image");
		emptyBtn = new Button("Add empty");

		levels = FXCollections.observableArrayList(Level.getCallback());
		imageChooser = getFileChooser();
		selectedLevel = -1;

		isBg = new CheckBox("Use background");
		isTmpNodes = new CheckBox("Use temp nodes");

		levelName = new TextField();
		nameUpdater = new Button("Update name");

		levelListWidget = new ListView<Level>(levels);
		levelListWidget.setMaxHeight(300.0);


		btns = new ArrayList<ToggleButton>();
		for(int i =0;i<16;i++){
			ToggleButton button = new ToggleButton("Button "+i);
			btns.add(button);
		}

		mainColor = new ColorPicker(Color.BLACK);
		fillColor = new ColorPicker(Color.WHITE);

		widthPicker = new Slider(5,50,10);
		widthPicker.setShowTickLabels(true);
		widthPicker.setShowTickMarks(true);

		clear = new Button("Clear");
		undo = new Button("<-");
		redo = new Button("->");

		save = new Button("Save");
		load = new Button("Load");
		push = new Button("Push");


	}

	public void start(Stage primaryStage) throws Exception {
		BorderPane mainLayout = new BorderPane();
		setOnClicks(primaryStage);

		Node leftMenu = new LayoutBuilder(10)
				.setTitle("Rooms")
				.addHorizontalButtonsPanel(imageChooserBtn, emptyBtn)
				//.addList(levelListWidget)
				.addContent(levelListWidget)
				.setTitle("Level settings")
				.addContent(isBg)
				.addContent(isTmpNodes)
				.setTitle("Level name")
				.addContent(levelName)
				.addHorizontalButtonsPanel(nameUpdater)
				.build();

		Node rightMenu = new LayoutBuilder(10)
				.setTitle("Tools")
				.addButtonsGrid(3,btns)
				.setTitle("Line color")
				.addContent(mainColor)
				.setTitle("Fill color")
				.addContent(fillColor)
				.setTitle("Line width")
				.addContent(widthPicker)
				.addHorizontalButtonsPanel(clear,undo,redo)
				.addContent(save)
				.addContent(load)
				.addContent(push)
				.build();

		rootCanvas = new StackPane();
		rootCanvas.setStyle("-fx-background-color: #666666;");
		ScrollPane scrollPane = new ScrollPane(rootCanvas);
		scrollPane.setMinSize(800.0, primaryStage.getMinHeight());
		scrollPane.setPadding(new Insets(5));


		mainLayout.setPadding(new Insets(3));
		mainLayout.setStyle("-fx-background-color: #666666;");
		mainLayout.setCenter(scrollPane);
		mainLayout.setLeft(leftMenu);
		mainLayout.setRight(rightMenu);
		Scene scene = new Scene(mainLayout);


		//primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Map builder");
		primaryStage.show();
	}

	private FileChooser getFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
		return fileChooser;
	}

	private void setOnClicks(final Stage stage) {

		imageChooserBtn.setOnAction(event -> {
			List<File> selectedImg = imageChooser.showOpenMultipleDialog(stage);
			if (selectedImg != null && !selectedImg.isEmpty()) {
				for (File img : selectedImg) {
					createLevel(img);
				}
			}
		});

		emptyBtn.setOnAction(event -> getInfoFromDialog());

		nameUpdater.setOnAction(event -> {
			if (selectedLevel >= 0) {
				levels
						.get(selectedLevel)
						.setName(levelName.getText());
			} else {
				levelName.setText("");
			}
		});

		levelListWidget.setOnMouseClicked(event ->
				selectLevel(levelListWidget
						.getSelectionModel()
						.getSelectedIndex()));

		isBg.setOnAction(event -> {
			if (selectedLevel >= 0) {
				levels.get(selectedLevel)
						.setUseBackground(isBg.isSelected());
			} else {
				isBg.setSelected(false);
			}
		});

		isTmpNodes.setOnAction(event -> {
			if (selectedLevel >= 0) {
				levels.get(selectedLevel)
						.setUseBackground(isTmpNodes.isSelected());
			} else {
				isTmpNodes.setSelected(false);
			}
		});

	}

	private void getInfoFromDialog(){
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(10));
		layout.setHgap(5);
		layout.setVgap(10);

		Label info = new Label("Enter the length and width (integers)");
		layout.add(info,0,0,3,1);
		Label widthTxt = new Label("Width:");
		FontStyler.setHeaderStyle(widthTxt,15);
		layout.add(widthTxt,0,1);
		TextField inputWidth = new TextField();
		layout.add(inputWidth,1,1);

		Label heightTxt = new Label("Height:");
		FontStyler.setHeaderStyle(heightTxt,15);
		layout.add(heightTxt,0,2);
		TextField inputHeight = new TextField();
		layout.add(inputHeight,1,2);

		Label colorText = new Label("Color:");
		FontStyler.setHeaderStyle(heightTxt,15);
		layout.add(colorText,0,3);
		ColorPicker colorPicker = new ColorPicker(Color.WHITE);
		layout.add(colorPicker,1,3);



		Button apply = new Button("OK");
		layout.add(apply,0,4);
		Button cancel = new Button("CANCEL");
		layout.add(cancel,1,4);

		Scene scene = new Scene(layout,290,220);
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.setTitle("Dialog");
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UTILITY);


		apply.setOnAction(event -> {
			try {
				int width = Integer.parseInt(inputWidth.getText());
				int height = Integer.parseInt(inputHeight.getText());
				Color color = colorPicker.getValue();

				float blue = (float) color.getBlue();
				float red = (float) color.getRed();
				float green = (float) color.getGreen();
				int colRes = new  java.awt.Color(red,green,blue).getRGB();

				File tmp = EmptyImageMaker.getEmptyImage(width, height,colRes);
				if(tmp != null){
					createLevel(tmp);
				}
				stage.close();
			}catch (Exception e){
				//todo: alert dialog
			}
		});

		cancel.setOnAction(event -> stage.close());
		stage.show();


	}

	private void selectLevel(int id){
		this.selectedLevel = id;
		Level level = levels.get(id);

		isBg.setSelected(level.isBackground());
		isTmpNodes.setSelected(level.isTmpNodes());
		levelName.setText(level.getName());

		CanvasController controller = level.getController();
		rootCanvas.getChildren().clear();
		rootCanvas.getChildren().addAll(controller.getLayers());
		// todo : setup tools

	}

	private void createLevel(File img) {
		try {
			Image tmp = new Image(new FileInputStream(img));

			Level level = new Level(levels.size());
			level.setUseBackground(true);
			level.setBackground(tmp);

			levels.add(level);

			if(selectedLevel < 0){
				selectLevel(0);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
