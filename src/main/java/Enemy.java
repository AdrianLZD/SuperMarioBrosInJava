package main.java;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.function.Supplier;

public class Enemy extends PhysicObject {
    private static final long serialVersionUID = 1L;

    private static final int GOOMBA = 1;
    private static final int KOOPA = 2;
    private static final int KOOPA_FLYING = 3;
    private static final int PIRANHA = 4;
    private static final int SHELL = 5;

    private static final int GOOMBA_VEL = 2;
    private static final int KOOPA_VEL = 2;
    private static final int KOOPA_FLYING_VEL = 2;
    private static final int PIRANHA_VEL = 1;
    private static final int SHELL_VEL = 6;

    private static Score scoreManager;
    private Mario mario;
    private static int cameraOffset = GameRunner.instance.cameraOffset * 2;

    private Hashtable<String, Integer> tableSprites;
    private Supplier<Boolean> tickMethod;
    private int id;
    private int sprite;
    private int destroyCounter;
    private int animCounter;
    private int behaviorCounter;
    private int destroyTime;
    private boolean alive;
    private boolean active;
    private boolean flipAnimation;

    public Enemy(Point position, int id){
        this.id = id;
        tableSprites = new Hashtable<>(12);
        isBlockActivator = false;
        alive = true;
        setEnemyProperties(position);
        setScoreManager();
        mario = Mario.getCurrentInstance();
    }
    
    private void setEnemyProperties(Point position) {
        switch (id) {
        case GOOMBA:
            sprite = Animator.G_LEFT;
            tickMethod = () -> goombaTick();
            horizontalVelocity = -GOOMBA_VEL;
            verticalVelocity = PhysicObject.getGravity();
            hCollisionOffset = -horizontalVelocity;
            vCollisionOffset = verticalVelocity * 2;
            tableSprites.put("right1", Animator.G_RIGHT);
            tableSprites.put("right2", Animator.G_RIGHT);
            tableSprites.put("left1", Animator.G_LEFT);
            tableSprites.put("left2", Animator.G_LEFT);
            tableSprites.put("dead", Animator.G_SMASH);
            tableSprites.put("flip", Animator.G_FLIP);
            setColliderSize(Animator.getEnemySprite(Animator.G_RIGHT));
            setLocation(position);
            break;
        case KOOPA:
            sprite = Animator.K_NORMAL_LEFT_WALK1;
            tickMethod = () -> koopaTick();
            horizontalVelocity = -KOOPA_VEL;
            verticalVelocity = PhysicObject.getGravity();
            hCollisionOffset = -horizontalVelocity;
            vCollisionOffset = verticalVelocity;
            tableSprites.put("right1", Animator.K_NORMAL_RIGHT_WALK1);
            tableSprites.put("right2", Animator.K_NORMAL_RIGHT_WALK2);
            tableSprites.put("left1", Animator.K_NORMAL_LEFT_WALK1);
            tableSprites.put("left2", Animator.K_NORMAL_LEFT_WALK2);
            tableSprites.put("dead", Animator.K_SHELL_NORMAL);
            tableSprites.put("flip", Animator.K_NORMAL_FLIP);
            setColliderSize(Animator.getEnemySprite(Animator.K_NORMAL_RIGHT_WALK1));
            setLocation(position.x, position.y-Block.SIZE/4);
            break;
        case KOOPA_FLYING:
            sprite = Animator.K_FLY_LEFT_WALK1;
            tickMethod = () -> flyingKoopaTick();
            horizontalVelocity = -KOOPA_FLYING_VEL;
            verticalVelocity = -KOOPA_FLYING_VEL;
            hCollisionOffset = -horizontalVelocity;
            vCollisionOffset = -verticalVelocity;
            tableSprites.put("right1", Animator.K_FLY_RIGHT_WALK1);
            tableSprites.put("right2", Animator.K_FLY_RIGHT_WALK2);
            tableSprites.put("left1", Animator.K_FLY_LEFT_WALK1);
            tableSprites.put("left2", Animator.K_FLY_LEFT_WALK2);
            tableSprites.put("dead", Animator.K_NORMAL_RIGHT_WALK1);
            tableSprites.put("flip", Animator.K_NORMAL_FLIP);
            setColliderSize(Animator.getEnemySprite(Animator.K_FLY_RIGHT_WALK1));
            setLocation(position.x, position.y - Block.SIZE / 4);
            break;
        case PIRANHA:
            sprite = Animator.PI_CLOSE;
            tickMethod = () -> piranhaTick();
            horizontalVelocity = 0;
            verticalVelocity = -PIRANHA_VEL;
            tableSprites.put("right1", Animator.PI_CLOSE);
            tableSprites.put("right2", Animator.PI_OPEN);
            tableSprites.put("left1", Animator.PI_CLOSE);
            tableSprites.put("left2", Animator.PI_OPEN);
            tableSprites.put("dead", Animator.G_RIGHT);
            tableSprites.put("flip", Animator.G_LEFT);
            setColliderSize(Animator.getEnemySprite(Animator.PI_OPEN));
            setLocation(position.x+Block.SIZE/2 + (Block.SIZE - width)/2, position.y + Block.SIZE);
            break;
        case SHELL:
            sprite = Animator.K_SHELL_NORMAL;
            tickMethod = () -> shellTick();
            horizontalVelocity = 0;
            verticalVelocity = 0;
            hCollisionOffset = SHELL_VEL;
            vCollisionOffset = PhysicObject.getGravity();
            tableSprites.put("right1", Animator.K_SHELL_NORMAL);
            tableSprites.put("right2", Animator.K_SHELL_NORMAL);
            tableSprites.put("left1", Animator.K_SHELL_NORMAL);
            tableSprites.put("left2", Animator.K_SHELL_NORMAL);
            tableSprites.put("dead", Animator.K_SHELL_NORMAL);
            tableSprites.put("flip", Animator.K_SHELL_FLIP);
            setColliderSize(Animator.getEnemySprite(Animator.K_SHELL_NORMAL));
            setLocation(position.x, position.y);
            break;
        default:
            break;
        }
    }

