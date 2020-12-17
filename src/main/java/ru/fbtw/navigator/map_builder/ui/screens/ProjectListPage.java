package ru.fbtw.navigator.map_builder.ui.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.fbtw.navigator.map_builder.auth.UserData;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.ui.control.Screen;

public class ProjectListPage implements Screen {

    private BorderPane mainLayout;

    private ObservableList<Project> projects;
    private Scene scene;
    private ListView<Project> projectListView;

    public ProjectListPage() {

        mainLayout = new BorderPane();
        projects = FXCollections.observableArrayList();
        projectListView = new ListView<>(projects);
        initScene();
    }

    private VBox getLeftLayout() {


        Label projectsLabel = new Label("Projects");
        projectsLabel.getStyleClass().add("projects");

       // projectListView.getStyleClass().add("list");

        VBox vBox = new VBox();

        vBox.getStyleClass().add("hbox-group");

        vBox.getChildren().addAll(projectsLabel,projectListView);
        return vBox;
    }

    @Override
    public void initScene() {
        mainLayout.setLeft(getLeftLayout());

        scene = new Scene(mainLayout);
        scene.getStylesheets().add("project-list.css");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(true);
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
        return null;
    }
}
