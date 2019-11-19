package com.cbapps.javafx.mycraft.components;

import com.cbapps.javafx.gamo.components.GameObjectComponentBase;

public class FloatingComponent extends GameObjectComponentBase {
	private double rize;
	private double value;

	public FloatingComponent(double rize) {
		this.rize = rize;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		value += rize * elapsedSeconds;
		getParentObject().setPosition(getParentObject().getPosition().addY(value).asPosition());
	}
}
