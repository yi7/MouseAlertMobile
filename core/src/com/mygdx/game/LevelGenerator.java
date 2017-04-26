package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Generates the Puzzle Level
 */
public class LevelGenerator extends ScreenAdapter implements GestureListener, Screen
{
    /**
     * Contains the different state of the Level
     */
    public enum LevelState
    {
        STANDBY,
        PLAY,
        RESET,
        GAMEOVER,
        WIN,
        FREE
    }

    private MiceAlert game;                                         /**<Game*/
    public LevelState level_state;                                 /**<Determines the State of the level*/
    public EntitySystem entity_system;                              /**<Data Structure for Entity*/
    public SpriteSystem sprite_system;                              /**<Data Structure for Sprite*/
    public TilemapSystem tilemap_system;                            /**<Data Structure for Level Coordinates*/

    private Texture sprite_sheet_texture;                           /**<Sprite Sheet*/
    private final int sprite_sheet_cols = 8;                        /**<Sprite Sheet columns*/
    private final int sprite_sheet_rows = 32;                        /**<Sprite Sheet rows*/

    private OrthographicCamera level_camera;                        /**<Camera for the level*/
    private OrthographicCamera hud_camera;                          /**<Camera for the hud*/
    private GestureDetector gestureDetector;                        /**<Determines touch input*/
    private Stage stage;                                            /**<HUD*/
    public boolean show_popup;
    private Stage popup_stage;

    private TiledMap tilemap;                                       /**<Tiled map*/
    private OrthogonalTiledMapRenderer tilemap_renderer;            /**<Tilemap Renderer*/
    private MapObjects objects;                                     /**<Objects in Tiled map*/

    public final float phone_width = Gdx.graphics.getWidth();      /**<The width of the phone screen*/
    public final float phone_height = Gdx.graphics.getHeight();    /**<The height of the phone screen*/
    public float level_width;                                      /**<The width of the Tilemap*/
    public float phone_scale;                                      /**<The scale of phone screen height*/
    private float delta_time;                                      /**<Game time*/

    private Entity entity_arrow;                                   /**<The Arrow Tile Entity that user can interact with*/

    /**
     * Constructor that initializes an empty level. Used in order to get phone scale
     */
    public LevelGenerator()
    {
        this.tilemap_system = new TilemapSystem();
        this.phone_scale = phone_height / ((float) tilemap_system.getTileFrameSize() * tilemap_system.tilemap_height);
        this.level_width = tilemap_system.tile_frame_size * tilemap_system.tilemap_width * phone_scale;
    }

    /**
     * Constructor that initializes the level
     * @param game contains the batch
     * @param level Level class
     */
    public LevelGenerator(MiceAlert game, Level level)
    {
        this.game = game;
        this.entity_system = new EntitySystem(this);
        this.sprite_sheet_texture = new Texture("image/MiceAlert_SpriteSheet.png");
        this.sprite_system = new SpriteSystem(sprite_sheet_texture, sprite_sheet_cols, sprite_sheet_rows);
        this.tilemap_system = new TilemapSystem();
        this.level_state = LevelState.STANDBY;
        this.show_popup = false;

        this.initializeTilemap(level.getPath());
        this.initializeLevelHud();
        this.initializeCamera();
        this.initializeGestureDetector();
        this.initializeTilemapEntities();

        entity_system.saveAllEntities();
        this.entity_arrow = null;
        this.delta_time = 0;
    }

    /**
     * Initializes the Tilemap and the Tilemap Renderer
     * @param level_path path of the level
     */
    public void initializeTilemap(String level_path)
    {
        this.phone_scale = phone_height / ((float) tilemap_system.getTileFrameSize() * tilemap_system.tilemap_height);
        this.level_width = tilemap_system.tile_frame_size * tilemap_system.tilemap_width * phone_scale;
        this.tilemap = new TmxMapLoader().load(level_path);
        //tilemap = new TmxMapLoader().load("level/MiceAlert_Map_TileMap_01.tmx");
        this.tilemap_renderer = new OrthogonalTiledMapRenderer(tilemap, phone_scale);
    }

    /**
     * Initializes the HUD on right side of Tilemap
     */
    public void initializeLevelHud()
    {
        LevelHud hud = new LevelHud(this, game);
        this.stage = hud.getHud();
    }

    public void initializeLevelCompleteWindow()
    {
        LevelHud hud = new LevelHud(this, game);
        this.popup_stage = hud.getWinHud();
    }

    /**
     * Initializes the Level Camera
     */
    public void initializeCamera()
    {
        this.level_camera = new OrthographicCamera(phone_width, phone_height);
        level_camera.setToOrtho(false, phone_width, phone_height);
        level_camera.update();

        this.hud_camera = new OrthographicCamera(phone_width - level_width, phone_height);
        hud_camera.setToOrtho(false, phone_width -level_width, phone_height);
        hud_camera.update();
        //Gdx.app.log("Yokaka", phone_width + " : " + phone_height + " : " + phone_scale);
    }

