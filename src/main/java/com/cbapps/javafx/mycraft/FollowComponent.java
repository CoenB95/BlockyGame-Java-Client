package com.cbapps.javafx.mycraft;

import com.cbapps.javafx.gamo.components.GameObjectComponent;
import com.cbapps.javafx.gamo.math.Position;
import com.cbapps.javafx.gamo.objects.GameObject;

public class FollowComponent extends GameObjectComponent {
	private GameObject objectToFollow;
	private Position offsetFromObject;

	public FollowComponent(GameObject objectToFollow, Position offsetFromObject) {
		this.objectToFollow = objectToFollow;
		this.offsetFromObject = offsetFromObject;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParentObject().setTargetPosition(objectToFollow.getPosition().add(offsetFromObject).withY(getParentObject().getTargetPosition().getY()));
	}
}
