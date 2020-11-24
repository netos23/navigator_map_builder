package ru.fbtw.navigator.map_builder.canvas.holder;

import javafx.scene.layout.Pane;
import ru.fbtw.navigator.map_builder.canvas.CanvasProperties;
import ru.fbtw.navigator.map_builder.canvas.node.NodeHolderManager;
import ru.fbtw.navigator.map_builder.canvas.probe.Probe;
import ru.fbtw.navigator.map_builder.canvas.probe.ProbeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class HolderManager {
	private LinkedList<Holder> holders;

	private ProbeManager manager;
	private NodeHolderManager nodeManager;
	private CanvasProperties canvasProperties;
	private Pane[] layers;


	public HolderManager(
			ProbeManager manager,
			CanvasProperties canvasProperties,
			NodeHolderManager nodeManager
	) {
		this.manager = manager;
		this.canvasProperties = canvasProperties;
		this.nodeManager = nodeManager;

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


	public Holder selectFirst(double x, double y) {

		Holder target = nodeManager.select(x, y);

		return target == null
				? holders.stream()
					.filter(h -> h.contains(x, y))
					.reduce((f, l) -> l)
					.orElse(null)
				: target;

	}

	@Deprecated
	public ArrayList<Holder> selectAll(Probe probe) {

		return holders.stream()
				.filter(h -> h.isAttachedToProbe(probe))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<Holder> selectAll(double x, double y) {
		Holder target = nodeManager.select(x, y);
		Probe probe = manager.select(x, y);

		return target == null
				? holders.stream()
					.filter(h -> h.contains(x, y) || h.isAttachedToProbe(probe))
					.collect(Collectors.toCollection(ArrayList::new))
				: new ArrayList<>(Collections.singletonList(target));
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


	public ProbeManager getManager() {
		return manager;
	}

	public CanvasProperties getCanvasProperties() {
		return canvasProperties;
	}


	public NodeHolderManager getNodeManager() {
		return nodeManager;
	}
}
