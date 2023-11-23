package com.pedagoquest.game;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final int capacity = 42;
    private List<Item> items;
    private List<Item> equippedItems;

    public Inventory() {
        this.items = new ArrayList<>(capacity);
        this.equippedItems = new ArrayList<>(4);
    }

    // Ajouter un item à l'inventaire si de la place est disponible
    public boolean addItem(Item item) {
        if (items.size() < capacity) {
            return items.add(item);
        }
        System.out.println("Inventory is full. Cannot add item: " + item.getName());
        return false;
    }

    // Équiper un item du joueur
    public boolean equipItem(Item item, Character character) {
        if (this.containsItem(item)) {
            if (character.equipItem(item)) {
                return items.remove(item);
            }
        }
        return false;
    }
    
    public boolean unequipItem(Item item, Character character) {
        //potentiel bug car l'item est ajouté après avoir été déséquipé
        character.unequipItem(item);
        return this.addItem(item);
    }

    // Jeter un item
    public boolean discardItem(Item item) {
        return items.remove(item);
    }

    //liste de tous les items
    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    //liste de tous les items équipés
    public List<Item> getEquippedItems() {
        return new ArrayList<>(equippedItems);
    }

    // Vérifie si l'inventaire contient un item spécifique
    public boolean containsItem(Item item) {
        return items.contains(item);
    }

    // Vérifie le nombre d'items dans l'inventaire
    public int getItemCount() {
        return items.size();
    }

    // Vérifie la capacité restante de l'inventaire
    public int getAvailableSpace() {
        return capacity - items.size();
    }

}
