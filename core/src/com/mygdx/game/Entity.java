package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.logging.Level;

/**
 * Contains template of a Entity
 */
public class Entity
{
    /**
     * Contains all the different types of an Entity
     */
    public enum EntityType
    {
        MOUSE_NEUTRAL,
        MOUSE_RACER,
        MOUSE_HOVER,
        CAT_RACER,
        WALL,
        TILE_BLOCK,
        TILE_HOME,
        TILE_ARROW
    }

    /**
     * Contains all the different state of an Entity
     */
    public enum EntityState
    {
        UP,
        RIGHT,
        DOWN,
        LEFT,
        FREE
    }

    public boolean inuse;               /**<Flag to determine whether an Entity is in use*/
    public Vector2 position;            /**<Position of the Entity*/
    public Vector2 frame;               /**<Frame of the Entity*/
    public int velocity;                /**<Speed of the Entity*/
    public EntityType type;             /**<Type of the Entity*/
    public EntityState state;           /**<State of the Entity*/
    public SpriteSystem sprite_system;  /**<Sprite System that contains Entity Animation*/

    /**
     * Constructor that creates an empty Entity
     */
    public Entity()
    {
        this.inuse = false;
        this.position = null;
        this.frame = null;
        this.velocity = 0;
        this.type = null;
        this.state = null;
        this.sprite_system = null;
    }

    /**
     * Constructor for saving Entity initial state
     * @param position position of the Entity
     * @param type type of the Entity
     * @param state state of the Entity
     */
    public Entity(Vector2 position, Entity.EntityType type, Entity.EntityState state)
    {
        this.position = position;
        this.type = type;
        this.state = state;
    }

    /**
     * Constructor that initializes a new Entity
     * @param position  position of the Entity
     * @param type type of the Entity
     * @param state state of the Entity
     * @param sprite_system Sprite System
     */
    public Entity(Vector2 position, EntityType type, EntityState state, SpriteSystem sprite_system)
    {
        this.inuse = true;
        this.position = position;
        this.frame = new Vector2(64, 64);
        this.velocity = 0;
        this.type = type;
        this.state = state;
        this.sprite_system = sprite_system;
    }

    /**
     * Sets the State of the Entity
     * @param state state of the Entity
     */
    public void setState(Entity.EntityState state)
    {
        this.state = state;
    }

    /**
     * Gets the Key for what Animation to draw
     * Key  Value
     * 0    TILE_BLOCK
     * 1    TILE_HOME
     * 2    TILE_ARROW_UP
     * 3    TILE_ARROW_RIGHT
     * 4    TILE_ARROW_DOWN
     * 5    TILE_ARROW_LEFT
     * 6    TILE_DOT
     * 7    TILE_HOLE
     * 8    CAT_RACER_UP
     * 9    CAT_RACER_RIGHT
     * 10   CAT_RACER_DOWN
     * 11   CAT_RACER_LEFT
     * 12   MOUSE_NEUTRAL_UP
     * 13   MOUSE_NEUTRAL_RIGHT
     * 14   MOUSE_NEUTRAL_DOWN
     * 15   MOUSE_NEUTRAL_RIGHT
     * @return Index of Animation depending on Entity Type and State
     */
    public int getKey()
    {
        switch(type)
        {
            case TILE_BLOCK:
                return 0;
            case TILE_HOME:
                return 1;
            case TILE_ARROW:
                switch(state)
                {
                    case UP:
                        return 2;
                    case RIGHT:
                        return 3;
                    case DOWN:
                        return 4;
                    case LEFT:
                        return 5;
                }
                break;
            case CAT_RACER:
                switch(state)
                {
                    case UP:
                        return 8;
                    case RIGHT:
                        return 9;
                    case DOWN:
                        return 10;
                    case LEFT:
                        return 11;
                }
            case MOUSE_NEUTRAL:
                switch(state)
                {
                    case UP:
                        return 12;
                    case RIGHT:
                        return 13;
                    case DOWN:
                        return 14;
                    case LEFT:
                        return 15;
                }
            default:
                break;
        }
        return 0;
    }

    /**
     * Draws the Entity
     * @param key Determines what Animation to draw
     * @param batch Sprite Batch
     * @param x X position on the Level
     * @param y Y position on the Level
     */
    public void draw(int key, Batch batch, float delta_time, float x, float y)
    {
        sprite_system.draw(key, batch, delta_time, x, y);
    }

    /**
     * Frees the Entity
     */
    public void free()
    {
        this.inuse = false;
        this.position = null;
        this.frame = null;
        this.velocity = 0;
        this.type = null;
        this.state = null;
    }

    /**
     * Acts based on what Entity is colliding with
     * @param collided_entity Entity it collided with
     * @param entity_system Entity System
     */
    public void think(Entity collided_entity, EntitySystem entity_system)
    {
        return;
    }

