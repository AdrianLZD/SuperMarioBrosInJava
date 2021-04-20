package main.java;

import static main.java.Animator.*;
import static main.java.MarioController.RIGHT;
import java.awt.*;
import java.util.Hashtable;

public class Mario extends PhysicObject {
    private static final long serialVersionUID = 1L;

    private static final int INVINCIBLE_TICKS = 150;
    private static final int DEAD_TICKS = 120;
    private static Mario instance;

    protected MarioState state;
    private MarioController controller;

    private Hashtable<String, Integer> movingSprites;
    private Hashtable<String, Integer> transitionSprites;
    private int currentSprite;
    private int previousSprite;
    private int currentAnimSpeed;
    private int animationTimer;
    private int transitionCounter;
    private int invincibleCounter;
    private int deadCounter;
    
    private boolean canMove;
    private boolean transitioning;
    private boolean invincible;
    private boolean alive;

    public Mario(Point position) {
        movingSprites = new Hashtable<>(16);
        transitionSprites = new Hashtable<>(8);
        controller = new MarioController(this);
        instance = this;
        canMove = true;
        isBlockActivator = true;
        alive = true;
        currentAnimSpeed = ANIMATION_SPEED;
        convertSmall();
        updateSize();
        setLocation(position);
    }

    private void convertSmall(){
        state = MarioState.SMALL;
        if(!transitioning){
            if (controller.getLastDirection() == RIGHT) {
                currentSprite = M_SMALL_RIGHT_IDLE;
            } else {
                currentSprite = M_SMALL_LEFT_IDLE;
            }
        }
        
        movingSprites.put("jump_r", M_SMALL_RIGHT_JUMP);
        movingSprites.put("jump_l", M_SMALL_LEFT_JUMP);
        movingSprites.put("walk1_r", M_SMALL_RIGHT_WALK1);
        movingSprites.put("walk2_r", M_SMALL_RIGHT_WALK2);
        movingSprites.put("walk1_l", M_SMALL_LEFT_WALK1);
        movingSprites.put("walk2_l", M_SMALL_LEFT_WALK2);
        movingSprites.put("idle_r", M_SMALL_RIGHT_IDLE);
        movingSprites.put("idle_l", M_SMALL_LEFT_IDLE);

        transitionSprites.put("idle_r", M_BIG_RIGHT_IDLE);
        transitionSprites.put("idle_l", M_BIG_LEFT_IDLE);
        transitionSprites.put("trans_r",EMPTY);
        transitionSprites.put("trans_l", EMPTY);
    }

    private void convertBig(){
        state = MarioState.BIG;
        y-=Block.SIZE;
        if(controller.getLastDirection() == RIGHT){
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
        state = MarioState.FIRE;
        if (controller.getLastDirection() == RIGHT) {
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
        setColliderSize(Animator.getMarioSprite(marioSpriteId));
    }

    public void tick() { 
        if(alive){
            if (canMove) {
                controller.tick();
                isFalling = controller.isFalling();
                checkCollisions();
                controller.setCollisions(collisions);
            }
            if (invincible && !transitioning) {
                checkIfStillInvincible();
            }
            controller.moveCamera();
        }else{
            killProcess();
        }
                  
        animSprite();
        
    }

    private void checkIfStillInvincible(){
        if (invincibleCounter > INVINCIBLE_TICKS) {
            invincible = false;
        }
        invincibleCounter++;
    }

    private void killProcess(){
        if(deadCounter++ > DEAD_TICKS ){
            Score.getInstance().decreaseLives();
            GameRunner.instance.restartCurrentLevel();
        }
        killAnimation();
        deadCounter++;
    }

    private void killAnimation(){
        if(deadCounter < DEAD_TICKS/4){
            verticalVelocity = -PhysicObject.getGravity();
        }else if(deadCounter < DEAD_TICKS/2){
            verticalVelocity = -PhysicObject.getGravity()/2;
        }else if(deadCounter < DEAD_TICKS/4 * 2){
            verticalVelocity = PhysicObject.getGravity()/2;
        }else{
            verticalVelocity = PhysicObject.getGravity();
        }

        setLocation(x, y + verticalVelocity);
    }

    private void animSprite() {
        if (animationTimer > currentAnimSpeed) {
            if(!alive){
                currentSprite = M_DEAD;
            }else if(transitioning){
                updateTransSprite();
            }else{
                updateAnimSprite();
            }
            animationTimer = 0;
        }
        animationTimer++;
    }

    private void updateAnimSprite(){
        if(invincible){
            if (invincibleCounter % 2 == 0) {
                previousSprite = currentSprite;
                currentSprite = EMPTY;
                return;
            }else if((invincibleCounter-1)%2 == 0){
                currentSprite = previousSprite;
            }
        }

        if (controller.isJumping()) {
            if (controller.getLastDirection() == RIGHT) {
                currentSprite = movingSprites.get("jump_r");
            }else{
                currentSprite = movingSprites.get("jump_l");
            }
        }else

        if (controller.isMovingRight()) {
            if (currentSprite == movingSprites.get("walk1_r")) {
                currentSprite = movingSprites.get("walk2_r");
            }else{
                currentSprite = movingSprites.get("walk1_r");
            }
        }else

        if (controller.isMovingLeft()) {
            if (currentSprite == movingSprites.get("walk1_l")) {
                currentSprite = movingSprites.get("walk2_l");
            }else{
               currentSprite = movingSprites.get("walk1_l");
            }
        }else

        if (controller.getLastDirection() == RIGHT) {
            currentSprite = movingSprites.get("idle_r");
        }else{
            currentSprite = movingSprites.get("idle_l");
        }
    }

    private void updateTransSprite(){
        String direction = (controller.getLastDirection() == RIGHT) ? "r" : "l";

        if (currentSprite == transitionSprites.get("idle_"+direction)) {
            currentSprite = transitionSprites.get("trans_"+direction);
            return;
        }
        currentSprite = transitionSprites.get("idle_"+direction);

        transitionCounter++;
        if (transitionCounter >= 3) {
            resetControls();
            if(invincible){
                startInvincibility();
            }
            return;
        }
    }

    private void startInvincibility(){
        invincibleCounter = 0;
        invincible = true;
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
        startChangeStateAnimation();
        convertBig();
        updateSize();
    }

    public void applyFire(){
        startChangeStateAnimation();
        convertFire();
        updateSize();
    }

    public void applyDamage(){
        if(invincible){
            return;
        }

        if(state == MarioState.SMALL){
            killMario();
        }else if(state == MarioState.BIG || state == MarioState.FIRE){
            startChangeStateAnimation();
            // Override the startChangeStateAnimation currentAnimSpeed
            currentAnimSpeed = (int) (ANIMATION_SPEED * 1.5);
            convertSmall();
            updateSize();
            startInvincibility();
        }
    }

    public void killMario(){
        if(!alive){
            return;
        }
        canMove = false;
        invincible = false;
        alive = false;
        deadCounter = 0;
        
    }

    private void startChangeStateAnimation(){
        canMove = false;
        transitioning = true;
        transitionCounter = 0;
        animationTimer = 0;
        currentAnimSpeed = (int) (ANIMATION_SPEED * 1.75);
    }

    public void activateMiniJump(){
        controller.activateMiniJump();
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

    public boolean isTransitioning(){
        return transitioning;
    }
    
    public boolean isAlive(){
        return alive;
    }

    public enum MarioState {
        SMALL(0), BIG(1), FIRE(1);

        private int size;

        private MarioState(int identifier) {
            size = identifier;
        }

        public int getSize() {
            return size;
        }
    }
}


