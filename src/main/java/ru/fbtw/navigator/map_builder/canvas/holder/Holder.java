package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;

import java.util.ArrayList;

public abstract class Holder {
	public static final int ADDITIONAL_WIDTH = 5;
	protected ArrayList<Probe> probes;

	public abstract void splitLayers(Pane[] layers);
	public abstract void extractProbes(ProbeManager manager);
	public abstract void remove();
	public abstract void replace();
	public abstract void resize();
	public abstract void getInfo();
	public abstract Shape getShape();

	protected  void initProbes(Shape shape, Probe ... probes){
		this.probes = new ArrayList<>();
		for(Probe probe : probes){
			probe.getAttachedShapes().add(shape);
			this.probes.add(probe);
		}

	}


}
