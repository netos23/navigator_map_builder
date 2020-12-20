package ru.fbtw.navigator.map_builder.ui.dialogs;

import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.utils.common.ProjectAsyncAction;
import ru.fbtw.navigator.map_builder.web_controllers.ProjectPublishController;
import ru.fbtw.navigator.map_builder.web_controllers.response.BaseResponse;

public class PublishAction extends ProjectAsyncAction {
	private SaveAction saveAction;
	private Project project;


	public PublishAction(Project project) {
		super(project);

		this.project = project;
		saveAction = new SaveAction(project);
	}


	@Override
	public void run() {
		try {
			BaseResponse response = ProjectPublishController.getInstance()
					.setProject(project, levelMap)
					.execute();
			System.out.println(response.getMessage());

			saveAction.execute();
		}catch (Exception ex){
			ex.printStackTrace();
			System.out.println("Error while publishing");
		}
		super.run();
	}
}
