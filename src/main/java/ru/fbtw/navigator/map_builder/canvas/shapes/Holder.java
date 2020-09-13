package ru.fbtw.navigator.map_builder.canvas.shapes;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public abstract class Holder {
	protected ArrayList<Probe> probes;

	public abstract void splitLayers(Pane[] layers);
	public abstract void remove();
	public abstract void replace();
	public abstract void resize();
	public abstract void getInfo();

	public abstract Shape getShape();


}
