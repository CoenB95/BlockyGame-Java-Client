package mycraft.gameobject;

import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.*;

public class GameScene {
	SubScene scene2D;
	SubScene scene3D;
	private Group group2D;
	private Group group3D;
	private List<GameObject> objects2D = new ArrayList<>();
	private List<GameObject> objects3D = new ArrayList<>();
	private Map<KeyCode, Boolean> keyMap = new HashMap<>();

	public GameScene(Scene scene, Pane root)
	{
		this.group2D = new Group();
		this.group3D = new Group();

		scene2D = new SubScene(group2D, 310, 310);
		scene2D.setFill(Color.TRANSPARENT);
		scene2D.setMouseTransparent(true);
		scene2D.heightProperty().bind(root.heightProperty());
		scene2D.widthProperty().bind(root.widthProperty());

		scene3D = new SubScene(group3D, 300, 300, true, SceneAntialiasing.BALANCED);
		scene3D.setFill(Color.DARKBLUE);
		scene3D.heightProperty().bind(root.heightProperty());
		scene3D.widthProperty().bind(root.widthProperty());

		root.getChildren().addAll(scene3D, scene2D);

		LightBase light = new AmbientLight();
		group3D.getChildren().add(light);

		scene.setOnKeyPressed(this::onKeyPressed);
		scene.setOnKeyReleased(this::onKeyReleased);
		scene3D.setOnMouseMoved(this::onMouseMove);
	}

	protected final void add2DNode(Node node) {
		this.group2D.getChildren().add(node);
	}

	protected final void add3DNode(Node node) {
		this.group3D.getChildren().add(node);
	}

	protected final void add2DNodes(Collection<Node> nodes) {
		this.group2D.getChildren().addAll(nodes);
	}

	protected final void add3DNodes(Collection<Node> nodes) {
		this.group3D.getChildren().addAll(nodes);
	}

	public final void add2DObject(GameObject object) {
		object.setParentScene(this, false);
		objects2D.add(object);
	}

	public final void add3DObject(GameObject object) {
		object.setParentScene(this, true);
		objects3D.add(object);
	}

	public final void add2DObjects(Collection<? extends GameObject> objects) {
		objects.forEach(this::add2DObject);
	}

	public final void add3DObjects(Collection<? extends GameObject> objects) {
		objects.forEach(this::add3DObject);
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
		objects2D.forEach(o -> o.onUpdate(elapsedSeconds));
		objects3D.forEach(o -> o.onUpdate(elapsedSeconds));
	}

	protected final void setCamera(Camera camera) {
		scene3D.setCamera(camera);
	}
}
