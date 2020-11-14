package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public abstract class Holder {
	public static final int ADDITIONAL_WIDTH = 5;
	protected ArrayList<Probe> probes;

	public abstract void splitLayers(Pane[] layers);

	public abstract void extractProbes(ProbeManager manager);

	public abstract void remove(Pane[] layers);
	protected  void remove(Pane[] layers, Shape decoration, Shape ... hitBoxes){
		layers[LayersName.DECORATION]
				.getChildren()
				.remove(decoration);

		layers[LayersName.INPUT_LAYER]
				.getChildren()
				.removeAll(hitBoxes);
	}

	public abstract void replace();

	public abstract void beginResize(double x, double y);

	public abstract void resize(double x, double y);

	public abstract void endResize(double x, double y, ProbeManager manager);

	public abstract void rebuildProbes(ProbeManager manager);

	public abstract void getInfo();

	public abstract Shape getShape();

	public abstract boolean contains(double x, double y);

	protected void initProbes(Shape shape, Probe... probes) {
		this.probes = new ArrayList<>();

		// Remove duplicate probes
		HashSet<Probe> uniqueProbes = new HashSet<>();
		Collections.addAll(uniqueProbes, probes);

		for (Probe probe : uniqueProbes) {
			probe.getAttachedShapes().add(shape);
			this.probes.add(probe);
		}

	}

	public boolean isAttachedToProbe(Probe probe) {
		return probes.contains(probe);
	}


}
