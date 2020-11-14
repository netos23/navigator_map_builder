package ru.fbtw.navigator.map_builder.canvas.probe;

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

	public Probe select(Vector2 pos){
		return select(pos.getX(),pos.getY());
	}

	public Probe select(double x, double y){
		for (Probe probe : probes) {
			if (probe.isContainsPoint(x, y)) {
				return probe;
			}
		}
		return null;
	}

	public Probe getPosOfExistingPoint(double x, double y) {
		Probe probe = select(x,y);

		return probe == null ? push(x, y) : probe;
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

	private boolean removeFromView(Probe probe){
		return controller.getLayers()[LayersName.INPUT_LAYER]
				.getChildren()
				.remove(probe.getHitBox());
	}

	public boolean remove(Shape o) {
		boolean isRemove = false;
		// todo оптимизировать
		for (Iterator<Probe> iterator = probes.iterator(); iterator.hasNext(); ) {
			Probe probe = iterator.next();
			ArrayList<Shape> attached = probe.getAttachedShapes();
			isRemove =  attached.remove(o) || isRemove;

			if (attached.isEmpty()) {
				removeFromView(probe);
				iterator.remove();
			}

		}
		return isRemove;
	}

	public boolean removeProbeCompletely(Probe o) {
		//boolean isRemove = false;
		// todo: разрулить менджмент холдеров
		removeFromView(o);
		return probes.remove(o);
	}

	public boolean removeEmptyProbe(Probe probe){

		if (probe.getAttachedShapes().isEmpty()) {
			removeFromView(probe);
			return probes.remove(probe);
		}
		return false;
	}


}
