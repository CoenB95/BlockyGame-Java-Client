package mycraft.movement;

import javafx.scene.Camera;

public class SmoothMouseCamera extends RobotMouseCamera {

	private double currentHorizontalRotation;
	private double currentVerticalRotation;
	private double targetHorizontalRotation;
	private double targetVerticalRotation;

	public SmoothMouseCamera(Camera camera) {
		super(camera);
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		double snappyness = 0.95;
		double newHorizontalRotation = snappyness * currentHorizontalRotation + (1.0 - snappyness) * targetHorizontalRotation;
		double newVerticalRotation = snappyness * currentVerticalRotation + (1.0 - snappyness) * targetVerticalRotation;
		horizontalTransform.setAngle(newHorizontalRotation);
		verticalTransform.setAngle(newVerticalRotation);
		currentHorizontalRotation = newHorizontalRotation;
		currentVerticalRotation = newVerticalRotation;
	}

	@Override
	public void updateCamera(double horizontalRotation, double verticalRotation) {
		targetHorizontalRotation = horizontalRotation;
		targetVerticalRotation = verticalRotation;
	}
}
