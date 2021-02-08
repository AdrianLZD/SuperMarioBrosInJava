package main.java;

import java.awt.Point;

public class BlockInteractable extends Block {

    private static final long serialVersionUID = 1L;
    private static final int UP_SPEED = -2;
    private static Mario mario;
    private byte animationTimer;
    private boolean animating;
    private boolean collision;
    private boolean broken;

    public BlockInteractable(Point position, int id) {
        super(position, id);
        mario = Mario.getCurrentInstance();
        collision = true;
    }

    public void activateBlock() {
        animating = true;
        animationTimer = 0;
        if (getId() == Block.BREAKABLE && 
            mario.getCurrentState().getSize() == MarioState.SMALL.getSize()) {
            collision = false;
            broken = true;
        }
    }

    public void tick() {
        if (animating) {
            animateBlock();
        }

    }

    private void animateBlock() {
        if (broken) {
            breakBlock();
        }else{
            upAnimation();
        }
        
    }

    private void breakBlock() {
        if (animationTimer < 10) {
            setLocation(x, y + UP_SPEED);
            currentSprite = Animator.getBlockSprite(Block.BREAKABLE_ANIM1);
        } else if (animationTimer < 20) {
            setLocation(x, y + UP_SPEED);
            currentSprite = Animator.getBlockSprite(Block.BREAKABLE_ANIM2);
        } else if (animationTimer < 30) {
            setLocation(x, y - UP_SPEED * 2);
            currentSprite = Animator.getBlockSprite(Block.BREAKABLE_ANIM3);
        } else {
            deactivateBlock();
            broken = false;
        }
        animationTimer++;
    }

    private void upAnimation(){
        if (animationTimer < 8) {
            setLocation(x, y + UP_SPEED);
        } else if (animationTimer < 16) {
            setLocation(x, y - UP_SPEED);
        } else{
            animating = false;
        }
        animationTimer++;
    }

    public boolean shouldCollide() {
        return collision;
    }

    public static boolean isInteractable(int id) {
        return id == Block.BREAKABLE || id == Block.MISTERY;
    }
}
