package com.pedagoquest.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;

public class Room {
    int x, y, width, height;
    private List<List<Integer>> groundTextures;
    private List<Rectangle> wallBounds;
    private int[][] wallTextures;
    private float doorX, doorY;
    private int doorWidth, doorHeight;
    private boolean isExplored = false;
    private Random random;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        wallTextures = new int[width][height]; // Assurez-vous que wallTextures est initialisé
        wallBounds = new ArrayList<>();
        this.random = new Random();
        
        groundTextures = new ArrayList<>();

        // Génération des textures pour chaque cellule de la salle
        for (int posY = 0; posY < height; posY++) {
            List<Integer> row = new ArrayList<>();
            for (int posX = 0; posX < width; posX++) {
                row.add(random.nextInt(20));
            }
            groundTextures.add(row);
        }
    }

    // Getter methods for the properties of the room
    public int getX() {
        return x;
    } 

    public int getGroundTexture(int x, int y) {
        if (x >= 0 && x < groundTextures.size() && y >= 0 && y < groundTextures.get(x).size()) {
            return groundTextures.get(x).get(y);
        } else {
            getNewTexture(x, y);
            return groundTextures.get(x).get(y);
        }
    }
    
    public void getNewTexture(int x, int y) {
        try {
            if (x >= 0 && x < groundTextures.size()) {
                // Assurez-vous que la liste à l'indice x est suffisamment grande
                while (groundTextures.get(x).size() <= y) {
                    groundTextures.get(x).add(random.nextInt(10));
                }
            } else {
                // Ajoutez de nouvelles listes jusqu'à l'indice x
                while (groundTextures.size() <= x) {
                    List<Integer> newRow = new ArrayList<>();
                    groundTextures.add(newRow);
                }
                // Ajoutez de nouveaux éléments à la liste à l'indice x
                while (groundTextures.get(x).size() <= y) {
                    groundTextures.get(x).add(random.nextInt(10));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    public void setWidth(int i) {
        this.width =  i;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public List<Rectangle> getWallBounds() {
        return wallBounds;
    }

    public void addWallBounds(Rectangle bounds) {
        wallBounds.add(bounds);
    }

    public Rectangle getDoorBounds() {
        return new Rectangle(doorX + 8, doorY + 8, doorWidth, doorHeight);
    }

    public void setDoorPosition(float x, float y) {
        this.doorX = x;
        this.doorY = y;
    }

    public void setDoorSize(int width, int height) {
        this.doorWidth = width;
        this.doorHeight = height;
    }

    public boolean isExplored() {
        return isExplored;
    }

    public void setExplored(boolean explored) {
        isExplored = explored;
    }
}
