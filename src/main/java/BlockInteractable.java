package main.java;

import java.awt.Point;

import main.java.Mario.MarioState;

public class BlockInteractable extends Block {

    private static final long serialVersionUID = 1L;
    private static final int UP_SPEED = 2;
    private PickUp pickUp;
    private byte animationTimer;
    private boolean animating;
    private boolean broken;
    private boolean used;

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
        if(used)
            return;
        
        if (getId() == Block.BREAKABLE){
            if(Mario.getCurrentState().equals(MarioState.BIG)){
                collision = false;
                broken = true;
            }
        }else{
            dropPickUp();
            deactivateBlock();
            used = true;
        }
        animating = true;
        animationTimer = 0;
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
            setLocation(x, y - UP_SPEED);
            currentSprite = Animator.getBlockSprite(Block.BREAKABLE_ANIM1);
        } else if (animationTimer < 20) {
            setLocation(x, y - UP_SPEED);
            currentSprite = Animator.getBlockSprite(Block.BREAKABLE_ANIM2);
        } else if (animationTimer < 30) {
            setLocation(x, y + UP_SPEED * 2);
            currentSprite = Animator.getBlockSprite(Block.BREAKABLE_ANIM3);
        } else {
            deactivateBlock();
            broken = false;
        }
        animationTimer++;
    }

    private void upAnimation(){
        if (animationTimer < 8) {
            setLocation(x, y - UP_SPEED);
        } else if (animationTimer < 16) {
            setLocation(x, y + UP_SPEED);
        } else{
            animating = false;
        }
        animationTimer++;
    }

    public boolean shouldCollide() {
        return collision;
    }

    public PickUp getPickUp(){
        return pickUp;
    }

    public static boolean mustBeInteractable(int id) {
        return id == Block.BREAKABLE || id == Block.MISTERY;
    }

    public static PickUp getPickUp(BlockInteractable block){
        return block.pickUp;
    }
}