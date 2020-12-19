package ru.fbtw.navigator.map_builder.ui.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.fbtw.navigator.map_builder.controller.ProjectUpdateController;
import ru.fbtw.navigator.map_builder.controller.response.BaseResponse;
import ru.fbtw.navigator.map_builder.core.Platform;
import ru.fbtw.navigator.map_builder.core.ProjectModel;
import ru.fbtw.navigator.map_builder.core.ProjectModelBuilder;
import ru.fbtw.navigator.map_builder.ui.control.Navigator;
import ru.fbtw.navigator.map_builder.ui.control.Screen;


public class ProjectSetupScreen implements Screen {

    private final VBox mainLayout;
    private Scene scene;

    private final TextField projectNameInput;
    private final TextField botName, botApi;
    private final TextField appName, webSite;

    private Button back, create;

    private final CheckBox[] platforms;
    private boolean isCreate;
    private ProjectModel model;

    private Node botSetup;
    private Node appSetup;

    private Label error;


    public ProjectSetupScreen(ProjectModel model) {
        isCreate = model == null;
        this.model = model;
        mainLayout = new VBox();
        platforms = new CheckBox[Platform.values().length];


        projectNameInput = new TextField();
        botName = new TextField();
        botApi = new TextField();
        appName = new TextField();
        webSite = new TextField();
        initScene();
    }

    private Node buildHeader() {
        Label title = new Label("Creating a new project");
        title.getStyleClass().add("label-header");
        title.setPadding(new Insets(0, 0, 20, 0));

        HBox.setHgrow(title, Priority.ALWAYS);
        return title;
    }

    private Node buildInputPair(String text, TextField input, double width) {
        HBox layout = new HBox(20);
        layout.setAlignment(Pos.BASELINE_LEFT);

        Label name = new Label(text);
        name.getStyleClass().add("label-name");


        input.setPrefWidth(width);
        input.getStyleClass().add("text-field");

        layout.getChildren().addAll(name, input);
        return layout;
    }

    private Node buildPlatformChose() {
        HBox layout = new HBox(15);
        layout.setPadding(new Insets(0, 0, 30, 0));

        platforms[Platform.TG_BOT.ordinal()] = new CheckBox("Telegram bot");
        platforms[Platform.TG_BOT.ordinal()].setOnAction(getOnClick(botSetup));


        platforms[Platform.APP.ordinal()] = new CheckBox("Mobile app");
        platforms[Platform.APP.ordinal()].setOnAction(getOnClick(appSetup));

        platforms[Platform.MAP_STATION.ordinal()] = new CheckBox("Map station");
        platforms[Platform.MAP_STATION.ordinal()].setDisable(true);

        platforms[Platform.PAPER_MAP.ordinal()] = new CheckBox("Paper map");
        platforms[Platform.PAPER_MAP.ordinal()].setDisable(true);

        layout.getChildren().addAll(platforms);

        return layout;
    }

    private Node buildBotSetup() {
        VBox layout = new VBox(10);

        layout.getChildren().addAll(
                buildInputPair("Bot name", botName, 700),
                buildInputPair("Bot apiKey", botApi, 685)
        );

        return layout;
    }

