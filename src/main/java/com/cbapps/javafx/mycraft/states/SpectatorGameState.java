package com.cbapps.javafx.mycraft.states;

import com.cbapps.gamo.components.FollowComponent;
import com.cbapps.gamo.components.SmoothRotationComponent;
import com.cbapps.gamo.components.SmoothTranslationComponent;
import com.cbapps.gamo.math.Vector3;
import com.cbapps.gamo.objects.GameObject;
import com.cbapps.gamo.state.IGameScene;
import com.cbapps.javafx.mycraft.objects.World;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.prefs.Preferences;

public class SpectatorGameState extends LookGameState {
	private final KeyCode forwardKey;
	private final KeyCode leftKey;
	private final KeyCode backwardKey;
	private final KeyCode rightKey;
	private final KeyCode jumpKey;
	private final KeyCode sneakKey;

	private final World world;
	private final GameObject steve;

	public SpectatorGameState(World world, GameObject steve) {
		super(world, steve);

		this.world = world;
		this.steve = steve;

		Preferences preferences = Preferences.userRoot();
		this.forwardKey = KeyCode.getKeyCode(preferences.get("craftfx.key.walk-forwards", KeyCode.W.getName()));
		this.backwardKey = KeyCode.getKeyCode(preferences.get("craftfx.key.walk-backwards", KeyCode.S.getName()));
		this.leftKey = KeyCode.getKeyCode(preferences.get("craftfx.key.strafe-left", KeyCode.A.getName()));
		this.rightKey = KeyCode.getKeyCode(preferences.get("craftfx.key.strafe-right", KeyCode.D.getName()));
		this.jumpKey = KeyCode.getKeyCode(preferences.get("craftfx.key.jump", KeyCode.SPACE.getName()));
		this.sneakKey = KeyCode.getKeyCode(preferences.get("craftfx.key.sneak", KeyCode.SHIFT.getName()));
	}

	@Override
	public void onStart(IGameScene scene) {
		super.onStart(scene);

		setGameMode("Spectator");
	}

	@Override
	public void onResume() {
		super.onResume();

		var camera = getScene().getCamera();
		camera.addComponent(FollowComponent.rotatingAndTranslating(steve));
		camera.addComponent(new SmoothRotationComponent(0.05));
		camera.addComponent(new SmoothTranslationComponent(0.95));
	}

	@Override
	public void onPause() {
		super.onPause();

		getScene().getCamera().removeAllComponents();
	}

	@Override
	public void onKeyPressed(KeyEvent event) {
		super.onKeyPressed(event);

		if (event.getCode() == KeyCode.ESCAPE) {
			getScene().pushState(new MenuGameState());
		} else if (event.getCode() == KeyCode.G) {
			var wasDebugShown = isDebugShown();
			var newGameMode = new SurvivalGameState(world, steve);
			getScene().popState();
			getScene().pushState(newGameMode);
			newGameMode.showDebug(wasDebugShown);
		}
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		super.onUpdate(elapsedSeconds);

		double walkAngle = 0;
		boolean walk = true;
		boolean jump = isKeyPressed(jumpKey);
		boolean sneak = isKeyPressed(sneakKey);

		if (isKeyPressed(forwardKey))
			walkAngle = isKeyPressed(leftKey) ? -45 : isKeyPressed(rightKey) ? 45 : 0;
		else if (isKeyPressed(leftKey))
			walkAngle = isKeyPressed(forwardKey) ? -45 : isKeyPressed(backwardKey) ? -135 : -90;
		else if (isKeyPressed(rightKey))
			walkAngle = isKeyPressed(forwardKey) ? 45 : isKeyPressed(backwardKey) ? 135 : 90;
		else if (isKeyPressed(backwardKey))
			walkAngle = isKeyPressed(leftKey) ? -135 : isKeyPressed(rightKey) ? 135 : 180;
		else
			walk = false;

		walkAngle = getPlayerRotationHorizontal() + walkAngle - 90;
		if (walk || jump || sneak) {

			Vector3 targetDelta = Vector3.identity();
			if (walk) {
				var t = new Vector3(Math.cos(Math.toRadians(walkAngle)) * .20, 0, Math.sin(Math.toRadians(walkAngle)) * .20);
				targetDelta = targetDelta.add(t);
			}
			if (jump)
				targetDelta = targetDelta.add(new Vector3(0, .20, 0));
			if (sneak)
				targetDelta = targetDelta.add(new Vector3(0, -.20, 0));

			steve.setPosition(steve.getPosition()
					.add(targetDelta));
		}
	}
}
