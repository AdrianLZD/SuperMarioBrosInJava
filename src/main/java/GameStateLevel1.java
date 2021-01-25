package main.java;

import java.awt.Graphics;

public class GameStateLevel1 extends GameState {

    private int lvlId;

    public GameStateLevel1(){
        super();
        lvlId = 1;
        createLevelMap(lvlId);
    }

    @Override
    protected void loadImages() {
        getBackground("lvl1");
    }

    @Override
    protected void paintElements(Graphics g) {
        super.paintElements(g);
        lvlMap.paintBlocks(g);
    }

    @Override
    protected void keyPressed(int k) {
    }

    @Override
    protected void keyReleased(int k) {
    }
    
}
