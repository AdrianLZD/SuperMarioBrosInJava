package main.java;

import java.awt.Graphics;
import java.awt.Point;

import main.java.Mario.MarioState;

public class GameStateLevel1 extends GameState {

    private static boolean checkpointReached = true;

    public GameStateLevel1(){
        super();
        lvlId = 1;
        checkpointPosition = 195 * Block.SIZE; // 84
        initDefaultBehavior(checkpointReached);
    }

    public GameStateLevel1(MarioState marioState){
        this();
        mario.returnToState(marioState);
    }

    @Override
    protected void spawnMario() {
        if(checkpointReached){
            mario = new Mario(new Point(checkpointPosition, 600));
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
        
        super.tick();

        if (mario.x >= checkpointPosition) {
            checkpointReached = true;
        }
    }

    @Override
    public void requestNextLevel() {
        nextLevelRequest++;
        System.out.println("request");
        if(nextLevelRequest == 2){
            checkpointReached = false;
            GameState newGameState = new GameStateLevel2(mario.state);
            gameRunner.setCurrentGameState(newGameState);
        }
    }
    
}
