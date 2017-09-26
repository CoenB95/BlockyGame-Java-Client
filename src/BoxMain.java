import javafx.animation.*;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class BoxMain extends Application {

	public static DoubleProperty cameraAngle = new SimpleDoubleProperty();
	private boolean ig;
	private boolean re;
	private double lastX = -1;
	private double lastY = -1;
	private boolean escape;

	private boolean movingForward;

	@Override
	public void start(Stage primaryStage) {
		Group group = new Group();
		Scene scene = new Scene(group, 600, 400, true, SceneAntialiasing.BALANCED);

		Block rect0 = new Block(500, 500, 500, "/cube.png");
		rect0.setTranslateY(-250);

		PointLight pointLight = new PointLight(Color.WHITE);
		Sphere lightSphere = new Sphere(10);
		lightSphere.setMaterial(new PhongMaterial(Color.ORANGE));
		Group light = new Group(lightSphere, pointLight);
		light.setTranslateZ(-500);
		light.setTranslateY(-750);

		Circle circle = new Circle(0, 0, 500, Color.WHITE);
		circle.getTransforms().add(new Rotate(90, new Point3D(1, 0, 0)));

		ImageView imageView = new ImageView("/snake_background2.png");
		imageView.getTransforms().add(new Rotate(90, new Point3D(1, 0, 0)));
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(6000);
		imageView.setTranslateX(-2000);
		imageView.setTranslateY(100);
		imageView.setTranslateZ(-2000);

		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.setNearClip(200);
		camera.setFarClip(9000);

		Rotate yRotation = new Rotate(0,  0, 0, -250, new Point3D(0, 1, 0));
		Rotate xRotation = new Rotate(0,  0, 0, -250, new Point3D(1, 0, 0));

		new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (movingForward) {
					camera.setTranslateX(camera.getTranslateX() + Math.cos(Math.toRadians(yRotation.getAngle() - 90)) * 10);
					camera.setTranslateZ(camera.getTranslateZ() - Math.sin(Math.toRadians(yRotation.getAngle() - 90)) * 10);
				}
			}
		}.start();

		int size = 16;
		int blockSize = 200;

		//generateBlocks(group, size, blockSize);
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				Terrain terrain = Terrain.generateRandom(blockSize, size, size, 2);
				terrain.setTranslateX(size * blockSize * x);
				terrain.setTranslateZ(size * blockSize * z);
				group.getChildren().add(terrain);
			}
		}
		//Terrain terrain = Terrain.generateRandom(blockSize, size, size, 2);

		//group.getChildren().add(terrain);
		group.getChildren().add(new Block(100, 100, 100, Color.BLUE));

		try {
			new Robot().mouseMove(800, 450);
		} catch (AWTException e) {
			e.printStackTrace();
		}

		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				escape = !escape;
				if (!escape) re = true;
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
			if (!ig && !escape) {
				double newX = event.getScreenX() * 0.2;
				double newY = event.getScreenY() * 0.2;
				if (lastX < 0) {
					lastX = newX;
					lastY = newY;
				}
				ig = true;
				if (!re) {
					yRotation.setAngle(yRotation.getAngle() + newX - lastX);
					xRotation.setAngle(xRotation.getAngle() - (newY - lastY));
					//lastX = newR;
				}
				try {

					new Robot().mouseMove(800, 450);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else if (ig) {
				ig = false;
				if (re) {

					System.out.println("Reset (ignore " + (event.getScreenX() * 0.2 - lastX) + ")");
					re = false;
				}
			}
			PickResult pickResult = event.getPickResult();
			if (!(pickResult.getIntersectedNode() instanceof Terrain))
				return;
			int blockNr = ((Terrain) pickResult.getIntersectedNode()).findBlockByFace(pickResult.getIntersectedFace());
			((Terrain) pickResult.getIntersectedNode()).markBlock(blockNr);
		});


		Translate distance = new Translate(0, 0, -350);
		camera.getTransforms().addAll(
				new Translate(blockSize * size, blockSize * -2, blockSize * size),//pivot
				yRotation,
				xRotation,
				distance);

		Timeline timeline = new Timeline(
				new KeyFrame(Duration.ZERO,
						new KeyValue(yRotation.angleProperty(), 0)),
				new KeyFrame(Duration.seconds(15),
						new KeyValue(yRotation.angleProperty(), 360))
		);
		timeline.setDelay(Duration.seconds(2));
		timeline.setCycleCount(Animation.INDEFINITE);
		//timeline.playFromStart();

		Timeline timeline2 = new Timeline(
				new KeyFrame(Duration.ZERO,
						new KeyValue(xRotation.angleProperty(), -10),
						new KeyValue(distance.zProperty(), -1000)),
				new KeyFrame(Duration.seconds(5),
						new KeyValue(xRotation.angleProperty(), -40, Interpolator.EASE_BOTH),
						new KeyValue(distance.zProperty(), -10000, Interpolator.EASE_BOTH))
		);
		timeline2.setAutoReverse(true);
		timeline2.setCycleCount(Animation.INDEFINITE);
		//timeline2.playFromStart();

		cameraAngle.bind(yRotation.angleProperty());

		System.out.println(Platform.isSupported(ConditionalFeature.SCENE3D));


		scene.setFill(Color.DARKSLATEBLUE);
		scene.setCamera(camera);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void generateBlocks(Group group, int size, int blockSize) {
		CompletableFuture.runAsync(() -> {
			Image img = new Image("/cube.png", 320, 320, true, false, false);//"/cube.png", false);
			for (int x = -size / 2; x < size / 2; x++) {
				for (int y = -size / 2; y < size / 2; y++) {
					RectBlock block = new RectBlock(blockSize, blockSize, blockSize, img);
					//if (Math.random() > 0.8) {
					if (Math.random() > 0.8 || x == 0 && y == 0) {
						block.setRaised(x == 0 && y == 0 ? 2 : 1);
					}
					//Shape3D block = new Block(blockSize, blockSize, blockSize,"/cube.png");
					//Shape3D block = new Box(blockSize, blockSize, blockSize);
					//block.setMaterial(new PhongMaterial(Color.WHITE, new Image("/cube.png"), null,
					//		null, null));
					block.setTranslateX(x * blockSize);
					block.setTranslateZ(y * blockSize);
					Platform.runLater(() -> group.getChildren().add(block));
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
