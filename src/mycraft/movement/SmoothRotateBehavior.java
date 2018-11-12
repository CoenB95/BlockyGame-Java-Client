package mycraft.movement;

import mycraft.gameobject.GameObject;
import mycraft.gameobject.GameObjectComponent;

public class SmoothRotateBehavior extends GameObjectComponent {

	private GameObject subject;
	private double snappyness;

	public SmoothRotateBehavior(GameObject subject, double snappyness) {
		this.snappyness = snappyness;
		this.subject = subject;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParentObject().setTargetRotation(getParentObject().getRotation().multiply(snappyness).add(
				subject.getRotation().multiply(1.0 - snappyness)));
	}
}
