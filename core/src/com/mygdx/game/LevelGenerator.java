package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class LevelGenerator extends ApplicationAdapter implements InputProcessor, Screen
{
    private static final int FRAME_COLS = 8, FRAME_ROWS = 1;

    MiceAlert game;
    OrthographicCamera camera;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    MapObjects objects;

    SpriteAnimation Animation_TracerCat;
    Texture texture;
    TextureAtlas textureAtlas;
    Sprite sprite;

    float stateTime;

    float testX, testY;

    public LevelGenerator(MiceAlert game)
    {
        this.game = game;
        texture = new Texture("Assets_Image/MiceAlert_Sprite_TracerCat.png");
        this.create();
    }

    @Override
    public void create()
    {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        TextureRegion[][] temp = TextureRegion.split(
                texture,
                texture.getWidth() / FRAME_COLS,
                texture.getHeight() / 4 / FRAME_ROWS);
        TextureRegion[] Frames_TracerCat = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for(int i  = 0; i < FRAME_ROWS; i++)
        {
            for(int j = 0; j < FRAME_COLS; j++)
            {
                Frames_TracerCat[index++] = temp[i][j];
            }
        }
        Animation_TracerCat = new SpriteAnimation(1f/4f, Frames_TracerCat);
        Animation_TracerCat.setScaling(w / 576f);
        Animation_TracerCat.setPlayMode(Animation.PlayMode.LOOP);


        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, w, h);
        //camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        tiledMap = new TmxMapLoader().load("Assets_Level/MiceAlert_Map_TileMap_00.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, w / 576f); //576 = 64px * 9tiles
        //tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);

        objects = tiledMap.getLayers().get("Layer_Spawn_Cat").getObjects();
        for(MapObject object : objects)
        {
            if(object instanceof RectangleMapObject)
            {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                testX = rect.getX() * w / 576f;
                testY = rect.getY() * w / 576f;
            }
        }
        stateTime = 0f;
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += Gdx.graphics.getDeltaTime();

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        //game.batch.draw(Animation_TracerCat.getKeyFrame(stateTime, true), 0, 0);
        Animation_TracerCat.draw(stateTime, game.batch, testX, testY);
        //Gdx.app.log("Yokaka", testX + " " + testY);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        //float aspectRatio = (float)width / (float)height;
        //camera = new OrthographicCamera(2f * aspectRatio, 2f);
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void hide()
    {

    }
}
