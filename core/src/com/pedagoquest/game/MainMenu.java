package com.pedagoquest.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class MainMenu extends ScreenAdapter {

    private final Texture background;
    private final Stage stage;
    private final Music backgroundMusic;
    private final Sound clickSound;
    private Character character;
    private final TextButton PersonnageButton;

    public MainMenu(final Game game) {
        this.background = new Texture("core/src/com/pedagoquest/game/assets/sprite/menu/Pedagoquest_back.png");

        stage = new Stage(new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // Charger la musique de fond
        backgroundMusic = Gdx.audio
                .newMusic(Gdx.files.internal("core/src/com/pedagoquest/game/assets/sprite/map/music/menu_music.ogg"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        backgroundMusic.setVolume(0.2f);

        // Charger le son de clic
        clickSound = Gdx.audio
                .newSound(Gdx.files.internal("core/src/com/pedagoquest/game/assets/sprite/menu/click_sound.wav"));

        Skin skin = new Skin();
        Drawable buttonDrawable = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files
                        .internal("core/src/com/pedagoquest/game/assets/sprite/menu/bouton.png"))));

        // Create a TextButtonStyle
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("core/src/com/pedagoquest/game/assets/sprite/map/fonts/pixel_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        textButtonStyle.font = font;
        textButtonStyle.up = buttonDrawable;

        PersonnageButton = new TextButton("Click to play with Tristan", textButtonStyle);

        PersonnageButton.setSize(500f, 150f);
        PersonnageButton.setPosition(Gdx.graphics.getWidth() / 2f - PersonnageButton.getWidth() / 2f,
                Gdx.graphics.getHeight() / 1.5f); // Initialize the character and control

        PersonnageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(); // Jouer le son de clic
                // Change the button's text when it is clicked
                if (PersonnageButton.getText().toString().equals("Click to play with Tristan")) {
                    PersonnageButton.setText("Click to play with Dims");
                } else {
                    PersonnageButton.setText("Click to play with Tristan");
                }
            }
        });

        // Bouton de lecture
        Texture playButtonTexture = new Texture("core/src/com/pedagoquest/game/assets/sprite/menu/bouton_v2.png");
        skin.add("playButton", playButtonTexture);
        ImageButton playButton = new ImageButton(skin.getDrawable("playButton"));
        playButton.setSize(500f, 150f);
        playButton.setPosition(Gdx.graphics.getWidth() / 2f - playButton.getWidth() / 2f,
                Gdx.graphics.getHeight() / 50f);

        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                clickSound.play(); // Jouer le son de clic
                if (PersonnageButton.getText().toString().equals("Click to play with Tristan")) {
                    Texture charactTexture = new Texture(
                            "core/src/com/pedagoquest/game/assets/sprite/sprite_dims/naked/Naked.png");
                    character = new Character("Dims", 1, 80, 100, 7, 6, 10, 10, 20, charactTexture);
                } else {
                    Texture charactTexture = new Texture(
                            "core/src/com/pedagoquest/game/assets/sprite/sprite_tristan/naked/Naked.png");
                    character = new Character("Tristan", 1, 100, 100, 7, 10, 10, 10, 10, charactTexture);
                }
                game.setScreen(new GameScreen((GameController) game, character));
            }
        });

        // Bouton de quitter
        Texture quitButtonTexture = new Texture("core/src/com/pedagoquest/game/assets/sprite/menu/bouton_quit.png");
        skin.add("quitButton", quitButtonTexture);
        ImageButton quitButton = new ImageButton(skin.getDrawable("quitButton"));
        quitButton.setSize(150f, 150f);
        quitButton.setPosition(Gdx.graphics.getWidth() / 1.1f,
                Gdx.graphics.getHeight() / 1.2f);

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                clickSound.play();
                Gdx.app.exit();
            }
        });

        final Texture soundOnTexture = new Texture("core/src/com/pedagoquest/game/assets/sprite/menu/sound_on.png");
        final Texture soundOffTexture = new Texture("core/src/com/pedagoquest/game/assets/sprite/menu/sound_off.png");
        final float buttonPadding = 20f;

        final ImageButton soundButton = new ImageButton(new TextureRegionDrawable(soundOnTexture),

                new TextureRegionDrawable(soundOffTexture));
        soundButton.setSize(150f, 150f);
        soundButton.setPosition(20f, Gdx.graphics.getHeight() - soundButton.getHeight() - 20f);
        soundButton.setChecked(true);
        soundButton.setPosition(Gdx.graphics.getWidth() - soundButton.getWidth() - buttonPadding, buttonPadding);

        soundButton.addListener(new ClickListener() {

            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                clickSound.play(); // Jouer le son de clic
                if (soundButton.isChecked()) {
                    backgroundMusic.play();
                } else {
                    backgroundMusic.pause();
                }
            }

        });

        stage.addActor(quitButton);
        stage.addActor(playButton);
        stage.addActor(soundButton);
        stage.addActor(PersonnageButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        backgroundMusic.dispose();
        clickSound.dispose();
    }
}