    private void setScoreManager(){
        if(scoreManager == null){
            scoreManager = Score.getInstance();
        }
    }

    public void paintEnemy(Graphics g){
        if(active){
            super.paint(g);
            g.drawImage(Animator.getEnemySprite(sprite), x, y, GameRunner.instance);
        }
    }

    public void tick(ArrayList<Enemy> enemies){
        if(!active){
            checkForActivation(mario.x);
            return;
        }

        if(mario.isTransitioning() || !mario.isAlive()){
            return;
        }

        if(alive){
            checkEnemiesCollisions(enemies);
            tickMethod.get();
        }else{
            destroy();
        }
    }

    private void checkForActivation(int marioXPos){
        //Check if enemy is visible
        if (x - cameraOffset <= marioXPos || x <= WindowManager.windowWidth){
            active = true;
        }
    }

    private boolean goombaTick(){
        isFalling = verticalVelocity > 0;
        applyVelocities();
        changeSpriteDirection();
        checkCollisions();
        checkMarioCollisions();
        return true;
    }

    private boolean koopaTick(){
        goombaTick();
        return true;
    }

    private boolean flyingKoopaTick(){
        flyingKoopaApplyVelocities();
        changeSpriteDirection();
        checkCollisions();
        checkMarioCollisions();
        return true;
    }

    private void flyingKoopaApplyVelocities(){
        if (behaviorCounter == 48) {
            verticalVelocity = KOOPA_FLYING_VEL;
        }else if(behaviorCounter > 48){
            behaviorCounter = 0;
        }
        behaviorCounter++;

        if(collisions[PhysicObject.COLLISION_BOTTOM]){
            verticalVelocity = -KOOPA_FLYING_VEL;
            behaviorCounter = 0;
        }

        if (collisions[PhysicObject.COLLISION_TOP]) {
            verticalVelocity = KOOPA_FLYING_VEL;
            behaviorCounter = 0;
        }

        if (collisions[PhysicObject.COLLISION_RIGHT] || collisions[PhysicObject.COLLISION_LEFT]) {
            horizontalVelocity *= -1;
        }

        setLocation(x + horizontalVelocity, y + verticalVelocity);
        
    }

    private boolean piranhaTick(){
        if(behaviorCounter <= 150){
            verticalVelocity = 0;
        }else if(behaviorCounter <= 151 + height){
            verticalVelocity = -1;
        }else if(behaviorCounter <= 151 + height*2){
            verticalVelocity = 1;
        }else{
            behaviorCounter=0;
        }
        behaviorCounter++;
        changeSpriteDirection();
        setLocation(x, y + verticalVelocity);
        checkMarioCollisions();
        
        return true;
    }

