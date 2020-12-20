package ru.fbtw.navigator.map_builder.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.fbtw.navigator.map_builder.ui.widget.Dialog;
import ru.fbtw.navigator.map_builder.ui.widget.ExecutableDialog;
import ru.fbtw.navigator.map_builder.utils.KeyManager;
import ru.fbtw.navigator.map_builder.utils.common.Action;

public class DialogViewer {
	public static void showAlert(String s, Alert.AlertType error) {

	}

	public static void showExecutableDialog(ExecutableDialog dialog){
		Stage dialogStage = new Stage();

		Action executableAction = dialog.getExecutableAction();
		executableAction.andThen(dialogStage::close);
		executableAction.execute();

		showDialog(dialog,dialogStage);
	}

	private static void showDialog(Dialog dialog, Stage dialogStage){
		Scene mainScene = new Scene(dialog.getLayout());
		mainScene.getStylesheets().add(dialog.getCssName());
		KeyManager.attachKeyManagerToScene(mainScene);

		dialogStage.setScene(mainScene);
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initStyle(StageStyle.UNDECORATED);

		dialogStage.show();
	}

	public static void showDialog(Dialog dialog){
		Stage dialogStage = new Stage();
		showDialog(dialog,dialogStage);
	}

	public static void showDialog(String title, Parent layout){
		Scene mainScene = new Scene(layout);
		KeyManager.attachKeyManagerToScene(mainScene);
		showStage(title, mainScene);
	}

	private static void showStage(String title, Scene mainScene) {
		Stage dialogStage = new Stage();
		dialogStage.setScene(mainScene);
		dialogStage.setTitle(title);
		dialogStage.setResizable(false);
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initStyle(StageStyle.UTILITY);

		dialogStage.show();
	}
}