    /**
     * Updates based on what Entity collided with
     * @param entity_system Entity System
     */
    public void update(EntitySystem entity_system)
    {
        this.step();
        Entity temp_entity = entity_system.collisionCheckAllEntities(this);
        if(temp_entity != null)
        {
            this.think(temp_entity, entity_system);
        }

        if(checkOutOfBounds() || (temp_entity != null && temp_entity.type == EntityType.TILE_BLOCK)) {
            switch(state) {
                case UP:
                    this.backstep();
                    this.state = EntityState.RIGHT;
                    this.step();
                    temp_entity = entity_system.collisionCheckAllEntities(this);
                    if (checkOutOfBounds() || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK)) {
                        this.backstep();
                        this.state = EntityState.LEFT;
                        this.step();
                        temp_entity = entity_system.collisionCheckAllEntities(this);
                        if (checkOutOfBounds() || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK)) {
                            this.backstep();
                            this.state = Entity.EntityState.DOWN;
                        }
                        else
                        {
                            this.backstep();
                        }
                    }
                    else
                    {
                        this.backstep();
                    }
                    break;
                case RIGHT:
                    this.backstep();
                    this.state = EntityState.DOWN;
                    this.step();
                    temp_entity = entity_system.collisionCheckAllEntities(this);
                    if (checkOutOfBounds() || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK)) {
                        this.backstep();
                        this.state = Entity.EntityState.UP;
                        this.step();
                        temp_entity = entity_system.collisionCheckAllEntities(this);
                        if (checkOutOfBounds() || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK)) {
                            this.backstep();
                            this.state = Entity.EntityState.LEFT;
                        }
                        else
                        {
                            this.backstep();
                        }
                    }
                    else
                    {
                        this.backstep();
                    }
                    break;
                case DOWN:
                    this.backstep();
                    this.state = EntityState.LEFT;
                    this.step();
                    temp_entity = entity_system.collisionCheckAllEntities(this);
                    if (checkOutOfBounds() || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK)) {
                        this.backstep();
                        this.state = Entity.EntityState.RIGHT;
                        this.step();
                        temp_entity = entity_system.collisionCheckAllEntities(this);
                        if (checkOutOfBounds() || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK)) {
                            this.backstep();
                            this.state = Entity.EntityState.UP;
                        }
                        else
                        {
                            this.backstep();
                        }
                    }
                    else
                    {
                        this.backstep();
                    }
                    break;
                case LEFT:
                    this.backstep();
                    this.state = EntityState.UP;
                    this.step();
                    temp_entity = entity_system.collisionCheckAllEntities(this);
                    if (checkOutOfBounds() || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK)) {
                        this.backstep();
                        this.state = Entity.EntityState.DOWN;
                        this.step();
                        temp_entity = entity_system.collisionCheckAllEntities(this);
                        if (checkOutOfBounds() || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK)) {
                            this.backstep();
                            this.state = Entity.EntityState.RIGHT;
                        }
                        else
                        {
                            this.backstep();
                        }
                    }
                    else
                    {
                        this.backstep();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Moves Entity forward based on state
     */
    public void step()
    {
        switch(state)
        {
            case UP:
                position.y += velocity;
                break;
            case RIGHT:
                position.x += velocity;
                break;
            case DOWN:
                position.y -= velocity;
                break;
            case LEFT:
                position.x -= velocity;
        }
    }

    /**
     * Moves Entity backward based on state
     */
    public void backstep()
    {
        switch(state)
        {
            case UP:
                position.y -= velocity;
                break;
            case RIGHT:
                position.x -= velocity;
                break;
            case DOWN:
                position.y += velocity;
                break;
            case LEFT:
                position.x += velocity;
        }
    }

    /**
     * Checks whether Entity is out of bounds or not
     * @return true if its out of bounds, otherwise false
     */
    public boolean checkOutOfBounds()
    {
        LevelGenerator level = new LevelGenerator();
        float x = position.x * level.phone_scale;
        float y = position.y * level.phone_scale;
        float frameX = frame.x * level.phone_scale;
        float frameY = frame.y * level.phone_scale;

        if(y + frameY > level.phone_height)
        {
            return true;
        }
        else if(x + frameX > level.level_width)
        {
            return true;
        }
        else if(y < 0)
        {
            return true;
        }
        else if(x < 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Converts a String to Entity Type
     * @param string String to convert to Entity Type
     * @return Entity Type
     */
    public EntityType str2entityType(String string)
    {
        if(string.equals("MOUSE_NEUTRAL"))
        {
            return EntityType.MOUSE_NEUTRAL;
        }
        else if(string.equals("MOUSE_HOVER"))
        {
            return EntityType.MOUSE_HOVER;
        }
        else if(string.equals("CAT_RACER"))
        {
            return EntityType.CAT_RACER;
        }
        else if(string.equals("WALL"))
        {
            return EntityType.WALL;
        }
        else if(string.equals("TILE_BLOCK"))
        {
            return EntityType.TILE_BLOCK;
        }
        else if(string.equals("TILE_HOME"))
        {
            return EntityType.TILE_HOME;
        }
        else if(string.equals("TILE_ARROW"))
        {
            return EntityType.TILE_ARROW;
        }
        else
        {
            return null;
        }
    }

    /**
     * Converts a String to Entity State
     * @param string String to convert to Entity State
     * @return Entity State
     */
    public EntityState str2entityState(String string)
    {
        if(string.equals("UP"))
        {
            return EntityState.UP;
        }
        else if(string.equals("RIGHT"))
        {
            return EntityState.RIGHT;
        }
        else if(string.equals("DOWN"))
        {
            return EntityState.DOWN;
        }
        else if(string.equals("LEFT"))
        {
            return EntityState.LEFT;
        }
        else if(string.equals("FREE"))
        {
            return EntityState.FREE;
        }
        else
        {
            return null;
        }
    }
}
