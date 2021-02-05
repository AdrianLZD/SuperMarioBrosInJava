package main.java;

import java.awt.Graphics;

public class GameStateLevel1 extends GameState {

    private int lvlId;

    public GameStateLevel1(){
        super();
        lvlId = 1;
        createLevelMap(lvlId);
        definePanelSize(SpriteAssets.getBackground("lvl1").getWidth(), WindowManager.WINDOW_HEIGHT);

        mario = new Mario();
    }

    @Override
    protected void loadImages() {
        getBackground("lvl1");
    }

    @Override
    protected void paintElements(Graphics g) {
        super.paintElements(g);
        lvlMap.paintBlocks(g);
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
    }
    
}
