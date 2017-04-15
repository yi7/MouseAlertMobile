package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelHudGenerator
{
    Stage stage;
    LevelGenerator level;
    private Texture stubTexture;
    private Image stubImage;

    public LevelHudGenerator(LevelGenerator level)
    {
        stage = new Stage();
        this.level = level;
        stubTexture = new Texture("Assets_Image/stub_button4.png");
        stubImage = new Image(stubTexture);
        stubImage.setWidth(Gdx.graphics.getWidth() - level.getLevelWidth());
        stubImage.setHeight(Gdx.graphics.getHeight());
    }

    public Stage getHud()
    {
        stubImage.addAction(
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
        stubImage.setPosition(level.getLevelWidth(), 0);
        stage.addActor(stubImage);

        return stage;
    }
}
