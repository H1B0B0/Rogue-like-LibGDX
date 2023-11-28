package com.pedagoquest.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Level currentLevel;
    private LevelGenerator levelGenerator;
    private Texture texture;
    private SpriteBatch spriteBatch;
    public TextureRegion ground;
    public TextureRegion ground1;
    public TextureRegion ground2;
    private TextureRegion ground3;
    public TextureRegion walls;
    private Control control;
    private Character character;
    private float zoom = 0.20f;
    private TextureRegion door;
    private List<Integer> exploredRooms = new ArrayList<Integer>();
    private List<Ennemy> enemies;
    private boolean isGenerated = false;
    private List<Item> droppedItems = new ArrayList<>();
    private boolean bosslevel = false;
    private int number_of_current_level = 1;
    private int LastVsision;
    private Boss boss;
    private boolean bossSpawned = false;
    private boolean doorspawned = false;
    private boolean shoproom = false;
    private List<Item> itemsToEquip = new ArrayList<>();
    private Texture health = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\assets_personnage\\hp\\hp.png");
    private Texture shield = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\assets_personnage\\shield\\shield.png");
    private Texture inventory = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\interfaces\\locked_badge.png");
    private Texture xp = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\assets_personnage\\Stats\\Xp.png");
    private Texture remainEnnemies = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\assets_personnage\\Stats\\Ennemies.png");
    private Texture gold = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\assets_personnage\\Stats\\Gold.png");
    private Texture floor = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\assets_personnage\\Stats\\Floor.png");
    private Texture damageTexture = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\assets_personnage\\Stats\\Attack.png");
    private Texture shopPanel = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\menu\\savedGame_Panel.png");
    private ShaderProgram shader_vision;
    private FreeTypeFontGenerator generator;
    private BitmapFont font;
    private Pixmap defaultPixmap;
    private int hotspotX;
    private int hotspotY;
    private Cursor defaultCursor;
    private int previousLevel;
    private float levelUpTimer = 0;
    Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private boolean displayLevelUp = false;
    private SpriteBatch uiBatch = new SpriteBatch();
    private Sound hitSound = Gdx.audio.newSound(
            Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\sounds\\hit_slash.mp3"));
    private Sound BossSound = Gdx.audio.newSound(
            Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\sounds\\boss.mp3"));
    private Sound Levelupsound = Gdx.audio.newSound(
            Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\sounds\\levelup.mp3"));
    private Sound tpSound = Gdx.audio.newSound(
            Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\sounds\\door_open.mp3"));
    private boolean shop = false;
    private Texture shopTexture = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\menu\\shop4x.png");
    private Texture Healthpotion = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\menu\\Conso_1.png");
    private Texture VisionIcon = new Texture(
            "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\menu\\eye_fire1.png");
    private TextureRegion[][] attackFrames;
    private GameController game;
    private Animation<TextureRegion> attackAnimation;
    private float stateTime;
    private boolean isGameOver = false;
    private Stage stage;
    private Texture gameOverTexture;
    private Sprite first_item_texture;
    private Item firstItem;
    private Item secondItem;
    private Sprite second_item_texture;
    private int firstItemPrice;
    private int secondItemPrice;
    private String basePath;
    private int number_max_of_upgrade = 0;
    private int number_of_upgrade_health = 0;

    public GameScreen(GameController game, Character character) {
        // Initialize components
        camera = new OrthographicCamera();
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        enemies = new ArrayList<Ennemy>(); // Initialize the enemies list
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        // Charger la texture de l'environnement
        init_level_texture();
        // Charger la texture du personnage
        levelGenerator = new LevelGenerator(300, 600, 3000, 3000);
        initGameLevel();

        // Initialize the character and control
        this.character = character;

        this.character.setScale(0.5f, 0.5f);
        control = new Control(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        spawnCharacterInRandomRoom();
        Gdx.input.setInputProcessor(control);
        shader_vision = new ShaderProgram(Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\fade_shader.vert"),
                Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\VisionShader.glsl"));

        // Load the font file
        generator = new FreeTypeFontGenerator(
                Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\fonts\\pixel_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Set font size
        parameter.size = 30;

        // Generate the font
        font = generator.generateFont(parameter);

        gameOverTexture = new Texture("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\menu\\deadback.png");

        Texture attackTexture = new Texture(Gdx.files.internal(
                "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\assets_personnage\\attack\\Retro Impact Effect F.png"));

        // Découper l'image en régions de texture
        attackFrames = TextureRegion.split(attackTexture, 64, 64);

        int FRAME_COLS = attackFrames[0].length;
        int FRAME_ROWS = attackFrames.length;
        TextureRegion[] attackAnimationFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                attackAnimationFrames[index++] = attackFrames[i][j];
            }
        }

        attackAnimation = new Animation<>(0.03f, attackAnimationFrames[64], attackAnimationFrames[65],
                attackAnimationFrames[66], attackAnimationFrames[67], attackAnimationFrames[68],
                attackAnimationFrames[69], attackAnimationFrames[70], attackAnimationFrames[71]);

        stateTime = 0f;
        firstItem = generateRandomItem(Item.type.COMMON);
        first_item_texture = firstItem.getSprite();

    }

    private void initGameLevel() {
        levelGenerator = new LevelGenerator(300, 400, 3000, 3000);
        currentLevel = levelGenerator.generateLevel(6, ground, walls);
    }

    private void initBossLevel() {
        levelGenerator = new LevelGenerator(400, 600, 3000, 3000);
        currentLevel = levelGenerator.generateLevel(1, ground, walls);
        bosslevel = true;
        bossSpawned = false;
    }

    private void spawnBoss() {

        int x = (int) this.character.getX() + MathUtils.random(-300, 300);
        int y = (int) this.character.getY() + MathUtils.random(-300, 300);

        // Use a default boss name if the enemies list is empty
        int randomBossName = MathUtils.random(0, 4);
        String bossName = "";
        switch (randomBossName) {
            case 0:
                bossName = "Gobelin";
                break;
            case 1:
                bossName = "Orc";
                break;
            case 2:
                bossName = "Zombie";
                break;
            case 3:
                bossName = "Squelette";
                break;
            default:
                bossName = "Gobelin";
                break;
        }

        int bossLevel = 10 * number_of_current_level;
        int bossHealth = 500 * number_of_current_level * 2;
        int bossMana = 200;
        float speed = 10 + number_of_current_level * 0.3f;

        // Create the boss
        boss = new Boss(bossName, bossLevel, bossHealth, bossMana, 30, 10, 10, 10, 40 * number_of_current_level, x, y,
                speed);

        enemies.add(boss);
    }

    private void setDefaultCursor() {
        // Original cursor image
        Pixmap originalPixmap = new Pixmap(
                Gdx.files.internal("core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\gdx\\cursor_mouse.png"));

        // New dimensions for the cursor
        int newWidth = 16; // For example, double the original size
        int newHeight = 32; // For example, double the original size

        // Create a new Pixmap with the new size
        Pixmap scaledPixmap = new Pixmap(newWidth, newHeight, originalPixmap.getFormat());

        // Draw the original pixmap onto the new one with scaling (resizing)
        scaledPixmap.drawPixmap(
                originalPixmap,
                0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(),
                0, 0, scaledPixmap.getWidth(), scaledPixmap.getHeight());

        originalPixmap.dispose();
        int xHotspot = newWidth / 2;
        int yHotspot = newHeight / 2;
        Cursor customCursor = Gdx.graphics.newCursor(scaledPixmap, xHotspot, yHotspot);

        // Set the cursor
        Gdx.graphics.setCursor(customCursor);
        scaledPixmap.dispose();
    }

    private void init_level_texture() {
        switch (MathUtils.random(0, 4)) {
            case 0:
                texture = new Texture(
                        "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\environment\\tiles_caves.png");
                ground = new TextureRegion(texture, 0, 0, 16, 16);
                ground1 = new TextureRegion(texture, 16, 0, 16, 16);
                ground2 = new TextureRegion(texture, 32, 0, 16, 16);
                ground3 = new TextureRegion(texture, 48, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                door = new TextureRegion(texture, 16, 16, 16, 16);
                break;
            case 1:
                texture = new Texture(
                        "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\environment\\tiles_city.png");
                ground = new TextureRegion(texture, 0, 0, 16, 16);
                ground1 = new TextureRegion(texture, 16, 0, 16, 16);
                ground2 = new TextureRegion(texture, 32, 0, 16, 16);
                ground3 = new TextureRegion(texture, 48, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                door = new TextureRegion(texture, 16, 16, 16, 16);
                break;
            case 2:
                texture = new Texture(
                        "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\environment\\tiles_halls.png");
                ground = new TextureRegion(texture, 0, 0, 16, 16);
                ground1 = new TextureRegion(texture, 16, 0, 16, 16);
                ground2 = new TextureRegion(texture, 32, 0, 16, 16);
                ground3 = new TextureRegion(texture, 48, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                door = new TextureRegion(texture, 16, 16, 16, 16);
                break;
            case 3:
                texture = new Texture(
                        "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\environment\\tiles_prison.png");
                ground = new TextureRegion(texture, 0, 0, 16, 16);
                ground1 = new TextureRegion(texture, 16, 0, 16, 16);
                ground2 = new TextureRegion(texture, 32, 0, 16, 16);
                ground3 = new TextureRegion(texture, 48, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                door = new TextureRegion(texture, 16, 16, 16, 16);
                break;
            case 4:
                texture = new Texture(
                        "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\map\\environment\\tiles_sewers.png");
                ground = new TextureRegion(texture, 0, 0, 16, 16);
                ground1 = new TextureRegion(texture, 16, 0, 16, 16);
                ground2 = new TextureRegion(texture, 32, 0, 16, 16);
                ground3 = new TextureRegion(texture, 48, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                walls = new TextureRegion(texture, 64, 0, 16, 16);
                door = new TextureRegion(texture, 16, 16, 16, 16);
                break;
        }
    }

    public void spawnEnemy() {
        // Get the room where the character currently is
        Room playerRoom = this.character.getCurrentRoom();

        if (playerRoom != null) {
            // Generate a random position within the room
            int x = MathUtils.random(playerRoom.getX(), playerRoom.getX() + playerRoom.getWidth());
            int y = MathUtils.random(playerRoom.getY(), playerRoom.getY() + playerRoom.getHeight());

            // Ensure the enemy is not spawned on the walls or outside the room
            x = MathUtils.clamp(x, playerRoom.getX() + 1, playerRoom.getX() + playerRoom.getWidth() - 1);
            y = MathUtils.clamp(y, playerRoom.getY() + 1, playerRoom.getY() + playerRoom.getHeight() - 1);

            float EnnemySpeed = 10 + number_of_current_level * 0.3f;

            // Choose enemy type based on the current level
            String enemyType;
            int level = number_of_current_level;
            switch (number_of_current_level % 4) {
                case 0:
                    enemyType = "Gobelin";
                    Ennemy enemy = new Ennemy(enemyType, number_of_current_level, 30 + (level * 30), 100,
                            2 * number_of_current_level, 10,
                            10, 10, 10 * number_of_current_level, x, y, EnnemySpeed);
                    enemies.add(enemy);
                    break;
                case 1:
                    enemyType = "zombie";
                    Ennemy zombie = new Ennemy(enemyType, number_of_current_level, 50 + (level * 30), 100,
                            2 * number_of_current_level,
                            10,
                            10, 10, 10 * number_of_current_level, x, y,
                            EnnemySpeed);
                    enemies.add(zombie);
                    break;
                case 2:
                    enemyType = "orc";
                    Ennemy orc = new Ennemy(enemyType, number_of_current_level, 30 + (level * 30), 100,
                            3 * number_of_current_level, 10,
                            10,
                            10, 10 * number_of_current_level, x, y,
                            EnnemySpeed);
                    enemies.add(orc);
                    break;
                case 3:
                    enemyType = "squelette";
                    Ennemy squelette = new Ennemy(enemyType, number_of_current_level, 50 + (level * 30), 100,
                            3 * number_of_current_level,
                            10, 10, 10, 10 * number_of_current_level, x, y,
                            EnnemySpeed);
                    enemies.add(squelette);
                    break;
                default:
                    enemyType = "Gobelin"; // Default case, if needed
                    Ennemy gobelin = new Ennemy(enemyType, number_of_current_level, 50 + (level * 30), 100,
                            2 * number_of_current_level,
                            10,
                            10, 10, 10 * number_of_current_level, x, y,
                            EnnemySpeed);
                    enemies.add(gobelin);
                    break;
            }

            // Create the enemy and add it to the enemies list
        }
    }

    private void spawnCharacterInRandomRoom() {
        // Obtenez une salle aléatoire parmi celles générées
        Room randomRoom = getRandomRoom();

        // Définissez les coordonnées du personnage au centre de la salle
        float characterX = randomRoom.getX() + (randomRoom.getWidth() - this.character.getWidth()) / 2;
        float characterY = randomRoom.getY() + (randomRoom.getHeight() - this.character.getHeight()) / 2;

        // Check if the character is within the room boundaries
        characterX = MathUtils.clamp(characterX, randomRoom.getX(),
                randomRoom.getX() + randomRoom.getWidth() - this.character.getWidth());
        characterY = MathUtils.clamp(characterY, randomRoom.getY(),
                randomRoom.getY() + randomRoom.getHeight() - this.character.getHeight());
        // Définissez les nouvelles coordonnées du personnage
        this.character.setPosition(characterX, characterY);
        exploredRooms.add(currentLevel.getRooms().indexOf(randomRoom));
        if (!bosslevel) {
            shop = true;
        }
        shoproom = true;
    }

    private Room getRandomRoom() {
        // Obtenez la liste des salles générées par le levelGenerator
        List<Room> rooms = currentLevel.getRooms();

        // Choisissez une salle aléatoire
        int randomIndex = MathUtils.random(rooms.size() - 1);

        return rooms.get(randomIndex);
    }

    // Méthode pour dessiner le sol de la salle avec des tuiles
    private void drawRoomFloor(SpriteBatch spriteBatch, TextureRegion ground, Room room) {

        // Draw tiles within the room
        for (int x = room.getX(); x < room.getX() + room.getWidth(); x += 16) {
            for (int y = room.getY(); y < room.getY() + room.getHeight(); y += 16) {
                int groundTexture = room.getGroundTexture((x - room.getX()) / 16,
                        (y - room.getY()) / 16);
                switch (groundTexture) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        spriteBatch.draw(ground, x, y, 16, 16);
                        break;
                    case 6:
                    case 7:
                        spriteBatch.draw(ground1, x, y, 16, 16);
                        break;
                    case 8:
                        spriteBatch.draw(ground2, x, y, 16, 16);
                        break;
                    case 9:
                        spriteBatch.draw(ground3, x, y, 16, 16);
                        break;
                    default:
                        spriteBatch.draw(ground, x, y, 16, 16);
                        break;
                }
            }
        }
    }

    private void drawRoomWalls(SpriteBatch spriteBatch, TextureRegion walls, Room room) {
        int topLeftX = room.getX();
        int topLeftY = room.getY();
        int bottomRightX = (room.getX() + room.getWidth());
        int bottomRightY = (room.getY() + room.getHeight());

        // Dessin des murs horizontaux
        for (int x = topLeftX; x < bottomRightX; x += walls.getRegionWidth()) {
            spriteBatch.draw(walls, x, topLeftY - walls.getRegionHeight(), walls.getRegionWidth(),
                    walls.getRegionHeight());
            spriteBatch.draw(walls, x, bottomRightY, walls.getRegionWidth(), walls.getRegionHeight());
        }

        // Dessin des murs verticaux
        for (int y = topLeftY; y < bottomRightY; y += walls.getRegionHeight()) {
            spriteBatch.draw(walls, topLeftX - walls.getRegionWidth(), y, walls.getRegionWidth(),
                    walls.getRegionHeight());
            spriteBatch.draw(walls, bottomRightX, y, walls.getRegionWidth(), walls.getRegionHeight());
        }
    }

    private void checkCollisions() {
        // Iterate over walls in the current level
        for (Room room : currentLevel.getRooms()) {
            checkRoomCollisions(room);
        }
    }

    private void checkRoomCollisions(Room room) {
        // Iterate over walls in the room
        for (Rectangle wallBounds : room.getWallBounds()) {
            if (this.character.collidesWith(wallBounds)) {
                // Calculate the change in position
                float deltaX = this.character.getX() - this.character.getPreviousX();
                float deltaY = this.character.getY() - this.character.getPreviousY();

                // Handle collision - adjust character position
                adjustCharacterPosition(wallBounds, deltaX, deltaY);
            }
        }
    }

    private void adjustCharacterPosition(Rectangle wallBounds, float deltaX, float deltaY) {
        float characterX = this.character.getX();
        float characterY = this.character.getY();

        float characterWidth = this.character.getWidth();
        float characterHeight = this.character.getHeight();

        if (deltaX < 0 && characterX < wallBounds.getX() + wallBounds.getWidth()
                && characterX + characterWidth > wallBounds.getX() + wallBounds.getWidth()) {
            characterX = wallBounds.getX() + wallBounds.getWidth();
        } else if (deltaX > 0 && characterX + characterWidth > wallBounds.getX() && characterX < wallBounds.getX()) {
            characterX = wallBounds.getX() - characterWidth;
        }

        if (deltaY < 0 && characterY < wallBounds.getY() + wallBounds.getHeight()
                && characterY + characterHeight > wallBounds.getY() + wallBounds.getHeight()) {
            characterY = wallBounds.getY() + wallBounds.getHeight();
        } else if (deltaY > 0 && characterY + characterHeight > wallBounds.getY() && characterY < wallBounds.getY()) {
            characterY = wallBounds.getY() - characterHeight;
        }

        // Set the new position for the character
        this.character.setPosition(characterX, characterY);
    }

    private void drawdoor(SpriteBatch spriteBatch, Room room) {
        if (enemies.size() > 0) {
            return;
        }
        if (door != null) {
            // Calculate the center of the room
            float centerX = (room.getX() + room.getWidth() / 2) - 100;
            float centerY = (room.getY() + room.getHeight() / 2);

            // Adjust position to draw the sprite at the center of the room
            float doorX = (centerX - door.getRegionWidth() / 2);
            float doorY = (centerY - door.getRegionHeight() / 2);

            // Draw the door sprite
            spriteBatch.draw(door, doorX, doorY, 16, 16);
            if (!shop && !shoproom) {
                doorspawned = true;
            }
        }
    }

    private Room CurrentRoom() {
        float characterX = this.character.getX();
        float characterY = this.character.getY();

        for (Room room : currentLevel.getRooms()) {
            if (characterX >= room.getX() && characterX < room.getX() + room.getWidth() &&
                    characterY >= room.getY() && characterY < room.getY() + room.getHeight()) {
                return room;
            }
        }
        return null;
    }

    @Override
    public void show() {
        setDefaultCursor();
        previousLevel = this.character.getLevel();
    }

    private void update(float delta) {
        // Update the character
        Room currentRoom = CurrentRoom();
        if (currentRoom != null && !currentRoom.equals(this.character.getCurrentRoom())) {
            int roomIndex = currentLevel.getRooms().indexOf(currentRoom);
            if (!exploredRooms.contains(roomIndex)) {
                // The room is being entered for the first time
                this.character.setCurrentRoom(currentRoom);
                exploredRooms.add(roomIndex);

                // Spawn enemies for the new room
                int randomEnemyNumber = MathUtils.random(1 + number_of_current_level, 5 + number_of_current_level);
                enemies.clear();
                if (isGenerated == false) {
                    for (int i = 0; i < randomEnemyNumber; i++) {
                        spawnEnemy();
                    }
                }
            }

            if (bosslevel) {
                if (!bossSpawned) {
                    spawnBoss();
                    bossSpawned = true;
                }
            }
        }

        for (Room room : currentLevel.getRooms()) {
            if (characterOverlapsWithDoor(this.character, room)) {
                if (room.isExplored()) {
                    continue;
                } else {
                    teleportCharacterToRandomRoom(this.character, currentLevel.getRooms(), exploredRooms);
                    isGenerated = false;
                    break;
                }
            }
        }

        for (Ennemy enemy : enemies) {
            enemy.update(delta, this.character);

            float attackRange = 10;
            float distanceToPlayer = Vector2.dst(enemy.getX(), enemy.getY(), this.character.getX(),
                    this.character.getY());

            if (boss != null && boss.isAlive()) {
                boss.update(delta, this.character);
            }

            if (distanceToPlayer <= attackRange) {
                attackPlayer(enemy);
            }

            if (boss != null && boss.isAlive()) {
                boss.update(delta, this.character);

                float bossAttackRange = 10;
                if (distanceToPlayer <= bossAttackRange) {
                    attackPlayer(boss);
                }
            }
            // Update the boss if it's alive and part of the game
            if (boss != null && boss.isAlive()) {
                boss.update(delta, this.character);
            }
        }
    }

    private void attackPlayer(Ennemy attacker) {
        int damage = attacker.getDamage();
        this.character.takeDamage(damage);
        // System.out.println("Attacking player. Player health: " +
        // character.getHealth());
    }

    private boolean characterOverlapsWithDoor(Character character, Room room) {
        if (enemies.size() > 0) {
            return false;
        }
        Rectangle doorBounds = room.getDoorBounds(); // Get the door bounds from the room
        return character.getBoundingRectangle().overlaps(doorBounds); // Check for overlap
    }

    private void teleportCharacterToRandomRoom(Character character, List<Room> rooms, List<Integer> exploredRooms) {
        Room newRoom = selectRandomRoom(rooms, character.getCurrentRoom());
        int newRoomIndex = currentLevel.getRooms().indexOf(newRoom);

        if (!exploredRooms.contains(newRoomIndex)) {
            System.out.println("added to explored rooms");
            exploredRooms.add(newRoomIndex);
            tpSound.play(1f);
            character.setPosition(newRoom.getX() + newRoom.getWidth() / 2, newRoom.getY() + newRoom.getHeight() / 2);
            character.setCurrentRoom(newRoom);
            shop = false;
            doorspawned = false;
            shoproom = false;
            // Spawn enemies only if not already generated for this room
            if (!isGenerated) {
                int randomEnemyNumber = MathUtils.random(1 + number_of_current_level, 5 + number_of_current_level);
                enemies.clear();
                for (int i = 0; i < randomEnemyNumber; i++) {
                    spawnEnemy();
                }
                isGenerated = true; // Set flag to true after spawning enemies
                System.out.println("teleport spawn");
            }
        } else if (exploredRooms.size() == currentLevel.getRooms().size()) {
            // Handle boss level or new level generation
            handleLevelTransition();
            droppedItems = new ArrayList<>();
        } else {
            // Reset isGenerated if the character is re-entering an explored room
            isGenerated = false;
        }
    }

    private void handleLevelTransition() {
        // Reset isGenerated when transitioning to a new level or boss level
        isGenerated = false;

        if (!bosslevel) {
            System.out.println("boss level");
            doorspawned = false;
            shoproom = false;
            reset_explored_rooms();
            System.out.println(exploredRooms.size());
            initBossLevel();
            LastVsision = this.character.getVision();
            this.character.setVision(900);
            spawnCharacterInRandomRoom();
            bosslevel = true;
            BossSound.play(0.7f);
        } else {
            try {
                System.out.println("new level");
                doorspawned = false;
                reset_explored_rooms();
                float shopitem = MathUtils.random(100);
                if (shopitem >= 99 - (number_of_current_level / 50)) {
                    firstItem = generateRandomItem(Item.type.LEGENDARY);
                    firstItemPrice = 8000 * number_of_current_level;
                } else if (shopitem >= 90 - (number_of_current_level / 50)) {
                    firstItem = generateRandomItem(Item.type.MYTHIC);
                    firstItemPrice = 5000 * number_of_current_level;
                } else if (shopitem >= 40 - (number_of_current_level / 50)) {
                    firstItem = generateRandomItem(Item.type.RARE);
                    firstItemPrice = 600;
                } else {
                    firstItem = generateRandomItem(Item.type.COMMON);
                    firstItemPrice = 100;
                }
                shopitem = MathUtils.random(100);
                if (shopitem >= 99 - (number_of_current_level / 50)) {
                    secondItem = generateRandomItem(Item.type.LEGENDARY);
                    secondItemPrice = 8000 * number_of_current_level;
                } else if (shopitem >= 90 - (number_of_current_level / 50)) {
                    secondItem = generateRandomItem(Item.type.MYTHIC);
                    secondItemPrice = 5000 * number_of_current_level;
                } else if (shopitem >= 40 - (number_of_current_level / 50)) {
                    secondItem = generateRandomItem(Item.type.RARE);
                    secondItemPrice = 600;
                } else {
                    secondItem = generateRandomItem(Item.type.COMMON);
                    secondItemPrice = 100;
                }
                first_item_texture = firstItem.getSprite();
                second_item_texture = secondItem.getSprite();
                System.out.println(exploredRooms.size());
                number_of_current_level++;
                init_level_texture();
                this.character.setVision(LastVsision);
                initGameLevel();
                spawnCharacterInRandomRoom();
                bosslevel = false;
                shop = true;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void reset_explored_rooms() {
        exploredRooms.clear();
        // Also reset the isGenerated flag for the new level
        isGenerated = false;
    }

    private Item generateRandomItem(Item.type rarity) {
        // Generate a random item based on rarity
        // You can customize this method based on your item generation logic
        // For simplicity, let's assume a few predefined items for each rarity
        List<Item> rareItems = new ArrayList<>();
        rareItems.add(new Item("Rare TOP", 2, 5, 0, 0, 0, 10, Item.type.TOP,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_RARE_TOP)))));
        rareItems.add(new Item("Rare Helmet", 2, 10, 0, 0, 0, 15, Item.type.HELMET,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_RARE_HELMET)))));
        rareItems.add(new Item("Rare Armor", 2, 10, 0, 0, 0, 25, Item.type.ARMOR,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_RARE_FULL)))));
        rareItems.add(new Item("Rare Weapon", 2, 0, 25, 0, 0, 0, Item.type.WEAPON,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.EPEE_RARE)))));

        List<Item> mythicItems = new ArrayList<>();
        mythicItems.add(new Item("Mythic TOP", 3, 10, 0, 0, 0, 20, Item.type.TOP,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_MYTHIQUE_TOP)))));
        mythicItems.add(new Item("Mythic Helmet", 3, 20, 0, 0, 0, 30, Item.type.HELMET,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_MYTHIQUE_HELMET)))));
        mythicItems.add(new Item("Mythic Armor", 3, 20, 0, 0, 0, 50, Item.type.ARMOR,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_MYTHIQUE_FULL)))));
        mythicItems.add(new Item("Mythic Armor", 3, 0, 50, 0, 0, 0, Item.type.WEAPON,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.EPEE_MYTIQUE)))));

        List<Item> commonItems = new ArrayList<>();
        commonItems.add(new Item("Common Helmet", 1, 0, 0, 0, 0, 10, Item.type.HELMET,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_COMMUN_HELMET)))));
        commonItems.add(new Item("Common TOP", 1, 0, 0, 0, 0, 5, Item.type.TOP,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_COMMUN_TOP)))));
        commonItems.add(new Item("Common Armor", 1, 0, 0, 0, 0, 15, Item.type.ARMOR,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_COMMUN_FULL)))));
        commonItems.add(new Item("Common Weapon", 1, 0, 10, 0, 0, 0, Item.type.WEAPON,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.EPEE_COMMUN)))));

        List<Item> legendaryItems = new ArrayList<>();
        legendaryItems.add(new Item("Legendary Helmet", 4, 30, 0, 0, 0, 60, Item.type.HELMET,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_LEGENDAIRE_HELMET)))));
        legendaryItems.add(new Item("Legendary TOP", 4, 20, 0, 0, 0, 40, Item.type.TOP,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_LEGENDAIRE_TOP)))));
        legendaryItems.add(new Item("Legendary Armor", 4, 20, 0, 0, 0, 100, Item.type.ARMOR,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.ARMURE_LEGENDAIRE_FULL)))));
        legendaryItems.add(new Item("Legendary Weapon", 4, 0, 100, 20, 0, 0, Item.type.WEAPON,
                new Sprite(new Texture(Gdx.files.internal(Assets.items.EPEE_LEGENDAIRE)))));

        switch (rarity) {
            case RARE:
                return getRandomItemFromList(rareItems);
            case MYTHIC:
                return getRandomItemFromList(mythicItems);
            case COMMON:
                return getRandomItemFromList(commonItems);
            case LEGENDARY:
                return getRandomItemFromList(legendaryItems);
            default:
                return null;
        }
    }

    private Item getRandomItemFromList(List<Item> itemList) {
        // Helper method to get a random item from the given list
        int randomIndex = (int) (Math.random() * itemList.size());
        return itemList.get(randomIndex);
    }

    private Room selectRandomRoom(List<Room> rooms, Room excludeRoom) {
        // Randomly select a room that is not the current room
        Room selectedRoom;
        do {
            if (rooms.size() == 1) {
                selectedRoom = rooms.get(0);
                break;
            }
            int randomIndex = MathUtils.random(0, rooms.size() - 1);
            selectedRoom = rooms.get(randomIndex);
        } while (selectedRoom.equals(excludeRoom));

        return selectedRoom;
    }

    private void handleMouseClick() {
        if (shop && control.LMB) {
            if (number_of_current_level == 1) {
                float itemX = Gdx.graphics.getWidth() / 2.1f;
                float itemY = Gdx.graphics.getHeight() / 4.7f;
                float itemWidth = 100;
                float itemHeight = 100;

                // Vérifier si le clic est dans la zone de l'objet du magasin
                if (control.mouse_click_pos.x >= itemX && control.mouse_click_pos.x <= itemX + itemWidth &&
                        control.mouse_click_pos.y >= itemY && control.mouse_click_pos.y <= itemY + itemHeight) {
                    this.character.equipItem(firstItem);
                    shop = false;
                }

            } else {
                float itemX1 = Gdx.graphics.getWidth() / 3.05f;
                float itemY1 = Gdx.graphics.getHeight() / 4.5f;
                float itemX2 = Gdx.graphics.getWidth() / 2.4f;
                float itemY2 = Gdx.graphics.getHeight() / 4.5f;
                float itemX3 = Gdx.graphics.getWidth() / 1.9f;
                float itemY3 = Gdx.graphics.getHeight() / 4.5f;
                float itemX4 = Gdx.graphics.getWidth() / 1.57f;
                float itemY4 = Gdx.graphics.getHeight() / 4.5f;
                float itemWidth = 100;
                float itemHeight = 100;

                // Vérifier si le clic est dans la zone de l'objet du magasin
                if (control.mouse_click_pos.x >= itemX1 && control.mouse_click_pos.x <= itemX1 + itemWidth &&
                        control.mouse_click_pos.y >= itemY1 && control.mouse_click_pos.y <= itemY1 + itemHeight) {
                    if (this.character.getGold() >= firstItemPrice) {
                        this.character.setGold(this.character.getGold() - firstItemPrice);
                        this.character.equipItem(firstItem);
                        shop = false;
                    }
                } else if (control.mouse_click_pos.x >= itemX2 && control.mouse_click_pos.x <= itemX2 + itemWidth &&
                        control.mouse_click_pos.y >= itemY2 && control.mouse_click_pos.y <= itemY2 + itemHeight) {
                    if (this.character.getGold() >= secondItemPrice) {
                        this.character.setGold(this.character.getGold() - secondItemPrice);
                        this.character.equipItem(secondItem);
                        shop = false;
                    }
                } else if (control.mouse_click_pos.x >= itemX3 && control.mouse_click_pos.x <= itemX3 + itemWidth &&
                        control.mouse_click_pos.y >= itemY3 && control.mouse_click_pos.y <= itemY3 + itemHeight
                        && number_max_of_upgrade < 5) {
                    if (this.character.getGold() >= 2000 * number_of_current_level) {
                        number_max_of_upgrade++;
                        this.character.setGold(this.character.getGold() - 2000 * number_of_current_level);
                        this.character.setVision(this.character.getVision() + 30);
                        this.character.setSpeed(this.character.getSpeed() + 20f);
                        shop = false;
                    }
                } else if (control.mouse_click_pos.x >= itemX4 && control.mouse_click_pos.x <= itemX4 + itemWidth &&
                        control.mouse_click_pos.y >= itemY4 && control.mouse_click_pos.y <= itemY4 + itemHeight
                        && number_of_upgrade_health < 5) {
                    if (this.character.getGold() >= 2500 * number_of_current_level) {
                        number_of_upgrade_health++;
                        this.character.setGold(this.character.getGold() - 2500 * number_of_current_level);
                        this.character.setMaxHealth(this.character.getMaxHealth() + 20);
                        this.character.setHealth(this.character.getMaxHealth());
                        shop = false;
                    }
                }
            }
            control.processed_click = true;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        handleMouseClick();

        // zoom camera
        camera.zoom = zoom;
        camera.update();

        // Get the character's position
        float characterX = this.character.getX();
        float characterY = this.character.getY();

        // Set the camera's position to the character's position
        camera.position.set(characterX + 6, characterY + 6, 0);

        // Update the camera
        camera.update();

        // Start drawing
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        if (displayLevelUp) {
            levelUpTimer += Gdx.graphics.getDeltaTime();
            if (levelUpTimer > 3) {
                displayLevelUp = false;
                levelUpTimer = 0;
            }
        }

        if (this.character.getLevel() > previousLevel) {
            displayLevelUp = true;
            previousLevel = this.character.getLevel();
            Levelupsound.play(0.3f);
        }
        stateTime += delta;

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            for (Ennemy enemy : enemies) {
                if (enemy.getBounds().contains(touchPos.x, touchPos.y)) {
                    enemy.takeDamage(this.character.getDamage());
                    hitSound.play(0.5f);
                    if (!enemy.isAlive()) {
                        float monsterdrop = MathUtils.random(100f);
                        if (monsterdrop >= 70) {
                            float dropRate = MathUtils.random(100f);
                            if (dropRate >= 99.9 - (number_of_current_level * 0.1)) {
                                System.out.println("The ennemy " + enemy.getName() + " dropped a lengendary item!");
                                Item legendaryItem = generateRandomItem(Item.type.LEGENDARY);
                                droppedItems.add(legendaryItem);
                                legendaryItem.setX(enemy.getX());
                                legendaryItem.setY(enemy.getY());
                            } else if (dropRate >= 99 - (number_of_current_level * 0.1)) {
                                System.out.println("The ennemy " + enemy.getName() + " dropped a mythic item!");
                                Item mythicItem = generateRandomItem(Item.type.MYTHIC);
                                droppedItems.add(mythicItem);
                                mythicItem.setX(enemy.getX());
                                mythicItem.setY(enemy.getY());
                            } else if (dropRate >= 75 - (number_of_current_level * 0.1)) {
                                System.out.println("The ennemy " + enemy.getName() + " dropped an rare item.");
                                Item rareItem = generateRandomItem(Item.type.RARE);
                                droppedItems.add(rareItem);
                                rareItem.setX(enemy.getX());
                                rareItem.setY(enemy.getY());
                            } else {
                                System.out.println("The ennemy " + enemy.getName() + " dropped a common item.");
                                Item commonItem = generateRandomItem(Item.type.COMMON);
                                droppedItems.add(commonItem);
                                commonItem.setX(enemy.getX());
                                commonItem.setY(enemy.getY());
                            }
                        }

                        if (bosslevel) {
                            enemies.remove(enemy);
                            this.character.setGold(this.character.getGold()
                                    + MathUtils.random(100 * number_of_current_level, 200 * number_of_current_level));
                            this.character.addExperience(enemy.getExperience() * 10);
                        } else {
                            enemies.remove(enemy);
                            this.character.setGold(this.character.getGold()
                                    + MathUtils.random(10 * number_of_current_level, 20 * number_of_current_level));
                            this.character.addExperience(enemy.getExperience());
                        }
                    }
                    break;
                }
            }
        }

        // Check if the shader compiled successfully
        if (!shader_vision.isCompiled()) {
            Gdx.app.error("Vision_ShaderProgram", "shader compilation failed:\n" + shader_vision.getLog());
        }

        // Set the shader of the spriteBatch
        spriteBatch.setShader(shader_vision);

        // Set the uniforms for the shader
        shader_vision.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader_vision.setUniformf("u_center", (Gdx.graphics.getWidth() / 2) + 16, (Gdx.graphics.getHeight() / 2) - 40);
        shader_vision.setUniformf("u_radius", shop ? 900 : this.character.getVision());

        // Render the game level with the vision shader
        renderGameLevel();

        // When the enemy is hit, call setHit(true)
        for (Ennemy enemy : enemies) {
            if (enemy.isHit()) {
                spriteBatch.setColor(Color.RED);
            }

            // Draw enemy
            if (bosslevel) {
                enemy.update(delta, this.character);
                enemy.draw(spriteBatch, 32, 32);
            } else {
                enemy.update(delta, this.character);
                enemy.draw(spriteBatch, 16, 16);
            }
        }

        spriteBatch.setColor(Color.WHITE); // Reset the color back to normal

        // Draw the attack animation for hit enemies
        for (Ennemy enemy : enemies) {
            if (enemy.isHit()) {
                TextureRegion currentFrame = attackAnimation.getKeyFrame(stateTime, true);
                spriteBatch.draw(currentFrame, enemy.getX(), enemy.getY(), 24, 24);
                stateTime += delta; // Increment stateTime only when the enemy is hit
            } else {
                // Draw enemy
                if (bosslevel) {
                    enemy.update(delta, this.character);
                    enemy.draw(spriteBatch, 32, 32);
                } else {
                    enemy.update(delta, this.character);
                    enemy.draw(spriteBatch, 16, 16);
                }
            }
        }

        float itemWidth = 12; // Replace with the desired width
        float itemHeight = 12; // Replace with the desired height

        for (Item item : droppedItems) {
            spriteBatch.draw(item.getSprite(), item.getX(), item.getY(), itemWidth, itemHeight);
        }

        for (Item item : droppedItems) {
            if (this.character.getBounds().overlaps(item.getBounds())) {
                itemsToEquip.add(item);
            }
        }

        Iterator<Item> iterator = itemsToEquip.iterator();

        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (this.character.equipItem(item)) {
                droppedItems.remove(item);
            }
            iterator.remove();
        }

        // debug
        // character.setVision(1500);

        // Reset the shader of the spriteBatch
        spriteBatch.setShader(null);

        // update the character
        float deltaTime = Gdx.graphics.getDeltaTime();
        this.character.update(deltaTime, control);

        // Check for collisions with walls
        checkCollisions();

        // Draw the character using the actor without applying the zoom
        this.character.draw(spriteBatch);
        // Draw equipped items
        if (this.character.getName() != "Dims") {
            basePath = "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\sprite_tristan\\";
        } else {
            basePath = "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\sprite_Dims\\";
        }

        // Check if Armor is equipped
        if (this.character.getEquippedItem("Armor") != null) {
            switch (this.character.getEquippedItem("Armor").getRarity()) {
                case 1:
                    this.character.updateTexture(new Texture(basePath + "Commun\\full_armor.png"));
                    break;
                case 2:
                    this.character.updateTexture(new Texture(basePath + "Rare\\full_armor.png"));
                    break;
                case 3:
                    this.character.updateTexture(new Texture(basePath + "Mythique\\full_armor.png"));
                    break;
                case 4:
                    this.character.updateTexture(new Texture(basePath + "Legendaire\\full_armor.png"));
                    break;
            }
        } else {
            // Check if Helmet and Top are both equipped
            if (this.character.getEquippedItem("Helmet") != null && this.character.getEquippedItem("Top") != null) {
                switch (this.character.getEquippedItem("Top").getRarity()) {
                    case 1:
                        this.character.updateTexture(new Texture(basePath + "Commun\\full_armor.png"));
                        break;
                    case 2:
                        this.character.updateTexture(new Texture(basePath + "Rare\\full_armor.png"));
                        break;
                    case 3:
                        this.character.updateTexture(new Texture(basePath + "Mythique\\full_armor.png"));
                        break;
                    case 4:
                        this.character.updateTexture(new Texture(basePath + "Legendaire\\full_armor.png"));
                        break;
                }
            } else if (this.character.getEquippedItem("Helmet") != null && this.character.getEquippedItem("Top") == null
                    && this.character.getEquippedItem("Armor") == null) {
                // Only Helmet is equipped
                switch (this.character.getEquippedItem("Helmet").getRarity()) {
                    case 1:
                        this.character.updateTexture(new Texture(basePath + "Commun\\helmet.png"));
                        break;
                    case 2:
                        this.character.updateTexture(new Texture(basePath + "Rare\\helmet.png"));
                        break;
                    case 3:
                        this.character.updateTexture(new Texture(basePath + "Mythique\\helmet.png"));
                        break;
                    case 4:
                        this.character.updateTexture(new Texture(basePath + "Legendaire\\helmet.png"));
                        break;
                }
            } else if (this.character.getEquippedItem("Top") != null && this.character.getEquippedItem("Helmet") == null
                    && this.character.getEquippedItem("Armor") == null) {
                // Only Top is equipped
                switch (this.character.getEquippedItem("Top").getRarity()) {
                    case 1:
                        this.character.updateTexture(new Texture(basePath + "Commun\\Top.png"));
                        break;
                    case 2:
                        this.character.updateTexture(new Texture(basePath + "Rare\\Top.png"));
                        break;
                    case 3:
                        this.character.updateTexture(new Texture(basePath + "Mythique\\Top.png"));
                        break;
                    case 4:
                        this.character.updateTexture(new Texture(basePath + "Legendaire\\Top.png"));
                        break;
                }
            }
        }

        if (this.character.getEquippedItem("Weapon") != null) {
            String basePathWeapon = "core\\src\\com\\pedagoquest\\game\\assets\\sprite\\items\\armes\\";
            Pixmap flippedPixmap = null;
            if (defaultCursor != null) {
                defaultCursor.dispose();
            }
            switch (this.character.getEquippedItem("Weapon").getRarity()) {
                case 1:
                    defaultPixmap = new Pixmap(Gdx.files.internal(basePathWeapon + "epee_commun.png"));
                    flippedPixmap = new Pixmap(defaultPixmap.getWidth(), defaultPixmap.getHeight(),
                            defaultPixmap.getFormat());
                    for (int x = 0; x < defaultPixmap.getWidth(); x++) {
                        for (int y = 0; y < defaultPixmap.getHeight(); y++) {
                            flippedPixmap.drawPixel(defaultPixmap.getWidth() - x - 1, y, defaultPixmap.getPixel(x, y));
                        }
                    }
                    hotspotX = flippedPixmap.getWidth() / 2;
                    hotspotY = flippedPixmap.getHeight() / 2;
                    defaultCursor = Gdx.graphics.newCursor(flippedPixmap, hotspotX, hotspotY);
                    Gdx.graphics.setCursor(defaultCursor);

                    // Libérez les ressources
                    flippedPixmap.dispose();
                    break;
                case 2:
                    defaultPixmap = new Pixmap(Gdx.files.internal(basePathWeapon + "epee_rare.png"));
                    flippedPixmap = new Pixmap(defaultPixmap.getWidth(), defaultPixmap.getHeight(),
                            defaultPixmap.getFormat());
                    for (int x = 0; x < defaultPixmap.getWidth(); x++) {
                        for (int y = 0; y < defaultPixmap.getHeight(); y++) {
                            flippedPixmap.drawPixel(defaultPixmap.getWidth() - x - 1, y, defaultPixmap.getPixel(x, y));
                        }
                    }
                    hotspotX = flippedPixmap.getWidth() / 2;
                    hotspotY = flippedPixmap.getHeight() / 2;
                    defaultCursor = Gdx.graphics.newCursor(flippedPixmap, hotspotX, hotspotY);
                    Gdx.graphics.setCursor(defaultCursor);

                    // Libérez les ressources
                    flippedPixmap.dispose();
                    break;
                case 3:
                    defaultPixmap = new Pixmap(Gdx.files.internal(basePathWeapon + "epee_mythique.png"));
                    flippedPixmap = new Pixmap(defaultPixmap.getWidth(), defaultPixmap.getHeight(),
                            defaultPixmap.getFormat());
                    for (int x = 0; x < defaultPixmap.getWidth(); x++) {
                        for (int y = 0; y < defaultPixmap.getHeight(); y++) {
                            flippedPixmap.drawPixel(defaultPixmap.getWidth() - x - 1, y, defaultPixmap.getPixel(x, y));
                        }
                    }
                    hotspotX = flippedPixmap.getWidth() / 2;
                    hotspotY = flippedPixmap.getHeight() / 2;
                    defaultCursor = Gdx.graphics.newCursor(flippedPixmap, hotspotX, hotspotY);
                    Gdx.graphics.setCursor(defaultCursor);

                    // Libérez les ressources
                    flippedPixmap.dispose();
                    break;
                case 4:
                    defaultPixmap = new Pixmap(Gdx.files.internal(basePathWeapon + "epee_legendaire.png"));
                    flippedPixmap = new Pixmap(defaultPixmap.getWidth(), defaultPixmap.getHeight(),
                            defaultPixmap.getFormat());
                    for (int x = 0; x < defaultPixmap.getWidth(); x++) {
                        for (int y = 0; y < defaultPixmap.getHeight(); y++) {
                            flippedPixmap.drawPixel(defaultPixmap.getWidth() - x - 1, y, defaultPixmap.getPixel(x, y));
                        }
                    }
                    hotspotX = flippedPixmap.getWidth() / 2;
                    hotspotY = flippedPixmap.getHeight() / 2;
                    defaultCursor = Gdx.graphics.newCursor(flippedPixmap, hotspotX, hotspotY);
                    Gdx.graphics.setCursor(defaultCursor);

                    // Libérez les ressources
                    flippedPixmap.dispose();
                    break;
            }
        }

        // End drawing
        spriteBatch.end();

        uiBatch.begin();

        // StatSheet Title
        font.draw(uiBatch, "StatSheet", 60, 140);
        // Health
        uiBatch.draw(health, 20, 60, 32, 32);
        font.draw(uiBatch, String.valueOf(this.character.getHealth()), 60, 85);
        // Armor
        uiBatch.draw(shield, 24, 22, 24, 24);
        font.draw(uiBatch, String.valueOf(this.character.getArmor()), 60, 45);
        // Experience (XP)
        uiBatch.draw(xp, 520, 60, 32, 32);
        font.draw(uiBatch, this.character.percentageToNextLevel() + "%", 560, 85);
        // Gold
        uiBatch.draw(gold, 120, 20, 32, 32);
        font.draw(uiBatch, String.valueOf(this.character.getGold()), 160, 45);

        // FightSheet Title
        font.draw(uiBatch, "FightSheet", 460, 140);
        // Remaining Enemies
        uiBatch.draw(remainEnnemies, 420, 60, 32, 32);
        font.draw(uiBatch, String.valueOf(enemies.size()), 460, 85);
        // Current Floor
        uiBatch.draw(floor, 420, 20, 32, 32);
        font.draw(uiBatch, String.valueOf(number_of_current_level), 460, 45);
        // Damage
        uiBatch.draw(damageTexture, 120, 60, 32, 32);
        font.draw(uiBatch, String.valueOf(this.character.getDamage()), 160, 85);
        // Level
        font.draw(uiBatch, "Level: " + this.character.getLevel(), 525, 40);

        uiBatch.draw(inventory, (Gdx.graphics.getWidth() / 2.05f) - 96, 5, 64, 64);
        uiBatch.draw(inventory, (Gdx.graphics.getWidth() / 2.05f - 32), 5, 64, 64);
        uiBatch.draw(inventory, (Gdx.graphics.getWidth() / 2.05f) + 32, 5, 64, 64);
        uiBatch.draw(inventory, (Gdx.graphics.getWidth() / 2.05f) + 96, 5, 64, 64);

        // Récupérez les items équipés
        Item equippedWeapon = this.character.getEquippedItem("Weapon");
        Item equippedArmor = this.character.getEquippedItem("Armor");
        Item equippedTop = this.character.getEquippedItem("Top");
        Item equippedHelmet = this.character.getEquippedItem("Helmet");

        // Dessinez les textures des items équipés
        if (equippedWeapon != null) {
            uiBatch.draw(equippedWeapon.getSprite(), (Gdx.graphics.getWidth() / 2.05f) - 90, 12, 50, 50);
        }
        if (equippedArmor != null) {
            uiBatch.draw(equippedArmor.getSprite(), (Gdx.graphics.getWidth() / 2.05f - 24), 15, 45, 45);
        }
        if (equippedTop != null) {
            uiBatch.draw(equippedTop.getSprite(), (Gdx.graphics.getWidth() / 2.05f) + 41, 12, 45, 45);
        }
        if (equippedHelmet != null) {
            uiBatch.draw(equippedHelmet.getSprite(), (Gdx.graphics.getWidth() / 2.05f) + 102, 12, 50, 50);
        }

        if (shop) {
            uiBatch.draw(shopTexture, Gdx.graphics.getWidth() / 1.3f, Gdx.graphics.getHeight() / 1.6f,
                    Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 3.2f);
            uiBatch.draw(shopPanel, Gdx.graphics.getWidth() / 3.3f, Gdx.graphics.getHeight() / 8,
                    Gdx.graphics.getWidth() / 2.38f, Gdx.graphics.getHeight() / 3.5f);
            if (number_of_current_level == 1) {
                font.draw(uiBatch, "You can take a start Item!", Gdx.graphics.getWidth() / 2.35f,
                        Gdx.graphics.getHeight() / 2.6f);
                uiBatch.draw(first_item_texture, Gdx.graphics.getWidth() / 2.1f,
                        Gdx.graphics.getHeight() / 4.7f, 100, 100);
            } else {
                font.draw(uiBatch, "Welcome to the shop!", Gdx.graphics.getWidth() / 2.28f,
                        Gdx.graphics.getHeight() / 2.6f);
                uiBatch.draw(first_item_texture, Gdx.graphics.getWidth() / 3.05f,
                        Gdx.graphics.getHeight() / 4.5f, 100, 100);
                font.draw(uiBatch, String.valueOf((int) firstItemPrice + " gold"),
                        Gdx.graphics.getWidth() / 3.05f,
                        Gdx.graphics.getHeight() / 5.2f);
                uiBatch.draw(second_item_texture, Gdx.graphics.getWidth() / 2.4f,
                        Gdx.graphics.getHeight() / 4.5f, 100, 100);
                font.draw(uiBatch, String.valueOf((int) secondItemPrice + " gold"),
                        Gdx.graphics.getWidth() / 2.4f,
                        Gdx.graphics.getHeight() / 5.2f);
                uiBatch.draw(VisionIcon, Gdx.graphics.getWidth() / 1.9f, Gdx.graphics.getHeight() / 4.5f, 100,
                        100);
                float upgrade_Vision = 2500 * number_of_current_level;
                font.draw(uiBatch, "+10 speed&Vision\n        " + (int) upgrade_Vision + " gold",
                        Gdx.graphics.getWidth() / 2f,
                        Gdx.graphics.getHeight() / 4.9f);
                uiBatch.draw(Healthpotion, Gdx.graphics.getWidth() / 1.57f, Gdx.graphics.getHeight() / 4.5f, 90,
                        90);
                font.draw(uiBatch, "Get treated", Gdx.graphics.getWidth() / 1.6f,
                        Gdx.graphics.getHeight() / 3f);
                float upgrade_health = 2000 * number_of_current_level;
                font.draw(uiBatch, "+20 HP max\n" + (int) upgrade_health + " gold", Gdx.graphics.getWidth() / 1.6f,
                        Gdx.graphics.getHeight() / 4.9f);
            }
        }

        if (doorspawned) {
            font.draw(uiBatch, "Door spawned! Find It!", Gdx.graphics.getWidth() / 2 - 110,
                    Gdx.graphics.getHeight() / 1.5f);
        }
        if (displayLevelUp) {
            font.draw(uiBatch, "Level Up!", Gdx.graphics.getWidth() / 2 - 32, Gdx.graphics.getHeight() / 2 + 100);
        }
        if (!this.character.isAlive()) {
            isGameOver = true;
        }

        if (isGameOver) {
            stage.getBatch().begin();
            stage.getBatch().draw(gameOverTexture, Gdx.graphics.getWidth() / 2f - gameOverTexture.getWidth() / 2f,
                    Gdx.graphics.getHeight() / 2f - gameOverTexture.getHeight() / 2f);
            stage.getBatch().end();

            if (Gdx.input.justTouched()) {
                game.setScreen(new MainMenu(game));
            }
        }

        uiBatch.end();
    }

    private void renderGameLevel() {
        for (Room room : currentLevel.getRooms()) {
            drawRoomFloor(spriteBatch, ground, room);
            drawRoomWalls(spriteBatch, walls, room);
            drawdoor(spriteBatch, room);
        }

    }

    @Override
    public void resize(int width, int height) {
        // Ajuster la caméra à la nouvelle taille de la fenêtre
        float aspectRatio = (float) width / (float) height;
        camera.setToOrtho(false, Gdx.graphics.getWidth() * aspectRatio, Gdx.graphics.getHeight());

        // Mettre à jour le viewport de la caméra
        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();
        camera.update();
    }

    @Override
    public void pause() {
        // Appelé lorsque le jeu est mis en pause
    }

    @Override
    public void resume() {
        // Appelé lorsque le jeu est repris après avoir été mis en pause
    }

    @Override
    public void hide() {
        // Appelé lorsque ce Screen n'est plus le Screen courant
    }

    @Override
    public void dispose() {
        texture.dispose();
        spriteBatch.dispose();
        shader_vision.dispose();
        shapeRenderer.dispose();
        this.character.getTexture().dispose();
        stage.dispose();
        gameOverTexture.dispose();
    }
}
