package ru.fbtw.navigator.map_builder.canvas.tools;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.fbtw.navigator.map_builder.canvas.holder.Holder;
import ru.fbtw.navigator.map_builder.canvas.holder.HolderManager;
import ru.fbtw.navigator.map_builder.utils.StringUtils;

public class InfoTool extends Tool {

	public InfoTool(HolderManager manager) {
		super(manager);
	}


	@Override
	public void onPressed(double x, double y) {
		holders = manager.selectAll(x, y);

		for (Holder h : holders) {
			showDialog(h);
		}
	}

	@Override
	public void onDragged(double x, double y) {

	}


	@Override
	public void onReleased(double x, double y) {

	}

	private void showDialog(Holder holder) {
		String titleName = StringUtils.holderToString(holder.getClass());

		Scene mainScene = new Scene(holder.getInfo(manager.getManager()));
		Stage dialogStage = new Stage();
		dialogStage.setScene(mainScene);
		dialogStage.setTitle(titleName);
		dialogStage.setResizable(false);
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initStyle(StageStyle.UTILITY);

		dialogStage.show();
	}
}
