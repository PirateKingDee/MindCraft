/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindcraft;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Chunks {
    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    private Block[][][] blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int startX, startY, startZ;
    private Random r;
    
    public void render(){
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE*24);
        glPopMatrix();
    }
    
    public void rebuildMesh(float startX, float startY, float startZ){
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*6*12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE)*6*12);
        for(float x = 0; x < CHUNK_SIZE; x++){
            for(float z = 0; z < CHUNK_SIZE; z++){
                for(float y = 0; y < CHUNK_SIZE; y++){
                    VertexPositionData.put(createCube((float)(startX + x * CUBE_LENGTH),(float)(y * CUBE_LENGTH + (int)(CHUNK_SIZE * 0.8)), (float)(startZ + z * CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(blocks[(int)x][(int)y][(int)z])));
                }
            }
        }
        VertexColorData.flip();
        VertexPositionData.flip();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    private float[] createCubeVertexCol(float[] CubeColorArray){
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for(int i =0; i < cubeColors.length; i++){
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        return cubeColors;
    }
    
    public static float[] createCube(float x, float y, float z){
        int offset = CUBE_LENGTH/2;
        return new float[]{
            //top quad
            x + offset, y + offset, z, 
            x - offset, y + offset, z, 
            x - offset, y + offset, z - CUBE_LENGTH, 
            x + offset, y + offset, z - CUBE_LENGTH,
            //bottom quad
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH, 
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            //front quad
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            //back quad
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            //left quad
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            //right quad
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z};
        
    }
    
    private float[] getCubeColor(Block block){
        switch(block.GetID()){
            case 1: return new float[] {0,1,0};
            case 2: return new float[] {1, 0.5f, 0};
            case 3: return new float[]{0,0f, 1};
        }
        return new float[]{1,1,1};
    }
    
    public Chunks (int startX, int startY, int startZ){
        r = new Random();
        blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for(int x = 0; x < CHUNK_SIZE; x ++){
            for(int y = 0; y < CHUNK_SIZE; y ++){
                for(int z = 0; z < CHUNK_SIZE; z++){
                    if(r.nextFloat() > 0.7f){
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    }
                    else if(r.nextFloat()>0.4f){
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    }
                    else if(r.nextFloat() > 0.2f){
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    }
                    else{
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                    }
                }
            }
        }
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        rebuildMesh(startX, startY, startZ);
    }
    
    
}
