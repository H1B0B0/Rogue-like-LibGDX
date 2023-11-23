package com.pedagoquest.game;

import java.util.ArrayList;
import java.util.List;

public abstract class Fight<E extends Ennemy, C extends Character> {
    protected E ennemy;
    protected C character;
    public List<Item> droppedItems = new ArrayList<>();

    public Fight(E ennemy, C character) {
        this.ennemy = ennemy;
        this.character = character;
    }

    public void CharacterAttack(E ennemy, C character) {
        if (character.isAlive() == true) {
            int damage = this.character.getDamage();
            int armor = this.ennemy.getArmor();
            int reducedDamage = (int) (damage - 0.5 * armor);
            ennemy.setHealth(ennemy.getHealth() - reducedDamage);
            EnnemyIsDead(ennemy);
        }
    }

    public void EnnemyAttack(E ennemy, C character) {
        if (character.isAlive() == true) {
            int damage = ennemy.getDamage();
            int armor = character.getArmor();
            int reducedDamage = (int) (damage - 0.5 * armor);
            character.setHealth(character.getHealth() - reducedDamage);
            CharacterIsDead(character);
        }
    }

    public void CharacterIsDead(C character) {
        if (character.getHealth() <= 0) {
            character.setAlive(false);
            System.out.println("The death of " + character.getName() + " is near...");
        }
    }

    public void EnnemyIsDead(E ennemy) {
        if (ennemy.getHealth() <= 0) {
            ennemy.setAlive(false);
        }
    }
}
