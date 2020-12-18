package ru.fbtw.navigator.map_builder.ui.screens;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.fbtw.navigator.map_builder.controller.ProjectListController;
import ru.fbtw.navigator.map_builder.controller.response.BaseResponse;
import ru.fbtw.navigator.map_builder.controller.response.ListResponse;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.core.ProjectModel;
import ru.fbtw.navigator.map_builder.io.ProjectLoader;
import ru.fbtw.navigator.map_builder.ui.control.Navigator;
import ru.fbtw.navigator.map_builder.ui.control.Screen;
import ru.fbtw.navigator.map_builder.utils.ImageUtils;


import java.io.IOException;

public class ProjectListPage implements Screen {

    private BorderPane mainLayout;

    private ObservableList<ProjectModel> projects;
    private Scene scene;
    private ListView<ProjectModel> projectListView;
    private Button add, edit, remove, settings;

    public ProjectListPage() {

        mainLayout = new BorderPane();
        projects = FXCollections.observableArrayList();
        projectListView = new ListView<>(projects);


        add = new Button();
        edit = new Button();
        remove = new Button();
        settings = new Button();

        initScene();
    }

    private void setupListView(){
        MultipleSelectionModel<ProjectModel> selectionModel = projectListView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        selectionModel.selectedItemProperty().addListener(this::changed);
    }

    private void changed(
            ObservableValue<? extends ProjectModel> changed,
            ProjectModel oldValue, ProjectModel newValue
    ){
        enableButtons(newValue != null);
    }

    private void enableButtons(boolean isEnable) {
        settings.setDisable(!isEnable);
        remove.setDisable(!isEnable);
        edit.setDisable(!isEnable);
    }

    private VBox getLeftLayout() {


        Label projectsLabel = new Label("Projects");
        projectsLabel.getStyleClass().add("projects");

        // projectListView.getStyleClass().add("list");

        VBox vBox = new VBox(10);

        vBox.getStyleClass().add("hbox-group");

        vBox.getChildren().addAll(projectsLabel, projectListView,buildButtonsBar());
        return vBox;
    }

    private Node buildButtonsBar(){
        HBox layout = new HBox(20);
        try {
            add.setGraphic(ImageUtils.loadImage("common/buttons/add.png"));
            remove.setGraphic(ImageUtils.loadImage("common/buttons/delete.png"));
            settings.setGraphic(ImageUtils.loadImage("common/buttons/settings.png"));
            edit.setGraphic(ImageUtils.loadImage("common/buttons/create.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        add.setOnAction(this::addOnClick);
        settings.setOnAction(this::settingsOnClick);
        remove.setOnAction(this::removeOnClick);
        edit.setOnAction(this::editOnClick);

        enableButtons(false);
        layout.getChildren().addAll(add,remove,settings,edit);

        return layout;
    }

    private void addOnClick(ActionEvent event){
        Navigator.replace(new ProjectSetupPage(null));
    }

    private void settingsOnClick(ActionEvent event){
        int selectedIndex = projectListView.getSelectionModel().getSelectedIndex();
        Navigator.replace(new ProjectSetupPage(projects.get(selectedIndex)));
    }

    private void removeOnClick(ActionEvent event){
        int selectedIndex = projectListView.getSelectionModel().getSelectedIndex();
        BaseResponse baseResponse = ProjectListController.getInstance()
                .setMethod(ProjectListController.REMOVE)
                .setTarget(projects.get(selectedIndex))
                .execute();

        if (baseResponse == null || !baseResponse.isSuccess()) {
            setErr("Something went wrong update page");
        }

        updateBody();
    }

    private void editOnClick(ActionEvent actionEvent){

            int selectedIndex = projectListView.getSelectionModel().getSelectedIndex();
            ProjectModel model = projects.get(selectedIndex);

            Project project;
            if(model.getBody() != null && !model.getBody().isEmpty()) {
                try {

                ProjectLoader loader = new ProjectLoader(model.getBody());
                project = loader.load();

                } catch (Exception e) {
                    e.printStackTrace();
                    project = new Project();
                }
            }else {
                project = new Project();
            }
            project.setModel(model);

            Navigator.replace(new LvlEditScreen(project));

    }


    @Override
    public void initScene() {
        setupListView();
        mainLayout.setLeft(getLeftLayout());

        scene = new Scene(mainLayout);
        scene.getStylesheets().add("project-list.css");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(true);
        updateBody();
    }

    private void updateBody() {
        projects.clear();
        BaseResponse baseResponse = ProjectListController.getInstance()
                .setMethod(ProjectListController.LIST)
                .execute();

        if (baseResponse != null && baseResponse.isSuccess()
                && baseResponse instanceof ListResponse) {
            ListResponse<ProjectModel> response = (ListResponse<ProjectModel>) baseResponse;
            projects.addAll(response.getBody());

        }else{
            setErr("Something went wrong update page");
        }
    }

    private void setErr(String text) {

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
