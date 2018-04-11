package mycraft.movement;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public abstract class MouseCamera {
	public static final double DEFAULT_SENSITIVITY = 0.2;

	protected Camera camera;
	protected Rotate horizontalTransform;
	protected Rotate verticalTransform;

	private double horizontalRotation;
	private double horizontalSensitivity = DEFAULT_SENSITIVITY;
	private double verticalRotation;
	private double verticalSensitivity = DEFAULT_SENSITIVITY;
	private boolean reset = false;

	public MouseCamera(Camera camera) {
		this.camera = camera;
		horizontalTransform = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
		verticalTransform = new Rotate(0, 0, 0, 0, new Point3D(1, 0, 0));
		camera.getTransforms().addAll(horizontalTransform, verticalTransform);
	}

	public void applyMouseMoved(MouseEvent event) {
		if (reset) {
			horizontalRotation = 0;
			verticalRotation = 0;
		}

		double newHorizontalRotation = (event.getScreenY() * horizontalSensitivity);
		double newVerticalRotation = (event.getScreenX() * verticalSensitivity);
		double horizontalDelta = newHorizontalRotation - horizontalRotation;
		double verticalDelta = newVerticalRotation - verticalRotation;
		horizontalRotation = newHorizontalRotation;
		verticalRotation = newVerticalRotation;

		System.out.format("Apply delta hor=%+.1f, ver=%+.1f, reset=%b\n", reset ? 0 : horizontalDelta, reset ? 0 : verticalDelta, reset);
		updateCamera(reset ? 0 : horizontalDelta, reset ? 0 : verticalDelta);
		reset = false;
	}

	public void resetDelta() {
		reset = true;
	}

	public abstract void updateCamera(double horizontalDelta, double verticalDelta);
}
