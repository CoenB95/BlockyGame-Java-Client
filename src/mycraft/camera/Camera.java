package mycraft.camera;

import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import mycraft.gameobject.GameObject;
import mycraft.movement.SimpleTranslateBehavior;
import mycraft.movement.SmoothRotateBehavior;

import java.awt.*;

public class Camera extends GameObject {
	public static final double DEFAULT_SENSITIVITY = 0.2;

	private javafx.scene.Camera camera;
	private Rotate horizontalRotate;
	private Rotate verticalRotate;

	private double horizontalSensitivity;
	private double verticalSensitivity;

	/**
	 * Notifies that the following mouse-movement is the first (after a break).
	 * This means that it is likely that the delta is much larger and therefore should be ignored.
	 */
	private boolean ignoreFirstMovement = false;
	private boolean ignoreRobot = false;

	public Camera(Scene scene) {
		this(scene, DEFAULT_SENSITIVITY);
		addComponent(new SmoothRotateBehavior(0.95));
		addComponent(new SimpleTranslateBehavior());
	}

	public Camera(Scene scene, double sensitivity) {
		horizontalSensitivity = sensitivity;
		verticalSensitivity = sensitivity;
		horizontalRotate = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
		verticalRotate = new Rotate(0, 0, 0, 0, new Point3D(1, 0, 0));
		Translate translation = new Translate(0, 0, 0);
		camera = new PerspectiveCamera(true);
		camera.setNearClip(200);
		camera.setFarClip(9000);
		camera.getTransforms().addAll(
				translation,
				horizontalRotate,
				verticalRotate
		);
		scene.setCamera(camera);
		setTargetPosition(getPosition().withY(400));
	}

	public void applyMouseMoved(MouseEvent event) {
		if (ignoreRobot) {
			ignoreRobot = false;
			System.out.println("Ignore robot's mouse movement");
			return;
		}
		System.out.println("Apply user's mouse movement");
		ignoreRobot = true;
		Platform.runLater(() -> {
			try {
				new Robot().mouseMove(800, 450);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		});

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
		camera.setTranslateX(getPosition().getX());
		camera.setTranslateY(-getPosition().getY());
		camera.setTranslateZ(getPosition().getZ());
		horizontalRotate.setAngle(getRotation().getHorizontal());
		verticalRotate.setAngle(getRotation().getVertical());
	}
}
