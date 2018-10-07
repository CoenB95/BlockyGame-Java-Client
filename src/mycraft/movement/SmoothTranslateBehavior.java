package mycraft.movement;

import mycraft.gameobject.GameObjectComponent;

public class SmoothTranslateBehavior extends GameObjectComponent {

	private double snappyness;

	public SmoothTranslateBehavior(double snappyness) {
		this.snappyness = snappyness;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParent().setPosition(getParent().getPosition().multiply(snappyness).add(
				getParent().getTargetPosition().multiply(1.0 - snappyness)));
	}
}
