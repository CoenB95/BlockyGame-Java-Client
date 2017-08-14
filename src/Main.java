import javafx.animation.*;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Rectangle rect0 = new Rectangle(500, 500);
		rect0.fillProperty().bind(Bindings.when(rect0.hoverProperty()).then(Color.RED)
				.otherwise(new Color(1.0, 0.0, 0.0, 0.3)));

		Rectangle rect1 = new Rectangle(500, 500);
		rect1.getTransforms().add(new Rotate(-90, 500, 0, 0, new Point3D(0, 1, 0)));
		rect1.setFill(Color.GREEN);

		Rectangle rect2 = new Rectangle(500, 500);
		rect2.setFill(Color.BLUE);
		rect2.getTransforms().add(new Rotate(90, new Point3D(0, 1, 0)));

		Rectangle rect3 = new Rectangle(500, 500);
		rect3.setFill(Color.ORANGE);
		rect3.getTransforms().add(new Rotate(180, 250, 0, -250, new Point3D(0, 1, 0)));

		Group cube = new Group(rect3, rect1, rect2, rect0);
		cube.setTranslateX(-250);
		cube.setTranslateY(-500);
		cube.setTranslateZ(250);

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
		camera.setFarClip(8000);

		Group group = new Group(imageView, circle, cube);

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
						new KeyValue(distance.zProperty(), -2000)),
				new KeyFrame(Duration.seconds(5),
						new KeyValue(xRotation.angleProperty(), -40, Interpolator.EASE_BOTH),
						new KeyValue(distance.zProperty(), -5000, Interpolator.EASE_BOTH))
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
