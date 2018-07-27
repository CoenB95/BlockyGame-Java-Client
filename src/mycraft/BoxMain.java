package mycraft;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mycraft.camera.Camera;
import mycraft.gameobject.GameObjectComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class BoxMain extends Application {

	public static DoubleProperty cameraAngle = new SimpleDoubleProperty();
	private Camera camera;
	private boolean escape = true;

	private int size = 16;
	private int blockSize = 200;
	private List<Terrain> chunks;

	private boolean movingForward;

	@Override
	public void start(Stage primaryStage) {
		Group group = new Group();

		Scene scene = new Scene(group, 600, 400, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.DARKBLUE);
		camera = new Camera(scene);

		LightBase light = new AmbientLight();
		group.getChildren().add(light);

		//Walking
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				camera.onUpdate(0.013);
				if (movingForward) {
					double newCX = camera.getTargetPosition().getX() + Math.cos(Math.toRadians(camera.getRotation().getHorizontal() - 90)) * 20;
					double newCZ = camera.getTargetPosition().getZ() - Math.sin(Math.toRadians(camera.getRotation().getHorizontal() - 90)) * 20;
					int chunkX = (int) Math.round(newCX / size / blockSize);
					int chunkZ = (int) Math.round(newCZ / size / blockSize);
					int chunkIndex = chunkX * 3 + chunkZ;
					double chunkBlockX = (newCX - chunkX * size * blockSize) / blockSize;
					double chunkBlockZ = (newCZ - chunkZ * size * blockSize) / blockSize;
					System.out.println("Chunk nr. " + chunkIndex + ", x=" + chunkBlockX + ", z=" + chunkBlockZ);
					if (chunkX < 0 || chunkX >= 3)
						newCX = camera.getTargetPosition().getX();
					if (chunkZ < 0 || chunkZ >= 3)
						newCZ = camera.getTargetPosition().getZ();
					camera.setTargetPosition(camera.getTargetPosition().withX(newCX).withZ(newCZ));
				}
			}
		}.start();

		chunks = new ArrayList<>();
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				Terrain terrain = Terrain.generateRandom(blockSize, size, size, 2);
				terrain.setTranslateX(size * blockSize * x);
				terrain.setTranslateZ(size * blockSize * z);
				group.getChildren().add(terrain);
				chunks.add(terrain);
			}
		}

		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				escape = !escape;
				if (!escape) camera.notifyFirstMovement();
				else System.out.println("escape");
			} else if (event.getCode() == KeyCode.W) {
				movingForward = true;
			}
		});

		scene.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.W) {
				movingForward = false;
			}
		});

		scene.setOnMouseMoved(event -> {
			if (escape)
				return;

			camera.applyMouseMoved(event);

			PickResult pickResult = event.getPickResult();
			if (!(pickResult.getIntersectedNode() instanceof Terrain))
				return;
			int blockNr = ((Terrain) pickResult.getIntersectedNode()).findBlockByFace(pickResult.getIntersectedFace());
			((Terrain) pickResult.getIntersectedNode()).markBlock(blockNr);
		});

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
