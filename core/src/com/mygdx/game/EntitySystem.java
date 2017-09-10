package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * The core data structure for Entity System
 */
public class EntitySystem
{
    private Entity[] entity_list;               /**<Data Structure which contains all entities*/
    private Preferences prefs;                  /**<Save data*/
    private LevelGenerator level_generator;     /**<Level Generator*/
    private Entity gameover_entity;             /**<Entity that triggered the gameover*/

    /**
     * Constructor that initializes an empty Entity Data Structure
     */
    public EntitySystem(LevelGenerator level_generator)
    {
        entity_list = new Entity[1000];
        this.prefs = Gdx.app.getPreferences("SaveInitialLevelState");
        this.level_generator = level_generator;

        for(int i = 0; i < entity_list.length; i++)
        {
            entity_list[i] = new Entity();
        }
    }

    /**
     * Inserts a new Entity into the Data Structure
     * @param entity The Entity to insert
     */
    public void newEntity(Entity entity)
    {
        for(int i = 0; i < entity_list.length; i++)
        {
            if(entity_list[i].inuse)
            {
                continue;
            }

            entity_list[i] = entity;
            break;
        }
    }

    /**
     * Checks whether an Entity is colliding with another Entity
     * @param self Entity to check
     * @param other Entity to check
     * @return whether a collision happened or not
     */
    public boolean collisionCheckEntity(Entity self, Entity other)
    {

        if(self.type == Entity.EntityType.TILE || other.type == Entity.EntityType.TILE)
        {
            if(self.subtype == Entity.EntitySubtype.TILE_HOLE || other.subtype == Entity.EntitySubtype.TILE_HOLE)
            {
                if ((self.subtype == Entity.EntitySubtype.TILE_HOLE && other.subtype == Entity.EntitySubtype.MOUSE_HOVER) ||
                    (self.subtype == Entity.EntitySubtype.MOUSE_HOVER && other.subtype == Entity.EntitySubtype.TILE_HOLE))
                {
                    return false;
                }

                if(self.subtype == Entity.EntitySubtype.TILE_HOLE)
                {
                    Entity temp_entity = this.getEntityOnTile(other.position, Entity.EntitySubtype.TILE_HOLE);
                    if(temp_entity == null)
                    {
                        return false;
                    }
                }
                else
                {
                    Entity temp_entity = this.getEntityOnTile(self.position, Entity.EntitySubtype.TILE_HOLE);
                    if(temp_entity == null)
                    {
                        return false;
                    }
                }
            }

            if(self.subtype == Entity.EntitySubtype.TILE_HOME || other.subtype == Entity.EntitySubtype.TILE_HOME)
            {
                if(self.subtype == Entity.EntitySubtype.TILE_HOME)
                {
                    Entity temp_entity = this.getEntityOnTile(other.position, Entity.EntitySubtype.TILE_HOME);
                    if(temp_entity == null)
                    {
                        return false;
                    }
                }
                else
                {
                    Entity temp_entity = this.getEntityOnTile(self.position, Entity.EntitySubtype.TILE_HOME);
                    if(temp_entity == null)
                    {
                        return false;
                    }
                }
            }

            if( (self.position.x + self.sprite_frame.x > other.position.x) &&
                (other.position.x + other.sprite_frame.x > self.position.x) &&
                (self.position.y + self.sprite_frame.y > other.position.y) &&
                (other.position.y + other.sprite_frame.y > self.position.y) )
            {
                return true;
            }
        }
        else if(self.type == Entity.EntityType.WALL || other.type == Entity.EntityType.WALL)
        {
            int sprite_frame = 64;
            int wall_frame = 48;
            if(self.type == Entity.EntityType.WALL)
            {
                if( (self.position.x + ((wall_frame - self.hitbox_frame.x)/2) + self.hitbox_frame.x > other.position.x + ((sprite_frame - other.hitbox_frame.x)/2)) &&
                    (other.position.x + ((sprite_frame - other.hitbox_frame.x)/2) + other.hitbox_frame.x > self.position.x) &&
                    (self.position.y + ((wall_frame - self.hitbox_frame.y)/2) + self.hitbox_frame.y > other.position.y) &&
                    (other.position.y + ((sprite_frame - other.hitbox_frame.y)/2) + other.hitbox_frame.y > self.position.y + ((sprite_frame - other.hitbox_frame.y)/2)) )
                {
                    return true;
                }
            }
            else
            {
                if( (self.position.x + ((sprite_frame - self.hitbox_frame.x)/2) + self.hitbox_frame.x > other.position.x) &&
                    (other.position.x + ((wall_frame - other.hitbox_frame.x)/2) + other.hitbox_frame.x > self.position.x + ((sprite_frame - self.hitbox_frame.x)/2)) &&
                    (self.position.y + ((sprite_frame - self.hitbox_frame.y)/2) + self.hitbox_frame.y > other.position.y) &&
                    (other.position.y + ((wall_frame - other.hitbox_frame.y)/2) + other.hitbox_frame.y > self.position.y + ((sprite_frame - self.hitbox_frame.y)/2)) )
                {
                    return true;
                }
            }
        }
        else
        {
            int sprite_frame = 64;
            if( (self.position.x + ((sprite_frame - self.hitbox_frame.x)/2) + self.hitbox_frame.x > other.position.x) &&
                (other.position.x + ((sprite_frame - other.hitbox_frame.x)/2) + other.hitbox_frame.x > self.position.x) &&
                (self.position.y + ((sprite_frame - self.hitbox_frame.y)/2) + self.hitbox_frame.y > other.position.y) &&
                (other.position.y + ((sprite_frame - other.hitbox_frame.y)/2) + other.hitbox_frame.y > self.position.y) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Draws all Entities in the Entity System.
     * @param batch Sprite Batch
     * @param delta_time Game Time
     */
    public void drawAllEntity(Batch batch, float delta_time)
    {
        this.drawAllSubTypeEntity(batch, delta_time, Entity.EntitySubtype.TILE_ARROW);
        this.drawAllTypeEntity(batch, delta_time, Entity.EntityType.TILE);
        this.drawAllTypeEntity(batch, delta_time, Entity.EntityType.MOUSE);
        this.drawAllTypeEntity(batch, delta_time, Entity.EntityType.CAT);
        this.drawAllTypeEntity(batch, delta_time, Entity.EntityType.WALL);
    }

    /**
     * Draws all Entities in the Entity System filtered by type
     * @param batch Sprite Batch
     * @param delta_time Game Time
     * @param type type to filter by
     */
    private void drawAllTypeEntity(Batch batch, float delta_time, Entity.EntityType type)
    {
        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            if(entity_list[i].type == type)
            {
                entity_list[i].draw(entity_list[i].getKey(), batch, delta_time, entity_list[i].position.x, entity_list[i].position.y);
            }
        }
    }

    /**
     * Draws all Entities in the Entity System filtered by subtype
     * @param batch Sprite Batch
     * @param delta_time Game Time
     * @param subtype subtype to filter by
     */
    private void drawAllSubTypeEntity(Batch batch, float delta_time, Entity.EntitySubtype subtype)
    {
        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            if(entity_list[i].subtype == subtype)
            {
                entity_list[i].draw(entity_list[i].getKey(), batch, delta_time, entity_list[i].position.x, entity_list[i].position.y);
            }
        }
    }

    /**
     * Saves all Entities into a Preference
     */
    public void saveAllEntities()
    {
        Entity entity;
        Json json = new Json();
        ArrayList<Entity> temp_entity_list = new ArrayList<Entity>();

        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            entity = new Entity(entity_list[i].position, entity_list[i].subtype, entity_list[i].state);
            temp_entity_list.add(entity);
        }

        String jsonString = json.prettyPrint(temp_entity_list);
        prefs.putString("SaveLevel", jsonString);
        prefs.flush();

        //Gdx.app.log("Yokaka", prefs.getString("SaveLevel"));
    }

