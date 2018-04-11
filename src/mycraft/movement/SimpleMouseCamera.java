package mycraft.movement;

import javafx.scene.Camera;

public class SimpleMouseCamera extends RobotMouseCamera {

	private double hRotation = 0;
	private double vRotation = 0;

	public SimpleMouseCamera(Camera camera) {
		super(camera);
	}

	@Override
	public void updateCamera(double hDelta, double vDelta) {
		hRotation += hDelta;
		vRotation += vDelta;
		verticalTransform.setAngle(vRotation);
		horizontalTransform.setAngle(hRotation);
	}
}
