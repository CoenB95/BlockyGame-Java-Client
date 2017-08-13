import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Rectangle rect0 = new Rectangle(100, 100, new Color(1.0, 0.0, 0.0, 0.3));
		rect0.getTransforms().add(new Rotate(-90, new Point3D(0, 1, 0)));
		//rect0.setTranslateZ(70);

		Rectangle rect1 = new Rectangle(5, 5, 90, 90);
		rect1.setFill(new Color(0.0, 0.0, 1.0, 0.3));
		//rect1.setTranslateZ(20);

		Rectangle rect2 = new Rectangle(100, 100, Color.GREEN);
		rect2.getTransforms().add(new Rotate(90, new Point3D(0, 1, 0)));

		Rectangle rect3 = new Rectangle(100, 100, Color.ORANGE);
		rect3.getTransforms().add(new Rotate(-90, 100, 0, 0, new Point3D(0, 1, 0)));

		Rectangle rect4 = new Rectangle(100, 100, Color.CYAN);
		rect4.getTransforms().add(new Rotate(90, 100, 0, 0, new Point3D(0, 1, 0)));
		//rect4.getTransforms().add(new Rotate(180, 50, 0, -50, new Point3D(0, 1, 0)));

		Group group = new Group(rect2, rect3, rect4, rect0, rect1);
		Rotate rotate = new Rotate(0, 50, 50, -50, new Point3D(0, 1, 0));
		group.getTransforms().add(rotate);

		Timeline timeline = new Timeline(
				new KeyFrame(Duration.ZERO,
						new KeyValue(rotate.angleProperty(), 0)),
				new KeyFrame(Duration.seconds(5),
						new KeyValue(rotate.angleProperty(), 360))
		);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.playFromStart();

		System.out.println(Platform.isSupported(ConditionalFeature.SCENE3D));

		StackPane stack = new StackPane(group);
		//stack.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(5), null)));
		Scene scene = new Scene(stack, 600, 400, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.GRAY);

		PerspectiveCamera camera = new PerspectiveCamera();
		scene.setCamera(camera);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
