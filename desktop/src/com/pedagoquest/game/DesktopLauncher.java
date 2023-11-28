package com.pedagoquest.game;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        DisplayMode primaryMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(120);
        config.setTitle("PedagoQuest");
        config.setFullscreenMode(primaryMode);
        new Lwjgl3Application(new GameController(), config);
    }
}
