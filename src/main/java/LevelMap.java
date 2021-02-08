package main.java;

import java.awt.*;
import java.io.*;
import java.util.*;

public class LevelMap {

    private int mapId;
    private Block[][] blocks;
    private ArrayList<BlockInteractable> interactableBlocks;

    public LevelMap(int mapId) {
        this.mapId = mapId;
        interactableBlocks = new ArrayList<>();
        loadMapFile();
    }

    private void loadMapFile() {
        try {
            BufferedReader fileBuffer = openLevelLayoutFile();
            readLevelLayoutFile(fileBuffer);
            fileBuffer.close();
            setPhysicsAndInteractableBlocks();
        } catch ( IOException e) {
            String message = "The file layout for level " + mapId + "could not be read.";
            ErrorLogger.logErrorMessage(message, e);
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
        StringTokenizer stringTokenizer;
        Point newBlockPosition;
        int newBlockId;
        for (int i = 0; i < height; i++) {
            fileLine = fileBuffer.readLine();
            stringTokenizer = new StringTokenizer(fileLine);
            for (int j = 0; j < width; j++) {
                newBlockPosition = new Point(j*Block.SIZE,i*Block.SIZE);
                newBlockId = Integer.parseInt(stringTokenizer.nextToken());
                if(BlockInteractable.isInteractable(newBlockId)){
                    blocks[i][j] = new BlockInteractable(newBlockPosition, newBlockId);
                    interactableBlocks.add((BlockInteractable) blocks[i][j]);
                }else{
                    blocks[i][j] = new Block(newBlockPosition, newBlockId);
                }
                
            }
        }
    }

    private void setPhysicsAndInteractableBlocks(){
        for(Block[] blockArray : blocks){
            for(Block block : blockArray){
                if(block.getId()!=0){
                    PhysicObject.addMapBlock(block);
                }
            }
        }

        PhysicObject.setInteractableBlocks(interactableBlocks);
    }

    public void paintBlocks(Graphics g){
        for(Block[] arrayB: blocks){
            for(Block b : arrayB){
                b.paintBlock(g);
            }
        }
    }

    public void tickInteractableBlocks(){
        for(BlockInteractable b : interactableBlocks){
            b.tick();
        }
    }

    public Block[][] getBlocks(){
        return blocks;
    }

}
