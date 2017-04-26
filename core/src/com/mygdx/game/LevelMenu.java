package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

public class LevelMenu extends ScreenAdapter
{
    private final MiceAlert game;
    private Stage level_menu;
    private Stage map_pack_menu;
    private Table level_layout;
    private ArrayList<Level> level_list;

    private Texture buttonTexture;
    private TextureRegion buttontextureRegion;
    private TextureRegionDrawable buttonTextureRegionDrawable;
    private Button playButton;

    public LevelMenu(MiceAlert game)
    {
        this.game = game;
        this.level_menu = new Stage();
        this.map_pack_menu = new Stage();
        this.level_layout = new Table();
        Json json = new Json();
        this.level_list = json.fromJson(ArrayList.class, Gdx.files.internal("level/test.json").readString());

        /*level_menu.setViewport(
            new StretchViewport(
                Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4),
                Gdx.graphics.getHeight()));*/
        level_layout.setWidth(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4));
        level_layout.setHeight(Gdx.graphics.getHeight());
        level_layout.setPosition(0, 0);

        buttonTexture = new Texture("image/testButton2.png");
        buttontextureRegion = new TextureRegion(buttonTexture);
        buttonTextureRegionDrawable = new TextureRegionDrawable(buttontextureRegion);

        this.makeMenu();
        Gdx.input.setInputProcessor(level_menu);
    }

    public void makeMenu()
    {
        //Gdx.app.log("Yokaka", level_list.size() + "");
        int row_index = 1;
        float phone_scale = Gdx.graphics.getHeight() / ((float) buttonTexture.getHeight() * 8);
        for(final Level level : level_list)
        {
            playButton = new Button(buttonTextureRegionDrawable);
            playButton.addListener(new ClickListener()
            {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    //Gdx.app.log("Yokaka", "test");
                    game.setScreen(new LevelGenerator(game, level));
                    return true;
                }
            });

            level_layout.add(playButton).size(playButton.getWidth() * phone_scale, playButton.getHeight() * phone_scale).pad(30);
            if(row_index % 4 == 0)
            {
                level_layout.row();
            }
            row_index++;
        }
        //playButton = new Button(buttonTextureRegionDrawable);
        level_menu.addActor(level_layout);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        level_menu.act(delta);
        level_menu.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        //level_menu.getViewport().update(width, height, true);
    }
}
