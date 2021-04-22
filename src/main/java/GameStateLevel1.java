package main.java;

import java.awt.Graphics;
import java.awt.Point;

import main.java.Mario.MarioState;

public class GameStateLevel1 extends GameState {

    private static final int CHECKPOINT_POSITION = 84 * Block.SIZE; //84
    private static boolean checkpointReached;

    public GameStateLevel1(){
        super();
        infoScreen = true;
        lvlId = 1;
        nextLevelRequest = 0;
        scoreManager.setTimer(360);
        setCameraPosition(checkpointReached);
        spawnMario();
        createLevelMap(lvlId);
        definePanelSize(SpriteAssets.getBackground("lvl"+lvlId).getWidth(), WindowManager.WINDOW_HEIGHT);
    }

    public GameStateLevel1(MarioState marioState){
        this();
        mario.returnToState(marioState);
    }

    @Override
    protected void spawnMario() {
        if(checkpointReached){
            mario = new Mario(new Point(CHECKPOINT_POSITION, 600));
        }else{
            mario = new Mario(new Point(150,600));
        }
    }

    @Override
    protected void loadImages() {
        getBackground("lvl1");
    }

    @Override
    protected void paintElements(Graphics g) {
        if(infoScreen){
            paintInfoScreen(g);
            return;
        }
        super.paintElements(g);
        lvlMap.paintPickUps(g);
        lvlMap.paintEnemies(g);
        lvlMap.paintBlocks(g, mario.x);
        mario.paintMario(g);
        lvlMap.paintFireballs(g);
    }

    @Override
    protected void keyPressed(int k) {
        mario.keyPressed(k);
    }

    @Override
    protected void keyReleased(int k) {
        mario.keyReleased(k);
    }

    @Override
    protected void tick() {
        if(infoScreen){
            tickScoreScreen();
            return;
        }
        
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

        if(mario.x >= CHECKPOINT_POSITION){
            checkpointReached = true;
        }

        if(mario.y >= YPOSITION_KILL_LIMIT){
            mario.killMario();
        }

        scoreManager.tick();
        if(scoreManager.getTimer()<=0){
            mario.killMario();
        }
    }

    @Override
    public void requestNextLevel() {
        nextLevelRequest++;
        if(nextLevelRequest == 1){
            checkpointReached = false;
            GameState newGameState = new GameStateLevel1();
            gameRunner.setCurrentGameState(newGameState);
        }
    }
    
}
