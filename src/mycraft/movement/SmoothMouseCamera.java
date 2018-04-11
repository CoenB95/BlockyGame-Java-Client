package mycraft.movement;

import javafx.animation.Interpolator;
import javafx.scene.Camera;
import javafx.util.Duration;
import mycraft.Rotate3DTransition;

public class SmoothMouseCamera extends mycraft.movement.RobotMouseCamera {

	private Rotate3DTransition ranim;
	private double horizontalRotation;
	private double verticalRotation;

	public SmoothMouseCamera(Camera camera) {
		super(camera);
		ranim = new Rotate3DTransition(Duration.millis(10), camera);
		ranim.setInterpolator(Interpolator.LINEAR);
	}

	@Override
	public void updateCamera(double horizontalDelta, double verticalDelta) {
		horizontalRotation += horizontalDelta;
		verticalRotation += verticalDelta;
		ranim.setToRotation(verticalRotation, horizontalRotation, 0);
		ranim.playFromStart();
	}
}
