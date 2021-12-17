package com.cldprods.test.core;

import com.cldprods.test.Launcher;
import com.cldprods.test.core.entity.Entity;
import com.cldprods.test.core.entity.Model;
import com.cldprods.test.core.utils.Transformation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RenderManager {

    private final WindowManager window;
    private ShaderManager shader;

    public  RenderManager() {
        window = Launcher.getWindow();
    }

    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shader.link();
        shader.createUniform("textureSampler");

        shader.createUniform("transformationMatrix");
    }

    public void render(Entity entity) {
        clear();
        shader.bind(); // use shader

        shader.setUniformInt("textureSampler",0);
        shader.setUniformMatrix("transformationMatrix", Transformation.createTransformationMatrix(entity));
        GL30.glBindVertexArray(entity.getModel().getId());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,entity.getModel().getTexture().getId());

        GL11.glDrawElements(GL11.GL_TRIANGLES,entity.getModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);
        //GL11.glDrawArrays(GL11.GL_TRIANGLES,0,model.getVertexCount());

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
        shader.unbind(); // don't use shader
    }

    public void clear() {

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        shader.cleanup(); // destroy shader
    }
}
