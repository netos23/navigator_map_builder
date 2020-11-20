package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class HolderManager {
	private LinkedList<Holder> holders;

	private ProbeManager manager;
	private CanvasProperties canvasProperties;
	private Pane[] layers;


	public HolderManager(ProbeManager manager, CanvasProperties canvasProperties) {
		this.manager = manager;
		this.canvasProperties = canvasProperties;

		holders = new LinkedList<>();
		layers = canvasProperties.getSource().getLayers();
	}

	public boolean push(Holder holder) {
		if (!holders.contains(holder)) {
			holder.splitLayers(layers);
			holder.extractProbes(manager);
			holders.add(holder);
			return true;
		} else {
			return false;
		}
	}

	public Holder selectFirst(Probe probe) {

		return holders.stream()
				.filter(h -> h.isAttachedToProbe(probe))
				.findFirst()
				.orElse(null);

	}


	public Holder selectFirst(double x, double y) {

		return holders.stream()
				.filter(h -> h.contains(x, y))
				.reduce((f, l) -> l)
				.orElse(null);

	}

	public ArrayList<Holder> selectAll(Probe probe) {

		return holders.stream()
				.filter(h -> h.isAttachedToProbe(probe))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<Holder> selectAll(double x, double y) {

		return holders.stream()
				.filter(h -> h.contains(x, y))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<Holder> selectAllByInsideContains(double x, double y) {

		return holders.stream()
				.filter(h -> h.containsInner(x, y))
				.collect(Collectors.toCollection(ArrayList::new));
	}


	public void remove(Holder holder) {
		manager.remove(holder.getShape());
		holders.remove(holder);

		holder.remove(layers);
		// разрулить историю
	}

	public boolean remove(double x, double y) {
		Holder tmp = selectFirst(x, y);

		if (tmp != null) {
			remove(tmp);
			return true;
		} else {
			return false;
		}
	}


	public void rebuildProbes(Holder h) {

	}

	public ProbeManager getManager() {
		return manager;
	}

	public CanvasProperties getCanvasProperties() {
		return canvasProperties;
	}
}
