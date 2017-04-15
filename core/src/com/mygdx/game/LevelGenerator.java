package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.Collection;

public class LevelGenerator extends ScreenAdapter implements GestureListener, Screen
{
    MiceAlert game;
    OrthographicCamera camera;
    OrthographicCamera hudCamera;
    Stage stage;
    GestureDetector gestureDetector;
    TiledMap tilemap;
    TiledMapRenderer tilemapRenderer;
    TilemapSystem tilemapObjectRenderer;
    MapObjects mapObjectsTiles;
    MapObjects mapObjectsWalls;

    TextButton button;

    Texture textureCatTracer;
    Texture textureMouseNeutral;
    Texture textureTileArrow;
    Texture textureTileBlock;
    Sprite spriteCatTracer;
    Sprite spriteMouseNeutral;
    Sprite spriteTileBlock;
    Sprite spriteTileArrow;
    Entity entityTileArrow;
    MapObjects mapObjectsCatTracer;

    MapObjects test;

    EntitySystem entitySystem;

    float deltaTime;
    float phoneScale;
    float phoneWidth;
    float phoneHeight;

    final int TILE_SIZE = 64;
    final int TILE_MAP_HEIGHT = 7;
    final int TPL = 9; //tiles per line

    public enum LevelState
    {
        STANDBY,
        PLAY,
        RESET,
        FINISH
    }
    LevelState levelState;

    public LevelGenerator(MiceAlert game)
    {
        this.game = game;
        entitySystem = new EntitySystem();
        stage = new Stage();
        levelState = LevelState.STANDBY;
        phoneWidth = Gdx.graphics.getWidth();
        phoneHeight = Gdx.graphics.getHeight();
        phoneScale = phoneHeight / ((float)TILE_SIZE * TILE_MAP_HEIGHT); //576 = 64px * 9tiles

        LevelHudGenerator hud = new LevelHudGenerator(this);
        stage = hud.getHud();

        this.create();
    }

    public void create()
    {
        camera = new OrthographicCamera(phoneWidth, phoneHeight);
        camera.setToOrtho(false, phoneWidth, phoneHeight);
        camera.update();

        hudCamera = new OrthographicCamera(phoneWidth - (TILE_SIZE * TPL * phoneScale), phoneHeight);
        hudCamera.setToOrtho(false, phoneWidth - (TILE_SIZE * TPL * phoneScale), phoneHeight);
        hudCamera.update();

        gestureDetector = new GestureDetector(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);

        tilemap = new TmxMapLoader().load("Assets_Level/MiceAlert_Map_TileMap_01.tmx");
        tilemapRenderer = new OrthogonalTiledMapRenderer(tilemap, phoneScale);
        tilemapObjectRenderer = new TilemapSystem();
        tilemapObjectRenderer.tilemapSetScale(phoneScale);
        //mapObjectsWalls = tilemap.getLayers().get("Layer_Collision_Walls").getObjects();

        textureTileBlock = new Texture("Assets_Image/Block.png");
        spriteTileBlock = new Sprite(textureTileBlock, 8, 4);
        spriteTileBlock.setScale(phoneScale);
        mapObjectsTiles = tilemap.getLayers().get("Layer_Collision_Tiles").getObjects();
        for(MapObject object : mapObjectsTiles)
        {
            if(object instanceof  RectangleMapObject)
            {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                Entity entityTileBlock = new Entity(Entity.entityType.TILE_BLOCK, spriteTileBlock, phoneScale);
                entityTileBlock.setPosition(rect.getX(), rect.getY());
                entityTileBlock.setFrameSize(rect.getWidth(), rect.getHeight());
                entityTileBlock.setState("UP");
                entityTileBlock.setOriginalState("UP");
                entityTileBlock.generateSpriteTextureRegion();
                //Gdx.app.log("Yokaka", rect.getX() + ", " + rect.getY());
                entitySystem.newEntity(entityTileBlock);
            }
        }

        textureCatTracer = new Texture("Assets_Image/MiceAlert_Sprite_TracerCat.png");
        textureMouseNeutral = new Texture("Assets_Image/MiceAlert_Sprite_NeutralMouse.png");
        spriteCatTracer = new Sprite(textureCatTracer, 8, 4);
        spriteCatTracer.setScale(phoneScale);
        spriteMouseNeutral = new Sprite(textureMouseNeutral, 8, 4);
        spriteMouseNeutral.setScale(phoneScale);
        mapObjectsCatTracer = tilemap.getLayers().get("Layer_Spawn_Cats").getObjects();
        for(MapObject object : mapObjectsCatTracer)
        {
            if (object instanceof RectangleMapObject)
            {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                Entity temp_entity = null;
                if(object.getProperties().get("Type", String.class).equals("CAT_TRACER"))
                {
                    temp_entity = new Entity(Entity.entityType.CAT_TRACER, spriteCatTracer, phoneScale);
                }
                else if(object.getProperties().get("Type", String.class).equals("MOUSE_NEUTRAL"))
                {
                    temp_entity = new Entity(Entity.entityType.MOUSE_NEUTRAL, spriteMouseNeutral, phoneScale);
                }

                temp_entity.setPosition(rect.getX(), rect.getY());
                temp_entity.setFrameSize(rect.getWidth(), rect.getHeight());
                temp_entity.setState(object.getProperties().get("State", String.class));
                temp_entity.setOriginalState(object.getProperties().get("State", String.class));
                temp_entity.generateSpriteTextureRegion();
                entitySystem.newEntity(temp_entity);
            }
        }

        textureTileArrow = new Texture("Assets_Image/Arrow.png");
        spriteTileArrow = new Sprite(textureTileArrow, 8, 4);
        spriteTileArrow.setScale(phoneScale);

        deltaTime = 0f;
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
        entitySystem.drawAllEntity(deltaTime, game.batch);
        game.batch.end();

        stage.draw();

        entitySystem.updateAllEntity(this);
    }

