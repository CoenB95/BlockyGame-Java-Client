package com.cbapps.javafx.mycraft.components;

import com.cbapps.javafx.gamo.components.GameObjectComponent;

public class FloatingComponent extends GameObjectComponent {
	private double rize;
	private double value;

	public FloatingComponent(double rize) {
		this.rize = rize;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		value += rize * elapsedSeconds;
		getParentObject().setTargetPosition(getParentObject().getTargetPosition().withY(value));
	}
}
