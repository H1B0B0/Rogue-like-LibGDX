package com.pedagoquest.game;

import com.badlogic.gdx.Game;

public class GameController extends Game {

    @Override
    public void create() {
        setScreen(new MainMenu(this));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
