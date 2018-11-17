package com.cbapps.javafx.mycraft.movement;

import com.cbapps.javafx.gamo.components.GameObjectComponent;
import com.cbapps.javafx.gamo.objects.GameObject;

public class SmoothTranslateBehavior extends GameObjectComponent {

	private GameObject subject;
	private double snappyness;

	public SmoothTranslateBehavior(GameObject subject, double snappyness) {
		this.snappyness = snappyness;
		this.subject = subject;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParentObject().setTargetPosition(getParentObject().getPosition().multiply(snappyness).add(
				subject.getPosition().multiply(1.0 - snappyness)));
	}
}
