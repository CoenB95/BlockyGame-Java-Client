package com.cbapps.javafx.mycraft.components;

import com.cbapps.javafx.gamo.components.GameObjectComponent;
import com.cbapps.javafx.gamo.objects.GameVector;

public class FloatingComponent implements GameObjectComponent {
	private double rize;
	private double value;

	public FloatingComponent(double rize) {
		this.rize = rize;
	}

	@Override
	public GameVector onUpdate(double elapsedSeconds, GameVector target) {
		value += rize * elapsedSeconds;
		return target.withPosition(target.getPosition().addY(value).asPosition());
	}
}
