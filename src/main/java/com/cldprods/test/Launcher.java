package com.cldprods.test;

import com.cldprods.test.core.EngineManager;
import com.cldprods.test.core.WindowManager;
import com.cldprods.test.test.MyGame;

public class Launcher {

    private static WindowManager window;
    private static MyGame game;

    public static void main(String[] args) {

        window = new WindowManager("Game Engine",1600,900,false);
        game = new MyGame();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static MyGame getGame() {
        return game;
    }
}
