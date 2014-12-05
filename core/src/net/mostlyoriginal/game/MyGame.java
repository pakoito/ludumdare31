package net.mostlyoriginal.game;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
    private static MyGame instance;

    @Override
    public void create() {
        instance = this;
        restart();
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }

    public void restart() {
        setScreen(new MainScreen());
    }

    public static MyGame getInstance()
    {
        return instance;
    }
}
