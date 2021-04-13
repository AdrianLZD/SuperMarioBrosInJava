package main.java;

import java.awt.Graphics;

public class GameStateLevel1 extends GameState {

    public GameStateLevel1(){
        super();
        lvlId = 1;
        mario = new Mario();
        createLevelMap(lvlId);
        definePanelSize(SpriteAssets.getBackground("lvl"+lvlId).getWidth(), WindowManager.WINDOW_HEIGHT);
    }

    @Override
    protected void loadImages() {
        getBackground("lvl1");
    }

    @Override
    protected void paintElements(Graphics g) {
        super.paintElements(g);
        lvlMap.paintPickUps(g);
        lvlMap.paintEnemies(g, mario.x);
        lvlMap.paintBlocks(g, mario.x);
        mario.paintMario(g);
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
        mario.tick();
        lvlMap.tickInteractableBlocks();
        lvlMap.tickPickUps();
        lvlMap.tickEnemies(mario.x);
        lvlMap.removeUsedObjects();
    }
    
}
