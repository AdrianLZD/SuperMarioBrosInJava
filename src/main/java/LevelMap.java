package main.java;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class LevelMap {

    private int id;
    private Block[][] blocks;

    public LevelMap(int id) {
        this.id = id;
        loadMapFile();
    }

    private void loadMapFile() {

        BufferedReader fileBuffer;
        try {
            fileBuffer = openLevelLayoutFile();
            readLevelLayoutFile(fileBuffer);
            fileBuffer.close();
        } catch ( IOException e) {
            String message = "The file layout for level " + id + "could not be read.";
            ErrorLogger.logErrorMessage(message, e);
        }
        
        
    }

    private BufferedReader openLevelLayoutFile() throws FileNotFoundException {
        String mapFilePath = "res/levels/lvl" + id + ".map";
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
                blocks[i][j] = new Block(newBlockPosition, newBlockId);
            }
        }
    }

}
