package main.java;

import java.awt.Graphics;

public class GameStateMenu extends GameState {

    public GameStateMenu() {
        super();
    }

    @Override
    protected void loadImages() {
        loadBackground("menu");
    }

    @Override
    protected void paintElements(Graphics g){
        super.paintElements(g);
    }

    @Override
    protected void keyPressed(int k) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void keyReleased(int k) {
        // TODO Auto-generated method stub

    }
}
