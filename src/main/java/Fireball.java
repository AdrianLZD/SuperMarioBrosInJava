package main.java;

import static main.java.MarioController.RIGHT;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.function.Supplier;

public class Fireball extends PhysicObject{
    public static final int MARIO_FIRE = 0;
    public static final int ENEMY_FIRE = 1;

    private Supplier<Boolean> velocitiesMethod;
    private Hashtable<String, Integer> movingSprites;
    private Hashtable<String, Integer> explosionSprites;

    private int id;
    private int direction;
    private int currentSprite;
    private int spriteCounter;
    private int behaviorCounter;
    private boolean exploting;

    public Fireball(Point position, boolean direction, int id){
        this.id = id;
        this.direction = (direction==RIGHT) ? 1 : -1; 
        spriteCounter = 0;
        setLocation(position);
        defineTypeProperties();
    }

    private void defineTypeProperties(){
        movingSprites = new Hashtable<>(8);
        explosionSprites = new Hashtable<>(6);
        explosionSprites.put("exp1", Animator.FIREBALL_E1);
        explosionSprites.put("exp2", Animator.FIREBALL_E2);
        explosionSprites.put("exp3", Animator.FIREBALL_E3);
        switch(id){
            case MARIO_FIRE:
                velocitiesMethod = () -> marioFireVelocities();
                currentSprite = Animator.FIREBALL_1;
                movingSprites.put("move1", Animator.FIREBALL_1);
                movingSprites.put("move2", Animator.FIREBALL_2);
                movingSprites.put("move3", Animator.FIREBALL_3);
                movingSprites.put("move4", Animator.FIREBALL_4);
                horizontalVelocity = 7 * direction;
                verticalVelocity = 5;
                hCollisionOffset = Math.abs(horizontalVelocity);
                vCollisionOffset = verticalVelocity;
                setColliderSize(Animator.getFireballSprite(currentSprite));
                break;
            case ENEMY_FIRE:
                velocitiesMethod = () -> enemyFireVelocities();
                currentSprite = Animator.FIREBALL_1;
                movingSprites.put("move1", Animator.FIREBALL_1);
                movingSprites.put("move2", Animator.FIREBALL_2);
                movingSprites.put("move3", Animator.FIREBALL_3);
                movingSprites.put("move4", Animator.FIREBALL_4);
                horizontalVelocity = 6 * direction;
                verticalVelocity = PhysicObject.getGravity();
                hCollisionOffset = Math.abs(horizontalVelocity);
                vCollisionOffset = verticalVelocity;
                
                setColliderSize(Animator.getFireballSprite(currentSprite));
                break;
            default:
                break;
        }
    }

    public void paintFireball(Graphics g){
        super.paint(g);
        g.drawImage(Animator.getFireballSprite(currentSprite), x, y, GameRunner.instance);
    }

    public void tick(ArrayList<Enemy> enemies){
        if(!exploting){
            checkCollisions();
            velocitiesMethod.get();
            updateSprite();
            enemiesCollisions(enemies);
            return;
        }
        updateExplosion();       
    }

    private void updateSprite(){
        if(spriteCounter < 10){
            currentSprite = movingSprites.get("move1");
        }else if(spriteCounter < 20){
            currentSprite = movingSprites.get("move2");
        } else if (spriteCounter < 30) {
            currentSprite = movingSprites.get("move3");
        } else if (spriteCounter < 40) {
            currentSprite = movingSprites.get("move4");
        }else{
            spriteCounter = 0;
        }
        spriteCounter ++;
    }

    private void updateExplosion(){
        if (spriteCounter < 5) {
            currentSprite = explosionSprites.get("exp1");
        } else if (spriteCounter < 10) {
            currentSprite = explosionSprites.get("exp2");
        } else if (spriteCounter < 15) {
            currentSprite = explosionSprites.get("exp3");
        } else {
            destroyFireball();
        }

        if(spriteCounter % 5 == 0){
            x -= Animator.getFireballSprite(Animator.FIREBALL_E1).getWidth() / 2;
            y -= Animator.getFireballSprite(Animator.FIREBALL_E1).getHeight() / 2;
        }
        spriteCounter++;
    }
    
    private void enemiesCollisions(ArrayList<Enemy> enemies){
        for(Enemy e : enemies){
            if(e.isInteractable()){
                if(intersects(e)){
                    e.killFire();
                    startExplosion();
                }
            }
        }
    }

    private boolean marioFireVelocities(){
        
        if(behaviorCounter == 15){
            verticalVelocity = 5;
        }else if(behaviorCounter >= 15){
            behaviorCounter = 0;
        }
        behaviorCounter++;

        if (collisions[PhysicObject.COLLISION_BOTTOM]) {
            verticalVelocity = -5;
            behaviorCounter = 0;
        }

        if (collisions[PhysicObject.COLLISION_TOP]) {
            verticalVelocity = 5;
            behaviorCounter = 0;
        }

        if (collisions[PhysicObject.COLLISION_RIGHT] || collisions[PhysicObject.COLLISION_LEFT]) {
            startExplosion();
        }

        setLocation(x + horizontalVelocity, y + verticalVelocity);
        return true;
    }

    private boolean enemyFireVelocities() {
        // TODO
        return true;
    }

    private void startExplosion(){
        spriteCounter = 0;
        exploting = true;
    }
    
    public void destroyFireball() {
        LevelMap.deleteObject(this);
    }
}
