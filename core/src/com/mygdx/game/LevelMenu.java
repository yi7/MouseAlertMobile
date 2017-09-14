package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

public class LevelMenu extends ScreenAdapter implements GestureDetector.GestureListener, Screen
{
    private final MiceAlert game;
    private Stage level_menu;
    private Stage map_pack_menu;
    private Table level_layout;
    private Table pack_layout;
    private ArrayList<Level> level_list;
    private ArrayList<MapPack> pack_list;
    private ScrollPane scroller;

    private Texture buttonTexture;
    private TextureRegion buttontextureRegion;
    private TextureRegionDrawable buttonTextureRegionDrawable;
    private Texture buttonTexture2;
    private TextureRegion buttontextureRegion2;
    private TextureRegionDrawable buttonTextureRegionDrawable2;
    private Button playButton;
    private GestureDetector gestureDetector;
    private float phone_scale;

    public LevelMenu(MiceAlert game)
    {
        this.game = game;
        this.level_menu = new Stage();
        this.map_pack_menu = new Stage();
        this.level_layout = new Table();
        this.pack_layout = new Table();

        Json json = new Json();
        //this.level_list = json.fromJson(ArrayList.class, Gdx.files.internal("level/pack01/pack01.json").readString());
        this.pack_list = json.fromJson(ArrayList.class, Gdx.files.internal("level/pack_location.json").readString());

        /*level_menu.setViewport(
            new StretchViewport(
                Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4),
                Gdx.graphics.getHeight()));*/
        level_layout.setWidth(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4));
        level_layout.setHeight(Gdx.graphics.getHeight());
        level_layout.setPosition(0, 0);

        /*pack_layout.setWidth(Gdx.graphics.getWidth() / 4);
        pack_layout.setHeight(Gdx.graphics.getHeight());
        pack_layout.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4), 0);*/

        buttonTexture = new Texture("image/testButton2.png");
        buttontextureRegion = new TextureRegion(buttonTexture);
        buttonTextureRegionDrawable = new TextureRegionDrawable(buttontextureRegion);
        this.phone_scale = Gdx.graphics.getHeight() / ((float) buttonTexture.getHeight() * 8);

        buttonTexture2 = new Texture("image/testButton.png");
        buttontextureRegion2 = new TextureRegion(buttonTexture);
        buttonTextureRegionDrawable2 = new TextureRegionDrawable(buttontextureRegion);

        //this.makeMenu();
        this.makePackMenu();

        this.gestureDetector = new GestureDetector(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(level_menu);
        multiplexer.addProcessor(map_pack_menu);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void makeMenu(String pack_path)
    {
        //Gdx.app.log("Yokaka", level_list.size() + "");
        Json json = new Json();
        this.level_list = json.fromJson(ArrayList.class, Gdx.files.internal(pack_path).readString());

        int row_index = 1;
        //float phone_scale = Gdx.graphics.getHeight() / ((float) buttonTexture.getHeight() * 8);
        for(final Level level : level_list)
        {
            playButton = new Button(buttonTextureRegionDrawable);
            playButton.addListener(new ActorGestureListener()
            {
                public void tap(InputEvent event, float x, float y, int pointer, int button)
                {
                    //Gdx.app.log("Yokaka", "test");
                    game.setScreen(new LevelGenerator(game, level));
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

    public void makePackMenu()
    {
        Table table = new Table();
        table.setWidth(Gdx.graphics.getWidth() / 4);
        table.setHeight(Gdx.graphics.getHeight());
        table.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 4), 0);

        pack_layout.row();
        pack_layout.row();
        pack_layout.row();
        pack_layout.row();
        for(final MapPack pack: pack_list)
        {
            playButton = new Button(buttonTextureRegionDrawable);
            playButton.addListener(new ActorGestureListener()
            {
                public void tap(InputEvent event, float x, float y, int pointer, int button)
                {
                    //Gdx.app.log("Yokaka", "test");
                    makeMenu(pack.getPath());
                }
            });
            pack_layout.add(playButton).size(playButton.getWidth() * phone_scale, playButton.getHeight() * phone_scale).pad(30);
            pack_layout.row();
        }

        scroller = new ScrollPane(pack_layout);
        table.add(scroller);
        table.padTop(120);
        table.padBottom(120);
        map_pack_menu.addActor(table);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        level_menu.act(delta);
        level_menu.draw();
        map_pack_menu.act(delta);
        map_pack_menu.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        //level_menu.getViewport().update(width, height, true);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        return false;
    }

    @Override
    public boolean longPress(float x, float y)
    {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
    {
        return false;
    }

    @Override
    public void pinchStop()
    {

    }
}
