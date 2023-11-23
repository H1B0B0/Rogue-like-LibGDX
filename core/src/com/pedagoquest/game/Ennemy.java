package com.pedagoquest.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Ennemy {
    private String name;
    private int level;
    private int health;
    private int mana;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int experience;
    private int damage;
    private int critRate;
    private int armor;
    private float x;
    private float y;
    private boolean isAlive; // Texture for the enemy sprite
    private float speed;
    protected Animation<TextureRegion> walkUpAnimation;
    protected Animation<TextureRegion> walkDownAnimation;
    protected Animation<TextureRegion> walkLeftAnimation;
    protected Animation<TextureRegion> walkRightAnimation;
    private TextureRegion currentFrame;
    private float stateTime;
    private boolean isHit;
    private float hitTimer;

    public Ennemy(String name, int level, int health, int mana, int strength, int dexterity, int intelligence,
            int critRate, int armor, int x, int y, float speed) {
        this.name = name;
        this.level = level;
        this.health = health;
        this.mana = mana;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.critRate = critRate;
        this.armor = armor;
        this.isAlive = true;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.name = name;
        this.level = level;
        // ... other initializations ...

        Texture enemySprite;
        TextureRegion[][] tmpFrames;

        // Load different sprites based on enemy name
        switch (name.toLowerCase()) {
            case "gobelin":
                enemySprite = new Texture(
                        "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\sprite_mob\\gobelin\\gobelin.png");
                break;
            case "orc":
                enemySprite = new Texture(
                        "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\sprite_mob\\orc\\orc.png");
                break;
            case "zombie":
                enemySprite = new Texture(
                        "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\sprite_mob\\zombie\\zombie.png");
                break;
            case "squelette":
                enemySprite = new Texture(
                        "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\sprite_mob\\squelette\\squelette.png");
                break;
            default:
                // Default sprite in case of an unrecognized name
                enemySprite = new Texture("default_sprite.png");
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

        walkUpAnimation = new Animation<>(0.1f, frames[9], frames[10], frames[11]); // Assuming the first row is for
                                                                                    // walking up
        walkDownAnimation = new Animation<>(0.1f, frames[0], frames[1], frames[2]); // And so on...
        walkLeftAnimation = new Animation<>(0.1f, frames[3], frames[4], frames[5]);
        walkRightAnimation = new Animation<>(0.1f, frames[6], frames[7], frames[8]);

        stateTime = 0f;
    }

    public void draw(SpriteBatch spriteBatch, int width, int height) {
        // Draw the current frame of the enemy sprite at its current position
        if (currentFrame != null) {
            spriteBatch.draw(currentFrame, x, y, width, height);
        }
    }

    public void update(float deltaTime, Character character) {
        // Calcul de la direction de l'ennemi
        Vector2 direction = new Vector2(character.getX() - x, character.getY() - y);
        // regarder si le personnage est dans le rayon de l'ennemi
        if (direction.len() > 1) {
            direction.nor();

            // Changement de la position de l'ennemi
            x += (x = direction.x * speed * deltaTime);
            y += (y = direction.y * speed * deltaTime);

            // Mouvement de l'ennemi
            if (Math.abs(direction.x) > Math.abs(direction.y)) {
                if (direction.x > 0) {
                    currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = walkLeftAnimation.getKeyFrame(stateTime, true);
                }
            } else {
                if (direction.y > 0) {
                    currentFrame = walkUpAnimation.getKeyFrame(stateTime, true);
                    // System.out.println("y = " + y);

                } else {
                    currentFrame = walkDownAnimation.getKeyFrame(stateTime, true);
                }
            }
            if (isHit) {
                hitTimer -= deltaTime;
                if (hitTimer <= 0) {
                    isHit = false;
                }
            }
        } else {
            // If the enemy is not moving, you can set a default frame or idle animation
            currentFrame = walkDownAnimation.getKeyFrame(0, true); // Example: Default to the first frame of walking
                                                                   // down
        }

        stateTime += deltaTime;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            isAlive = false;
        } else {
            isHit = true;
            hitTimer = 0.1f; // Hit effect lasts for 0.2 seconds
        }
    }

    // Add a method to get the enemy's bounding box
    public Rectangle getBounds() {
        if (currentFrame != null) {
            return new Rectangle(x, y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        }
        return new Rectangle(x, y, 32, 32);
        // Return an empty rectangle if no current frame is set
    }

    // #region Getters and Setters
    public boolean isHit() {
        return isHit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public int getExperience() {
        int experience = MathUtils.random(10, 50) * level;
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getDamage() {
        this.damage = strength * level;
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getCritRate() {
        return critRate;
    }

    public void setCritRate(int critRate) {
        this.critRate = critRate;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return (int) speed;
    }

    public void setWalkDownAnimation(Animation<TextureRegion> walkDownAnimation) {
        this.walkDownAnimation = walkDownAnimation;
    }

    public void setWalkLeftAnimation(Animation<TextureRegion> walkLeftAnimation) {
        this.walkLeftAnimation = walkLeftAnimation;
    }

    public void setWalkRightAnimation(Animation<TextureRegion> walkRightAnimation) {
        this.walkRightAnimation = walkRightAnimation;
    }

    public void setWalkUpAnimation(Animation<TextureRegion> walkUpAnimation) {
        this.walkUpAnimation = walkUpAnimation;
    }

    public void setHit(boolean hit) {
        isHit = hit;
        if (hit) {
            hitTimer = 0.1f; // Set the hit timer when the enemy is hit
        }
    }

    // #endregion
}