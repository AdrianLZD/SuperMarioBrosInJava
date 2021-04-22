package main.java;

import java.awt.Graphics;
import java.awt.Point;

import main.java.Mario.MarioState;

public class GameStateLevel2 extends GameState{

    private static boolean checkpointReached;

    public GameStateLevel2(){
        super();
        lvlId = 1;
        checkpointPosition = 10 * Block.SIZE;
        initDefaultBehavior(checkpointReached);
    }

    public GameStateLevel2(MarioState marioState){
        this();
        mario.returnToState(marioState);
    }


    @Override
    protected void spawnMario() {
        if (checkpointReached) {
            mario = new Mario(new Point(checkpointPosition, 600));
        } else {
            mario = new Mario(new Point(150, 600));
        }
        
    }

    @Override
    protected void loadImages() {
        getBackground("lvl2");
        
    }

    @Override
    protected void paintElements(Graphics g) {
        if (infoScreen) {
            paintInfoScreen(g);
            return;
        }
        super.paintElements(g);
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
        if (infoScreen) {
            tickScoreScreen();
            return;
        }

        super.tick();

        if (mario.x >= checkpointPosition) {
            checkpointReached = true;
        }
        
    }

    @Override
    public void requestNextLevel() {
        nextLevelRequest++;
        if (nextLevelRequest == 1) {
            checkpointReached = false;
            GameState newGameState = new GameStateLevel2();
            gameRunner.setCurrentGameState(newGameState);
        }
        
    }
    
}
