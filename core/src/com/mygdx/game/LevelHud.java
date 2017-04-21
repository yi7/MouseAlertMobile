package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LevelHud
{
    private Stage stage;
    private Table table;
    private LevelGenerator level;

    private Texture stubTexture;
    private Image stubImage;

    private Texture buttonTexture;
    private TextureRegion buttontextureRegion;
    private TextureRegionDrawable buttonTextureRegionDrawable;
    private Button playButton;
    private Button resetButton;

    public LevelHud(LevelGenerator level)
    {
        this.stage = new Stage();
        this.table = new Table();
        this.level = level;

        table.setWidth(level.phone_width - level.level_width);
        table.setHeight(level.phone_height - (level.phone_width - level.level_width));
        table.setPosition(level.level_width, 0);

        stubTexture = new Texture("image/stubHud.png");
        stubImage = new Image(stubTexture);
        stubImage.setWidth(level.phone_width - level.level_width);
        stubImage.setHeight(level.phone_height);

        buttonTexture = new Texture("image/testButton.png");
        buttontextureRegion = new TextureRegion(buttonTexture);
        buttonTextureRegionDrawable = new TextureRegionDrawable(buttontextureRegion);
        playButton = new Button(buttonTextureRegionDrawable);
        resetButton = new Button(buttonTextureRegionDrawable);
    }

    public Stage getHud()
    {
        stubImage.setPosition(level.level_width, 0);
        stage.addActor(stubImage);

        playButton.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Gdx.app.log("Yokaka", "Play");
                level.setLevelState(LevelGenerator.LevelState.PLAY);
                return true;
            }
        });

        resetButton.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Gdx.app.log("Yokaka", "Reset");
                level.setLevelState(LevelGenerator.LevelState.RESET);
                return true;
            }
        });

        table.add(playButton).size(playButton.getWidth() * level.phone_scale, playButton.getWidth() * level.phone_scale).pad(10);
        table.row();
        table.add(resetButton).size(resetButton.getWidth() * level.phone_scale, resetButton.getWidth() * level.phone_scale).pad(10);
        stage.addActor(table);

        return stage;
    }
}
