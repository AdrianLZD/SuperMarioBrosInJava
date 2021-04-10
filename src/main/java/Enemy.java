package main.java;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Hashtable;
import java.util.function.Supplier;

public class Enemy extends PhysicObject {
    private static final long serialVersionUID = 1L;

    private static final int GOOMBA = 1;
    private static final int KOOPA = 2;
    private static final int KOOPA_FLYING = 3;
    private static final int PIRANHA = 4;

    private static Score scoreManager;
    private static Mario mario;
    private Hashtable<String, Integer> tableSprites;

    private Supplier<Boolean> tickMethod;
    private int id;
    private int sprite;

    public Enemy(Point position, int id){
        this.id = id;
        isBlockActivator = false;
        tableSprites = new Hashtable<>();
        setLocation(position);
        setEnemyProperties();
        setScoreManager();
        setMarioInstace();
        
    }

    private void setScoreManager(){
        if(scoreManager == null){
            scoreManager = Score.getInstance();
        }
    }

    private void setMarioInstace(){
        if (mario == null) {
            mario = Mario.getCurrentInstance();
        }
    }

    private void setEnemyProperties(){
        switch(id){
            case GOOMBA:
                sprite = Animator.G_LEFT;
                tickMethod = () -> goombaTick();
                horizontalVelocity = -2;
                verticalVelocity = PhysicObject.getGravity();
                hCollisionOffset = -horizontalVelocity;
                vCollisionOffset = verticalVelocity * 2;
                tableSprites.put("right", Animator.G_RIGHT);
                tableSprites.put("left", Animator.G_LEFT);
                setColliderSize(Animator.getEnemySprite(Animator.G_RIGHT));
                break;
            case KOOPA:
                sprite = Animator.K_NORMAL_LEFT_WALK1;
                break;
            case KOOPA_FLYING:
                sprite = Animator.K_FLY_LEFT_WALK1;
                break;
            case PIRANHA:
                sprite = Animator.PI_CLOSE;
                break;
            default:
                break;
        }
        verticalVelocity = PhysicObject.getGravity();
        
    }

    public void paintEnemy(Graphics g){
        super.paint(g);
        g.drawImage(Animator.getEnemySprite(sprite), x, y, GameRunner.instance);
    }

    public void tick(){
        tickMethod.get();
    }

    private boolean goombaTick(){
        isFalling = verticalVelocity > 0;
        applyVelocities();
        changeSpriteDirection();
        checkCollisions();
        checkMarioCollision();
        return true;
    }

    private void changeSpriteDirection(){
        if(horizontalVelocity<0){
            sprite = tableSprites.get("left");
        }else{
            sprite = tableSprites.get("right");
        }
    }

    private void checkMarioCollision() {
        if(intersects(mario.getBounds())){
            if(mario.y + mario.height  <= y + mario.verticalVelocity){
                scoreManager.addToPoints(100);
            }else{
                System.out.println("Mario dead");
            }
        }        
    }

    public static boolean mustSpawnEnemy(String[] token){
        return !(token.length < 2 || token[0].charAt(0)!='0');
    }
    
}