    private Node buildAppSetup() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 0, 20, 0));

        layout.getChildren().addAll(
                buildInputPair("Mobile app name", appName, 600),
                buildInputPair("Company web site(can be empty)", webSite, 400)
        );

        return layout;
    }

    private Node buildFooterButtons() {
        HBox layout = new HBox(20);
        layout.setAlignment(Pos.CENTER_RIGHT);

        error = new Label("");
        error.getStyleClass().add("label-err");
        error.setVisible(false);

        String buttonText = isCreate ? "Create" : "Save";
        create = new Button(buttonText);
        create.setOnAction(isCreate ? this::create : this::update);

        back = new Button("Back");
        back.setOnAction(this::back);

        layout.getChildren().addAll(error, back, create);

        return layout;
    }

    private void setModel() {
        projectNameInput.setText(model.getName());

        for (Platform platform : model.getPlatforms()) {
            platforms[platform.ordinal()].setSelected(true);
        }

        if (platforms[Platform.TG_BOT.ordinal()].isSelected()) {
            botName.setText(model.getTelegramName());
            botApi.setText(model.getTelegramApiKey());
            botSetup.setDisable(false);
        }

        if (platforms[Platform.APP.ordinal()].isSelected()) {
            appName.setText(model.getAppName());
            webSite.setText(model.getUserPackage());
            appSetup.setDisable(false);
        }
    }

    private void update(ActionEvent actionEvent) {
        ProjectModel projectModel = getProjectModel();
        ProjectUpdateController controller = ProjectUpdateController.getInstance();
        BaseResponse response = controller.setMethod(ProjectUpdateController.UPDATE)
                .setCredentials(projectModel)
                .execute();

        if (response != null) {
            if (response.isSuccess()) {
                Navigator.replace(new ProjectListScreen());
            } else {
                setError(response.getMessage());
            }
        } else {
            setError("Internal error, please reboot or update app");
        }
    }

    private void create(ActionEvent actionEvent) {
        ProjectModel projectModel = getProjectModel();
        ProjectUpdateController controller = ProjectUpdateController.getInstance();
        BaseResponse response = controller.setMethod(ProjectUpdateController.CREATE)
                .setCredentials(projectModel)
                .execute();

        if (response != null) {
            if (response.isSuccess()) {
                Navigator.replace(new ProjectListScreen());
            } else {
                setError(response.getMessage());
            }
        } else {
            setError("Internal error, please reboot or update app");
        }

    }

    private ProjectModel getProjectModel() {

        ProjectModelBuilder builder = new ProjectModelBuilder();

        builder.setName(projectNameInput.getText());

        if (platforms[Platform.TG_BOT.ordinal()].isSelected()) {
            builder.addPlatform(Platform.TG_BOT)
                    .setTelegramName(botName.getText())
                    .setTelegramApiKey(botApi.getText());
        }
        if (platforms[Platform.APP.ordinal()].isSelected()) {
            builder.addPlatform(Platform.APP)
                    .setAppName(appName.getText())
                    .setUserPackage(webSite.getText());
        }

        ProjectModel projectModel = builder.createProjectModel();

        if(model != null) {
            projectModel.setId(model.getId());
        }

        return projectModel;
    }

    private void back(ActionEvent actionEvent) {
        Navigator.replace(new ProjectListScreen());
    }

    private Node buildPlatformsHeader() {
        Label title = new Label("Select platforms");
        title.getStyleClass().add("label-plat");
        title.setPadding(new Insets(20, 0, 10, 0));

        HBox.setHgrow(title, Priority.ALWAYS);
        return title;
    }

    private EventHandler<ActionEvent> getOnClick(Node target) {
        return event -> {
            CheckBox checkBox = (CheckBox) event.getSource();
            target.setDisable(!checkBox.isSelected());
        };
    }

    private void setError(String text) {
        error.setVisible(true);
        error.setText(text);
    }


    @Override
    public void initScene() {
        mainLayout.getStyleClass().add("hbox-group");

        botSetup = buildBotSetup();
        botSetup.setDisable(true);

        appSetup = buildAppSetup();
        appSetup.setDisable(true);

        mainLayout.getChildren().addAll(
                buildHeader(),
                buildInputPair("Name", projectNameInput, 745),
                buildPlatformsHeader(),
                buildPlatformChose(),
                botSetup,
                appSetup,
                buildFooterButtons()
        );

        mainLayout.setAlignment(Pos.TOP_LEFT);
        ScrollPane pane = new ScrollPane(mainLayout);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        pane.setPannable(true);

        if (!isCreate) {
            setModel();
        }
        scene = new Scene(pane);
        scene.getStylesheets().add("project-setup.css");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMinWidth(300);
    }

    @Override
    public void dispose() {

    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public String getTitle() {
        return "";
    }
}
