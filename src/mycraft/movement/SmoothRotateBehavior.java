package mycraft.movement;

import mycraft.gameobject.GameObjectComponent;

public class SmoothRotateBehavior extends GameObjectComponent {


	private double snappyness;

	public SmoothRotateBehavior(double snappyness) {
		this.snappyness = snappyness;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParent().setRotation(getParent().getRotation().multiply(snappyness).add(
				getParent().getTargetRotation().multiply(1.0 - snappyness)));
	}
}
