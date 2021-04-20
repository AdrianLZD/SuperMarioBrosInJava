package main.java;

import java.awt.*;
import java.io.*;
import java.util.*;

public class LevelMap {

    private static LevelMap instance;
    private Queue<Object> toRemove;
    private Queue<Object> toAdd;
    private Object auxObject;
    private int mapId;
    private Block[][] blocks;
    private ArrayList<BlockInteractable> interactableBlocks;
    private ArrayList<PickUp> pickUps;
    private ArrayList<Enemy> enemies;
    private ArrayList<Fireball> fireballs;
    
    public LevelMap(int mapId) {
        this.mapId = mapId;
        instance = this;
        toAdd = new LinkedList<>();
        toRemove = new LinkedList<>();
        interactableBlocks = new ArrayList<>();
        pickUps = new ArrayList<>();
        enemies = new ArrayList<>();
        fireballs = new ArrayList<>();
        loadMapFile();
    }

    private void loadMapFile() {
        try {
            BufferedReader fileBuffer = openLevelLayoutFile();
            readLevelLayoutFile(fileBuffer);
            fileBuffer.close();
            setPhysicsBlocks();
        } catch ( IOException e) {
            String message = "The file layout for level " + mapId + "could not be read.";
            MyLogger.logErrorMessage(message, e);
        }
    }

    private BufferedReader openLevelLayoutFile() throws FileNotFoundException {
        String mapFilePath = "res/levels/lvl" + mapId + ".map";
        File mapFile = new File(mapFilePath);
        FileReader mapFileReader = new FileReader(mapFile);
        BufferedReader br = new BufferedReader(mapFileReader);
        return br;
    }

    private void readLevelLayoutFile(BufferedReader fileBuffer) throws NumberFormatException, IOException {
        int width = Integer.parseInt(fileBuffer.readLine());
        int height = Integer.parseInt(fileBuffer.readLine());
        blocks = new Block[height][width];

        String fileLine;
        String[] currentToken;
        StringTokenizer stringTokenizer;
        Point newBlockPosition;
        int newBlockId;
        int pickUpType;
        int enemyType;
        for (int i = 0; i < height; i++) {
            fileLine = fileBuffer.readLine();
            stringTokenizer = new StringTokenizer(fileLine);
            for (int j = 0; j < width; j++) {
                newBlockPosition = new Point(j*Block.SIZE,i*Block.SIZE);
                currentToken = stringTokenizer.nextToken().split("\\.");
                newBlockId = Integer.parseInt(currentToken[0]);
                if(Enemy.mustSpawnEnemy(currentToken)){
                    enemyType = Integer.parseInt(currentToken[1]);
                    enemies.add(new Enemy(newBlockPosition, enemyType));
                }

                if(BlockInteractable.mustBeInteractable(newBlockId)){
                    if(currentToken.length > 1){
                        pickUpType = Integer.parseInt(currentToken[1]);
                        blocks[i][j] = new BlockInteractable(newBlockPosition, newBlockId, PickUp.Type.typeById(pickUpType));
                        pickUps.add(BlockInteractable.getPickUp((BlockInteractable)blocks[i][j]));
                    }else{
                        blocks[i][j] = new BlockInteractable(newBlockPosition, newBlockId);
                    }
                    interactableBlocks.add((BlockInteractable) blocks[i][j]);
                }else {
                    blocks[i][j] = new Block(newBlockPosition, newBlockId);
                }
                
            }
        }
    }

    private void setPhysicsBlocks(){
        for(Block[] blockArray : blocks){
            for(Block block : blockArray){
                if(block.getId()!=0){
                    PhysicObject.addMapBlock(block);
                }
            }
        }
    }

    public void paintBlocks(Graphics g, int xPos){
        for(Block[] arrayB: blocks){
            for(Block b : arrayB){
                if(b.x > xPos-Block.SIZE - WindowManager.windowWidth/3 &&
                   b.x < xPos+Block.SIZE + WindowManager.windowWidth){
                    b.paintBlock(g);
                }
            }
        }
    }

    public void paintPickUps(Graphics g){
        for (PickUp p : pickUps) {
            p.paintPickUp(g);
        }
    }

    public void paintEnemies(Graphics g){
        for(Enemy e : enemies){
            e.paintEnemy(g);
        }
    }

    public void paintFireballs(Graphics g){
        for(Fireball f : fireballs){
            f.paintFireball(g);
        }
    }

    public void tickInteractableBlocks(){
        for(BlockInteractable b : interactableBlocks){
            b.tick();
        }
    }

    public void tickPickUps(){
        for(PickUp p : pickUps){
            p.tick();
        }
    }

    public void tickEnemies(){
        for(Enemy e : enemies){
            e.tick(enemies);
        }
    }

    public void tickFireballs(){
        for(Fireball f : fireballs){
            f.tick(enemies);
        }
    }

    public void removeUsedObjects(){
        while(!toRemove.isEmpty()){
            auxObject = toRemove.poll();
            if (auxObject instanceof Enemy) {
                enemies.remove(auxObject);
            } else if (auxObject instanceof PickUp) {
                pickUps.remove(auxObject);
            }else if(auxObject instanceof Fireball){
                fireballs.remove(auxObject);
            }
        }
    }
    
    public void addNewObjects(){
        while (!toAdd.isEmpty()) {
            auxObject = toAdd.poll();
            if (auxObject instanceof Enemy) {
                enemies.add((Enemy)auxObject);
            } else if (auxObject instanceof Fireball) {
                fireballs.add((Fireball)auxObject);
            }
        }
    }

    public Block[][] getBlocks(){
        return blocks;
    }

    public static void addObject(Object o){
        instance.toAdd.add(o);
    }

    public static void deleteObject(Object obj){
        instance.toRemove.add(obj);
    }

}
