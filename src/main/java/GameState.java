package main.java;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class GameState {
    protected static BufferedImage background;
    protected boolean imagesLoaded;
    protected GameRunner gameRunner;
    protected LevelMap lvlMap;

    public GameState() {
        if(!imagesLoaded){
            loadImages();
            imagesLoaded = true;
        }
        gameRunner = GameRunner.instance;
    }

    protected abstract void loadImages();

    protected abstract void keyPressed(int k);

    protected abstract void keyReleased(int k);

    protected void loadBackground(String backgroundName) {
        String backgroundFilePath = "res/backgrounds/" + backgroundName + ".png";
        File backgroundFile = new File(backgroundFilePath);
        try {
            background = ImageIO.read(backgroundFile);
        } catch (IOException e) {
            String errorMessage = "The " + backgroundName + " background was not found.";
            ErrorLogger.logErrorMessage(errorMessage, e);
        }
    }

    protected void paintElements(Graphics g){
        paintBackground(g);
    }

    protected void paintBackground(Graphics g){
        //offset calculated taking into account images desired position
        //and an arbitrary value previously used in the app
        int backgroundOffset = -((896-WindowManager.WINDOW_HEIGHT)+31);
        g.drawImage(background, 0, backgroundOffset, gameRunner);
    }

    protected void createLevelMap(int id){
        if(this instanceof GameStateMenu){
            ErrorLogger.logInfoMessage("Creating a level map for the menu is unsupported. Aborting task.");
            return;
        }

        lvlMap = new LevelMap(id);
        
    }

    

}