    /**
     * Checks whether the passed Entity intersects with any Entity on the Entity System
     * @param entity
     * @return
     */
    public Entity collisionCheckAllEntities(Entity entity)
    {
        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            if(entity == entity_list[i])
            {
                continue;
            }

            if(entity.type == entity_list[i].type)
            {
                continue;
            }

            if(collisionCheckEntity(entity, entity_list[i]))
            {
                return entity_list[i];
            }
        }
        return null;
    }

    /**
     * Updates all Entities on the Entity System
     */
    public void updateAllEntities()
    {
        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            entity_list[i].update(this);
        }
    }

    /**
     * Frees all Entity and initializes them again from the save file
     * @param sprite_system
     */
    public void resetAllEntities(SpriteSystem sprite_system)
    {
        Entity temp_entity;
        Json json = new Json();
        ArrayList<Entity> save = json.fromJson(ArrayList.class, prefs.getString("SaveLevel"));
        this.freeAllEntities();

        for(Entity entity : save)
        {
            switch(entity.subtype)
            {
                case CAT_NEUTRAL:
                    temp_entity = new EntityCatNeutral(entity.position, entity.subtype, entity.state, sprite_system);
                    break;
                case CAT_RACER:
                    temp_entity = new EntityCatRacer(entity.position, entity.subtype, entity.state, sprite_system);
                    break;
                case MOUSE_NEUTRAL:
                    temp_entity = new EntityMouseNeutral(entity.position, entity.subtype, entity.state, sprite_system);
                    break;
                case MOUSE_HOVER:
                    temp_entity = new EntityMouseHover(entity.position, entity.subtype, entity.state, sprite_system);
                    break;
                case TILE_ARROW:
                    temp_entity = new EntityTileArrow(entity.position, entity.subtype, entity.state, sprite_system);
                    break;
                case VWALL_NEUTRAL:
                case HWALL_NEUTRAL:
                    temp_entity = new EntityWall(entity.position, entity.subtype, entity.state, sprite_system);
                    break;
                default:
                    temp_entity = new Entity(entity.position, entity.subtype, entity.state, sprite_system);
                    break;
            }
            this.newEntity(temp_entity);
        }
        level_generator.setLevelState(LevelGenerator.LevelState.STANDBY);
    }

    /**
     * Frees all Entities
     */
    public void freeAllEntities()
    {
        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            entity_list[i].free();
        }
    }

    /**
     * Gets the Entity specified by coordinate and type
     * @param coordinate the coordinate to check
     * @param subtype the type of Entity to check
     * @return Entity that is on the coordinate. Null if there is none
     */
    public Entity getEntityOnTile(Vector2 coordinate, Entity.EntitySubtype subtype)
    {

        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            if( entity_list[i].position.x == coordinate.x &&
                entity_list[i].position.y == coordinate.y &&
                entity_list[i].subtype == subtype)
            {
                return entity_list[i];
            }
        }
        return null;
    }

    /**
     * Checks whether an Arrow Tile can be placed or not
     * @param coordinate the coordinate to check
     * @return True if Tile is occupied, otherwise False
     */
    public boolean checkOpenTile(Vector2 coordinate)
    {
        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            if(entity_list[i].subtype == Entity.EntitySubtype.TILE_ARROW)
            {
                continue;
            }

            if( entity_list[i].position.x == coordinate.x &&
                entity_list[i].position.y == coordinate.y )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the State of the Level
     * @param level_state state of the level_generator
     */
    public void setLevelState(LevelGenerator.LevelState level_state)
    {
        level_generator.setLevelState(level_state);
    }

    /**
     * Checks whether player won or not
     * @return True if all mice made it home, otherwise false
     */
    public boolean checkWinState()
    {
        int mouse_count = 0;

        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            if(entity_list[i].type == Entity.EntityType.MOUSE)
            {
                mouse_count++;
            }
        }

        if(mouse_count == 0)
        {
            level_generator.setLevelState(LevelGenerator.LevelState.WIN);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Gets the Cat Entity that caused the game over
     * @return
     */
    public Entity getGameOverEntity()
    {
        return gameover_entity;
    }

    /**
     * Sets the Cat Entity that caused the game over
     * @param entity
     */
    public void setGameOverEntity(Entity entity)
    {
        this.gameover_entity = entity;
    }
}
