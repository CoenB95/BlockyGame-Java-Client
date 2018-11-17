package com.cbapps.javafx.mycraft;

import com.cbapps.javafx.gamo.components.SmoothRotationComponent;
import com.cbapps.javafx.gamo.components.SmoothTranslationComponent;
import com.cbapps.javafx.gamo.math.RotationalDelta;
import com.cbapps.javafx.gamo.objects.GameObjectBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;

public class Steve extends GameObjectBase {
	public static final double DEFAULT_SENSITIVITY = 0.2;

	private double horizontalSensitivity;
	private double verticalSensitivity;
	/**
	 * Notifies that the following mouse-movement is the first (after a break).
	 * This means that it is likely that the delta is much larger and therefore should be ignored.
	 */
	private boolean ignoreFirstMovement = false;

	public Steve() {
		this(DEFAULT_SENSITIVITY);

		addComponent(SmoothTranslationComponent.direct());
		addComponent(SmoothRotationComponent.direct());
	}

	public Steve(double sensitivity) {
		this.horizontalSensitivity = sensitivity;
		this.verticalSensitivity = sensitivity;
	}

	public void applyMouseMoved(MouseEvent event) {
		try {
			new Robot().mouseMove(800, 450);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

		if (!ignoreFirstMovement) {
			double horizontalDelta = (event.getScreenX() - 800) * horizontalSensitivity;
			double verticalDelta = -(event.getScreenY() - 450) * verticalSensitivity;
			setTargetRotation(getTargetRotation().add(new RotationalDelta(horizontalDelta, verticalDelta, 0)));
		}
		ignoreFirstMovement = false;
	}

	public void notifyFirstMovement() {
		ignoreFirstMovement = true;
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		super.onUpdate(elapsedSeconds);
	}
}
