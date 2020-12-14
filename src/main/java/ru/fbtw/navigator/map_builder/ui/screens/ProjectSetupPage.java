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
import ru.fbtw.navigator.map_builder.core.Platforms;
import ru.fbtw.navigator.map_builder.ui.control.Navigator;
import ru.fbtw.navigator.map_builder.ui.control.Screen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class ProjectSetupPage implements Screen {

    private final VBox mainLayout;
    private Scene scene;

    private TextField projectNameInput;

    private TextField botName, botApi;

    private TextField appName, webSite;

    private Button back, create;

    private final CheckBox[] platforms;
    private boolean isCreate;

    private Node botSetup;
    private Node appSetup;

    private Label error;


    public ProjectSetupPage(boolean isCreate) {
        this.isCreate = isCreate;
        mainLayout = new VBox();
        platforms = new CheckBox[Platforms.values().length];
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

        input = new TextField();
        input.setPrefWidth(width);
        input.getStyleClass().add("text-field");

        layout.getChildren().addAll(name, input);
        return layout;
    }

    private Node buildPlatformChose() {
        HBox layout = new HBox(15);
        layout.setPadding(new Insets(0,0,30,0));

        platforms[Platforms.TG_BOT.ordinal()] = new CheckBox("Telegram bot");
        platforms[Platforms.TG_BOT.ordinal()].setOnAction(getOnClick(botSetup));


        platforms[Platforms.APP.ordinal()] = new CheckBox("Mobile app");
        platforms[Platforms.APP.ordinal()].setOnAction(getOnClick(appSetup));

        platforms[Platforms.MAP_STATION.ordinal()] = new CheckBox("Map station");
        platforms[Platforms.MAP_STATION.ordinal()].setDisable(true);

        platforms[Platforms.PAPER_MAP.ordinal()] = new CheckBox("Paper map");
        platforms[Platforms.PAPER_MAP.ordinal()].setDisable(true);

        layout.getChildren().addAll(platforms);

        return layout;
    }

    private Node buildBotSetup() {
        VBox layout = new VBox(10);

        layout.getChildren().addAll(
          buildInputPair("Bot name",botName,700),
          buildInputPair("Bot apiKey",botApi,685)
        );

        return layout;
    }

    private Node buildAppSetup() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10,0,20,0));

        layout.getChildren().addAll(
                buildInputPair("Mobile app name", appName, 600),
                buildInputPair("Company web site(can be empty)", webSite, 400)
        );

        return layout;
    }

    private Node buildFooterButtons(){
        HBox layout = new HBox(20);
        layout.setAlignment(Pos.CENTER_RIGHT);

        error = new Label("");
        error.getStyleClass().add("label-err");

        String buttonText = isCreate ? "Create" : "Save";
        create = new Button(buttonText);
        create.setOnAction(isCreate ? this::create : this::update);

        back = new Button("Back");
        back.setOnAction(this::back);

        layout.getChildren().addAll(error,back,create);

        return layout;
    }

    private void update(ActionEvent actionEvent) {

    }

    private void create(ActionEvent actionEvent) {
        String name = appName.getText();

        Set<Platforms> platformsSet = new HashSet<>();
        for(int i =0; i < platforms.length; i++){
            if(platforms[i] != null && platforms[i].isSelected()){
                platformsSet.add(Platforms.values()[i]);
            }
        }

        if(platformsSet.contains(Platforms.TG_BOT)){

        }
        if(platformsSet.contains(Platforms.TG_BOT)){

        }
    }

    private void back(ActionEvent actionEvent) {
        Navigator.replase(new ProjectListPage());
    }

    private Node buildPlatformsHeader() {
        Label title = new Label("Select platforms");
        title.getStyleClass().add("label-plat");
        title.setPadding(new Insets(20, 0, 10, 0));

        HBox.setHgrow(title, Priority.ALWAYS);
        return title;
    }

    private EventHandler<ActionEvent> getOnClick(Node target){
        return event ->{
            CheckBox checkBox = (CheckBox) event.getSource();
            target.setDisable(!checkBox.isSelected());
        };
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
                buildInputPair("Name",projectNameInput,745),
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
