package ru.fbtw.navigator.map_builder.ui.dialogs;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.ui.widget.ExecutableDialog;
import ru.fbtw.navigator.map_builder.utils.common.Action;

public class SimpleExecutableDialog implements ExecutableDialog {

    private HBox layout;
    private String title;
    private Action action;

    public SimpleExecutableDialog(String title, Action action) {
        this.title = title;
        this.action = action;

        layout = new HBox();
        layout.setFillHeight(true);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("hbox");
        layout.minWidth(500);

        Label savingLabel = new Label(title);
        ProgressIndicator progressIndicator = new ProgressIndicator();

        layout.getChildren().addAll(savingLabel, progressIndicator);
    }

    @Override
    public Parent getLayout() {
        return layout;
    }

    @Override
    public String getCssName() {
        return "dialog.css";
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Action getExecutableAction() {
        return action;
    }


}