    private boolean shellTick(){
        isFalling = verticalVelocity > 0;
        applyVelocities();
        checkCollisions();
        checkMarioCollisions();
        return true;
    }

    private void changeSpriteDirection(){
        if(horizontalVelocity<0){
            if(animCounter > Animator.ANIMATION_SPEED*2){
                if(sprite == tableSprites.get("left1")){
                    sprite = tableSprites.get("left2");
                }else{
                    sprite = tableSprites.get("left1");
                }
                animCounter = 0;
            }
        }else{
            if (animCounter > Animator.ANIMATION_SPEED * 2) {
                if (sprite == tableSprites.get("right1")) {
                    sprite = tableSprites.get("right2");
                }else{
                    sprite = tableSprites.get("right1");
                }
                animCounter = 0;
            }
        }
        animCounter++;
    }

    private void checkMarioCollisions() {
        if(intersects(mario)){
            if (id == PIRANHA){
                mario.applyDamage();
                return;
            }
            //Mario is on top of the enemy
            if (mario.y + mario.height <= y + mario.verticalVelocity - verticalVelocity) {
                if (id == KOOPA_FLYING) {
                    replaceEnemy(KOOPA);
                } else if (id == KOOPA) {
                    replaceEnemy(SHELL);
                } else if (id == SHELL) {
                    mario.activateMiniJump();
                    horizontalVelocity = 0;
                } else {
                    mario.activateMiniJump();
                    kill();
                }
            } else {
                //Shell not moving will not kill mario
                if (id == SHELL && horizontalVelocity == 0) {
                    int direction = 1;
                    if (mario.x > x + width / 2) {
                        direction = -1;
                    }
                    horizontalVelocity = SHELL_VEL * direction;
                    x+=horizontalVelocity*2;
                } else {
                    mario.applyDamage();
                }
            }
        }        
    }

    private void checkEnemiesCollisions(ArrayList<Enemy> enemies){
        for(Enemy e : enemies){
            if(e.isInteractable() && intersects(e) && e!=this){
                if(e.id == SHELL && e.horizontalVelocity != 0){
                    if(id == SHELL && horizontalVelocity != 0){
                        e.killFlip();
                    }
                    killFlip();
                }else{
                    if(id == SHELL && horizontalVelocity!=0){
                        return;
                    }
                    int direction = 1;
                    int offset = e.width;
                    if (e.x > x + width / 2) {
                        offset = - width;
                        direction = -1;
                    }
                    x = e.x + offset;
                    e.horizontalVelocity *= direction;
                    horizontalVelocity *= direction;
                    changeSpriteDirection();
                    e.changeSpriteDirection();
                }
                
            }
        }
    }

    private void replaceEnemy(int id){
        Enemy replacement = new Enemy(getLocation(), id);
        if(horizontalVelocity != 0){
            replacement.horizontalVelocity = Math.abs(replacement.horizontalVelocity) * Integer.signum(horizontalVelocity);
        }
        mario.activateMiniJump();
        LevelMap.addObject(replacement);
        LevelMap.deleteObject(this);
    }

    private void kill(){
        scoreManager.addToPoints(100);
        sprite = tableSprites.get("dead");
        horizontalVelocity = 0;
        verticalVelocity = 0;
        destroyCounter = 0;
        destroyTime = 30;
        alive = false;      
    }

    public void killFlip(){
        if(id == PIRANHA){
            return;
        }
        kill();
        //Override the sprite and destroyTime set by the kill method
        sprite = tableSprites.get("flip");
        destroyTime = 60;
        flipAnimation = true;
    }

    private void destroy(){
        if(flipAnimation){
            if(destroyCounter<10){
                setLocation(x, y-2);
            }else{
                setLocation(x, y+4);
            }
        }
        if(destroyCounter>destroyTime){
            LevelMap.deleteObject(this);
        }
        destroyCounter++;
    }

    public boolean isInteractable(){
        return alive && active;
    }

    public static boolean mustSpawnEnemy(String[] token){
        return !(token.length < 2 || token[0].charAt(0)!='0');
    }
    
}
