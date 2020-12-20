package ru.fbtw.navigator.map_builder.utils.common;

import javafx.application.Platform;

public abstract class AsyncAction extends Thread implements Action {
    private Action thenAction;


    @Override
    public void run() {
        if (thenAction != null) {
            Platform.runLater(() -> thenAction.execute());
        }
    }

    @Override
    public void execute() {
        setDaemon(true);
        start();
    }


    @Override
    public void andThen(Action action) {
        if(thenAction == null) {
            thenAction = action;
        }else{
            thenAction.andThen(action);
        }
    }
}
