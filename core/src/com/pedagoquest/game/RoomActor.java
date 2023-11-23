package com.pedagoquest.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RoomActor extends Actor {
    private Room room;
    private TextureRegion groundTexture;
    private TextureRegion wallsTexture;

    public RoomActor(Room room, TextureRegion groundTexture, TextureRegion wallsTexture) {
        this.room = room;
        this.groundTexture = groundTexture;
        this.wallsTexture = wallsTexture;

        // Définir la position et la taille de l'acteur en fonction de la salle
        setBounds(room.getX(), room.getY(), room.getWidth(), room.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Dessiner le sol de la salle
        for (float x = getX(); x < getX() + getWidth(); x += groundTexture.getRegionWidth()) {
            for (float y = getY(); y < getY() + getHeight(); y += groundTexture.getRegionHeight()) {
                batch.draw(groundTexture, x, y);
            }
        }

        // Dessiner les murs de la salle
        for (float x = getX(); x < getX() + getWidth(); x += wallsTexture.getRegionWidth()) {
            batch.draw(wallsTexture, x, getY() - wallsTexture.getRegionHeight(), wallsTexture.getRegionWidth(),
                    wallsTexture.getRegionHeight());
            batch.draw(wallsTexture, x, getY() + getHeight(), wallsTexture.getRegionWidth(),
                    wallsTexture.getRegionHeight());
        }

        // Dessiner les murs verticaux
        for (float y = getY(); y < getY() + getHeight(); y += wallsTexture.getRegionHeight()) {
            batch.draw(wallsTexture, getX() - wallsTexture.getRegionWidth(), y, wallsTexture.getRegionWidth(),
                    wallsTexture.getRegionHeight());
            batch.draw(wallsTexture, getX() + getWidth(), y, wallsTexture.getRegionWidth(),
                    wallsTexture.getRegionHeight());
        }
    }
}
