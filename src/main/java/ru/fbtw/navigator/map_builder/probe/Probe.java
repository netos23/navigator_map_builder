package ru.fbtw.navigator.map_builder.probe;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

import static ru.fbtw.navigator.map_builder.utils.MathUtils.sqr;

public class Probe {
	public static final double VISUAL_RADIUS = 10;


	private Circle hitBox;
	private ArrayList<Shape> attachedShapes;
	private RadialGradient gradient;



	public Probe(double x, double y){

		// Setup visible gradient color
		Stop[] stops = new Stop[]{
				new Stop(0, Color.GREY),
				new Stop(1,Color.BLACK)
		};
		gradient = new RadialGradient(0,0,
				0.5,0.5,0.8,true, CycleMethod.NO_CYCLE,stops);


		hitBox = new Circle(x,y,VISUAL_RADIUS, Color.TRANSPARENT);

		// Hide the shape by default
		hitBox.setStroke(Color.TRANSPARENT);

		// Show on Mouse entered and hide on mouse exit
		hitBox.setOnMouseEntered(event -> hitBox.setFill(gradient));
		hitBox.setOnMouseExited(event -> hitBox.setFill(Color.TRANSPARENT));

		attachedShapes = new ArrayList<>();
	}


	@Override
	public boolean equals(Object obj) {
		try {
			Probe other = (Probe) obj;
			return other.getHitBox().getCenterX() == this.getHitBox().getCenterX()
					&& other.getHitBox().getCenterY() == this.getHitBox().getCenterY();
		}catch (Exception ex){
			return false;
		}
	}

	public boolean isContainsPoint(double x, double y){
		//return sqr(x - hitBox.getCenterX()) + sqr(y - hitBox.getCenterY()) <= sqr(VISUAL_RADIUS);
		return hitBox.contains(x,y);
	}


	public Circle getHitBox() {
		return hitBox;
	}

	public void setHitBox(Circle hitBox) {
		this.hitBox = hitBox;
	}

	public void setPosition(double x, double y){
		hitBox.setCenterX(x);
		hitBox.setCenterY(y);
	}

	public ArrayList<Shape> getAttachedShapes() {
		return attachedShapes;
	}


	public double getX(){
		return hitBox.getCenterX();
	}

	public double getY(){
		return hitBox.getCenterY();
	}
}

