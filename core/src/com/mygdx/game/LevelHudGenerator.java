package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LevelHudGenerator
{
    Stage stage;
    Table table;
    LevelGenerator level;

    private Texture stubTexture;
    private Image stubImage;

    private Texture buttonTexture;
    private TextureRegion buttontextureRegion;
    private TextureRegionDrawable buttonTextureRegionDrawable;
    private Button playButton;
    private Button resetButton;

    public LevelHudGenerator(LevelGenerator level)
    {
        stage = new Stage();
        table = new Table();
        table.setWidth(Gdx.graphics.getWidth() - level.getLevelWidth());
        table.setHeight(Gdx.graphics.getHeight() - (Gdx.graphics.getWidth() - level.getLevelWidth()));
        table.setPosition(level.getLevelWidth(), 0);
        this.level = level;

        stubTexture = new Texture("Assets_Image/stubHud.png");
        stubImage = new Image(stubTexture);
        stubImage.setWidth(Gdx.graphics.getWidth() - level.getLevelWidth());
        stubImage.setHeight(Gdx.graphics.getHeight());

        buttonTexture = new Texture("Assets_Image/testButton.png");
        buttontextureRegion = new TextureRegion(buttonTexture);
        buttonTextureRegionDrawable = new TextureRegionDrawable(buttontextureRegion);
        playButton = new Button(buttonTextureRegionDrawable);
        resetButton = new Button(buttonTextureRegionDrawable);
    }

    public Stage getHud()
    {
        /*stubImage.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.fadeIn(1f),
                        Actions.delay(4)));
        stubImage.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("Yokaka", "Stub Test");
                return true;
            }
        });
        */
        stubImage.setPosition(level.getLevelWidth(), 0);
        stage.addActor(stubImage);

        playButton.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("Yokaka", "Play");
                return true;
            }
        });

        resetButton.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("Yokaka", "Reset");
                return true;
            }
        });

        table.add(playButton).size(playButton.getWidth() * level.getScale(), playButton.getWidth() * level.getScale()).pad(10);
        //table.row();
        table.add(resetButton).size(resetButton.getWidth() * level.getScale(), resetButton.getWidth() * level.getScale()).pad(10);
        stage.addActor(table);

        return stage;
    }
}
