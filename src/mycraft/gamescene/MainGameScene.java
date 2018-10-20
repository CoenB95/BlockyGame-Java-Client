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
import mycraft.math.Rotation;

import java.util.ArrayList;
import java.util.List;

public class MainGameScene extends GameScene {
	private KeyCode forwardKey = KeyCode.W;
	private KeyCode leftKey = KeyCode.A;
	private KeyCode backwardKey = KeyCode.S;
	private KeyCode rightKey = KeyCode.D;
	private KeyCode jumpKey = KeyCode.SPACE;
	private KeyCode sneakKey = KeyCode.SHIFT;

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

	private Terrain getChunk(Position position)
	{
		int chunkX = (int) Math.round(position.getX() / size / blockSize);
		int chunkZ = (int) Math.round(position.getZ() / size / blockSize);
		int index = chunkX * 3 + chunkZ;
		if (index < 0 || index >= chunks.size())
			return null;

		return chunks.get(index);
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

		Terrain currentChunk = getChunk(camera.getPosition());
		double horAngle = 0;
		double verAngle = 0;
		boolean walk = true;
		boolean jump = isKeyPressed(jumpKey);
		boolean sneak = isKeyPressed(sneakKey);

		if (isKeyPressed(forwardKey))
			horAngle = isKeyPressed(leftKey) ? -45 : isKeyPressed(rightKey) ? 45 : 0;
		else if (isKeyPressed(leftKey))
			horAngle = isKeyPressed(forwardKey) ? -45 : isKeyPressed(backwardKey) ? -135 : -90;
		else if (isKeyPressed(rightKey))
			horAngle = isKeyPressed(forwardKey) ? 45 : isKeyPressed(backwardKey) ? 135 : 90;
		else if (isKeyPressed(backwardKey))
			horAngle = isKeyPressed(leftKey) ? -135 : isKeyPressed(rightKey) ? 135 : 180;
		else
			walk = false;

		if (walk || jump || sneak) {
			Rotation direction = camera.getRotation().addHorizontal(horAngle).withVertical(verAngle);

			Position targetDelta = Position.ORIGIN;
			if (walk)
				targetDelta = targetDelta.add(direction, 20);
			if (jump)
				targetDelta = targetDelta.add(new Rotation(0, 90, 0), 20);
			if (sneak)
				targetDelta = targetDelta.add(new Rotation(0, -90, 0), 20);

			//targetDelta = targetDelta.withY(0);
			Terrain nextChunk = getChunk(camera.getTargetPosition().add(targetDelta));
			//double newCX = camera.getTargetPosition().getX() + Math.cos(Math.toRadians(camera.getRotation().getHorizontal() - 90)) * 20;
			//double newCZ = camera.getTargetPosition().getZ() - Math.sin(Math.toRadians(camera.getRotation().getHorizontal() - 90)) * 20;
			//int chunkX = (int) Math.round(newCX / size / blockSize);
			//int chunkZ = (int) Math.round(newCZ / size / blockSize);
			//int chunkIndex = chunkX * 3 + chunkZ;
			double chunkBlockX = camera.getPosition().getX() % (size * blockSize);
			double chunkBlockZ = camera.getPosition().getZ() % (size * blockSize);
			if (currentChunk != null) {
				System.out.format("x: %5.0f, y: %5.0f, z: %5.0f (chunk %3.0f, %3.0f | block %2.0f, %2.0f) ",
						camera.getPosition().getX(), camera.getPosition().getY(), camera.getPosition().getZ(),
						currentChunk.getPosition().getX() / size / blockSize, currentChunk.getPosition().getZ() / size / blockSize,
						chunkBlockX / blockSize, chunkBlockZ / blockSize);
			}
			if (nextChunk == null) {
				System.out.println("end-of-world");
			} else {
				System.out.println("safe");
			}
			/*if (chunkX < 0 || chunkX >= 3)
				newCX = camera.getTargetPosition().getX();
			if (chunkZ < 0 || chunkZ >= 3)
				newCZ = camera.getTargetPosition().getZ();*/

			camera.setTargetPosition(camera.getTargetPosition()
					.add(targetDelta)
					.limitX(-0.5 * size * blockSize, 2.5 * size * blockSize)
					.limitZ(-0.5 * size * blockSize, 2.5 * size * blockSize));
		}
	}
}
