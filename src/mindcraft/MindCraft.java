/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindcraft;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author andyl
 */
public class MindCraft {
    
    private FPCameraController fp;
    private DisplayMode displayMode;
    public void start(){
        //FPCameraController fp = new FPCameraController(0,0,0);
        try{
            fp = new FPCameraController(0,0,0);
            createWindow();
            initGL();
            fp.gameLoop();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void createWindow()throws Exception{
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for(int i = 0; i < d.length; i++){
            if(d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32){
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("MineCraft");
        Display.create();
    }
    
    public void initGL(){
        glClearColor(0,0,0,0);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        GLU.gluPerspective(100, (float)displayMode.getWidth()/(float)displayMode.getHeight(), 0.1f, 300);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       MindCraft mc = new MindCraft();
       mc.start();
    }
    
}
