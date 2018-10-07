package mycraft.camera;

import javafx.scene.PerspectiveCamera;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;
import mycraft.gameobject.GameObjectBase;
import mycraft.movement.SmoothRotateBehavior;
import mycraft.movement.SmoothTranslateBehavior;

public class Camera extends GameObjectBase {
	public static final double DEFAULT_SENSITIVITY = 0.2;

	private javafx.scene.Camera camera;

	private double horizontalSensitivity;
	private double verticalSensitivity;

	/**
	 * Notifies that the following mouse-movement is the first (after a break).
	 * This means that it is likely that the delta is much larger and therefore should be ignored.
	 */
	private boolean ignoreFirstMovement = false;

	public Camera() {
		this(DEFAULT_SENSITIVITY);
		addComponent(new SmoothRotateBehavior(0.8));
		addComponent(new SmoothTranslateBehavior(0.8));
	}

	public Camera(double sensitivity) {
		horizontalSensitivity = sensitivity;
		verticalSensitivity = sensitivity;
		camera = new PerspectiveCamera(true);
		camera.setNearClip(200);
		camera.setFarClip(9000);

		setTargetPosition(getPosition().withY(400));
		setNode(camera);
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
			setTargetRotation(getTargetRotation().addHorizontal(horizontalDelta).addVertical(verticalDelta));
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
