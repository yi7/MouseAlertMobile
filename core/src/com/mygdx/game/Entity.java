package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

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
        MOUSE,
        CAT,
        TILE
    }

    public enum EntitySubtype
    {
        MOUSE_NEUTRAL,
        MOUSE_RACER,
        MOUSE_HOVER,
        CAT_RACER,
        TILE_BLOCK,
        TILE_HOME,
        TILE_ARROW,
        TILE_DOT,
        TILE_HOLE,
        GO_CIRCLE
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
        NONE,
        FREE
    }

    public boolean inuse;               /**<Flag to determine whether an Entity is in use*/
    public Vector2 position;            /**<Position of the Entity*/
    public Vector2 sprite_frame;        /**<Sprite Frame of the Entity*/
    public Vector2 hitbox_frame;        /**<Hitbox Frame of the Entity*/
    public int velocity;                /**<Speed of the Entity*/
    public EntityType type;             /**<Type of the Entity*/
    public EntitySubtype subtype;       /**<Subtype of the Entity*/
    public EntityState state;           /**<State of the Entity*/
    public SpriteSystem sprite_system;  /**<Sprite System that contains Entity Animation*/

    /**
     * Constructor that creates an empty Entity
     */
    public Entity()
    {
        this.inuse = false;
        this.position = null;
        this.sprite_frame = null;
        this.velocity = 0;
        this.type = null;
        this.state = null;
        this.sprite_system = null;
    }

    /**
     * Constructor for saving Entity initial state
     * @param position position of the Entity
     * @param subtype subtype of the Entity
     * @param state state of the Entity
     */
    public Entity(Vector2 position, Entity.EntitySubtype subtype, Entity.EntityState state)
    {
        this.position = position;
        this.subtype = subtype;
        this.state = state;
    }

    /**
     * Constructor that initializes a new Entity
     * @param position  position of the Entity
     * @param subtype subtype of the Entity
     * @param state state of the Entity
     * @param sprite_system Sprite System
     */
    public Entity(Vector2 position, EntitySubtype subtype, EntityState state, SpriteSystem sprite_system)
    {
        this.inuse = true;
        this.position = position;
        this.sprite_frame = new Vector2(64, 64);
        this.hitbox_frame = new Vector2(64, 64);
        this.velocity = 0;
        this.type = EntityType.TILE;
        this.subtype = subtype;
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
     * 8    Game Over Circle
     * 9
     * 10
     * 11
     * 12
     * 13
     * 14
     * 15
     * 16   CAT_RACER_UP
     * 17   CAT_RACER_RIGHT
     * 18   CAT_RACER_DOWN
     * 19   CAT_RACER_LEFT
     * 20   MOUSE_NEUTRAL_UP
     * 21   MOUSE_NEUTRAL_RIGHT
     * 22   MOUSE_NEUTRAL_DOWN
     * 23   MOUSE_NEUTRAL_LEFT
     * 24   MOUSE_HOVER_UP
     * 25   MOUSE_HOVER_RIGHT
     * 26   MOUSE_HOVER_DOWN
     * 27   MOUSE_HOVER_LEFT
     * 28   MOUSE_RACER_UP
     * 29   MOUSE_RACER_RIGHT
     * 30   MOUSE_RACER_DOWN
     * 31   MOUSE_RACER_LEFT
     * @return Index of Animation depending on Entity Type and State
     */
    public int getKey()
    {
        switch(subtype)
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
            case TILE_DOT:
                return 6;
            case TILE_HOLE:
                return 7;
            case GO_CIRCLE:
                return 8;
            case CAT_RACER:
                switch(state)
                {
                    case UP:
                        return 16;
                    case RIGHT:
                        return 17;
                    case DOWN:
                        return 18;
                    case LEFT:
                        return 19;
                }
            case MOUSE_NEUTRAL:
                switch(state)
                {
                    case UP:
                        return 20;
                    case RIGHT:
                        return 21;
                    case DOWN:
                        return 22;
                    case LEFT:
                        return 23;
                }
            case MOUSE_HOVER:
                switch(state)
                {
                    case UP:
                        return 24;
                    case RIGHT:
                        return 25;
                    case DOWN:
                        return 26;
                    case LEFT:
                        return 27;
                }
            case MOUSE_RACER:
                switch(state)
                {
                    case UP:
                        return 28;
                    case RIGHT:
                        return 29;
                    case DOWN:
                        return 30;
                    case LEFT:
                        return 31;
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
        this.sprite_frame = null;
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

        if(checkOutOfBounds() || (temp_entity != null && temp_entity.subtype == EntitySubtype.TILE_BLOCK))
        {
            this.backstep();
            switch(state)
            {
                case UP:
                    this.state = EntityState.RIGHT;
                    this.step();
                    temp_entity = entity_system.collisionCheckAllEntities(this);
                    if (checkOutOfBounds() || (temp_entity != null && temp_entity.subtype == EntitySubtype.TILE_BLOCK)) {
                        this.backstep();
                        this.state = EntityState.LEFT;
                        this.step();
                        temp_entity = entity_system.collisionCheckAllEntities(this);
                        if (checkOutOfBounds() || (temp_entity != null && temp_entity.subtype == EntitySubtype.TILE_BLOCK)) {
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
                    this.state = EntityState.DOWN;
                    this.step();
                    temp_entity = entity_system.collisionCheckAllEntities(this);
                    if (checkOutOfBounds() || (temp_entity != null && temp_entity.subtype == EntitySubtype.TILE_BLOCK)) {
                        this.backstep();
                        this.state = Entity.EntityState.UP;
                        this.step();
                        temp_entity = entity_system.collisionCheckAllEntities(this);
                        if (checkOutOfBounds() || (temp_entity != null && temp_entity.subtype == EntitySubtype.TILE_BLOCK)) {
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
                    this.state = EntityState.LEFT;
                    this.step();
                    temp_entity = entity_system.collisionCheckAllEntities(this);
                    if (checkOutOfBounds() || (temp_entity != null && temp_entity.subtype == EntitySubtype.TILE_BLOCK)) {
                        this.backstep();
                        this.state = Entity.EntityState.RIGHT;
                        this.step();
                        temp_entity = entity_system.collisionCheckAllEntities(this);
                        if (checkOutOfBounds() || (temp_entity != null && temp_entity.subtype == EntitySubtype.TILE_BLOCK)) {
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
                    this.state = EntityState.UP;
                    this.step();
                    temp_entity = entity_system.collisionCheckAllEntities(this);
                    if (checkOutOfBounds() || (temp_entity != null && temp_entity.subtype == EntitySubtype.TILE_BLOCK)) {
                        this.backstep();
                        this.state = Entity.EntityState.DOWN;
                        this.step();
                        temp_entity = entity_system.collisionCheckAllEntities(this);
                        if (checkOutOfBounds() || (temp_entity != null && temp_entity.subtype == EntitySubtype.TILE_BLOCK)) {
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
            this.step();
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
                break;
            default:
                break;
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
                break;
            default:
                break;
        }
    }

    /**
     * Checks whether Entity is out of bounds or not
     * @return true if its out of bounds, otherwise false
     */
    public boolean checkOutOfBounds()
    {
        if(this.position == null)
        {
            return false;
        }

        LevelGenerator level = new LevelGenerator();
        float x = position.x * level.phone_scale;
        float y = position.y * level.phone_scale;
        float frameX = sprite_frame.x * level.phone_scale;
        float frameY = sprite_frame.y * level.phone_scale;

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
    public EntitySubtype str2entitySubtype(String string)
    {
        if(string.equals("MOUSE_NEUTRAL"))
        {
            return EntitySubtype.MOUSE_NEUTRAL;
        }
        else if(string.equals("MOUSE_HOVER"))
        {
            return EntitySubtype.MOUSE_HOVER;
        }
        else if(string.equals("CAT_RACER"))
        {
            return EntitySubtype.CAT_RACER;
        }
        else if(string.equals("TILE_BLOCK"))
        {
            return EntitySubtype.TILE_BLOCK;
        }
        else if(string.equals("TILE_HOME"))
        {
            return EntitySubtype.TILE_HOME;
        }
        else if(string.equals("TILE_ARROW"))
        {
            return EntitySubtype.TILE_ARROW;
        }
        else if(string.equals("TILE_DOT"))
        {
            return EntitySubtype.TILE_DOT;
        }
        else if(string.equals("TILE_HOLE"))
        {
            return EntitySubtype.TILE_HOLE;
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
        else if(string.equals("NONE"))
        {
            return EntityState.NONE;
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
