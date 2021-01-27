package main.java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class GameState {

    private static final int BACKGROUND_OFFSET = -31;

    protected BufferedImage background;
    protected GameRunner gameRunner;
    protected LevelMap lvlMap;
    protected boolean imagesLoaded;


    public GameState() {
        loadImages();
        gameRunner = GameRunner.instance;
    }

    protected abstract void loadImages();

    protected abstract void keyPressed(int k);

    protected abstract void keyReleased(int k);

    protected void getBackground(String backgroundName) {
        background = SpriteAssets.getBackground(backgroundName);
    }

    protected void paintElements(Graphics g){
        paintBackground(g);
    }

    protected void paintBackground(Graphics g){
        g.drawImage(background, 0, BACKGROUND_OFFSET, gameRunner);
    }

    protected void definePanelSize(int width, int height){
        Dimension dimension = new Dimension(width, height);
        gameRunner.definePanelSize(dimension);
    }

    protected void createLevelMap(int id){
        if(this instanceof GameStateMenu){
            ErrorLogger.logInfoMessage("Creating a level map for the menu is unsupported. Aborting task.");
            return;
        }

        lvlMap = new LevelMap(id);
        
    }

    

}
