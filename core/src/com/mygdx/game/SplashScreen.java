package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SplashScreen implements Screen
{
    MiceAlert game;

    private Texture texture;
    private Texture clickToPlay;
    private Image splashImage;
    private Image splashText;
    private Stage stage;

    public SplashScreen(MiceAlert game)
    {
        this.game = game;
        texture = new Texture("image/background.png");
        splashImage = new Image(texture);
        clickToPlay = new Texture("image/clickToPlayStub.png");
        splashText = new Image(clickToPlay);
        splashImage.setFillParent(true);
        stage = new Stage();
    }

    @Override
    public void show()
    {
        splashImage.addAction(
            Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(3f),
                Actions.delay(1)));
        splashImage.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                game.setScreen(new LevelGenerator(game, "level/MiceAlert_Map_TileMap_01.tmx"));
                return true;
            }
        });

        splashText.addAction(Actions.alpha(0));
        splashText.act(0);
        splashText.addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.fadeIn(1f),
                    Actions.fadeOut(1f))));
        //splashText.setPosition(Gdx.graphics.getWidth() / 32, Gdx.graphics.getHeight() - splashText.getHeight());

        stage.addActor(splashImage);
        stage.addActor(splashText);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        stage.act(delta);
        stage.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        texture.dispose();
        clickToPlay.dispose();
    }
}
