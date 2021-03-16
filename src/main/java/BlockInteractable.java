package main.java;

import java.awt.Graphics;
import java.awt.Point;

public class BlockInteractable extends Block {

    private static final long serialVersionUID = 1L;
    private static final int UP_SPEED = -2;
    private PickUp pickUp;
    private byte animationTimer;
    private boolean animating;
    private boolean broken;

    public BlockInteractable(Point position, int id) {
        super(position, id);
        collision = true;
    }

    public BlockInteractable(Point position, int id, PickUp.Type pType){
        this(position, id);
        this.pickUp = new PickUp(pType);
    }

    @Override
    public void activateBlock() {
        animating = true;
        animationTimer = 0;
        if (getId() == Block.BREAKABLE) {
            collision = false;
            broken = true;
        }
        dropPickUp();
    }  

    private void dropPickUp(){
        if(pickUp != null){
            Point p = new Point(x, y-Block.SIZE);
            pickUp.spawnPickUp(p);
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
    @Override
    public void paintBlock(Graphics g){
        super.paintBlock(g);
        if(pickUp != null){
            pickUp.paintPickUp(g);
        }
    }

    public boolean shouldCollide() {
        return collision;
    }

    public static boolean isInteractable(int id) {
        return id == Block.BREAKABLE || id == Block.MISTERY;
    }
}