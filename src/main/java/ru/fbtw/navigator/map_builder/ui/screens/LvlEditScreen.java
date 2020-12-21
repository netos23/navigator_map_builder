package ru.fbtw.navigator.map_builder.ui.screens;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.fbtw.navigator.map_builder.canvas.CanvasController;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.core.Level;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.ui.LayoutUtils;
import ru.fbtw.navigator.map_builder.ui.dialogs.DialogViewer;
import ru.fbtw.navigator.map_builder.ui.FontStyler;
import ru.fbtw.navigator.map_builder.ui.LayoutBuilder;
import ru.fbtw.navigator.map_builder.ui.ToggleButtonGridBuilder;
import ru.fbtw.navigator.map_builder.ui.control.Navigator;
import ru.fbtw.navigator.map_builder.ui.control.Screen;
import ru.fbtw.navigator.map_builder.ui.dialogs.PublishAction;
import ru.fbtw.navigator.map_builder.ui.dialogs.SaveAction;
import ru.fbtw.navigator.map_builder.ui.dialogs.SimpleExecutableDialog;
import ru.fbtw.navigator.map_builder.ui.widget.ExecutableDialog;
import ru.fbtw.navigator.map_builder.utils.ImageUtils;
import ru.fbtw.navigator.map_builder.utils.KeyManager;
import ru.fbtw.navigator.map_builder.utils.common.Action;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LvlEditScreen implements Screen {
	private Button imageChooserBtn, emptyBtn, remove;
	private ListView<Level> levelListWidget;
	private CheckBox isBg, isTmpNodes;
	private TextField levelName;
	private Button nameUpdater;
	private FileChooser imageChooser;

	private ArrayList<ToggleButton> drawingToolButtons;
	private ArrayList<ToggleButton> settingsToolButtons;
	private ColorPicker mainColor, fillColor;
	private CheckBox isUseFill;
	private Slider widthPicker;
	private Button back, clear, undo, redo, save, publish, push;

	private ObservableList<Level> levels;
	private int selectedLevel;
	private StackPane rootCanvas;

	private CanvasProperties properties;
	private ArrayList<ToggleButton> nodeToolButtons;
	private int minHeight = 30;
	private Scene scene;

	private Project project;

	public LvlEditScreen(Project project) {
		this.project = project;
		this.levels = project.getLevels();

		imageChooserBtn = LayoutUtils.getImageButton("add image", 50, 50);
		emptyBtn = LayoutUtils.getImageButton("add", 50, 50);
		remove = LayoutUtils.getImageButton("delete", 50, 50);

		imageChooser = getFileChooser();
		selectedLevel = -1;

		isBg = new CheckBox("Use background");
		isTmpNodes = new CheckBox("Use temp nodes");
		isTmpNodes.setDisable(true);

		levelName = new TextField();
		nameUpdater = new Button("Update name");

		levelListWidget = new ListView<>(levels);
		levelListWidget.setMaxHeight(300.0);

		properties = CanvasProperties.DEFAULT_CANVAS_PROPERTIES;


		settingsToolButtons = new ToggleButtonGridBuilder()
				.setOnClick(this::selectTool)
				.setUseName(false)
				.setUseImage(true)
				.setSource(CanvasController.settingsToolNames)
				.build();
		drawingToolButtons = new ToggleButtonGridBuilder()
				.setOnClick(this::selectTool)
				.setUseName(false)
				.setUseImage(true)
				.setSource(CanvasController.drawingToolsNames)
				.build();
		nodeToolButtons = new ToggleButtonGridBuilder()
				.setOnClick(this::selectTool)
				.setUseName(false)
				.setUseImage(true)
				.setSource(CanvasController.nodeToolNames)
				.build();

		mainColor = new ColorPicker(Color.BLACK);
		fillColor = new ColorPicker(Color.WHITE);
		isUseFill = new CheckBox("Use fill?");


		widthPicker = new Slider(5, 50, 10);
		widthPicker.setValue(5);
		widthPicker.setShowTickLabels(true);
		widthPicker.setShowTickMarks(true);

		back = LayoutUtils.getImageButton("close",80,80);

		clear = LayoutUtils.getImageButton("clear",80,80);
		clear.setDisable(true);

		undo = LayoutUtils.getImageButton("undo",80,80);
		undo.setDisable(true);

		redo = LayoutUtils.getImageButton("redo",80,80);
		redo.setDisable(true);

		save = LayoutUtils.getImageButton("save",80,80);
		publish = LayoutUtils.getImageButton("publish",80,80);
		push = LayoutUtils.getImageButton("connect levels",80,80);

		initScene();
	}

	public void initScene() {
		BorderPane mainLayout = new BorderPane();

		Node levelsMenu = new LayoutBuilder(10)
				.setTitle("Rooms")
				.addHorizontalButtonsPanel(false, 5, imageChooserBtn, emptyBtn, remove)
				//.addList(levelListWidget)
				.addContent(levelListWidget)
				.setTitle("Level settings")
				.addContent(isBg)
				.addContent(isTmpNodes)
				.setTitle("Level name")
				.addContent(levelName)
				.wrapWithScrolView()
				.addHorizontalButtonsPanel(false, 20, nameUpdater)
				.build();

		Node toolsMenu = new LayoutBuilder(10)
				.setTitle("Primitives")
				.addButtonsGrid(2, drawingToolButtons, true)
				.setTitle("Tools")
				.addButtonsGrid(2, settingsToolButtons, false)
				.setTitle("Nodes")
				.addButtonsGrid(2, nodeToolButtons, false)
				.setTitle("Paint")
				.addContent(isUseFill)
				.addContent(buildLineColor())
				.addContent(buildFillColor())
				.addContent(widthPicker)
				.wrapWithScrolView()
				.build();

		Node topMenu = new LayoutBuilder(10)
				.addHorizontalButtonsPanel(false, 20, back, clear, undo, redo,
						save, publish, push)
				.build();

		rootCanvas = new StackPane();
		rootCanvas.getStyleClass().add("canvas");
		ScrollPane scrollPane = new ScrollPane(rootCanvas);
		scrollPane.setMinSize(800.0, 600);
		scrollPane.setPadding(new Insets(5));


		mainLayout.setPadding(new Insets(3));
		mainLayout.getStyleClass().add("canvas");
		mainLayout.setCenter(scrollPane);
		mainLayout.setLeft(toolsMenu);
		mainLayout.setRight(levelsMenu);
		mainLayout.setTop(topMenu);

		scene = new Scene(mainLayout);
		scene.getStylesheets().add("main-theme.css");
		KeyManager.attachKeyManagerToScene(scene);
	}

	/*private Node buildFillAccept() {
		HBox layout = new HBox(10);
		Text text = new Text("Use fill?");
		layout.getChildren().addAll(isUseFill,text);
		return layout;
	}*/

	private Node buildLineColor(){
		HBox layout = new HBox(10);
		LayoutUtils.setSize(mainColor,50,50);
		Label text = new Label("line color");
		text.getStyleClass().add("text");
		layout.getChildren().addAll(mainColor,text);
		return layout;
	}
	private Node buildFillColor(){
		HBox layout = new HBox(10);
		LayoutUtils.setSize(fillColor,50,50);
		Label text = new Label("fill color");
		text.getStyleClass().add("text");
		layout.getChildren().addAll(fillColor,text);
		return layout;
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setResizable(true);
		setOnClicks(primaryStage);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public String getTitle() {
		return "Mapper";
	}

	@Override
	public void dispose() {

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
				levels.get(selectedLevel)
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
			// todo: вырезать?
			//todo: алерт об отсутствии функции
		});

		widthPicker.valueProperty()
				.addListener((observable, oldVal, newVal) -> {

					if (!levels.isEmpty()) {
						levels.get(selectedLevel)
								.getProperties()
								.setLineWidth((Double) newVal);
					}/*else{
				//todo: алерт об отсутствии уровней
				showAlert(Alert.AlertType.ERROR, "Level list is empty");
			}*/
				});

		mainColor.valueProperty()
				.addListener((observable, oldVal, newVal) -> {
					if (!levels.isEmpty()) {
						levels.get(selectedLevel)
								.getProperties()
								.setColor(newVal);
					}/*else{
				//todo: алерт об отсутствии уровней
				showAlert(Alert.AlertType.ERROR, "Level list is empty");
			}*/
				});

		fillColor.valueProperty()
				.addListener((observable, oldVal, newVal) -> {
					if (!levels.isEmpty()) {
						levels.get(selectedLevel)
								.getProperties()
								.setFillColor(newVal);
					}/*else{
				//todo: алерт об отсутствии уровней
				showAlert(Alert.AlertType.ERROR, "Level list is empty");
			}*/
				});


		isUseFill.setOnAction((value) -> {
			if (!levels.isEmpty()) {
				levels.get(selectedLevel)
						.getProperties()
						.setUseFill(((CheckBox) value.getSource()).isSelected());
			}
		});
		isUseFill.setSelected(false);

		push.setOnAction(event -> Navigator.push(new LvlConnectScreen(project)));
		save.setOnAction(event -> save());
		publish.setOnAction(event -> publish());
		back.setOnAction(event -> Navigator.replace(new ProjectListScreen()));

	}

	private void save() {
		SaveAction action = new SaveAction(project);
		//action.setLocalSave(true);
		ExecutableDialog executable = new SimpleExecutableDialog("saving", action);
		DialogViewer.showExecutableDialog(executable);
	}

	private void publish() {
		//todo: проверка безопасности проекта
		Action action = new PublishAction(project);
		ExecutableDialog executable = new SimpleExecutableDialog("publishing", action);
		DialogViewer.showExecutableDialog(executable);
	}

	private void getInfoFromDialog() {
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(10));
		layout.setHgap(5);
		layout.setVgap(10);

		Label info = new Label("Enter the length and WIDTH (integers)");
		layout.add(info, 0, 0, 3, 1);
		Label widthTxt = new Label("Width:");
		FontStyler.setHeaderStyle(widthTxt, 15);
		layout.add(widthTxt, 0, 1);
		TextField inputWidth = new TextField();
		layout.add(inputWidth, 1, 1);

		Label heightTxt = new Label("Height:");
		FontStyler.setHeaderStyle(heightTxt, 15);
		layout.add(heightTxt, 0, 2);
		TextField inputHeight = new TextField();
		layout.add(inputHeight, 1, 2);

		Label colorText = new Label("Color:");
		FontStyler.setHeaderStyle(heightTxt, 15);
		layout.add(colorText, 0, 3);
		ColorPicker colorPicker = new ColorPicker(Color.WHITE);
		layout.add(colorPicker, 1, 3);


		Button apply = new Button("OK");
		layout.add(apply, 0, 4);
		Button cancel = new Button("CANCEL");
		layout.add(cancel, 1, 4);

		Scene scene = new Scene(layout, 290, 220);
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
				int colRes = new java.awt.Color(red, green, blue).getRGB();

				File tmp = ImageUtils.getEmptyImage(width, height, colRes);
				if (tmp != null) {
					createLevel(tmp);
				}
				stage.close();
			} catch (Exception e) {
				//todo: alert dialog
			}
		});

		cancel.setOnAction(event -> stage.close());
		stage.show();


	}

	private void selectLevel(int id) {
		this.selectedLevel = id;
		Level level = levels.get(id);

		isBg.setSelected(level.isBackground());
		isTmpNodes.setSelected(level.isTmpNodes());
		levelName.setText(level.getName());

		CanvasController controller = level.getController();
		properties.setSource(controller);
		rootCanvas.getChildren().clear();
		rootCanvas.getChildren().addAll(controller.getLayers());


	}

	private void selectTool(ActionEvent event) {
		final ToggleButton source = (ToggleButton) event.getSource();

		int index = drawingToolButtons.indexOf(source);

		if (index == -1) {
			index = settingsToolButtons.indexOf(source);

			index = index == -1
					? nodeToolButtons.indexOf(source)
					+ drawingToolButtons.size()
					+ settingsToolButtons.size()
					: index + drawingToolButtons.size();
		}


		properties.setTool(index);
	}

	private void createLevel(File img) {
		try {
			Image tmp = new Image(new FileInputStream(img));

			Level level = new Level(levels.size(), properties);
			level.setUseBackground(true);
			level.setBackground(tmp);

			levels.add(level);

			if (selectedLevel < 0) {
				selectLevel(0);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


}
