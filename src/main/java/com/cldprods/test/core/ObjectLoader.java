package com.cldprods.test.core;

import com.cldprods.test.core.entity.Model;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

    private static final int VERTICES_POS_LOCATION_ATTRIB = 0;
    private static final int TEXCOORDS_LOCATION_ATTRIB = 1;

    private static final int VECTOR3_COMPONENTS_COUNT  = 3;
    private static final int  VECTOR2_COMPONENTS_COUNT= 1;

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public Model loadModel(float[] vertices, float[] textureCoords, int[] indices) {
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataAttribList(0,3,vertices); // attrib0 : vertices
        storeDataAttribList(1,2,textureCoords); //attrib1 : textureCoordsLocation
        unbind();
        return  new Model(id,indices.length);
        //return  new Model(id,vertices.length / 3);
    }

    public int loadTexture(String filename) throws Exception {
        int width,height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1); // width
            IntBuffer h = stack.mallocInt(1); // height
            IntBuffer c = stack.mallocInt(1); //number of channels

            buffer = STBImage.stbi_load(filename,w,h,c,4);
            if(buffer == null)
                throw new Exception("Image File "+filename+" not loaded "+ STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();
        }

        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id); // bind texture
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT,1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return  id;
    }

    private int createVAO() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
    }

    private void storeDataAttribList(int attribNum, int vertexCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribNum,vertexCount, GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL30.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }
}
