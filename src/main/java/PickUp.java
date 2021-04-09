package main.java;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import main.java.Mario.MarioState;

public class PickUp extends PhysicObject {
    private static final long serialVersionUID = 1L;

    public static final int COIN = 0;
    public static final int MOOSHROOM = 1;
    public static final int FLOWER = 2;
    public static final int LIFE = 3;
    public static final int STAR = 4;
    public static final int PICKUP_COUNT = 5;

    private static Score scoreManager;
    private static Mario mario;

    private Type type;
    private Supplier<Boolean> tickMethod;
    private int stateCounter;
    private int horizontalVelocity;
    private int verticalVelocity;
    private int id;
    private int sprite;
    
    private boolean[] collisions;
    private boolean active;
    private boolean isFalling;

    public PickUp(Type type){
        this.type = type;
        defineTypeProperties();
        setScoreManager();
        collisions = new boolean[4];

        if (mario == null) {
            mario = Mario.getCurrentInstance();
        }
    }

    private void defineTypeProperties(){
        switch (type.id) {
            case 1:
                tickMethod = () -> coinTick();
                verticalVelocity = 5;
                id = COIN;
                sprite = Animator.P_COIN;
                break;
            case 2:
                tickMethod = () -> coinTick();
                id = COIN;
                verticalVelocity = 5;
                sprite = Animator.P_COIN;
                break;
            case 3:
                //Type of powerup will change depending on Mario state
                tickMethod = () -> powerTick();
                id = -1;
                break;
            case 4:
                tickMethod = () -> powerTick();
                id = LIFE;
                verticalVelocity = PhysicObject.getGravity();
                horizontalVelocity = 3;
                sprite = Animator.P_LIFE;
                hCollisionOffset = horizontalVelocity;
                vCollisionOffset = verticalVelocity * 2;
                setColliderSize(SpriteAssets.getPickUpSprite(LIFE));
                break;
            default:
                break;
        }
    }

    private void setColliderSize(BufferedImage sizeReference){
        int width = sizeReference.getWidth();
        int height = sizeReference.getHeight();

        setSize(width, height);
    }
    
    private void setScoreManager(){
        if(scoreManager == null){
            scoreManager = Score.getInstance();
        }
    }

    public void spawnPickUp(Point p) {
        x = p.x;
        y = p.y + Block.SIZE/2;
        active = true;

        if(type == Type.POWER){
            definePowerBehavoir();
        }
    }

    private void definePowerBehavoir(){
        if (mario.state == MarioState.SMALL) {
            id = MOOSHROOM;
            sprite = Animator.P_MOOSHROOM;
            verticalVelocity = PhysicObject.getGravity();
            horizontalVelocity = 3;
            hCollisionOffset = horizontalVelocity;
            vCollisionOffset = verticalVelocity * 2;
            setColliderSize(Animator.getPickUpSprite(Animator.P_MOOSHROOM));
        } else {
            id = FLOWER;
            sprite = Animator.P_FLOWER;
            setColliderSize(Animator.getPickUpSprite(Animator.P_FLOWER));
            
        }
        
    }

    public void paintPickUp(Graphics g){
        super.paint(g);
        if(active){
            g.drawImage(Animator.getPickUpSprite(sprite), x, y, GameRunner.instance);
        }
    }

    public void tick(){
        if(active){
            tickMethod.get();
        }
    }

    private boolean coinTick() {
        if (stateCounter < 10) {
            y -= verticalVelocity;
            stateCounter++;
        } else if (stateCounter < 50) {
            scoreManager.addToCoins(1);
            scoreManager.addToPoints(100);
            active = false;
        }
        
        return true;
    }

    private boolean powerTick(){
        
        if (stateCounter < Block.SIZE / 2 + 1) {
            y -= 1;
            stateCounter++;
        }else if(id == MOOSHROOM || id == LIFE){
            isFalling = verticalVelocity > 0;
            applyVelocities();
            collisions = checkCollisions(isFalling, false);
        }
        
        checkMarioCollision();
        
        return true;
    }

    private void applyVelocities(){
        if (collisions[PhysicObject.COLLISION_BOTTOM]) {
            verticalVelocity = 0;
        } else {
            verticalVelocity = PhysicObject.getGravity();
        }

        if(collisions[PhysicObject.COLLISION_RIGHT] || collisions[PhysicObject.COLLISION_LEFT]){
            horizontalVelocity *= -1;
        }

        setLocation(x + horizontalVelocity, y + verticalVelocity);
    }

    private void checkMarioCollision(){
        if(intersects(mario.getBounds())){
            if(id == MOOSHROOM){
                mario.applyMooshroom();
                scoreManager.addToPoints(1000);
            }else if(id == FLOWER){
                mario.applyFire();
                scoreManager.addToPoints(1000);
            }
           
            active = false;
        }
    }

    public enum Type {
        COIN(1), COIN_MULTIPLE(2), POWER(3), LIFE(4);

        private int id;

        private Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Type typeById(int id) {
            for (Type t : values()) {
                if (t.id == id) {
                    return t;
                }
            }
            return null;
        }
    }


}
