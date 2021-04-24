package main.java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import main.java.Mario.MarioState;

public class GameStateLevel4 extends GameState {

    private int bowserBattleStart;
    private boolean battleStarted;
    private boolean battleFinished;

    private Rectangle finishWallCollider;
    private Block[] battleWall;
    private Block[] finishWall;

    public GameStateLevel4() {
        super();
        lvlId = 4;
        bowserBattleStart = 127 * Block.SIZE;
        initDefaultBehavior(false);
        createBattleWalls();
        
    }

    public GameStateLevel4(MarioState marioState) {
        this();
        mario.returnToState(marioState);
    }

    private void createBattleWalls(){
        Point blockPosition;

        battleWall = new Block[5];
        for(int i = 0; i<battleWall.length; i++){
            blockPosition = new Point(new Point(bowserBattleStart - Block.SIZE, Block.SIZE * (5 + i)));
            battleWall[i] = new Block(blockPosition, Block.CASTLE_GROUND);
        }

        finishWall = new Block[6];
        int xOffset = 0;
        int yOffset = -1;
        for(int i = 0; i<finishWall.length; i++){
            xOffset = (i%2==0) ? Block.SIZE : 0;
            yOffset = (xOffset==0) ? yOffset : yOffset + 1;
            blockPosition = new Point(new Point(bowserBattleStart + 18* Block.SIZE + xOffset, Block.SIZE * (6 + yOffset)));
            finishWall[i] = new Block(blockPosition, Block.CASTLE_GROUND);
        }

        finishWallCollider = new Rectangle(bowserBattleStart + 18* Block.SIZE, 6*Block.SIZE, Block.SIZE, 3 * Block.SIZE);
    }

    @Override
    protected void spawnMario() {
        mario = new Mario(new Point(Block.SIZE * 100, Block.SIZE * 6));
    }

    @Override
    protected void loadImages() {
        getBackground("lvl4");
    }

    @Override
    protected void paintElements(Graphics g) {
        if (infoScreen) {
            paintInfoScreen(g);
            return;
        }
        // Can not use super because mario needs to be painted after the blocks
        paintBackground(g);
        lvlMap.paintPickUps(g);
        lvlMap.paintEnemies(g);
        lvlMap.paintBlocks(g, mario.x);
        mario.paintMario(g);
        lvlMap.paintFireballs(g);

        paintWalls(g);
        
    }

    private void paintWalls(Graphics g){
        if (battleStarted) {
            for (Block b : battleWall) {
                b.paintBlock(g);
            }
        }

        if (!battleFinished) {
            for (Block b : finishWall) {
                b.paintBlock(g);
            }
        }
        g.setColor(Color.RED);
        g.drawRect(finishWallCollider.x, finishWallCollider.y, finishWallCollider.width, finishWallCollider.height);
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
        if(!battleStarted){
            checkIfMarioIsInBattle();
        }

        marioFinishWallCollision();

    }

    private void checkIfMarioIsInBattle(){
        if(mario.x >= bowserBattleStart){
            for(Block b : battleWall){
                PhysicObject.addMapBlock(b);
            }
            battleStarted = true;
        }
    }

    private void marioFinishWallCollision(){
        if(finishWallCollider.intersects(mario)){
            mario.x -= (mario.x + mario.width) - finishWallCollider.x - 1;
        }
    }

    @Override
    public void requestNextLevel() {
        nextLevelRequest++;
        System.out.println("request");
        if (nextLevelRequest == 2) {
            GameState newGameState = new GameStateLevel4(mario.state);
            gameRunner.setCurrentGameState(newGameState);
        }
    }
}