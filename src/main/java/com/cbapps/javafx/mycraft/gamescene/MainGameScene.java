package com.cbapps.javafx.mycraft.gamescene;

import com.cbapps.javafx.gamo.components.FollowComponent;
import com.cbapps.javafx.gamo.components.SmoothRotationComponent;
import com.cbapps.javafx.gamo.components.SmoothTranslationComponent;
import com.cbapps.javafx.gamo.math.Position;
import com.cbapps.javafx.gamo.math.PositionalDelta;
import com.cbapps.javafx.gamo.math.Rotation;
import com.cbapps.javafx.gamo.math.RotationalDelta;
import com.cbapps.javafx.gamo.objects.Camera;
import com.cbapps.javafx.gamo.scenes.GameScene;
import com.cbapps.javafx.mycraft.ChunkView;
import com.cbapps.javafx.mycraft.Steve;
import com.cbapps.javafx.mycraft.Terrain;
import com.cbapps.javafx.mycraft.TextObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;

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

	private Steve steve;
	private Camera camera;
	private List<Terrain> chunks;
	private StringProperty debugText = new SimpleStringProperty();
	private StringProperty coordinateText = new SimpleStringProperty();
	private StringProperty escapeText = new SimpleStringProperty();

	public MainGameScene(Scene scene, Pane root) {
		super(scene, root);

		steve = new Steve();
		camera = new Camera(200, 9000);
		camera.addComponent(FollowComponent.rotatingAndTranslating(steve));
		camera.addEditor(new SmoothRotationComponent(0.8));
		camera.addEditor(new SmoothTranslationComponent( 0.95));

		chunks = new ArrayList<>();
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				Terrain terrain = Terrain.generateRandom(this, blockSize, size, size, 2);
				terrain.setTargetVector(terrain.getTargetVector().withPosition(new Position(size * blockSize * x, 0, (size * blockSize * z))));
				chunks.add(terrain);
			}
		}

		debugText.bind(Bindings.concat(coordinateText, escapeText));

		TextObject label = new TextObject();
		label.textProperty().bind(debugText);

		add3DObject(steve);
		add3DObject(camera);
		add3DObjects(chunks);
		add2DObject(label);
		setCamera(camera.camera);
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
			if (!escape) steve.notifyFirstMovement();
			else System.out.println("escape");
		}
	}

	@Override
	public void onMouseMove(MouseEvent event) {
		super.onMouseMove(event);
		if (escape)
			return;

		steve.applyMouseMoved(event);

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
			RotationalDelta direction = steve.getTargetVector().getRotation().withVertical(verAngle).addHorizontal(horAngle);

			PositionalDelta targetDelta = PositionalDelta.ZERO;
			if (walk)
				targetDelta = targetDelta.add(direction, 20);
			if (jump)
				targetDelta = targetDelta.add(new Rotation(0, 90, 0), 20);
			if (sneak)
				targetDelta = targetDelta.add(new Rotation(0, -90, 0), 20);

			steve.setTargetVector(steve.getTargetVector().withPosition(steve.getCurrentVector().getPosition()
					.add(targetDelta)
					.limitX(-0.5 * size * blockSize, 2.5 * size * blockSize)
					.limitZ(-0.5 * size * blockSize, 2.5 * size * blockSize).asPosition()));
		}

		Terrain currentChunk = getChunk(steve.getCurrentVector().getPosition());
		if (currentChunk != null) {
			double globalX = 0.5 * size + Math.floor(steve.getCurrentVector().getPosition().getX() / blockSize);
			double globalY = 0.5 * size + Math.floor(steve.getCurrentVector().getPosition().getY() / blockSize);
			double globalZ = 0.5 * size + Math.floor(steve.getCurrentVector().getPosition().getZ() / blockSize);
			double chunkX = Math.abs(globalX) % size;
			double chunkZ = Math.abs(globalZ) % size;
			coordinateText.set(String.format("x: %2.0f y: %2.0f z: %2.0f (block %2.0f,%2.0f in chunk %2.0f,%2.0f)%n",
					globalX, globalY, globalZ,
					chunkX, chunkZ,
					currentChunk.getCurrentVector().getPosition().getX() / size / blockSize, currentChunk.getCurrentVector().getPosition().getZ() / size / blockSize));
		}
		escapeText.set(escape ? "In menu\n" : "In game");
	}
}
