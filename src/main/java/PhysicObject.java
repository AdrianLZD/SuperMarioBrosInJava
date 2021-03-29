package main.java;

import java.awt.*;
import java.util.ArrayList;

public class PhysicObject extends Rectangle {

    private static final long serialVersionUID = 1L;
    private static final int GRAVITY = 7;
    private static final int collisionOffset = 0;

    private static ArrayList<Block> mapBlocks = new ArrayList<>();
    private boolean[] collisions = new boolean[4];

    private Rectangle tCollider;
    private Rectangle rCollider;
    private Rectangle bCollider;
    private Rectangle lCollider;

    protected int hCollisionOffset;
    protected int vCollisionOffset;

    

    protected void checkCollisions(boolean isFalling) {
        setTopCollider();
        setRightCollider();
        setBottomCollider();
        setLeftCollider();

        boolean tCollision = false;
        boolean rCollision = false;
        boolean bCollision = false;
        boolean lCollision = false;

        for (Block block : mapBlocks) {
            if (block.isActive()) {
                if (!isFalling && tCollider.intersects(block)) {
                    setLocation(x, block.y + Block.SIZE + collisionOffset);
                    tCollision = true;
                    if(block instanceof BlockInteractable){
                        block.activateBlock();
                    }
                }

                if (rCollider.intersects(block)) {
                    setLocation(block.x - (int) getWidth() - collisionOffset, y);
                    rCollision = true;
                }

                if (bCollider.intersects(block)) {
                    setLocation(x, block.y - (int) getHeight() - collisionOffset);
                    bCollision = true;
                }

                if (lCollider.intersects(block)) {
                    setLocation(block.x + Block.SIZE + collisionOffset, y);
                    lCollision = true;
                }
            }
        }

        collisions[CollisionSide.TOP] = tCollision;
        collisions[CollisionSide.RIGHT] = rCollision;
        collisions[CollisionSide.BOTTOM] = bCollision;
        collisions[CollisionSide.LEFT] = lCollision;
    }

    private void setTopCollider() {
        Point coordinate = new Point(x + hCollisionOffset, y - vCollisionOffset);
        Dimension collisionSize = new Dimension((int) getWidth() - hCollisionOffset * 2, vCollisionOffset * 2);
        tCollider = new Rectangle(coordinate, collisionSize);
    }

    private void setRightCollider() {
        Point coordinate = new Point(x + (int) getWidth() - hCollisionOffset, y + vCollisionOffset);
        Dimension collisionSize = new Dimension(hCollisionOffset * 2, (int) getHeight() - vCollisionOffset * 2);
        rCollider = new Rectangle(coordinate, collisionSize);
    }

    private void setBottomCollider() {
        Point coordinate = new Point(x + hCollisionOffset, y + (int) getHeight() - vCollisionOffset);
        Dimension collisionSize = new Dimension((int) getWidth() - hCollisionOffset * 2, vCollisionOffset * 2);
        bCollider = new Rectangle(coordinate, collisionSize);
    }

    private void setLeftCollider() {
        Point coordinate = new Point(x - hCollisionOffset, y + vCollisionOffset);
        Dimension collisionSize = new Dimension(hCollisionOffset * 2, (int) getHeight() - vCollisionOffset * 2);
        lCollider =  new Rectangle(coordinate, collisionSize);
    }

    protected void paint(Graphics g) {
        //paintColliders(g);
    }

    @SuppressWarnings("unused")
    private void paintColliders(Graphics g) {
        g.setColor(Color.blue);
        setTopCollider();
        g.drawRect(tCollider.x, tCollider.y, tCollider.width, tCollider.height);

        g.setColor(Color.red);
        setRightCollider();
        g.drawRect(rCollider.x, rCollider.y, rCollider.width, rCollider.height);

        g.setColor(Color.green);
        setBottomCollider();
        g.drawRect(bCollider.x, bCollider.y, bCollider.width, bCollider.height);

        g.setColor(Color.black);
        setLeftCollider();
        g.drawRect(lCollider.x, lCollider.y, lCollider.width, lCollider.height);

    }

    public static void addMapBlock(Block block) {
        mapBlocks.add(block);
    }


    public boolean isGrounded() {
        return collisions[CollisionSide.BOTTOM];
    }

    public boolean isTopColliding() {
        return collisions[CollisionSide.TOP];
    }

    public static int getGravity() {
        return GRAVITY;
    }
}

final class CollisionSide {
    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;

}
