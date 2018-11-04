package mycraft;

import mycraft.gameobject.GameObject;
import mycraft.gameobject.GameObjectComponent;
import mycraft.math.Position;

public class FollowComponent extends GameObjectComponent {
	private GameObject objectToFollow;
	private Position offsetFromObject;

	public FollowComponent(GameObject objectToFollow, Position offsetFromObject) {
		this.objectToFollow = objectToFollow;
		this.offsetFromObject = offsetFromObject;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParentObject().setPosition(objectToFollow.getPosition().withY(getParentObject().getPosition().getY()).add(offsetFromObject));
	}
}
