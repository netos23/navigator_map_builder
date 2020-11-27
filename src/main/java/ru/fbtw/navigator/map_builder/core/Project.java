package ru.fbtw.navigator.map_builder.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.fbtw.navigator.map_builder.navigation.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Project implements Serializable {
	private HashSet<Platforms> platforms;
	private ObservableList<Level> levels;
	private ArrayList<Node> levelNodeSystem;

	public Project() {
		levels = FXCollections.observableArrayList(Level.getCallback());
	}

	public HashSet<Platforms> getPlatforms() {
		return platforms;
	}

	public ObservableList<Level> getLevels() {
		return levels;
	}
}
