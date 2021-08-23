package com.cbapps.javafx.mycraft.states;

import com.cbapps.gamo.javafx.GroupObject;
import com.cbapps.gamo.javafx.TextObject;
import com.cbapps.gamo.math.Vector3;
import com.cbapps.gamo.state.GameStateBase;
import com.cbapps.gamo.state.IGameScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MenuGameState extends GameStateBase {
    private final GroupObject menuGroup;

    public MenuGameState() {
        this.menuGroup = new GroupObject();
    }

    @Override
    public void onStart(IGameScene scene) {
        super.onStart(scene);

        var menuText = new TextObject();
        menuText.setCentered(true);
        menuText.setFontSize(32);
        menuText.setText("Menu");

        var text2 = new TextObject();
        text2.setCentered(true);
        text2.setPosition(Vector3.y(-40));
        text2.setText("Hallo!");

        menuGroup.addObjects(menuText, text2);
        scene.get2D().addObject(menuGroup);
    }

    @Override
    public void onStop() {
        super.onStop();

        getScene().get2D().removeObject(menuGroup);
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        super.onKeyPressed(event);

        if (event.getCode() == KeyCode.ESCAPE) {
            getScene().popState();
        }
    }

    @Override
    public void onUpdate(double elapsedSeconds) {
        super.onUpdate(elapsedSeconds);

        menuGroup.setPosition(new Vector3(getScene().getWidth() / 2, -getScene().getHeight() / 2, 0));
    }
}
