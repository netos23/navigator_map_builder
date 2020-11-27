package ru.fbtw.navigator.map_builder.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.fbtw.navigator.map_builder.utils.KeyManager;

public class DialogViewer {
	public static void showAlert(String s, Alert.AlertType error) {

	}

	public static void showDialog(String title, Parent layout){
		Scene mainScene = new Scene(layout);
		KeyManager.attachKeyManagerToScene(mainScene);
		Stage dialogStage = new Stage();
		dialogStage.setScene(mainScene);
		dialogStage.setTitle(title);
		dialogStage.setResizable(false);
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initStyle(StageStyle.UTILITY);

		dialogStage.show();
	}
}
