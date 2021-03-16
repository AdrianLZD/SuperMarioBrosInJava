package main.java;

import static main.java.Animator.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mario extends PhysicObject {
    private static final long serialVersionUID = 1L;
    private static Mario instance;

    protected MarioState state;
    private MarioController controller;

    private int currentSprite;
    private int animationTimer;

    public Mario() {
        initializeVariables();
        setLocation(150, 600);
        updateSize();
        controller = new MarioController(this);
        instance = this;
    }

    private void initializeVariables() {
        state = MarioState.SMALL;
        currentSprite = M_SMALL_RIGHT_IDLE;
    }

    private void updateSize() {
        int marioSpriteId;
        if (state.getSize() == 0) {
            marioSpriteId = M_SMALL_LEFT_IDLE;
        } else {
            marioSpriteId = M_BIG_LEFT_IDLE;
        }
        BufferedImage sizeReference = Animator.getMarioSprite(marioSpriteId);
        int width = sizeReference.getWidth();
        int height = sizeReference.getHeight();

        setSize(width, height);
    }

    public void paintMario(Graphics g) {
        super.paint(g);
        //g.setColor(Color.GREEN);
        //g.drawRect(x, y, width, height);
        paintSprite(g);
    }

    private void paintSprite(Graphics g) {
        g.drawImage(Animator.getMarioSprite(currentSprite), x, y, null);
    }

    public void tick() {
        controller.tick();
        checkCollisions(controller.isFalling());
        selectSprite();
        controller.moveCamera();
    }

    private void selectSprite() {
        if (animationTimer > ANIMATION_SPEED) {
            switch (state.getSize()) {
                case 0:
                    selectSmallSprite();
                    break;
                case 1:
                    break;
                default:
                    break;
            }
            animationTimer = 0;
        }
        animationTimer++;
    }

    private void selectSmallSprite(){
        if (controller.isJumping()) {
            if (controller.getLastDirection() == controller.RIGHT) {
                currentSprite = M_SMALL_RIGHT_JUMP;
                return;
            }
            currentSprite = M_SMALL_LEFT_JUMP;
            return;
        }

        if (controller.isMovingRight()) {
            if (currentSprite == M_SMALL_RIGHT_WALK1) {
                currentSprite = M_SMALL_RIGHT_WALK2;
                return;
            }
            currentSprite = M_SMALL_RIGHT_WALK1;
            return;
        }

        if (controller.isMovingLeft()) {
            if (currentSprite == M_SMALL_LEFT_WALK1) {
                currentSprite = M_SMALL_LEFT_WALK2;
                return;
            }
            currentSprite = M_SMALL_LEFT_WALK1;
            return;
        }

        if (controller.getLastDirection() == controller.RIGHT) {
            currentSprite = M_SMALL_RIGHT_IDLE;
            return;
        }
        currentSprite = M_SMALL_LEFT_IDLE;
    }

    public void keyPressed(int k) {
        controller.keyPressed(k);
    }

    public void keyReleased(int k) {
        controller.keyReleased(k);
    }

    public static Mario getCurrentInstance(){
        return instance;
    }

    public MarioState getCurrentState(){
        return state;
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
