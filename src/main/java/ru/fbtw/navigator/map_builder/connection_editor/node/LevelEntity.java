package ru.fbtw.navigator.map_builder.connection_editor.node;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import ru.fbtw.navigator.map_builder.connection_editor.LayersName;
import ru.fbtw.navigator.map_builder.core.navigation.LevelNode;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.util.ArrayList;

public class LevelEntity {
	private static final double SOCKET_RADIUS = 30.0;
	private static final double SINGLTONE_RAIUS = 50.0;
	private static final double OFFSET = 10.0;
	private Circle bg;
	private ArrayList<Circle> sockets;
	private ArrayList<String> socketsNames;
	private LevelNode node;

	private boolean isSingleton;

	private Vector2 origin;
	private Vector2 beginPosition;
	private Vector2[] socketsPositions;
	private double r;

	public LevelEntity(LevelNode node) {
		this.node = node;
		sockets = new ArrayList<>();
		socketsNames = new ArrayList<>(node.getSockets().keySet());

		initSockets(socketsNames);
	}


	public LevelEntity(double x, double y, LevelNode node) {
		this(node);
		setPosition(x, y);
	}

	private void initSockets(ArrayList<String> strings) {
		if (strings.size() == 1) {
			initSingleton(strings.iterator().next());
		} else if (strings.size() == 0) {
			initNoneSocket();
		} else {
			initMultiSockets(strings);
		}
	}

	private void initMultiSockets(ArrayList<String> strings) {
		final Paint socketColor = Color.GREEN;
		final Paint bgColor = Color.CORAL;

		double angle = Math.PI * 2.0 / strings.size();
		double side = 2 * SOCKET_RADIUS + OFFSET;
		r = side / (2 * Math.sin(angle / 2));

		bg = new Circle(r);
		bg.setFill(bgColor);

		for (int i = 0; i < strings.size(); i++) {
			double tmpAngle = angle * i;
			double x = r * Math.cos(tmpAngle);
			double y = r * Math.sin(tmpAngle);

			Circle socket = new Circle(x, y, SOCKET_RADIUS, socketColor);
			sockets.add(socket);
			setToolTip(socket,strings.get(i));
		}

	}

	private void initNoneSocket() {
		final Paint noneSocketColor = Color.GREY;
		r = SINGLTONE_RAIUS;
		isSingleton = true;
		bg = new Circle(r);
		bg.setFill(noneSocketColor);
	}

	private void initSingleton(String name) {
		final Paint socketColor = Color.GREEN;

		r = SINGLTONE_RAIUS;
		isSingleton = true;
		bg = new Circle(r);
		bg.setFill(socketColor);

		sockets.add(bg);
		setToolTip(bg,name);
	}

	public void setPosition(double x, double y) {
		beginReplace(bg.getCenterX(),bg.getCenterY());
		endReplace(x, y);
	}

	public void beginReplace(double x, double y) {
		origin = new Vector2(x, y);
		beginPosition = new Vector2(bg.getCenterX(), bg.getCenterY());
		socketsPositions = new Vector2[sockets.size()];

		for (int i = 0; i < sockets.size(); i++) {
			Circle socket = sockets.get(i);
			socketsPositions[i] = new Vector2(socket.getCenterX(), socket.getCenterY());
		}
	}

	public void replace(double x, double y) {
		Vector2 currentPosition = new Vector2(x, y);
		Vector2 delta = origin.subtract(currentPosition);

		moveCircle(bg, delta, beginPosition);

		for (int i = 0; i < sockets.size(); i++) {
			Circle socket = sockets.get(i);
			moveCircle(socket, delta, socketsPositions[i]);
		}
	}

	private void moveCircle(Circle circle, Vector2 delta, Vector2 beginPosition) {
		circle.setCenterX(beginPosition.getX() + delta.getX());
		circle.setCenterY(beginPosition.getY() + delta.getY());
	}

	private void setToolTip(Circle socket, String name) {
		Tooltip tooltip = new Tooltip(name);
		socket.setOnMouseEntered(e ->
				tooltip.show(socket, 0, 0));

		socket.setOnMouseExited(e ->
				tooltip.hide());
	}

	public void endReplace(double x, double y) {
		replace(x, y);
	}

	public boolean contains(double x, double y) {
		if (isSingleton) {
			return bg.contains(x, y);
		} else {
			return bg.contains(x, y) || sockets.stream().anyMatch(e -> e.contains(x, y));
		}
	}

	public int socketContains(double x, double y) {
		for (int i = 0; i < sockets.size(); i++) {
			Circle socket = sockets.get(i);

			if (socket.contains(x, y)) {
				return i;
			}
		}

		return -1;
	}


	public void splitLayers(Pane[] layers) {
		layers[LayersName.MAIN].getChildren()
				.add(bg);

		layers[LayersName.INPUT].getChildren()
				.addAll(sockets);
	}


	public double getR() {
		return r;
	}

	public double getX() {
		return bg.getCenterX();
	}
	public double getY() {
		return bg.getCenterY();
	}
}
