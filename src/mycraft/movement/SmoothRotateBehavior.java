package mycraft.movement;

import gamo.components.GameObjectComponent;
import gamo.math.Rotation;
import gamo.objects.GameObject;

public class SmoothRotateBehavior extends GameObjectComponent {

	private GameObject subject;
	private double snappyness;

	public SmoothRotateBehavior(GameObject subject, double snappyness) {
		this.snappyness = snappyness;
		this.subject = subject;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		Rotation r1 = getParentObject().getRotation();
		Rotation r2 = subject.getRotation();
		double deltaHor = r1.smallestHorizontalDeltaTo(r2) * (1.0 - snappyness);
		double deltaVer = r1.smallestVerticalDeltaTo(r2) * (1.0 - snappyness);
		double deltaRol = r1.smallestRollDeltaTo(r2) * (1.0 - snappyness);
		getParentObject().setTargetRotation(getParentObject().getRotation().add(
				deltaHor, deltaVer, deltaRol));
		//getParentObject().setTargetRotation(getParentObject().getRotation().multiply(snappyness).add(
		//		subject.getRotation().multiply(1.0 - snappyness)));
	}
}
