package ru.fbtw.navigator.map_builder.ui.dialogs;

import ru.fbtw.navigator.map_builder.web_controllers.EditorController;
import ru.fbtw.navigator.map_builder.web_controllers.response.BaseResponse;
import ru.fbtw.navigator.map_builder.core.Project;
import ru.fbtw.navigator.map_builder.io.Serializer;
import ru.fbtw.navigator.map_builder.utils.StringUtils;
import ru.fbtw.navigator.map_builder.utils.common.AsyncAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class SaveAction extends AsyncAction {

    private final Project project;
    private boolean localSave;


    public SaveAction(Project project) {
        this.project = project;
    }

    @Override
    public void run() {
        if (localSave) {
            try {
                //saves locally
                Serializer serializer = new Serializer();
                String res = serializer.writeProject(project);

                File save = new File("saves/" + StringUtils.nextHashName() + ".json");
                PrintStream printStream = new PrintStream(save);
                printStream.print(res);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        EditorController editorController = EditorController.getInstance();
        BaseResponse response = editorController.setCredentials(project)
                .execute();

        System.out.println(response.getMessage());


        super.run();
    }

    public boolean isLocalSave() {
        return localSave;
    }

    public SaveAction setLocalSave(boolean localSave) {
        this.localSave = localSave;
        return this;
    }


}
