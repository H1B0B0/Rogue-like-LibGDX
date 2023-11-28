package com.pedagoquest.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Game;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class PedagoQuest extends Game {
    Control control;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    ShapeRenderer shapeRenderer;
    SpriteBatch spriteBatch;
    Texture texture;
    TextureRegion Character;
    Character character;
    int levelWidth = 800; // Largeur du niveau
    int levelHeight = 600; // Hauteur du niveau
    int roomCount = 10; // Nombre de salles par niveau
    float characterX = 100; // Position initiale X du personnage
    float characterY = 100; // Position initiale Y du personnage
    float characterSpeed = 200; // Vitesse de déplacement du personnage
    
    @Override
    public void create() {
        
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        shapeRenderer = new ShapeRenderer(); // Initialisation de shapeRenderer
        spriteBatch = new SpriteBatch(); // Initialisation de spriteBatch
        texture = new Texture("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\sprite_tristan\\naked\\naked.png"); // Initialisation de characterSprite
        Character = new TextureRegion(texture, 32, 0, 32, 32);
        OrthographicCamera camera = new OrthographicCamera();
        control = new Control(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        Gdx.input.setInputProcessor(control);
    }
    
    @Override
    public void render() {
        super.render(); // Call the render method of the current screen
        // Update character position based on controls
        float deltaTime = Gdx.graphics.getDeltaTime();
        character.update(deltaTime, control);
        
        // Draw the character at the updated position
        character.update(Gdx.graphics.getDeltaTime(), control);
        
        spriteBatch.begin();
        
        character.draw(spriteBatch);
        character.update(deltaTime, control);
        spriteBatch.end();
    }
    
    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }    
}