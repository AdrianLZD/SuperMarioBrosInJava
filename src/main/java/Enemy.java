package main.java;

import java.awt.Graphics;
import java.awt.Point;
import java.util.function.Supplier;

public class Enemy extends PhysicObject {
    private static final long serialVersionUID = 1L;

    private static final int GOOMBA = 1;
    private static final int KOOPA = 2;
    private static final int KOOPA_FLYING = 3;
    private static final int PIRANHA = 4;

    private Supplier<Boolean> tickMethod;
    private int id;
    private int sprite;
    private int verticalVelocity;
    private int horizontalVelocity;
    private boolean isFalling;

    public Enemy(Point position, int id){
        this.id = id;
        setLocation(position);
        setEnemyProperties();
    }

    private void setEnemyProperties(){
        switch(id){
            case GOOMBA:
                sprite = Animator.G_LEFT;
                tickMethod = () -> goombaTick();
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
    }

    public void paintEnemy(Graphics g){
        g.drawImage(Animator.getEnemySprite(sprite), x, y, GameRunner.instance);
    }

    public void tick(){
        tickMethod.get();
    }

    private boolean goombaTick(){
        isFalling = verticalVelocity > 0;
        return true;
    }

    public static boolean mustSpawnEnemy(String[] token){
        return !(token.length < 2 || token[0].charAt(0)!='0');
    }
    
}
