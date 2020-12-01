package ru.fbtw.navigator.map_builder.core;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Project implements Serializable {
	private HashSet<Platforms> platforms;
	private ObservableList<Level> levels;
	private ArrayList<LevelNode> levelNodeSystem;


	public Project() {
		levels = FXCollections.observableArrayList(Level.getCallback());
		levels.addListener(this::updateLevelNodeSystem);
		levelNodeSystem = new ArrayList<>();

	}
	public void updateLevelNodeSystem(ListChangeListener.Change<? extends Level> change){
		if(change.next()){
			if(change.wasAdded()) {
				List<? extends Level> newLevels = change.getAddedSubList();

				for(Level lvl : newLevels){
					levelNodeSystem.add(new LevelNode(lvl));
				}
			}

			if(change.wasRemoved()){
				List<? extends Level> newLevels = change.getRemoved();

				// todo: удаление уровней
			}
		}
	}
	public HashSet<Platforms> getPlatforms() {
		return platforms;
	}

	public ObservableList<Level> getLevels() {
		return levels;
	}

	public void update(){
		for(LevelNode node : levelNodeSystem){
			node.updateSockets();
		}
	}

	public ArrayList<LevelNode> getNodeSystem() {
		return levelNodeSystem;
	}
}
