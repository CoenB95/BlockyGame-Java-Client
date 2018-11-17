package com.cbapps.javafx.mycraft;

import javafx.animation.Transition;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Rotate3DTransition extends Transition {

	private Node node;
	private Rotate rotateX;
	private Rotate rotateY;
	private Rotate rotateZ;

	private double deltaX = 0;
	private double deltaY = 0;
	private double deltaZ = 0;
	private double rotateFromX = 0;
	private double rotateFromY = 0;
	private double rotateFromZ = 0;
	private double rotateStartX = 0;
	private double rotateStartY = 0;
	private double rotateStartZ = 0;
	private double rotateToX = 0;
	private double rotateToY = 0;
	private double rotateToZ = 0;

	public Rotate3DTransition(Duration duration, Node node) {
		this.node = node;
		this.rotateX = new Rotate(0,  0, 0, 0, new Point3D(1, 0, 0));
		this.rotateY = new Rotate(0,  0, 0, 0, new Point3D(0, 1, 0));
		this.rotateZ = new Rotate(0,  0, 0, 0, new Point3D(0, 0, 1));
		node.getTransforms().addAll(rotateZ, rotateY, rotateX);
		setCycleDuration(duration);
	}

	public void setToRotation(double valueX, double valueY, double valueZ) {
		rotateToX = valueX;
		rotateToY = valueY;
		rotateToZ = valueZ;
	}

	@Override
	protected void interpolate(double frac) {
		rotateX.setAngle(rotateStartX + frac * deltaX);
		rotateY.setAngle(rotateStartY + frac * deltaY);
		rotateZ.setAngle(rotateStartZ + frac * deltaZ);
	}

	@Override
	public void play() {
		if (getStatus() == Status.STOPPED) {
//			rotateStartX = Double.isNaN(rotateFromX) ? rotateX.getAngle() : rotateFromX;
//			rotateStartY = Double.isNaN(rotateFromY) ? rotateY.getAngle() : rotateFromY;
//			rotateStartZ = Double.isNaN(rotateFromZ) ? rotateZ.getAngle() : rotateFromZ;
			rotateStartX = rotateX.getAngle();
			rotateStartY = rotateY.getAngle();
			rotateStartZ = rotateZ.getAngle();
			deltaX = rotateToX - rotateStartX;
			deltaY = rotateToY - rotateStartY;
			deltaZ = rotateToZ - rotateStartZ;
		}
		super.play();
	}
}
