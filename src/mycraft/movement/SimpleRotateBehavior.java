package mycraft.movement;

import mycraft.gameobject.GameObjectComponent;

public class SimpleRotateBehavior extends GameObjectComponent {

	public SimpleRotateBehavior() {

	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParent().setRotation(getParent().getTargetRotation());
	}
}
