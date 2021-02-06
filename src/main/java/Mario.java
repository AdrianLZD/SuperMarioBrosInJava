package main.java;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Mario extends PhysicObject {
    private static final long serialVersionUID = 1L;
    
    protected MarioState state;
    private MarioController controller;

    private int currentSprite;

    public Mario(){
        initializeVariables();
        setLocation(150, 400);
        updateSize();
        controller = new MarioController(this);
    }

    private void initializeVariables(){
        state = MarioState.SMALL;
        currentSprite = Animator.M_SMALL_RIGHT_IDLE;
    }

    private void updateSize(){
        int marioSpriteId;
        if(state.getSize()==0){
            marioSpriteId = Animator.M_SMALL_LEFT_IDLE;
        }else{
            marioSpriteId = Animator.M_BIG_LEFT_IDLE;
        }
        BufferedImage sizeReference = Animator.getMarioSprite(marioSpriteId);
        int width = sizeReference.getWidth();
        int height = sizeReference.getHeight();

        setSize(width, height);
    }

    public void paintMario(Graphics g){
        super.paint(g);
        //g.setColor(Color.GREEN);
        //g.drawRect(x, y, width, height);
        paintSprite(g);
    }

    private void paintSprite(Graphics g){
        g.drawImage(Animator.getMarioSprite(currentSprite), x, y, null);
    }

    public void tick(){
        controller.tick();
        checkCollisions();
    }

    public void keyPressed(int k){
        controller.keyPressed(k);
    }

    public void keyReleased(int k){
        controller.keyReleased(k);
    }
}

enum MarioState {
    SMALL(0), BIG(1), FIRE(1), SMALL_JUMP(0), BIG_JUMP(1), FIRE_JUMP(1), FIRE_SHOOT(1);

    private int size;

    private MarioState(int identifier) {
        size = identifier;
    }

    public int getSize() {
        return size;
    }
}
