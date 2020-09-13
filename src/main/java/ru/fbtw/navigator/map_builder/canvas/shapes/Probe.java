package ru.fbtw.navigator.map_builder.canvas.shapes;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import ru.fbtw.navigator.map_builder.utils.Vector2;

import java.awt.event.MouseEvent;

public class Probe {
	public static final double VISUAL_RADIUS = 5;


	private Circle hitBox;
	private RadialGradient gradient;



	public Probe(double x, double y){
		Stop[] stops = new Stop[]{
				new Stop(0, Color.GREY),
				new Stop(1,Color.BLACK)
		};
		gradient = new RadialGradient(0,0,
				0.5,0.5,0.8,true, CycleMethod.NO_CYCLE,stops);

		hitBox = new Circle(x,y,VISUAL_RADIUS, Color.TRANSPARENT);
		hitBox.setStroke(Color.TRANSPARENT);
		hitBox.setOnMouseEntered(event -> hitBox.setFill(gradient));
		hitBox.setOnMouseExited(event -> hitBox.setFill(Color.TRANSPARENT));

		hitBox.setOnMousePressed(event -> {

			//todo: скрещивание
		});
	}


	public Circle getHitBox() {
		return hitBox;
	}

	public void setHitBox(Circle hitBox) {
		this.hitBox = hitBox;
	}
}

