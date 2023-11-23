package com.pedagoquest.game;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.Random;

public class CrossLevelGenerator {
    private final Random random;
    private final int levelWidth;
    private final int levelHeight;

    public CrossLevelGenerator(int levelWidth, int levelHeight) {
        this.random = new Random();
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
    }

    public Level generateCrossLevel(TextureRegion ground, TextureRegion walls) {
        Level level = new Level();
        // Place a central room
        int centerWidth = 5;
        int centerHeight = 5;
        int centerX = (levelWidth - centerWidth) / 2;
        int centerY = (levelHeight - centerHeight) / 2;
        Room centerRoom = new Room(centerX, centerY, centerWidth, centerHeight);
        level.addRoom(centerRoom);

        // Place horizontal corridor
        int corridorWidth = 3;
        int corridorHeight = 1;
        int corridorX = (levelWidth - corridorWidth) / 2;
        int corridorY = centerY;
        Room horizontalCorridor = new Room(corridorX, corridorY, corridorWidth, corridorHeight);
        level.addRoom(horizontalCorridor);

        // Place vertical corridor
        int verticalCorridorWidth = 1;
        int verticalCorridorHeight = 3;
        int verticalCorridorX = centerX;
        int verticalCorridorY = (levelHeight - verticalCorridorHeight) / 2;
        Room verticalCorridor = new Room(verticalCorridorX, verticalCorridorY, verticalCorridorWidth, verticalCorridorHeight);
        level.addRoom(verticalCorridor);

        return level;
    }

}
