package mycraft.gamescene;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
	private StringProperty coordinateText = new SimpleStringProperty();
	private StringProperty debugText = new SimpleStringProperty();
	private StringProperty escapeText = new SimpleStringProperty();

	public MainGameScene(Scene scene, Pane root) {
		super(scene, root);

		camera = new Camera();

		chunks = new ArrayList<>();
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				Terrain terrain = Terrain.generateRandom(this, blockSize, size, size, 2);
				terrain.setPosition(new Position(size * blockSize * x, 0, (size * blockSize * z)));
				chunks.add(terrain);
			}
		}

		debugText.bind(Bindings.concat(coordinateText, escapeText));

		Label t = new Label();
		t.textProperty().bind(debugText);
		t.setFont(Font.font("Monospaced"));
		t.setTextFill(Color.WHITE);
		t.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.3), null, null)));
		t.setPadding(new Insets(5));

		add2DNode(t);
		add3DObject(camera);
		add3DObjects(chunks);
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

			camera.setTargetPosition(camera.getTargetPosition()
					.add(targetDelta)
					.limitX(-0.5 * size * blockSize, 2.5 * size * blockSize)
					.limitZ(-0.5 * size * blockSize, 2.5 * size * blockSize));
		}

		Terrain currentChunk = getChunk(camera.getPosition());
		if (currentChunk != null) {
			double globalX = 0.5 * size + Math.floor(camera.getPosition().getX() / blockSize);
			double globalY = 0.5 * size + Math.floor(camera.getPosition().getY() / blockSize);
			double globalZ = 0.5 * size + Math.floor(camera.getPosition().getZ() / blockSize);
			double chunkX = Math.abs(globalX) % size;
			double chunkZ = Math.abs(globalZ) % size;
			coordinateText.set(String.format("x: %2.0f y: %2.0f z: %2.0f (block %2.0f,%2.0f in chunk %2.0f,%2.0f)%n",
					globalX, globalY, globalZ,
					chunkX, chunkZ,
					currentChunk.getPosition().getX() / size / blockSize, currentChunk.getPosition().getZ() / size / blockSize));
		}
		escapeText.set(escape ? "In menu\n" : "In game");
	}
}
