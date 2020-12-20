package ru.fbtw.navigator.map_builder.ui.screens;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.fbtw.navigator.map_builder.auth.UserData;
import ru.fbtw.navigator.map_builder.web_controllers.response.AuthResponse;
import ru.fbtw.navigator.map_builder.ui.control.Navigator;
import ru.fbtw.navigator.map_builder.ui.control.Screen;
import ru.fbtw.navigator.map_builder.utils.ImageUtils;
import ru.fbtw.navigator.map_builder.web_controllers.LoginFormController;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LoginScreen implements Screen {
    private final URI registrationUrl = new URI("https://yandex.ru");

    private TextField loginInput;
    private PasswordField passwordInput;
    private Button registration;
    private Button submit;
    private Label onError;
    private ProgressIndicator indicator;

    private VBox mainLayout;
    private Scene scene;

    private LoginFormController controller;


    public LoginScreen() throws IOException, URISyntaxException {
        mainLayout = new VBox();
        controller = LoginFormController.getInstance();
        initScene();
    }

    private Label getHint() {
        Label label = new Label("navigation solutions");
        label.getStyleClass().add("label-disc");
        return label;
    }

    private HBox getHeaderLayout() throws IOException {
        Label appName = new Label("MAPPER");
        appName.getStyleClass().add("label-header");


        ImageView logo = ImageUtils.loadImage("icons/logo.png");

        HBox headerLayout = new HBox();
        headerLayout.setPrefHeight(50);
        headerLayout.setAlignment(Pos.TOP_CENTER);


        headerLayout.getChildren().addAll(logo, appName);

        return headerLayout;
    }

    private Label getMessage() {
        Label label = new Label("Welcome to mapper.\n Log in to continue");
        label.getStyleClass().add("label-msg");
        label.setPadding(new Insets(50, 0, 0, 0));
        VBox.setVgrow(label, Priority.ALWAYS);
        return label;
    }

    private Node[] getLoginForm() {
        Label loginDescription = new Label("Login");
        loginDescription.setPadding(new Insets(20, 0, 5, 0));
        loginDescription.getStyleClass().add("label-form");

        loginInput = new TextField();

        Label passwordDescription = new Label("Password");
        passwordDescription.getStyleClass().add("label-form");
        passwordDescription.setPadding(new Insets(10, 0, 5, 0));

        passwordInput = new PasswordField();

        return new Node[]{
                loginDescription,
                loginInput,
                passwordDescription,
                passwordInput
        };
    }

    private HBox getFooterButtons() {
        indicator = new ProgressIndicator();

        registration = new Button("Sing up");
        registration.getStyleClass().add("button-sing-up");
        registration.setOnAction(this::onRegistrationSubmit);

        submit = new Button("Log in");
        submit.getStyleClass().add("button-log-in");
        submit.setOnAction(this::submitForm);

        HBox layout = new HBox(20);
        layout.setPadding(new Insets(10, 0, 0, 0));
        layout.setAlignment(Pos.CENTER_RIGHT);
        layout.getChildren().addAll(registration, submit);
        return layout;
    }

    private Label getOnError() {
        Label label = new Label("An error occurred.");
        label.getStyleClass().add("label-err");
        label.setVisible(false);
        label.setPadding(new Insets(5, 0, 0, 0));
        return label;
    }

    public void setErr(String text) {
        onError.setVisible(true);
        onError.setText(text);
    }

    private void onRegistrationSubmit(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(registrationUrl);
        } catch (Exception e) {
            e.printStackTrace();
            setErr("An error occurred.");
        }
    }

    private void submitForm(ActionEvent actionEvent) {
        try {
            String login = loginInput.getText();
            String password = passwordInput.getText();

            AuthResponse response = controller.setCredentials(login, password)
                    .execute();

            if (response.isSuccess()) {
                UserData.setToken(response.getToken());
                Navigator.replace(new ProjectListScreen());
                /*Screen firstScreen = new ProjectSetupPage(true);
                Navigator.replace(firstScreen);*/
            } else {
                setErr(response.getMessage());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            setErr("An error occurred. Check your Internet connection");
        }

    }

    @Override
    public void initScene() {
        HBox headerLayout = null;
        try {
            headerLayout = getHeaderLayout();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Label message = getMessage();
        mainLayout.getStyleClass().add("hbox-group");
        Label addDescription = getHint();
        onError = getOnError();

        mainLayout.getChildren().addAll(headerLayout, addDescription, message);
        mainLayout.getChildren().addAll(getLoginForm());
        mainLayout.getChildren().addAll(getFooterButtons());
        mainLayout.getChildren().add(onError);

        scene = new Scene(mainLayout);
        scene.getStylesheets().add("login.css");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
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
        return "Mapper";
    }
}
