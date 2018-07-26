package mycraft.movement;

import javafx.scene.Camera;

public class SimpleMouseCamera extends RobotMouseCamera {

	public SimpleMouseCamera(Camera camera) {
		super(camera);
	}

	@Override
	public void onUpdate(double elapsedSeconds) {

	}

	@Override
	public void updateCamera(double horizontalRotation, double verticalRotation) {
		horizontalTransform.setAngle(horizontalRotation);
		verticalTransform.setAngle(verticalRotation);
	}
}
