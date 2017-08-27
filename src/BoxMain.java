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
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class BoxMain extends Application {

	public static DoubleProperty cameraAngle = new SimpleDoubleProperty();

	@Override
	public void start(Stage primaryStage) throws Exception {
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
		camera.setNearClip(500);
		camera.setFarClip(12000);

		Group group = new Group();

		int size = 16;
		int blockSize = 400;

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

		Rotate yRotation = new Rotate(0,  0, 0, 0, new Point3D(0, 1, 0));
		Rotate xRotation = new Rotate(0,  0, 0, 0, new Point3D(1, 0, 0));
		Translate distance = new Translate(0, 0, -350);
		camera.getTransforms().addAll(
				new Translate(0, blockSize * -1.5, 0),//pivot
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
		timeline.playFromStart();

		Timeline timeline2 = new Timeline(
				new KeyFrame(Duration.ZERO,
						new KeyValue(xRotation.angleProperty(), -10),
						new KeyValue(distance.zProperty(), -1000)),
				new KeyFrame(Duration.seconds(5),
						new KeyValue(xRotation.angleProperty(), -40, Interpolator.EASE_BOTH),
						new KeyValue(distance.zProperty(), -5000, Interpolator.EASE_BOTH))
		);
		timeline2.setAutoReverse(true);
		timeline2.setCycleCount(Animation.INDEFINITE);
		timeline2.playFromStart();

		cameraAngle.bind(yRotation.angleProperty());

		System.out.println(Platform.isSupported(ConditionalFeature.SCENE3D));

		Scene scene = new Scene(group, 600, 400, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.BLACK);
		scene.setCamera(camera);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
