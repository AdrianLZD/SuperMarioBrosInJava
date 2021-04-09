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

    private Hashtable<String, Integer> movingSprites;
    private Hashtable<String, Integer> transitionSprites;
    private int currentSprite;
    private int currentAnimSpeed;
    private int animationTimer;
    private int transitionCounter;
    

    private boolean canMove;
    private boolean transitioning;

    public Mario() {
        movingSprites = new Hashtable<>(16);
        transitionSprites = new Hashtable<>(8);
        controller = new MarioController(this);
        instance = this;
        canMove = true;
        currentAnimSpeed = ANIMATION_SPEED;
        convertSmall();
        updateSize();
        setLocation(150, 600);
    }

    private void convertSmall(){
        state = MarioState.SMALL;
        if (controller.getLastDirection() == controller.RIGHT) {
            currentSprite = M_SMALL_RIGHT_IDLE;
        } else {
            currentSprite = M_SMALL_LEFT_IDLE;
        }
        
        movingSprites.put("jump_r", M_SMALL_RIGHT_JUMP);
        movingSprites.put("jump_l", M_SMALL_LEFT_JUMP);
        movingSprites.put("walk1_r", M_SMALL_RIGHT_WALK1);
        movingSprites.put("walk2_r", M_SMALL_RIGHT_WALK2);
        movingSprites.put("walk1_l", M_SMALL_LEFT_WALK1);
        movingSprites.put("walk2_l", M_SMALL_LEFT_WALK2);
        movingSprites.put("idle_r", M_SMALL_RIGHT_IDLE);
        movingSprites.put("idle_l", M_SMALL_LEFT_IDLE);
    }

    private void convertBig(){
        state = MarioState.BIG;
        y-=Block.SIZE;
        if(controller.getLastDirection() == controller.RIGHT){
            currentSprite = M_BIG_RIGHT_IDLE;
        }else{
            currentSprite = M_BIG_LEFT_IDLE;
        }
        
        movingSprites.put("jump_r", M_BIG_RIGHT_JUMP);
        movingSprites.put("jump_l", M_BIG_LEFT_JUMP);
        movingSprites.put("walk1_r", M_BIG_RIGHT_WALK1);
        movingSprites.put("walk2_r", M_BIG_RIGHT_WALK2);
        movingSprites.put("walk1_l", M_BIG_LEFT_WALK1);
        movingSprites.put("walk2_l", M_BIG_LEFT_WALK2);
        movingSprites.put("idle_r", M_BIG_RIGHT_IDLE);
        movingSprites.put("idle_l", M_BIG_LEFT_IDLE);

        transitionSprites.put("idle_r", M_BIG_RIGHT_IDLE);
        transitionSprites.put("idle_l", M_BIG_LEFT_IDLE);
        transitionSprites.put("trans_r", M_SMALL_RIGHT_TRANSITION);
        transitionSprites.put("trans_l", M_SMALL_LEFT_TRANSITION);
    }

    private void convertFire(){
        state = MarioState.BIG;
        if (controller.getLastDirection() == controller.RIGHT) {
            currentSprite = M_FIRE_RIGHT_IDLE;
        } else {
            currentSprite = M_FIRE_LEFT_IDLE;
        }
        movingSprites.put("jump_r", M_FIRE_RIGHT_JUMP);
        movingSprites.put("jump_l", M_FIRE_LEFT_JUMP);
        movingSprites.put("walk1_r", M_FIRE_RIGHT_WALK1);
        movingSprites.put("walk2_r", M_FIRE_RIGHT_WALK2);
        movingSprites.put("walk1_l", M_FIRE_LEFT_WALK1);
        movingSprites.put("walk2_l", M_FIRE_LEFT_WALK2);
        movingSprites.put("idle_r", M_FIRE_RIGHT_IDLE);
        movingSprites.put("idle_l", M_FIRE_LEFT_IDLE);

        transitionSprites.put("idle_r", M_FIRE_RIGHT_IDLE);
        transitionSprites.put("idle_l", M_FIRE_LEFT_IDLE);
        transitionSprites.put("trans_r", M_BIG_RIGHT_IDLE);
        transitionSprites.put("trans_l", M_BIG_LEFT_IDLE);
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
        if(canMove){
            controller.tick();
            controller.setCollisions(checkCollisions(controller.isFalling(), true));
        }
        
        animSprite();
        controller.moveCamera();
    }

    private void animSprite() {
        if (animationTimer > currentAnimSpeed) {
            if(transitioning){
                updateTransSprite();
            }else{
                updateAnimSprite();
            }
            animationTimer = 0;
        }
        animationTimer++;
    }

    private void updateAnimSprite(){
        if (controller.isJumping()) {
            if (controller.getLastDirection() == controller.RIGHT) {
                currentSprite = movingSprites.get("jump_r");
                return;
            }
            currentSprite = movingSprites.get("jump_l");
            return;
        }

        if (controller.isMovingRight()) {
            if (currentSprite == movingSprites.get("walk1_r")) {
                currentSprite = movingSprites.get("walk2_r");
                return;
            }
            currentSprite = movingSprites.get("walk1_r");
            return;
        }

        if (controller.isMovingLeft()) {
            if (currentSprite == movingSprites.get("walk1_l")) {
                currentSprite = movingSprites.get("walk2_l");
                return;
            }
            currentSprite = movingSprites.get("walk1_l");
            return;
        }

        if (controller.getLastDirection() == controller.RIGHT) {
            currentSprite = movingSprites.get("idle_r");
            return;
        }
        currentSprite = movingSprites.get("idle_l");
    }

    private void updateTransSprite(){
        String direction = "r";
        direction = (controller.getLastDirection() == controller.RIGHT) ? "r" : "l";

        if (currentSprite == transitionSprites.get("idle_"+direction)) {
            currentSprite = transitionSprites.get("trans_"+direction);
            return;
        }
        currentSprite = transitionSprites.get("idle_"+direction);

        transitionCounter++;
        if (transitionCounter >= 3) {
            resetControls();
            return;
        }
    }

    private void resetControls(){
        transitioning = false;
        canMove = true;
        currentAnimSpeed = ANIMATION_SPEED;
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
        canMove = false;
        transitioning = true;
        transitionCounter = 0;
        animationTimer = 0;
        currentAnimSpeed = (int) (ANIMATION_SPEED*1.75);
        convertBig();
        updateSize();
    }

    public void applyFire(){
        canMove = false;
        transitioning = true;
        transitionCounter = 0;
        animationTimer = 0;
        currentAnimSpeed = (int) (ANIMATION_SPEED * 1.75);
        convertFire();
        updateSize();
    }

    public void keyPressed(int k) {
        if(canMove){
            controller.keyPressed(k);
        }
        
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


