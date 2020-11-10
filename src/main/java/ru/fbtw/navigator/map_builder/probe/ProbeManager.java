package ru.fbtw.navigator.map_builder.probe;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import ru.fbtw.navigator.map_builder.canvas.CanvasController;
import ru.fbtw.navigator.map_builder.canvas.LayersName;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

public class ProbeManager {
	private ArrayList<Probe> probes;
	private CanvasController controller;

	public ProbeManager(CanvasController controller) {
		this.controller = controller;
		probes = new ArrayList<>();

	}

	public Probe getPosOfExistingPoint(double x, double y) {
		for (Probe probe : probes) {
			if (probe.isContainsPoint(x, y)) {
				return probe;
			}
		}

		return push(x, y);
	}

	public Probe getPosOfExistingPoint(MouseEvent event) {
		return getPosOfExistingPoint(event.getX(), event.getY());
	}

	public Vector2 getPosOfExistingTempPoint(MouseEvent event) {
		return getPosOfExistingTempPoint(event.getX(), event.getY());
	}

	public Vector2 getPosOfExistingTempPoint(double x, double y) {
		for (Probe probe : probes) {
			if (probe.isContainsPoint(x, y)) {
				Circle hitBox = probe.getHitBox();
				return new Vector2(hitBox.getCenterX(), hitBox.getCenterY());
			}
		}

		return new Vector2(x, y);
	}

	public boolean push(Probe probe) {
		if (!probes.contains(probe)) {
			pushToView(probe);
			return true;
		} else {
			return false;
		}
	}

	private Probe push(double x, double y) {
		Probe probe = new Probe(x, y);
		pushToView(probe);
		return probe;
	}

	private void pushToView(Probe probe) {
		probes.add(probe);

		controller.getLayers()[LayersName.INPUT_LAYER]
				.getChildren()
				.add(probe.getHitBox());
	}

	public boolean remove(Shape o) {
		boolean isRemove = false;

		for (Iterator<Probe> iterator = probes.iterator(); iterator.hasNext(); ) {
			Probe probe = iterator.next();
			ArrayList<Shape> attached = probe.getAttachedShapes();
			isRemove = isRemove || attached.remove(o);

			if (attached.isEmpty()) {
				iterator.remove();
			}

		}
		return isRemove;
	}


}
