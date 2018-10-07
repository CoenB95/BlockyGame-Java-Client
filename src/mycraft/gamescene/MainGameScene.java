package mycraft.gamescene;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import mycraft.ChunkView;
import mycraft.Terrain;
import mycraft.camera.Camera;
import mycraft.gameobject.GameScene;
import mycraft.math.Position;

import java.util.ArrayList;
import java.util.List;

public class MainGameScene extends GameScene {
	private KeyCode forwardKey = KeyCode.W;
	private KeyCode leftKey = KeyCode.A;
	private KeyCode backwardKey = KeyCode.S;
	private KeyCode rightKey = KeyCode.D;

	private boolean escape = true;

	private int size = 16;
	private int blockSize = 200;

	private Camera camera;
	private List<Terrain> chunks;

	public MainGameScene(Scene scene) {
		super(scene);

		camera = new Camera();

		chunks = new ArrayList<>();
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				Terrain terrain = Terrain.generateRandom(blockSize, size, size, 2);
				terrain.setPosition(new Position(size * blockSize * x, 0, (size * blockSize * z)));
				chunks.add(terrain);
			}
		}

		addObject(camera);
		addObjects(chunks);
	}

	@Override
	public void onKeyPressed(KeyEvent event) {
		super.onKeyPressed(event);
		if (event.getCode() == KeyCode.ESCAPE) {
			escape = !escape;
			if (!escape) camera.notifyFirstMovement();
			else System.out.println("escape");
		}
	}

	@Override
	public void onMouseMove(MouseEvent event) {
		super.onMouseMove(event);
		if (escape)
			return;

		camera.applyMouseMoved(event);

		PickResult pickResult = event.getPickResult();
		if (!(pickResult.getIntersectedNode() instanceof ChunkView))
			return;
		int blockNr = ((ChunkView) pickResult.getIntersectedNode()).getChunk()
				.findBlockByFace(pickResult.getIntersectedFace());
		((ChunkView) pickResult.getIntersectedNode()).getChunk().markBlock(blockNr);
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		super.onUpdate(elapsedSeconds);

		if (isKeyPressed(forwardKey)) {
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
}
