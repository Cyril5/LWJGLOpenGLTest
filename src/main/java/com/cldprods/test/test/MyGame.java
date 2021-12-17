package com.cldprods.test.test;

import com.cldprods.test.Launcher;
import com.cldprods.test.core.*;
import com.cldprods.test.core.entity.Entity;
import com.cldprods.test.core.entity.Model;
import com.cldprods.test.core.entity.Texture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class MyGame implements ILogic {

    private static final float CAMERA_MOVE_SPEED = 0.01f;

    private final RenderManager renderer;
    private  final ObjectLoader loader;
    private final WindowManager window;

    private Entity entity;
    private Camera camera;

    Vector3f cameraMoveDir;

    public MyGame() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraMoveDir = new Vector3f(0,0,0);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        float[] vertices = new float[] {
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.0f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
                8, 10, 11, 9, 8, 11,
                12, 13, 7, 5, 12, 7,
                14, 15, 6, 4, 14, 6,
                16, 18, 19, 17, 16, 19,
                4, 6, 7, 5, 4, 7,
        };


        Model model = loader.loadModel(vertices,textCoords,indices);
        model.setTexture(new Texture(loader.loadTexture("textures/texture2.png")));
        camera.setPosition(0,0,3f);
        entity = new Entity(model,new Vector3f(0,0,0f),new Vector3f(0,0,0),1);
    }

    @Override
    public void input() {

        cameraMoveDir.set(0,0,0);
        if(window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            cameraMoveDir.z = -1;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            cameraMoveDir.z = 1;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            cameraMoveDir.x = -1;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            cameraMoveDir.x = 1;
        }

        // Up & down
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            cameraMoveDir.y = 1;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            cameraMoveDir.y = -1;
        }
    }



    @Override
    public void update() {
        camera.movePosition(
                cameraMoveDir.x * CAMERA_MOVE_SPEED,
                cameraMoveDir.y * CAMERA_MOVE_SPEED,
                cameraMoveDir.z * CAMERA_MOVE_SPEED);

        entity.incrementRotation(0.0f,0.5f,0.0f);
    }

    @Override
    public void render() {

        if(window.isResize()) {
            GL11.glViewport(0,0,window.getWidth(),window.getHeight());
            window.setResize(true);
        }

        window.setClearColour(0.0f,0.0f,0.0f,0.0f);
        renderer.render(entity,camera);
    }


    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
