package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen
{
    MiceAlert game;

    private Skin skin;
    private Stage stage;
    private Texture playTexture;
    private Texture settingTexture;
    private Texture storeTexture;
    private Texture stubTexture;
    private Image playImage;
    private Image settingImage;
    private Image storeImage;
    private Image stubImage;
    private TextButton startButton;
    private TextButton optionButton;

    public MainMenu(MiceAlert game)
    {
        this.game = game;
        playTexture = new Texture("image/stub_button1.png");
        settingTexture = new Texture("image/stub_button2.png");
        storeTexture = new Texture("image/stub_button3.png");
        stubTexture = new Texture("image/stub_button4.png");
        playImage = new Image(playTexture);
        playImage.setWidth(Gdx.graphics.getWidth() / 4);
        playImage.setHeight(Gdx.graphics.getHeight());
        settingImage = new Image(settingTexture);
        settingImage.setWidth(Gdx.graphics.getWidth() / 4);
        settingImage.setHeight(Gdx.graphics.getHeight());
        storeImage = new Image(storeTexture);
        storeImage.setWidth(Gdx.graphics.getWidth() / 4);
        storeImage.setHeight(Gdx.graphics.getHeight());
        stubImage = new Image(stubTexture);
        stubImage.setWidth(Gdx.graphics.getWidth() / 4);
        stubImage.setHeight(Gdx.graphics.getHeight());
        /*skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        startButton = new TextButton("Start", skin, "default");
        optionButton = new TextButton("Option", skin, "default");*/
        stage = new Stage();
    }

    @Override
    public void show()
    {
        /*startButton.setPosition(Gdx.graphics.getWidth() / 2 - 210/2f, Gdx.graphics.getHeight() / 2 + 70f);
        startButton.setWidth(Gdx.graphics.getWidth() / 4);
        startButton.setHeight(Gdx.graphics.getWidth() / 16);
        optionButton.setPosition(Gdx.graphics.getWidth() / 2 - 210/2f, Gdx.graphics.getHeight() / 2 - 10f);
        optionButton.setWidth(Gdx.graphics.getWidth() / 8);
        optionButton.setHeight(Gdx.graphics.getWidth() / 32);

        stage.addActor(startButton);
        stage.addActor(optionButton);*/

        playImage.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.fadeIn(1f),
                        Actions.delay(1)));
        playImage.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("Yokaka", "Play Test");
                return true;
            }
        });
        playImage.setPosition(0, 0);

        settingImage.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.fadeIn(1f),
                        Actions.delay(2)));
        settingImage.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("Yokaka", "Setting Test");
                return true;
            }
        });
        settingImage.setPosition(Gdx.graphics.getWidth() / 4, 0);

        storeImage.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.fadeIn(1f),
                        Actions.delay(3)));
        storeImage.addListener(new ClickListener()
        {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.log("Yokaka", "Store Test");
                return true;
            }
        });
        storeImage.setPosition(Gdx.graphics.getWidth() / 4 + storeImage.getWidth(), 0);

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
        stubImage.setPosition(Gdx.graphics.getWidth() / 4 + stubImage.getWidth() * 2, 0);

        stage.addActor(playImage);
        stage.addActor(settingImage);
        stage.addActor(storeImage);
        stage.addActor(stubImage);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
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

    }
}
