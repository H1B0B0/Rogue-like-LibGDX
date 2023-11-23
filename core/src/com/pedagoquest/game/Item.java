package com.pedagoquest.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Item {
    private String name;
    private int rarity;
    private int health;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int armor;
    private Sprite sprite;
    private float x;
    private float y;
    type ItemType;

    public Item(String name, int rarity, int health, int strength, int dexterity, int intelligence, int armor, type ItemType, Sprite sprite) {
        this.rarity = rarity;
        this.health = health;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.armor = armor;
        this.name = name;
        this.ItemType = ItemType;
        this.sprite = sprite;
        
    }

    public enum type {
        ARMOR,
        WEAPON,
        SHOES,
        HELMET,
        TOP,
        LEGENDARY,  // Add EPIC to the enum
        RARE,
        MYTHIC,
        COMMON
    }

    //#region Getters and Setters
    public String getName() {
        return name;
    }

    public void draw(SpriteBatch batch, Character character) {
        batch.draw(sprite, character.getX() + x, character.getY() + y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, sprite.getWidth() - 54, sprite.getHeight() - 54);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRarity() {
        return rarity;
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getArmor() {
        return armor;
    }

    public type getItemsType() {
        return ItemType;
    }

    public void setItemsType(type ItemsType) {
        this.ItemType = ItemsType;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    //#endregion
}
