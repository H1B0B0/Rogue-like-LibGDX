package com.pedagoquest.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CharacterActor extends Actor {
    private TextureRegion characterRegion;

    public CharacterActor(TextureRegion characterRegion) {
        this.characterRegion = characterRegion;
        setSize(characterRegion.getRegionWidth(), characterRegion.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(characterRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}

