package mycraft.movement;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

public abstract class MouseCamera {
	public static final double DEFAULT_SENSITIVITY = 0.2;

	protected Camera camera;
	protected Rotate horizontalTransform;
	protected Rotate verticalTransform;

	private double horizontalRotation;
	private double horizontalSensitivity = DEFAULT_SENSITIVITY;
	private double verticalRotation;
	private double verticalSensitivity = DEFAULT_SENSITIVITY;

	/**
	 * Notifies that the following mouse-movement is the first (after a break).
	 * This means that it is likely that the delta is much larger and therefore should be ignored.
	 */
	private boolean firstMovement = false;

	public MouseCamera(Camera camera) {
		this.camera = camera;
		horizontalTransform = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
		verticalTransform = new Rotate(0, 0, 0, 0, new Point3D(1, 0, 0));
		camera.getTransforms().addAll(horizontalTransform, verticalTransform);
	}

	public void applyMouseMoved(MouseEvent event) {
		if (!firstMovement) {
			double horizontalDelta = (event.getScreenX() - 800) * horizontalSensitivity;
			double verticalDelta = -(event.getScreenY() - 450) * verticalSensitivity;
			horizontalRotation += horizontalDelta;
			verticalRotation += verticalDelta;
		}

		System.out.format("Apply delta hor=%+.1f, ver=%+.1f, firstMovement=%b\n", firstMovement ? 0 : horizontalRotation, firstMovement ? 0 : verticalRotation, firstMovement);
		updateCamera(horizontalRotation, verticalRotation);
		firstMovement = false;
	}

	public void notifyFirstMovement() {
		firstMovement = true;
	}

	public abstract void onUpdate(double elapsedSeconds);

	public abstract void updateCamera(double horizontalRotation, double verticalRotation);
}
