package com.cbapps.javafx.mycraft.states;

import com.cbapps.gamo.components.FollowComponent;
import com.cbapps.gamo.components.SmoothRotationComponent;
import com.cbapps.gamo.components.SmoothTranslationComponent;
import com.cbapps.gamo.javafx.GroupObject;
import com.cbapps.gamo.math.Vector3;
import com.cbapps.gamo.objects.AmbientLight;
import com.cbapps.gamo.objects.GameObject;
import com.cbapps.gamo.state.GameStateBase;
import com.cbapps.gamo.state.IGameScene;
import com.cbapps.javafx.mycraft.objects.World;

import java.util.concurrent.CompletableFuture;

public class MainGameState extends GameStateBase {
	private static final int WORLD_DEPTH = 4;
	private static final int WORLD_HEIGHT = 4;
	private static final int WORLD_WIDTH = 4;
	private static final int CHUNK_DEPTH = 16;
	private static final int CHUNK_HEIGHT = 16;
	private static final int CHUNK_WIDTH = 16;
	private static final float BLOCK_DEPTH = 1.0f;
	private static final float BLOCK_HEIGHT = 1.0f;
	private static final float BLOCK_WIDTH = 1.0f;

	private CompletableFuture<Void> builderFuture;
	private AmbientLight light;
	private World world;
	private GameObject steve;

	public MainGameState() {

	}

	@Override
	public void onStart(IGameScene scene) {
		super.onStart(scene);

		steve = new GroupObject();
		steve.setPosition(new Vector3(48, 16, 165));

		var camera = scene.getCamera();
		camera.setPosition(steve.getPosition());
		camera.setClip(0.1, 9000);
		camera.setFieldOfView(60);

		light = new AmbientLight();

		world = new World(
				WORLD_WIDTH, WORLD_HEIGHT, WORLD_DEPTH,
				CHUNK_WIDTH, CHUNK_HEIGHT, CHUNK_DEPTH,
				BLOCK_WIDTH, BLOCK_HEIGHT, BLOCK_DEPTH);

		builderFuture = world.generateAsync(1234);

		scene.get3D().addObjects(light, world, steve);
		scene.pushState(new SpectatorGameState(world, steve));
		scene.pushState(new MenuGameState());
	}

	@Override
	public void onStop() {
		super.onStop();

		builderFuture.cancel(true);

		getScene().get3D().removeObjects(light, world, steve);
	}
}
