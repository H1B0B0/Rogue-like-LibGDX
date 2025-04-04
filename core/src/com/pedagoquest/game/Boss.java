package com.pedagoquest.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Boss extends Ennemy {
    // Additional Boss-specific attributes (if any)

    public Boss(String name, int level, int health, int mana, int strength, int dexterity, int intelligence, int critRate, int armor, int x, int y, float speed) {
        super(name, level, health, mana, strength, dexterity, intelligence, critRate, armor, x, y, speed);

        //System.out.println("Generating Boss " + name + " at " + x + ", " + y);
        
        // Load animations specific to the boss
        Texture enemySprite;
        TextureRegion[][] tmpFrames;

        // Load different sprites based on boss name
        switch (name.toLowerCase()) {
            case "gobelin":
                enemySprite = new Texture("core/src/com/pedagoquest/game/assets/sprite/sprite_mob/gobelin/boss_gobelin.png");
                System.out.println("Loading boss_gobelin.png");
                break;
            case "orc":
                enemySprite = new Texture("core/src/com/pedagoquest/game/assets/sprite/sprite_mob/orc/boss_orc.png");
                System.out.println("Loading boss_orc.png");
                break;
            case "zombie":
                enemySprite = new Texture("core/src/com/pedagoquest/game/assets/sprite/sprite_mob/zombie/boss_zombie.png");
                System.out.println("Loading boss_zombie.png");
                break;
            case "squelette":
                enemySprite = new Texture("core/src/com/pedagoquest/game/assets/sprite/sprite_mob/squelette/boss_squelette.png");
                System.out.println("Loading boss_squelette.png");
                break;
            default:
                enemySprite = new Texture("core/src/com/pedagoquest/game/assets/sprite/sprite_mob/gobelin/boss_gobelin.png");
                System.out.println("Loading default boss_gobelin.png");
                break;
        }

        tmpFrames = TextureRegion.split(enemySprite, 32, 32);

        int FRAME_COLS = tmpFrames[0].length;
        int FRAME_ROWS = tmpFrames.length;

        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmpFrames[i][j];
            }
        }

        this.walkUpAnimation = new Animation<>(0.5f, frames[9], frames[10], frames[11]);
        this.walkDownAnimation = new Animation<>(0.5f, frames[0], frames[1], frames[2]);
        this.walkLeftAnimation = new Animation<>(0.5f, frames[3], frames[4], frames[5]);
        this.walkRightAnimation = new Animation<>(0.5f, frames[6], frames[7], frames[8]);
    }

    @Override
    public int getExperience() {
        return 100*getLevel();
    }

    @Override
    public int getDamage() {
        return (3*getStrength())+(getLevel()*10);
    }

}
