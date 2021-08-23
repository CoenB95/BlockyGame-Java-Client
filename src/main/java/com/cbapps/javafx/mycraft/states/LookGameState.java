package com.cbapps.javafx.mycraft.states;

import com.cbapps.gamo.math.Quaternion;
import com.cbapps.gamo.math.Vector3;
import com.cbapps.gamo.objects.GameObject;
import com.cbapps.javafx.mycraft.objects.World;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;

public class LookGameState extends DebugGameState {
	private final Robot robot;
	private final GameObject steve;

	private boolean didRobot = false;
	private MouseEvent lastMouse;
	private double playerRotationHorizontal = 0;
	private double playerRotationVertical = 0;

	public LookGameState(World world, GameObject steve) {
		super(world, steve);

		this.steve = steve;
		this.robot = new Robot();
	}

	@Override
	public void onResume() {
		super.onResume();

		didRobot = false;
		lastMouse = null;

		var vec = steve.getOrientation().multiply(Vector3.unitX());
//		playerRotationHorizontal = Math.toDegrees(-Math.acos(vec.x()));
//		playerRotationHorizontal = vec.pitch() - vec.yaw() * 2;
//		playerRotationVertical = vec.roll();
		System.out.format("Start Angles: x=%.1f hor=%.1f ver=%.1f%n", vec.x(), playerRotationHorizontal, playerRotationVertical);
	}

	@Override
	public void onMouseMove3D(MouseEvent event) {
		super.onMouseMove3D(event);

		// Handle look-around
		if (didRobot) {
			didRobot = false;
		} else if (lastMouse != null) {
			var diffX = event.getX() - lastMouse.getX();
			var diffY = event.getY() - lastMouse.getY();
			playerRotationHorizontal += diffX * 0.2;
			playerRotationVertical -= diffY * 0.2;

			steve.setOrientation(Quaternion.identity()
					.multiply(Quaternion.of(Vector3.unitY(), playerRotationHorizontal))
					.multiply(Quaternion.of(Vector3.unitX(), playerRotationVertical)));

			robot.mouseMove(lastMouse.getScreenX(), lastMouse.getScreenY());
			didRobot = true;
		}

		lastMouse = event;

//		PickResult pickResult = event.getPickResult();
//		if (!(pickResult.getIntersectedNode() instanceof ChunkView))
//			return;
//		int blockNr = ((ChunkView) pickResult.getIntersectedNode()).getChunk()
//				.findBlockByFace(pickResult.getIntersectedFace());
//		((ChunkView) pickResult.getIntersectedNode()).getChunk().markBlock(blockNr);
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		super.onUpdate(elapsedSeconds);

//		var vec = steve.getOrientation().toEulerAngles();
//		System.out.format("Angles: r=%.1f p=%.1f y=%.1f%n", vec.roll(), vec.pitch(), vec.yaw());
	}

	public double getPlayerRotationHorizontal() {
		return playerRotationHorizontal;
	}
}