    public float getLevelWidth()
    {
        return TILE_SIZE * TPL * phoneScale;
    }

    public void setLevelState(LevelState levelState)
    {
        this.levelState = levelState;
    }

    public float getScale()
    {
        return phoneScale;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        int mapX;
        int mapY;
        int tile_position;

        Vector2 tile_coordinate;

        if((x / phoneScale) <= (TILE_SIZE * TPL))
        {
            mapX = (int)(x / TILE_SIZE / phoneScale);
            mapY = (int)(y / TILE_SIZE / phoneScale);
            tile_position = TPL * mapY + mapX;

            if(tile_position < 63)
            {
                tile_coordinate = tilemapObjectRenderer.getMapCoordinate(tile_position);
                if(!entitySystem.checkAllTile(tile_coordinate))
                {
                    Gdx.app.log("Yokaka", "No Tile");
                    entityTileArrow = new Entity(Entity.entityType.TILE_ARROW, spriteTileArrow, phoneScale);
                    entityTileArrow.setPosition(tile_coordinate.x, (tile_coordinate.y));
                    entityTileArrow.setFrameSize(64f, 64f);
                    entityTileArrow.generateSpriteTextureRegion();
                }
                else
                {
                    Gdx.app.log("Yokaka", "Exist");
                }

                //entitySystem.newEntity(entityTileArrow);

                //Gdx.app.log("Yokaka", tile_coordinate.x + ", " + tile_coordinate.y);
            }


            //Gdx.app.log("Yokaka", "Touch");
            //Gdx.app.log("Yokaka", tile_position + "");
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        int mapX;
        int mapY;
        int tile_position;
        Entity entity;

        Vector2 tile_coordinate;

        if((x / phoneScale) <= (TILE_SIZE * TPL))
        {
            mapX = (int)(x / TILE_SIZE / phoneScale);
            mapY = (int)(y / TILE_SIZE / phoneScale);
            tile_position = TPL * mapY + mapX;

            if(tile_position < 63)
            {
                tile_coordinate = tilemapObjectRenderer.getMapCoordinate(tile_position);
                entitySystem.tapTile(tile_coordinate);
            }

            //Gdx.app.log("Yokaka", TILE_SIZE * TPL * phoneScale + " : " + Gdx.graphics.getWidth());
        }
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
        if(entityTileArrow != null)
        {
            if(Math.abs(velocityX) > Math.abs(velocityY))
            {
                if(velocityX > 0)
                {
                    //right
                    entityTileArrow.setState("RIGHT");
                    entitySystem.newEntity(entityTileArrow);
                    Gdx.app.log("Yokaka", "right");
                }
                else
                {
                    //left
                    entityTileArrow.setState("LEFT");
                    entitySystem.newEntity(entityTileArrow);
                    Gdx.app.log("Yokaka", "left");
                }
            }
            else
            {
                if(velocityY > 0)
                {
                    //down
                    entityTileArrow.setState("DOWN");
                    entitySystem.newEntity(entityTileArrow);
                    Gdx.app.log("Yokaka", "down");
                }
                else
                {
                    //up
                    entityTileArrow.setState("UP");
                    entitySystem.newEntity(entityTileArrow);
                    Gdx.app.log("Yokaka", "up");
                }
            }
        }
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
