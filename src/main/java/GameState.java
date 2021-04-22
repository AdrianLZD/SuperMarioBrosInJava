package main.java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class GameState {

    private static final int BACKGROUND_OFFSET = -31;
    private static final int INFO_SCREEN_TICKS = 100;
    protected static final int YPOSITION_KILL_LIMIT = WindowManager.WINDOW_HEIGHT + Block.SIZE;
    protected static Score scoreManager;

    protected BufferedImage background;
    protected GameRunner gameRunner;
    protected LevelMap lvlMap;
    protected Mario mario;
    protected int nextLevelRequest;
    protected int lvlId;
    protected int checkpointPosition;
    protected boolean imagesLoaded;
    protected boolean infoScreen;
    protected boolean levelFinished;

    private int infoScreenCounter;
    
    public GameState() {
        loadImages();
        scoreManager = Score.getInstance();
        gameRunner = GameRunner.instance;
    }

    public void initDefaultBehavior(boolean checkpointReached){
        infoScreen = true;
        nextLevelRequest = 0;
        scoreManager.setTimer(360);
        setCameraPosition(checkpointReached);
        spawnMario();
        createLevelMap(lvlId);
        definePanelSize(SpriteAssets.getBackground("lvl" + lvlId).getWidth(), WindowManager.WINDOW_HEIGHT);

    }

    protected abstract void spawnMario();

    protected abstract void loadImages();

    protected abstract void keyPressed(int k);

    protected abstract void keyReleased(int k);

    protected void tick(){
        mario.tick();

        if (levelFinished) {
            return;
        }

        lvlMap.tickInteractableBlocks();
        lvlMap.tickPickUps();
        lvlMap.tickEnemies();
        lvlMap.tickFireballs();
        lvlMap.removeUsedObjects();
        lvlMap.addNewObjects();

        if (mario.y >= YPOSITION_KILL_LIMIT) {
            mario.killMario();
        }

        scoreManager.tick();
        if (scoreManager.getTimer() <= 0) {
            mario.killMario();
        }
    }

    public abstract void requestNextLevel();

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
        lvlMap.paintPickUps(g);
        lvlMap.paintEnemies(g);
        lvlMap.paintBlocks(g, mario.x);
        mario.paintMario(g);
        lvlMap.paintFireballs(g);
    }

    protected void paintBackground(Graphics g){
        g.drawImage(background, 0, BACKGROUND_OFFSET, gameRunner);
    }

    protected void paintInfoScreen(Graphics g){
        scoreManager.paintFullDetails(g, lvlId);
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

    protected void endLevel(){
        if(levelFinished){
            return;
        }

        if (this instanceof GameStateMenu) {
            MyLogger.logInfoMessage("Ending the menu is not allowed. Aborting task.");
            return;
        }
        mario.startEndAnimation(lvlMap.getFlag());
    }

    
}
