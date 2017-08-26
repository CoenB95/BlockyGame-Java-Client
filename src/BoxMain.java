import javafx.animation.*;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class BoxMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Block rect0 = new Block(500, 500, 500);
		rect0.setMaterial(new PhongMaterial(Color.WHITE, new Image("/cube.png"), null, null, null));
		rect0.setTranslateY(500);
		//rect0.setTranslateZ(-500);

		Box rect1 = new Box(500, 500, 500);
		rect1.setMaterial(new PhongMaterial(Color.WHITE, new Image("/cube.png"), null, null, null));
		//rect1.setTranslateX(500);

		Box rect2 = new Box(500, 500, 500);
		rect2.setMaterial(new PhongMaterial(Color.BLUE));
		rect2.setTranslateY(-500);

		Box rect3 = new Box(500, 500, 500);
		rect3.setMaterial(new PhongMaterial(Color.CYAN));
		rect3.setTranslateY(-1000);

		PointLight pointLight = new PointLight(Color.WHITE);
		Sphere lightSphere = new Sphere(10);
		lightSphere.setMaterial(new PhongMaterial(Color.ORANGE));
		Group light = new Group(lightSphere, pointLight);
		light.setTranslateZ(-500);
		light.setTranslateY(-750);

		Group cube = new Group(rect3, rect1, rect2, rect0);
		//cube.setTranslateX(-250);
		cube.setTranslateY(-750);
		//cube.setTranslateZ(250);

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

		Group group = new Group(imageView, circle, cube
				//, light
		);

		Rotate yRotation = new Rotate(0,  0, 0, 0, new Point3D(0, 1, 0));
		Rotate xRotation = new Rotate(0,  0, 0, 0, new Point3D(1, 0, 0));
		Translate distance = new Translate(0, 0, -350);
		camera.getTransforms().addAll(
				new Translate(0, -250, 0),//pivot
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
						new KeyValue(distance.zProperty(), -10000, Interpolator.EASE_BOTH))
		);
		timeline2.setAutoReverse(true);
		timeline2.setCycleCount(Animation.INDEFINITE);
		timeline2.playFromStart();

		System.out.println(Platform.isSupported(ConditionalFeature.SCENE3D));

		Scene scene = new Scene(group, 600, 400, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.BLACK);
		scene.setCamera(camera);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
