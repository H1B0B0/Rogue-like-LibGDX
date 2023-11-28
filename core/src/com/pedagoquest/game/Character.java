package com.pedagoquest.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Character {
    private String name;
    private int level;
    private int health;
    private int maxHealth;
    private int mana;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int experience;
    private int currentExp;
    private int expToNextLevel;
    private int damage;
    private int critRate;
    private int armor;
    private int gold;
    private boolean isAlive;
    private int maxLevel;
    private Item ItemArmor;
    private Item ItemWeapon;
    private Item ItemShoes;
    private Item ItemHelmet;
    private Item ItemTop;
    private Vector2 position;
    private Rectangle bounds;
    private float speed;
    private static final int FRAME_COLS = 3;
    private static final int FRAME_ROWS = 4;
    Animation<TextureRegion> walkUpAnimation;
    Animation<TextureRegion> walkDownAnimation;
    Animation<TextureRegion> walkLeftAnimation;
    Animation<TextureRegion> walkRightAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    TextureRegion currentFrame;
    float stateTime;
    private float width;
    private float height;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float previousX;
    private float previousY;
    private int vision;
    private Room currentRoom; // The current room in which the character is
    private Map<String, Item> equippedItems = new HashMap<>();
    private TextureRegion[][] tmp;
    private float invincibilityTime = 1.0f; // 1 second of invincibility
    private float timeSinceLastHit = 0;
    private boolean isLevelingUp = false;
    private float levelUpTime = 0f;
    private float levelUpDuration = 3.0f; // Duration of the level-up effect in seconds
    private Color levelUpColor = new Color(1, 1, 0, 1);
    private boolean isFlashing = false;
    private Color flashingColor = new Color(1, 1, 1, 0.5f); // White color with some transparency
    private Sound deathSound = Gdx.audio.newSound(
            Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\sounds\\burning.mp3"));
    private Sound Stepsound = Gdx.audio.newSound(
            Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\sounds\\step.mp3"));
    private int stepCounter = 0;
    private int stepsPerSound = 23;

    public Character() {
        position = new Vector2(0, 0);
    }

    public Character(String name, int level, int health, int mana, int strength, int dexterity, int intelligence,
            int critRate, int armor, Texture walkSheet) {
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
        this.maxLevel = 100;
        this.position = new Vector2(0, 0);
        this.walkSheet = walkSheet;
        tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        System.out.println("Character " + this.name + " created!");
        this.speed = 100.0f;
        this.bounds = new Rectangle(position.x, position.y, 32, 32); // Initialize the bounding box, set WIDTH and
                                                                     // HEIGHT appropriately
        vision = 200;
        this.maxHealth = health;
        this.gold = 0;

        // Create an array to hold all frames for all animations
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Create the animations for each direction
        walkUpAnimation = new Animation<TextureRegion>(0.1f, walkFrames[9], walkFrames[10], walkFrames[11]);
        walkDownAnimation = new Animation<TextureRegion>(0.1f, walkFrames[0], walkFrames[1], walkFrames[2]);
        walkLeftAnimation = new Animation<TextureRegion>(0.1f, walkFrames[3], walkFrames[4], walkFrames[5]);
        walkRightAnimation = new Animation<TextureRegion>(0.1f, walkFrames[6], walkFrames[7], walkFrames[8]);

        // Initialize stateTime
        stateTime = 0f;
    }

    private void applyFlashingEffect(SpriteBatch batch, float deltaTime) {
        if (timeSinceLastHit < invincibilityTime) {
            // Alternate between white and normal color
            isFlashing = !isFlashing;
            Color flashColor = isFlashing ? Color.WHITE : Color.CLEAR;
            batch.setColor(flashColor);
        } else {
            batch.setColor(Color.WHITE); // Reset to normal color
        }
    }

    public Rectangle getBoundingRectangle() {
        return bounds;
    }

    // #region Getters and Setters
    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int health) {
        this.maxHealth = health;
    }

    public void updateTexture(Texture sprite) {
        this.walkSheet = sprite;
        this.tmp = TextureRegion.split(this.walkSheet, this.walkSheet.getWidth() / FRAME_COLS,
                this.walkSheet.getHeight() / FRAME_ROWS);
        // Create an array to hold all frames for all animations
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        // Create the animations for each direction
        walkUpAnimation = new Animation<TextureRegion>(0.1f, walkFrames[9], walkFrames[10], walkFrames[11]);
        walkDownAnimation = new Animation<TextureRegion>(0.1f, walkFrames[0], walkFrames[1], walkFrames[2]);
        walkLeftAnimation = new Animation<TextureRegion>(0.1f, walkFrames[3], walkFrames[4], walkFrames[5]);
        walkRightAnimation = new Animation<TextureRegion>(0.1f, walkFrames[6], walkFrames[7], walkFrames[8]);
    }

    public Texture getTexture() {
        return this.walkSheet;
    }

    public int getVision() {
        return vision;
    }

    public void setVision(int vision) {
        this.vision = vision;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHealth() {
        return this.health;
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

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
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

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getDamage() {
        return strength + level * 2;
    }

    public void setDamage(int damage) {
        this.damage = strength * level + damage;
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

    public Rectangle getBounds() {
        return this.bounds;
    }

    // #endregion

    public void update(float deltaTime, Control control) {
        // Update stateTime for animations

        if (timeSinceLastHit < invincibilityTime) {
            timeSinceLastHit += deltaTime;
        }

        if (isLevelingUp) {
            levelUpTime += deltaTime;
            if (levelUpTime >= levelUpDuration) {
                isLevelingUp = false;
            }
        }

        stateTime += deltaTime;

        // Update position based on control input
        if (control.up) {
            previousY = position.y; // Store the previous Y position before updating
            position.y += speed * deltaTime;
            stepCounter++;
            if (stepCounter >= stepsPerSound) {
                Stepsound.play(0.5f);
                stepCounter = 0;
            }
            currentFrame = walkUpAnimation.getKeyFrame(stateTime, true);
            this.bounds.setPosition(this.position.x, this.position.y);
        } else if (control.down) {
            previousY = position.y; // Store the previous Y position before updating
            position.y -= speed * deltaTime;
            stepCounter++;
            if (stepCounter >= stepsPerSound) {
                Stepsound.play(0.5f);
                stepCounter = 0;
            }
            currentFrame = walkDownAnimation.getKeyFrame(stateTime, true);
            this.bounds.setPosition(this.position.x, this.position.y);
        } else if (control.left) {
            previousX = position.x; // Store the previous X position before updating
            position.x -= speed * deltaTime;
            stepCounter++;
            if (stepCounter >= stepsPerSound) {
                Stepsound.play(0.5f);
                stepCounter = 0;
            }
            currentFrame = walkLeftAnimation.getKeyFrame(stateTime, true);
            this.bounds.setPosition(this.position.x, this.position.y);
        } else if (control.right) {
            previousX = position.x; // Store the previous X position before updating
            position.x += speed * deltaTime;
            stepCounter++;
            if (stepCounter >= stepsPerSound) {
                Stepsound.play(0.5f);
                stepCounter = 0;
            }
            currentFrame = walkRightAnimation.getKeyFrame(stateTime, true);
            this.bounds.setPosition(this.position.x, this.position.y);
        } else {
            currentFrame = walkDownAnimation.getKeyFrame(0, true);
            this.bounds.setPosition(this.position.x, this.position.y);
        }

        // Collision detection and handling

    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    // Method to update the current room, call this when the character moves
    public void setCurrentRoom(Room newRoom) {
        this.currentRoom = newRoom;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean collidesWith(Rectangle other) {
        return this.bounds.overlaps(other);
    }

    public void draw(SpriteBatch batch) {
        if (timeSinceLastHit < invincibilityTime) {
            batch.setColor(flashingColor); // Set the flashing color
        } else {
            batch.setColor(Color.WHITE); // Reset to normal color
        }

        // Draw the character
        batch.draw(currentFrame, position.x, position.y, currentFrame.getRegionWidth() * scaleX,
                currentFrame.getRegionHeight() * scaleY);

        // Reset the batch color back to normal after drawing the character
        batch.setColor(Color.WHITE);

        if (isLevelingUp) {
            // Apply level-up color effect and draw the character again
            batch.setColor(levelUpColor); // Level-up color
            batch.draw(currentFrame, position.x, position.y, currentFrame.getRegionWidth() * scaleX,
                    currentFrame.getRegionHeight() * scaleY);
            batch.setColor(Color.WHITE); // Reset to default color
        }

    }

    // #region Equipement
    public boolean equipItem(Item item) {
        switch (item.getItemsType()) {
            case ARMOR:
                if (ItemArmor == null || item.getRarity() > ItemArmor.getRarity()) {
                    if (ItemHelmet != null && ItemTop != null) {
                        if ((ItemHelmet != null ? ItemHelmet.getArmor() : 0)
                                + (ItemTop != null ? ItemTop.getArmor() : 0) < (ItemArmor != null ? ItemArmor.getArmor()
                                        : 0)) {
                            ItemArmor = item;
                            unequipItem(ItemHelmet);
                            unequipItem(ItemTop);
                            updateStats(item, true);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        updateStats(item, true);
                        ItemArmor = item;
                        return true;
                    }
                } else {
                    return false;
                }
            case WEAPON:
                if (ItemWeapon == null || item.getRarity() > ItemWeapon.getRarity()) {
                    ItemWeapon = item;
                    updateStats(item, true);
                    return true;
                } else {
                    return false;
                }
            case SHOES:
                if (ItemShoes == null || item.getRarity() > ItemShoes.getRarity()) {
                    ItemShoes = item;
                    updateStats(item, true);
                    return true;
                } else {
                    return false;
                }
            case HELMET:
                if (ItemHelmet == null || item.getRarity() > ItemHelmet.getRarity()) {
                    if (ItemArmor == null) {
                        ItemHelmet = item;
                        updateStats(item, true);
                        return true;
                    } else if (ItemArmor.getRarity() < item.getRarity()) {
                        unequipItem(ItemArmor);
                        ItemHelmet = item;
                        updateStats(item, true);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            case TOP:
                if (ItemTop == null || item.getRarity() > ItemTop.getRarity()) {
                    if (ItemArmor == null) {
                        ItemTop = item;
                        updateStats(item, true);
                        return true;
                    } else if (ItemArmor.getRarity() < item.getRarity()) {
                        unequipItem(ItemArmor);
                        ItemTop = item;
                        updateStats(item, true);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            default:
                System.out.println("Unknown item type.");
                return false;
        }
    }

    public Item getEquippedItem(String itemType) {
        switch (itemType) {
            case "Armor":
                return ItemArmor;
            case "Helmet":
                return ItemHelmet;
            case "Top":
                return ItemTop;
            case "Weapon":
                return ItemWeapon;
            default:
                return null;
        }
    }

    public void unequipItem(Item item) {
        Item itemToUnequip = null;
        switch (item.getItemsType()) {
            case ARMOR:
                itemToUnequip = ItemArmor;
                ItemArmor = null;
                break;
            case WEAPON:
                itemToUnequip = ItemWeapon;
                ItemWeapon = null;
                break;
            case SHOES:
                itemToUnequip = ItemShoes;
                ItemShoes = null;
                break;
            case HELMET:
                itemToUnequip = ItemHelmet;
                ItemHelmet = null;
                break;
            case TOP:
                itemToUnequip = ItemTop;
                ItemTop = null;
                break;
            default:
                System.out.println("Unknown item type.");
                return;
        }
        if (itemToUnequip != null) {
            updateStats(itemToUnequip, false);
        }
    }

    private void updateStats(Item item, boolean equip) {
        if (equip) {
            this.health += item.getHealth();
            this.strength += item.getStrength();
            this.dexterity += item.getDexterity();
            this.intelligence += item.getIntelligence();
            this.armor += item.getArmor();
        } else {
            this.health -= item.getHealth();
            this.strength -= item.getStrength();
            this.dexterity -= item.getDexterity();
            this.intelligence -= item.getIntelligence();
            this.armor -= item.getArmor();
        }
    }
    // #endregion

    // #region Leveling
    public void addExperience(int exp) {
        if (this.level >= this.maxLevel) {
            return;
        } else {
            this.currentExp += exp;
            while (this.currentExp >= this.expToNextLevel) {
                levelUp();
            }
        }
    }

    private void levelUp() {
        this.level++;
        this.currentExp -= this.expToNextLevel;
        this.expToNextLevel = calculateExpToNextLevel(this.level);
        this.maxHealth += 10;
        this.health = this.maxHealth;
        this.strength += 2;
        this.armor += 5;
        System.out.println(this.name + " has leveled up to level " + this.level + "!");
        isLevelingUp = true;
        levelUpTime = 0f;
    }

    public float getPreviousX() {
        return previousX;
    }

    public float getPreviousY() {
        return previousY;
    }

    private int calculateExpToNextLevel(int level) {
        if (level <= 2) {
            return 100;
        }
        return calculateExpToNextLevel(level - 1) + calculateExpToNextLevel(level - 2);
    }

    public int percentageToNextLevel() {
        return (int) ((float) currentExp / (float) expToNextLevel * 100);
    }

    // Méthode pour obtenir la position X du personnage
    public float getX() {
        return position.x;
    }

    // Méthode pour obtenir la position Y du personnage
    public float getY() {
        return position.y;
    }

    // Méthode pour obtenir la largeur du personnage
    public float getWidth() {
        return bounds.width; // Utilisez la largeur de la boîte de collision pour la largeur du personnage
    }

    // Méthode pour obtenir la hauteur du personnage
    public float getHeight() {
        return bounds.height; // Utilisez la hauteur de la boîte de collision pour la hauteur du personnage
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
        this.bounds.setPosition(x, y);
    }
    // #endregion

    public TextureRegion getCurrentFrame() {
        if (currentFrame != null) {
            return new TextureRegion(currentFrame);
        } else {
            return null;
        }
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void takeDamage(int damage) {
        if (timeSinceLastHit >= invincibilityTime) {
            this.health -= damage / (this.armor / 10);
            timeSinceLastHit = 0;
        }

        if (this.health <= 0) {
            this.isAlive = false;
            deathSound.play();
            System.out.println(this.name + " has died.");
        }
    }
}