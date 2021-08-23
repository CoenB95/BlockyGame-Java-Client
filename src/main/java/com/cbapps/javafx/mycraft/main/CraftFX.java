package com.cbapps.javafx.mycraft.main;

import com.cbapps.gamo.javafx.JavaFXGameApp;
import com.cbapps.gamo.state.IGameState;
import com.cbapps.javafx.mycraft.states.MainGameState;

public class CraftFX extends JavaFXGameApp {

    @Override
    public IGameState createState() {
        return new MainGameState();
    }
}
