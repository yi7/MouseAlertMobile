package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MainMenu implements Screen
{
    MiceAlert game;

    private Skin skin;
    private Stage stage;
    private Button startButton;
    private Button optionButton;
    Texture background;

    public MainMenu(MiceAlert game)
    {
        this.game = game;
        //startButton = new Button(skin, "play_button_inactive.png");
        //optionButton = new Button(skin, "exit_button_inactive.png");
        background = new Texture("background.png");
    }

    @Override
    public void show()
    {
        //startButton.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        //stage.addActor(startButton);
        //Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        //stage.draw();
        //game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        background.dispose();
    }
}
