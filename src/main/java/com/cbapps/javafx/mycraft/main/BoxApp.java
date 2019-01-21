package com.cbapps.javafx.mycraft.main;

import com.cbapps.javafx.gamo.scenes.GameScene;
import com.cbapps.javafx.mycraft.gamescene.MainGameScene;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BoxApp extends Application {
	public static DoubleProperty cameraAngle = new SimpleDoubleProperty();
	private GameScene mainScene;

	@Override
	public void start(Stage primaryStage) {
		Pane group = new StackPane();
		Scene scene = new Scene(group, 600, 400);
		scene.setFill(Color.ANTIQUEWHITE);
		mainScene = new MainGameScene(scene, group);

		//Walking
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				mainScene.onUpdate(0.013);
			}
		}.start();

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
