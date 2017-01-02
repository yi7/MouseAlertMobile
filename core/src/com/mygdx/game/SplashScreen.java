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
        texture = new Texture("background.png");
        splashImage = new Image(texture);
        clickToPlay = new Texture("clickToPlayStub.png");
        splashText = new Image(clickToPlay);
        splashImage.setFillParent(true);
        stage = new Stage();
    }

    @Override
    public void show()
    {
        stage.addActor(splashImage);
        Gdx.input.setInputProcessor(stage);

        splashImage.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.fadeIn(3f),
                        Actions.delay(1)));
        splashImage.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                game.setScreen(new MainMenu(game));
                return true;
            }
        });

        stage.addActor(splashText);
        splashText.addAction(Actions.alpha(0));
        splashText.act(0);
        splashText.addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.fadeIn(1f),
                                Actions.fadeOut(1f))));
        splashText.setPosition(Gdx.graphics.getWidth() / 32, Gdx.graphics.getHeight() - splashText.getHeight());
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
        /*game.batch.begin();
        splashText.act(Gdx.graphics.getDeltaTime());
        splashText.draw(game.batch, 1);
        //game.batch.draw(playButton, Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 60, 300, 120);
        game.batch.end();*/
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
