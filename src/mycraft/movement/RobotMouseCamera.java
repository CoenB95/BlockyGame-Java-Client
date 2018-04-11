package mycraft.movement;

import javafx.application.Platform;
import javafx.scene.Camera;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public abstract class RobotMouseCamera extends MouseCamera {

	private boolean ignoreRobot = false;

	public RobotMouseCamera(Camera camera) {
		super(camera);
	}

	@Override
	public void applyMouseMoved(MouseEvent event) {
		System.out.print("Event: " + event);
		if (ignoreRobot) {
			ignoreRobot = false;
			System.out.println("Ignore robot's mouse movement");
		} else {
			System.out.println("Apply user's mouse movement");
			super.applyMouseMoved(event);
//			ignoreRobot = true;
			Platform.runLater(() -> {
				try {
//					ignoreRobot = true;
					new Robot().mouseMove(800, 450);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			});
		}
	}
}
