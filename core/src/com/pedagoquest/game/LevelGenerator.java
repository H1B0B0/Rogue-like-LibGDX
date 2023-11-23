package com.pedagoquest.game;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class LevelGenerator {
    private Random random;
    private final int minRoomSize;
    private final int maxRoomSize;
    private final int levelWidth;
    private final int levelHeight;

    public LevelGenerator(int minRoomSize, int maxRoomSize, int levelWidth, int levelHeight) {
        this.random = new Random();
        this.minRoomSize = minRoomSize;
        this.maxRoomSize = maxRoomSize;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
    }

    public Level generateLevel(int numberOfRooms, TextureRegion ground, TextureRegion walls) {
        System.out.println("Generating Level...");
        Level level = new Level();
        TiledMapTileLayer wallsLayer = new TiledMapTileLayer(levelWidth, levelHeight, 16, 16);
        generateRooms(level, numberOfRooms, ground, walls, wallsLayer);
        return level;
    }

    private void generateRooms(Level level, int numberOfRooms, TextureRegion ground, TextureRegion walls, TiledMapTileLayer wallsLayer) {
        System.out.println("Generating Rooms...");
    
        int minDistanceBetweenRooms = 50; // Minimum spacing between rooms
    
        for (int i = 0; i < numberOfRooms; i++) {
            System.out.println("Generating Room " + (i + 1) + " out of " + numberOfRooms);
    
            int maxWidthInTiles = Math.max(2, maxRoomSize); // Assuming maxRoomSize is in pixels
            int maxHeightInTiles = Math.max(2, maxRoomSize); // Assuming maxRoomSize is in pixels
            
            int minWidthInTiles = Math.max(2, minRoomSize); // Assuming minRoomSize is in pixels
            int minHeightInTiles = Math.max(2, minRoomSize); // Assuming minRoomSize is in pixels
            
            int widthInTiles = random.nextInt((maxWidthInTiles - minWidthInTiles) / 16 + 1) * 16 + minWidthInTiles;
            int heightInTiles = random.nextInt((maxHeightInTiles - minHeightInTiles) / 16 + 1) * 16 + minHeightInTiles;

            widthInTiles = (widthInTiles / 16) * 16;
            heightInTiles = (heightInTiles / 16) * 16;
    
            int width = widthInTiles;
            int height = heightInTiles;
    
            boolean roomPlaced = false;
            int attempts = 0;
            int maxAttempts = 100;
    
            do {
                int x = random.nextInt(levelWidth - width);
                int y = random.nextInt(levelHeight - height);
    
                if (!doesOverlap(level, x, y, width, height, minDistanceBetweenRooms)) {
                    Room room = new Room(x, y, width, height);
            
                    // Set the door position to match the position where the door image is drawn
                    float doorCenterX = (x + width / 2) - 100;
                    float doorCenterY = y + height / 2;

                    room.setDoorPosition(doorCenterX, doorCenterY);
                    room.setDoorSize(1, 1);
            
                    generateRoomWalls(level, room, walls, wallsLayer);
                    level.addRoom(room);
                    roomPlaced = true;
                }
                attempts++;
            } while (!roomPlaced && attempts < maxAttempts);
    
            if (!roomPlaced) {
                System.out.println("Unable to place room after 100 attempts.");
            }
        }
    }    

    private boolean doesOverlap(Level level, int x, int y, int width, int height, int minDistanceBetweenRooms) {
        for (Room existingRoom : level.getRooms()) {
            if (x < existingRoom.getX() + existingRoom.getWidth() + minDistanceBetweenRooms
                    && x + width > existingRoom.getX() - minDistanceBetweenRooms
                    && y < existingRoom.getY() + existingRoom.getHeight() + minDistanceBetweenRooms
                    && y + height > existingRoom.getY() - minDistanceBetweenRooms) {
                return true;
            }
        }
        return false;
    }

    private void generateRoomWalls(Level level, Room room, TextureRegion walls, TiledMapTileLayer wallsLayer) {
        System.out.println("Generating Walls for Room...");
    
        int topLeftX = room.getX();
        int topLeftY = room.getY();
        int bottomRightX = room.getX() + room.getWidth();
        int bottomRightY = room.getY() + room.getHeight();
    
        // Générer les murs horizontaux
        for (int x = topLeftX; x < bottomRightX; x++) {
            Cell cellTop = new Cell();
            cellTop.setTile(new StaticTiledMapTile(walls));
            wallsLayer.setCell(x, topLeftY, cellTop);
            room.addWallBounds(new Rectangle(x, topLeftY, 1, 1));

            Cell cellBottom = new Cell();
            cellBottom.setTile(new StaticTiledMapTile(walls));
            wallsLayer.setCell(x, bottomRightY, cellBottom);
            room.addWallBounds(new Rectangle(x, bottomRightY + 16, 1, 1));
        }

        // Générer les murs verticaux
        for (int y = topLeftY; y < bottomRightY; y++) {
            Cell cellLeft = new Cell();
            cellLeft.setTile(new StaticTiledMapTile(walls));
            wallsLayer.setCell(topLeftX, y, cellLeft);
            room.addWallBounds(new Rectangle(topLeftX - 1, y, 1, 1));

            Cell cellRight = new Cell();
            cellRight.setTile(new StaticTiledMapTile(walls));
            wallsLayer.setCell(bottomRightX, y, cellRight);
            room.addWallBounds(new Rectangle(bottomRightX + 16, y, 1, 1));
        }
    }     
}
