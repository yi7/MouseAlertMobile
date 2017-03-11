package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;

public class LevelGenerator extends ScreenAdapter implements InputProcessor, Screen
{
    MiceAlert game;
    OrthographicCamera camera;
    TiledMap tilemap;
    TiledMapRenderer tilemapRenderer;
    TilemapSystem tilemapObjectRenderer;
    MapObjects mapObjectsTiles;
    MapObjects mapObjectsWalls;

    Texture textureCatTracer;
    Sprite spriteCatTracer;
    SpriteAnimation spriteAnimationCatTracer;
    MapObjects mapObjectsCatTracer;

    MapObjects test;

    EntitySystem entitySystem;

    float deltaTime;
    float phoneScale;
    float phoneWidth;
    float phoneHeight;

    public LevelGenerator(MiceAlert game)
    {
        this.game = game;
        entitySystem = new EntitySystem();
        this.create();
    }

    public void create()
    {
        phoneWidth = Gdx.graphics.getWidth();
        phoneHeight = Gdx.graphics.getHeight();
        phoneScale = phoneHeight / 576f; //576 = 64px * 9tiles

        camera = new OrthographicCamera(phoneWidth, phoneHeight);
        camera.setToOrtho(false, phoneWidth, phoneHeight);
        camera.update();

        Gdx.input.setInputProcessor(this);

        tilemap = new TmxMapLoader().load("Assets_Level/MiceAlert_Map_TileMap_00.tmx");
        tilemapRenderer = new OrthogonalTiledMapRenderer(tilemap, phoneScale);
        tilemapObjectRenderer = new TilemapSystem();
        tilemapObjectRenderer.tilemapSetScale(phoneScale);
        mapObjectsTiles = tilemap.getLayers().get("Layer_Collision_Tiles").getObjects();
        mapObjectsWalls = tilemap.getLayers().get("Layer_Collision_Walls").getObjects();

        textureCatTracer = new Texture("Assets_Image/MiceAlert_Sprite_TracerCat.png");
        spriteCatTracer = new Sprite(textureCatTracer, 8, 1);
        spriteCatTracer.setScale(phoneScale);
        spriteAnimationCatTracer = new SpriteAnimation(1f/4f, spriteCatTracer.generateSpriteTextureRegion());
        spriteAnimationCatTracer.setScale(spriteCatTracer.getScale());
        spriteAnimationCatTracer.setPlayMode(Animation.PlayMode.LOOP);
        mapObjectsCatTracer = tilemap.getLayers().get("Layer_Spawn_Cats").getObjects();
        for(MapObject object : mapObjectsCatTracer)
        {
            if (object instanceof RectangleMapObject)
            {
                Entity entityCatTracer = new Entity(Entity.entityType.CAT_TRACER, spriteCatTracer);

                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                entityCatTracer.setPosition(rect.getX(), rect.getY());
                entityCatTracer.setState(object.getProperties().get("State", String.class));

                entitySystem.newEntity(entityCatTracer);
            }
        }

        deltaTime = 0f;
    }

    public void generateLevel()
    {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        deltaTime += Gdx.graphics.getDeltaTime();

        camera.update();
        tilemapRenderer.setView(camera);
        tilemapRenderer.render();

        game.batch.begin();

        game.batch.setProjectionMatrix(camera.combined);

        tilemapObjectRenderer.tilemapRenderObject(mapObjectsTiles, game.batch, deltaTime);
        tilemapObjectRenderer.tilemapRenderObject(mapObjectsWalls, game.batch, deltaTime);

        entitySystem.drawAllEntity(deltaTime, game.batch);

        game.batch.end();
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
