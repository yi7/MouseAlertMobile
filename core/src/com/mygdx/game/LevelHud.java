package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LevelHud
{
    private Stage stage;
    private Table table;
    private LevelGenerator level_generator;

    private Texture stubTexture;
    private Image stubImage;

    private Texture buttonTexture;
    private TextureRegion buttontextureRegion;
    private TextureRegionDrawable buttonTextureRegionDrawable;
    private Button playButton;
    private Button resetButton;

    private BitmapFont font;
    private Label text;
    private LabelStyle label_style;

    public LevelHud(LevelGenerator level_generator)
    {
        this.stage = new Stage();
        this.table = new Table();
        this.level_generator = level_generator;

        table.setWidth(level_generator.phone_width - level_generator.level_width);
        table.setHeight(level_generator.phone_height - (level_generator.phone_width - level_generator.level_width));
        table.setPosition(level_generator.level_width, 0);

        stubTexture = new Texture("image/stubHud.png");
        stubImage = new Image(stubTexture);
        stubImage.setWidth(level_generator.phone_width - level_generator.level_width);
        stubImage.setHeight(level_generator.phone_height);

        buttonTexture = new Texture("image/testButton.png");
        buttontextureRegion = new TextureRegion(buttonTexture);
        buttonTextureRegionDrawable = new TextureRegionDrawable(buttontextureRegion);
        playButton = new Button(buttonTextureRegionDrawable);
        resetButton = new Button(buttonTextureRegionDrawable);

        label_style = new LabelStyle();
        label_style.font = new BitmapFont(Gdx.files.internal("skin/default.fnt"), false);
    }

    public Stage getHud()
    {
        stubImage.setPosition(level_generator.level_width, 0);
        stage.addActor(stubImage);

        text = new Label("Test", label_style);
        text.setColor(Color.BLACK);
        text.setFontScale(5f, 5f);

        playButton.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Gdx.app.log("Yokaka", "Play");
                if(level_generator.level_state == LevelGenerator.LevelState.STANDBY)
                {
                    level_generator.setLevelState(LevelGenerator.LevelState.PLAY);
                }
                return true;
            }
        });

        resetButton.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Gdx.app.log("Yokaka", "Reset");
                level_generator.setLevelState(LevelGenerator.LevelState.RESET);
                return true;
            }
        });

        //table.add
        table.add(text);
        table.row();
        table.add(playButton).size(playButton.getWidth() * level_generator.phone_scale, playButton.getWidth() * level_generator.phone_scale).pad(10);
        table.row();
        table.add(resetButton).size(resetButton.getWidth() * level_generator.phone_scale, resetButton.getWidth() * level_generator.phone_scale).pad(10);
        stage.addActor(table);

        return stage;
    }

    public Stage getWinHud()
    {
        Stage popup_stage = popup_stage = new Stage();
        Texture texture = new Texture("image/popup_window.png");
        Image image = new Image(texture);
        image.setFillParent(true);
        popup_stage.addActor(image);
        Table popup_table = new Table();
        popup_table.setFillParent(true);
        Texture buttonTexture = new Texture("image/testButton2.png");
        TextureRegion buttontextureRegion = new TextureRegion(buttonTexture);;
        TextureRegionDrawable buttonTextureRegionDrawable = new TextureRegionDrawable(buttontextureRegion);;
        Button restartButton = new Button(buttonTextureRegionDrawable);
        Button menuButton = new Button(buttonTextureRegionDrawable);

        restartButton.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                level_generator.setLevelState(LevelGenerator.LevelState.RESET);
                level_generator.show_popup = false;
                level_generator.initializeGestureDetector();
                return true;
            }
        });

        menuButton.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Gdx.app.log("Yokaka", "Reset");
                //level_generator.setLevelState(LevelGenerator.LevelState.RESET);
                return true;
            }
        });
        popup_table.add(restartButton).size(restartButton.getWidth() * level_generator.phone_scale, restartButton.getHeight() * level_generator.phone_scale).pad(10);
        popup_table.add(menuButton).size(menuButton.getWidth() * level_generator.phone_scale, menuButton.getHeight() * level_generator.phone_scale).pad(10);
        popup_stage.addActor(popup_table);
        return popup_stage;
    }
}
