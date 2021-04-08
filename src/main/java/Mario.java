package main.java;

import static main.java.Animator.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class Mario extends PhysicObject {
    private static final long serialVersionUID = 1L;
    private static Mario instance;

    protected MarioState state;
    private MarioController controller;

    private Hashtable<String, Integer> tableSprites;
    private int currentSprite;
    private int animationTimer;

    public Mario() {
        tableSprites = new Hashtable<>();
        controller = new MarioController(this);
        instance = this;
        convertSmall();
        updateSize();
        setLocation(150, 600);
    }

    private void convertSmall(){
        state = MarioState.SMALL;
        currentSprite = M_SMALL_RIGHT_IDLE;
        tableSprites.put("jump_r", M_SMALL_RIGHT_JUMP);
        tableSprites.put("jump_l", M_SMALL_LEFT_JUMP);
        tableSprites.put("walk1_r", M_SMALL_RIGHT_WALK1);
        tableSprites.put("walk2_r", M_SMALL_RIGHT_WALK2);
        tableSprites.put("walk1_l", M_SMALL_LEFT_WALK1);
        tableSprites.put("walk2_l", M_SMALL_LEFT_WALK2);
        tableSprites.put("idle_r", M_SMALL_RIGHT_IDLE);
        tableSprites.put("idle_l", M_SMALL_LEFT_IDLE);
    }

    private void convertBig(){
        state = MarioState.BIG;
        y-=Block.SIZE;
        currentSprite = M_BIG_RIGHT_IDLE;
        tableSprites.put("jump_r", M_BIG_RIGHT_JUMP);
        tableSprites.put("jump_l", M_BIG_LEFT_JUMP);
        tableSprites.put("walk1_r", M_BIG_RIGHT_WALK1);
        tableSprites.put("walk2_r", M_BIG_RIGHT_WALK2);
        tableSprites.put("walk1_l", M_BIG_LEFT_WALK1);
        tableSprites.put("walk2_l", M_BIG_LEFT_WALK2);
        tableSprites.put("idle_r", M_BIG_RIGHT_IDLE);
        tableSprites.put("idle_l", M_BIG_LEFT_IDLE);
    }

    private void convertFire(){
        state = MarioState.BIG;
        currentSprite = M_FIRE_RIGHT_IDLE;
        tableSprites.put("jump_r", M_FIRE_RIGHT_JUMP);
        tableSprites.put("jump_l", M_FIRE_LEFT_JUMP);
        tableSprites.put("walk1_r", M_FIRE_RIGHT_WALK1);
        tableSprites.put("walk2_r", M_FIRE_RIGHT_WALK2);
        tableSprites.put("walk1_l", M_FIRE_LEFT_WALK1);
        tableSprites.put("walk2_l", M_FIRE_LEFT_WALK2);
        tableSprites.put("idle_r", M_FIRE_RIGHT_IDLE);
        tableSprites.put("idle_l", M_FIRE_LEFT_IDLE);
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

    public void tick() {
        controller.tick();
        controller.setCollisions(checkCollisions(controller.isFalling(), true));
        animSprite();
        controller.moveCamera();
    }

    private void animSprite() {
        if (animationTimer > ANIMATION_SPEED) {
            selectAnimSprite();
            animationTimer = 0;
        }
        animationTimer++;
    }

    private void selectAnimSprite(){
        if (controller.isJumping()) {
            if (controller.getLastDirection() == controller.RIGHT) {
                currentSprite = tableSprites.get("jump_r");
                return;
            }
            currentSprite = tableSprites.get("jump_l");
            return;
        }

        if (controller.isMovingRight()) {
            if (currentSprite == tableSprites.get("walk1_r")) {
                currentSprite = tableSprites.get("walk2_r");
                return;
            }
            currentSprite = tableSprites.get("walk1_r");
            return;
        }

        if (controller.isMovingLeft()) {
            if (currentSprite == tableSprites.get("walk1_l")) {
                currentSprite = tableSprites.get("walk2_l");
                return;
            }
            currentSprite = tableSprites.get("walk1_l");
            return;
        }

        if (controller.getLastDirection() == controller.RIGHT) {
            currentSprite = tableSprites.get("idle_r");
            return;
        }
        currentSprite = tableSprites.get("idle_l");
    }

    public void paintMario(Graphics g) {
        super.paint(g);
        // g.setColor(Color.GREEN);
        // g.drawRect(x, y, width, height);
        paintSprite(g);
    }

    private void paintSprite(Graphics g) {
        g.drawImage(Animator.getMarioSprite(currentSprite), x, y, null);
    }

    public void applyMooshroom(){
        convertBig();
        updateSize();
    }

    public void applyFire(){
        convertFire();
        updateSize();
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

    public static MarioState getCurrentState(){
        return instance.state;
    }

    public enum MarioState {
        SMALL(0), BIG(1);

        private int size;

        private MarioState(int identifier) {
            size = identifier;
        }

        public int getSize() {
            return size;
        }
    }
}


