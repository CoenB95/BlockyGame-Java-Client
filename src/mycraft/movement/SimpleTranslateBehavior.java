package mycraft.movement;

import mycraft.gameobject.GameObjectComponent;

public class SimpleTranslateBehavior extends GameObjectComponent {

	public SimpleTranslateBehavior() {

	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParentObject().setPosition(getParentObject().getTargetPosition());
	}
}
