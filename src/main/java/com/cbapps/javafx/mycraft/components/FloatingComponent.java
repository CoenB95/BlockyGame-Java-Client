package com.cbapps.javafx.mycraft.components;

import com.cbapps.gamo.components.GameObjectComponentBase;
import com.cbapps.gamo.math.Vector3;

public class FloatingComponent extends GameObjectComponentBase {
	private final double rize;

	public FloatingComponent(double rize) {
		this.rize = rize;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		getParentObject().setPosition(getParentObject().getPosition().add(Vector3.y(rize * elapsedSeconds)));
	}
}
