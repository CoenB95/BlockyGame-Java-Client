package mycraft.movement;

import mycraft.gameobject.GameObjectComponent;

public class SmoothRotateBehavior extends GameObjectComponent {


	private double snappyness;

	public SmoothRotateBehavior(double snappyness) {
		this.snappyness = snappyness;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParentObject().setRotation(getParentObject().getRotation().multiply(snappyness).add(
				getParentObject().getTargetRotation().multiply(1.0 - snappyness)));
	}
}
