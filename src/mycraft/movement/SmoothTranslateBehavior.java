package mycraft.movement;

import mycraft.gameobject.GameObjectComponent;

public class SmoothTranslateBehavior extends GameObjectComponent {

	private double snappyness;

	public SmoothTranslateBehavior(double snappyness) {
		this.snappyness = snappyness;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParentObject().setPosition(getParentObject().getPosition().multiply(snappyness).add(
				getParentObject().getTargetPosition().multiply(1.0 - snappyness)));
	}
}
