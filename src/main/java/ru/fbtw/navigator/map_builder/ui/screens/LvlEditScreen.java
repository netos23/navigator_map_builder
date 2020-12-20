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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.fbtw.navigator.map_builder.canvas.CanvasController;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.core.Level;
import ru.fbtw.navigator.map_builder.core.Project;
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
	private Button imageChooserBtn, emptyBtn;
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
	private Button clear, undo, redo;
	private Button save, load, push;

	private ObservableList<Level> levels;
	private int selectedLevel;
	private StackPane rootCanvas;

	private CanvasProperties properties;
	private ArrayList<ToggleButton> nodeToolButtons;
	private int minHeight = 30;
	private Scene scene;

	private Project project;

	public LvlEditScreen(Project project)  {
		this.project = project;
		this.levels = project.getLevels();

		imageChooserBtn = new Button("Add image");
		emptyBtn = new Button("Add empty");


		imageChooser = getFileChooser();
		selectedLevel = -1;

		isBg = new CheckBox("Use background");
		isTmpNodes = new CheckBox("Use temp nodes");

		levelName = new TextField();
		nameUpdater = new Button("Update name");

		levelListWidget = new ListView<>(levels);
		levelListWidget.setMaxHeight(300.0);

		properties = CanvasProperties.DEFAULT_CANVAS_PROPERTIES;


		settingsToolButtons = new ToggleButtonGridBuilder()
				.setOnClick(this::selectTool)
				.setUseName(true)
				.setUseImage(false)
				.setSource(CanvasController.settingsToolNames)
				.build();
		drawingToolButtons = new ToggleButtonGridBuilder()
				.setOnClick(this::selectTool)
				.setUseName(true)
				.setUseImage(false)
				.setSource(CanvasController.drawingToolsNames)
				.build();
		nodeToolButtons = new ToggleButtonGridBuilder()
				.setOnClick(this::selectTool)
				.setUseName(true)
				.setUseImage(false)
				.setSource(CanvasController.nodeToolNames)
				.build();

		mainColor = new ColorPicker(Color.BLACK);
		mainColor.setMinHeight(minHeight);
		fillColor = new ColorPicker(Color.WHITE);
		fillColor.setMinHeight(minHeight);
		isUseFill = new CheckBox();


		widthPicker = new Slider(5, 50, 10);
		widthPicker.setValue(5);
		widthPicker.setShowTickLabels(true);
		widthPicker.setShowTickMarks(true);

		clear = new Button("Clear");
		undo = new Button("<-");
		redo = new Button("->");

		save = new Button("Save");
		load = new Button("Load");
		push = new Button("Push");

		initScene();
	}

	public void initScene() {
		BorderPane mainLayout = new BorderPane();

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
				.setTitle("Primitives")
				.addButtonsGrid(3, drawingToolButtons, true)
				.setTitle("Tools")
				.addButtonsGrid(3, settingsToolButtons, false)
				.setTitle("Nodes")
				.addButtonsGrid(3, nodeToolButtons, false)
				.setTitle("Line color")
				.addContent(mainColor)
				.wrapWithScrolView()
				.setOptionalTitle("Fill color", isUseFill)
				.addContent(fillColor)
				.setTitle("Line WIDTH")
				.addContent(widthPicker)
				.addHorizontalButtonsPanel(clear, undo, redo)
				.addContent(save)
				.addContent(load)
				.addContent(push)
				.build();

		rootCanvas = new StackPane();
		rootCanvas.setStyle("-fx-background-color: #666666;");
		ScrollPane scrollPane = new ScrollPane(rootCanvas);
		scrollPane.setMinSize(800.0, 600);
		scrollPane.setPadding(new Insets(5));


		mainLayout.setPadding(new Insets(3));
		mainLayout.setStyle("-fx-background-color: #666666;");
		mainLayout.setCenter(scrollPane);
		mainLayout.setLeft(leftMenu);
		mainLayout.setRight(rightMenu);

		scene = new Scene(mainLayout);
		KeyManager.attachKeyManagerToScene(scene);
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

	private void setOnClicks(final Stage stage){

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

		save.setOnAction(event -> {
			save();
		});

		load.setOnAction(event -> {
			publish();
		});

	}

	private void save() {
		Action action = new SaveAction(project);
		ExecutableDialog executable = new SimpleExecutableDialog("saving",action);
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
