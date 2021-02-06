package main.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class PhysicObject extends Rectangle {

    private static final long serialVersionUID = 1L;
    private static final int gravity = 8;
    private static final int collisionOffset = 1;

    private static ArrayList<Block> mapBlocks = new ArrayList<>();

    private boolean grounded;

    protected int horizontalOffset;
    protected int verticalOffset;

    private Rectangle[] collisions = new Rectangle[4];

    protected void tick() {
        checkIfGrounded();
    }

    protected void applyGravity() {
        setLocation(x, y + gravity);
    }

    protected void checkIfGrounded() {
        grounded = downBlockCollision();
    }

    protected boolean downBlockCollision() {
        Point topLeftCollision = new Point(x, y+(int)getHeight()-verticalOffset);
        Dimension collisionSize = new Dimension((int)getWidth(), verticalOffset*2);
        collisions[0] = new Rectangle(topLeftCollision, collisionSize);
        for (Block block : mapBlocks) {
            if (collisions[0].intersects(block)) {
                setLocation(x, block.y - (int)getHeight() - collisionOffset);
                return true;
            }
        }
        return false;
    }

    protected boolean pointCollision(Point point, PhysicObject pObject) {
        return pObject.contains(point);
    }

    protected boolean pointCollision(Point point, Block block) {
        return block.contains(point);
    }

    protected boolean isColliding(PhysicObject pObject) {
        return intersects(pObject);
    }

    protected Rectangle collision(PhysicObject pObject){
        if(isColliding(pObject))
            return intersection(pObject);
        else
            return null;
    }

    protected void paint(Graphics g){
        g.setColor(Color.blue);
        g.drawRect(collisions[0].x, collisions[0].y, collisions[0].width, collisions[0].height);
    }

    public static void addMapBlock(Block block) {
        mapBlocks.add(block);
    }

    public boolean isGrounded(){
        return grounded;
    }

    public void setGrounded(boolean value){
        grounded = value;
    }

    public static int getGravity(){
        return gravity;
    }
}
