package com.pedagoquest.game;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class LootTable {
    //attributes
    private Random random;
    private List<Item> possibleLoot;
    private int totalWeight;
    
    //constructor
    public LootTable() {
        this.random = new Random();
        this.possibleLoot = new ArrayList<>();
        this.totalWeight = 0;
    }

    //methods
    public void addItemToLootTable(Item item) {
        int weight = getWeightByRarity(item.getRarity());
        possibleLoot.add(item);
        totalWeight += weight;
    }

    public Item getRandomItem() {
        int randomIndex = random.nextInt(totalWeight) + 1;
        int currentWeight = 0;
        
        for (Item item : possibleLoot) {
            currentWeight += getWeightByRarity(item.getRarity());
            if (randomIndex <= currentWeight) {
                return item;
            }
        }
        return null;
    }
    private int getWeightByRarity(int rarity) {
        switch (rarity) {
            case 1: return 50;
            case 2: return 30;
            case 3: return 15;
            case 4: return 5;
            default: return 1;
        }
    }
}
