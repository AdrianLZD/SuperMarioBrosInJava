package main.java;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Mario extends PhysicObject {
    private static final long serialVersionUID = 1L;
    
    private MarioState state;
    private int currentSprite;

    public Mario(){
        initializeVariables();
        setLocation(150, 400);
        updateSize();
        gravityOn = true;
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
        paintSprite(g);
    }

    private void paintSprite(Graphics g){
        g.drawImage(Animator.getMarioSprite(currentSprite), x, y, null);
    }

    public void keyPressed(int k){
        if(k==KeyEvent.VK_UP){
            setLocation(100,500);
            gravityOn = true;
        }
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