    /**
     * Initializes the Gesture Detector
     */
    public void initializeGestureDetector()
    {
        this.gestureDetector = new GestureDetector(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Initializes the Entities on the Tilemap
     */
    public void initializeTilemapEntities()
    {
        objects = tilemap.getLayers().get("Layer_Entities").getObjects();
        for(MapObject object : objects)
        {
            if(object instanceof RectangleMapObject)
            {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                Entity temp_entity = new Entity();
                Vector2 temp_position = new Vector2(rect.getX(), rect.getY());
                Entity.EntitySubtype subtype = temp_entity.str2entitySubtype(object.getProperties().get("Type", String.class));
                Entity.EntityState state = temp_entity.str2entityState(object.getProperties().get("State", String.class));

                switch(subtype)
                {
                    case CAT_RACER:
                        temp_entity = new EntityCatRacer(temp_position, subtype, state, sprite_system);
                        break;
                    case MOUSE_NEUTRAL:
                        temp_entity = new EntityMouseNeutral(temp_position, subtype, state, sprite_system);
                        break;
                    case TILE_ARROW:
                        temp_entity = new EntityTileArrow(temp_position, subtype, state, sprite_system);
                        break;
                    default:
                        temp_entity = new Entity(temp_position, subtype, state, sprite_system);
                        break;
                }
                entity_system.newEntity(temp_entity);
            }
        }
    }

    /**
     * Sets the State of the Level
     * @param level_state State of the Level
     */
    public void setLevelState(LevelState level_state)
    {
        this.level_state = level_state;
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level_camera.update();
        tilemap_renderer.setView(level_camera);
        tilemap_renderer.render();

        game.batch.begin();
        game.batch.setProjectionMatrix(level_camera.combined);
        entity_system.drawAllEntity(game.batch, delta_time);

        switch(level_state)
        {
            case PLAY:
                entity_system.checkWinState();
                entity_system.updateAllEntities();
                break;
            case RESET:
                entity_system.resetAllEntities(sprite_system);
                break;
            case GAMEOVER:
                Entity entity = entity_system.getGameOverEntity();
                if(entity != null)
                {
                    sprite_system.draw
                    (
                        8, game.batch, delta_time,
                        entity.position.x - (entity.sprite_frame.x / 2),
                        entity.position.y - (entity.sprite_frame.y / 2),
                        entity.sprite_frame.x * 2,
                        entity.sprite_frame.y * 2
                    );
                }
                break;
            case WIN:
                this.initializeLevelCompleteWindow();
                this.level_state = LevelState.STANDBY;
                this.show_popup = true;
                Gdx.input.setInputProcessor(popup_stage);
                break;
            case FREE:
                this.dispose();
            default:
                break;
        }
        game.batch.end();
        stage.draw();

        if(show_popup)
        {
            popup_stage.draw();
        }
        else
        {

        }

        delta_time += Gdx.graphics.getDeltaTime();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        if(level_state == LevelState.STANDBY)
        {
            int mapX;
            int mapY;
            int tile_position;
            Entity temp_entity;
            Vector2 tile_coordinate;

            if((x / phone_scale) <= (tilemap_system.getTileFrameSize() * tilemap_system.tilemap_width))
            {
                mapX = (int)(x / tilemap_system.getTileFrameSize() / phone_scale);
                mapY = (int)(y / tilemap_system.getTileFrameSize() / phone_scale);
                tile_position = tilemap_system.tilemap_width * mapY + mapX;

                if(tile_position < tilemap_system.tile_count)
                {
                    tile_coordinate = tilemap_system.getMapCoordinate(tile_position);

                    if(!entity_system.checkOpenTile(tile_coordinate))
                    {
                        entity_arrow = null;
                        return false;
                    }

                    temp_entity = entity_system.getEntityOnTile(tile_coordinate, Entity.EntitySubtype.TILE_ARROW);
                    if(temp_entity == null)
                    {
                        entity_arrow = new EntityTileArrow(tile_coordinate, Entity.EntitySubtype.TILE_ARROW, Entity.EntityState.UP, sprite_system);
                    }
                    else
                    {
                        entity_arrow = temp_entity;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        if(level_state == LevelState.STANDBY)
        {
            int mapX;
            int mapY;
            int tile_position;
            Vector2 tile_coordinate;
            Entity entity;

            if ((x / phone_scale) <= (tilemap_system.tile_frame_size * tilemap_system.tilemap_width)) {
                mapX = (int) (x / tilemap_system.tile_frame_size / phone_scale);
                mapY = (int) (y / tilemap_system.tile_frame_size / phone_scale);
                tile_position = tilemap_system.tilemap_width * mapY + mapX;

                if (tile_position < tilemap_system.tile_count) {
                    tile_coordinate = tilemap_system.getMapCoordinate(tile_position);
                    entity = entity_system.getEntityOnTile(tile_coordinate, Entity.EntitySubtype.TILE_ARROW);
                    if (entity != null) {
                        entity.free();
                    }
                }
            }
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
        if(entity_arrow != null && level_state == LevelState.STANDBY)
        {
            if(Math.abs(velocityX) > Math.abs(velocityY))
            {
                if(velocityX > 0)
                {
                    //right
                    entity_arrow.setState(Entity.EntityState.RIGHT);
                    entity_system.newEntity(entity_arrow);
                }
                else
                {
                    //left
                    entity_arrow.setState(Entity.EntityState.LEFT);
                    entity_system.newEntity(entity_arrow);
                }
            }
            else
            {
                if(velocityY > 0)
                {
                    //down
                    entity_arrow.setState(Entity.EntityState.DOWN);
                    entity_system.newEntity(entity_arrow);
                }
                else
                {
                    //up
                    entity_arrow.setState(Entity.EntityState.UP);
                    entity_system.newEntity(entity_arrow);
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
