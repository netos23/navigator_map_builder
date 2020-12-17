package ru.fbtw.navigator.map_builder.core;

import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import ru.fbtw.navigator.map_builder.core.navigation.LevelConnection;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Project implements Serializable {
    private static Callback<Project, Observable[]> callback = param -> new Observable[]{param.name};
    private StringProperty name;

    private ObservableList<Level> levels;

    private ArrayList<LevelNode> levelNodeSystem;
    private HashSet<LevelConnection> connections;

    public Project() {
        levels = FXCollections.observableArrayList(Level.getCallback());
        levels.addListener(this::updateLevelNodeSystem);
        levelNodeSystem = new ArrayList<>();
        connections = new HashSet<>();
    }

    public void updateLevelNodeSystem(ListChangeListener.Change<? extends Level> change) {
        if (change.next()) {
            if (change.wasAdded()) {
                List<? extends Level> newLevels = change.getAddedSubList();

                for (Level lvl : newLevels) {
                    levelNodeSystem.add(new LevelNode(lvl));
                }
            }

            if (change.wasRemoved()) {
                List<? extends Level> newLevels = change.getRemoved();

                // todo: удаление уровней
            }
        }
    }


    public ObservableList<Level> getLevels() {
        return levels;
    }

    public void update() {
        for (LevelNode node : levelNodeSystem) {
            node.updateBasicSockets();
        }
    }

    public ArrayList<LevelNode> getNodeSystem() {
        return levelNodeSystem;
    }

    public HashSet<LevelConnection> getConnections() {
        return connections;
    }

    public void setLevels(List<Level> levels) {
        this.levels.addAll(levels);
    }
}
