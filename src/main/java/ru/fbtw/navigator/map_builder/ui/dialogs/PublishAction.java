package ru.fbtw.navigator.map_builder.ui.dialogs;

import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.utils.common.AsyncAction;
import ru.fbtw.navigator.map_builder.web_controllers.ProjectPublishController;
import ru.fbtw.navigator.map_builder.web_controllers.response.BaseResponse;

public class PublishAction extends AsyncAction {
    private SaveAction saveAction;
    private Project project;

    public PublishAction(Project project){
        this.project = project;
        saveAction = new SaveAction(project);
    }

    @Override
    public void run() {
        BaseResponse response = ProjectPublishController.getInstance()
                .setProject(project)
                .execute();

        System.out.println(response.getMessage());

        saveAction.execute();
        super.run();
    }
}
