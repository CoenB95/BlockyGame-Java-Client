package mycraft.gameobject;

import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.*;

public class GameScene {
	private Scene scene;
	private Group group;
	private List<GameObject> objects = new ArrayList<>();
	private Map<KeyCode, Boolean> keyMap = new HashMap<>();

	public GameScene(Scene scene)
	{
		this.scene = scene;
		this.group = new Group();
		scene.setRoot(group);

		LightBase light = new AmbientLight();
		group.getChildren().add(light);

		scene.setFill(Color.DARKBLUE);
		scene.setOnKeyPressed(this::onKeyPressed);
		scene.setOnKeyReleased(this::onKeyReleased);
		scene.setOnMouseMoved(this::onMouseMove);
	}

	protected final void addNode(Node node) {
		this.group.getChildren().add(node);
	}

	protected final void addNodes(Collection<Node> nodes) {
		this.group.getChildren().addAll(nodes);
	}

	public final void addObject(GameObject object) {
		object.setParentScene(this);
		objects.add(object);
	}

	public final void addObjects(Collection<? extends GameObject> objects) {
		objects.forEach(this::addObject);
	}

	public final boolean isKeyPressed(KeyCode key) {
		return keyMap.getOrDefault(key, false);
	}

	public void onKeyPressed(KeyEvent event)
	{
		keyMap.put(event.getCode(), true);
	}

	public void onKeyReleased(KeyEvent event)
	{
		keyMap.put(event.getCode(), false);
	}

	public void onMouseMove(MouseEvent event)
	{

	}

	public void onUpdate(double elapsedSeconds) {
		objects.forEach(o -> o.onUpdate(elapsedSeconds));
	}

	protected final void setCamera(Camera camera) {
		group.getScene().setCamera(camera);
	}
}
