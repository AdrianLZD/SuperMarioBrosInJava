package main.java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class GameState {

    private static final int BACKGROUND_OFFSET = -31;
    private static final int INFO_SCREEN_TICKS = 100;
    protected static final int YPOSITION_KILL_LIMIT = WindowManager.WINDOW_HEIGHT + Block.SIZE;
    private static Score scoreInstance;

    protected BufferedImage background;
    protected GameRunner gameRunner;
    protected LevelMap lvlMap;
    protected Mario mario;
    protected int lvlId;
    protected int checkpointPosition;
    protected boolean imagesLoaded;
    protected boolean infoScreen;

    private int infoScreenCounter;
    


    public GameState() {
        loadImages();
        scoreInstance = Score.getInstance();
        gameRunner = GameRunner.instance;
    }

    protected abstract void spawnMario();

    protected abstract void loadImages();

    protected abstract void keyPressed(int k);

    protected abstract void keyReleased(int k);

    protected abstract void tick();

    protected void setCameraPosition(boolean checkpointReached){
        if (checkpointReached) {
            GameRunner.instance.moveHorizontalScroll(checkpointPosition);
        } else {
            GameRunner.instance.moveAbsoluteHorizontalScroll(0);
        }
    }

    protected void getBackground(String backgroundName) {
        background = SpriteAssets.getBackground(backgroundName);
    }

    protected void paintElements(Graphics g){
        paintBackground(g);
    }

    protected void paintBackground(Graphics g){
        g.drawImage(background, 0, BACKGROUND_OFFSET, gameRunner);
    }

    protected void paintInfoScreen(Graphics g){
        scoreInstance.paintFullDetails(g, lvlId);
    }

    protected void definePanelSize(int width, int height){
        Dimension dimension = new Dimension(width, height);
        gameRunner.definePanelSize(dimension);
    }

    protected void createLevelMap(int id){
        if(this instanceof GameStateMenu){
            MyLogger.logInfoMessage("Creating a level map for the menu is unsupported. Aborting task.");
            return;
        }
        lvlMap = new LevelMap(id);
    }

    protected void displayScoreScreen(){
        infoScreen = true;
        infoScreenCounter = 0;
    }

    protected void tickScoreScreen(){
        if(infoScreenCounter > INFO_SCREEN_TICKS){
            infoScreen = false;
        }
        infoScreenCounter++;
    }
}
