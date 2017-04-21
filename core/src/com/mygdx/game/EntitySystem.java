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
    Entity[] entity_list; /**<Data Structure which contains all entities*/
    Preferences prefs;

    /**
     * Constructor that initializes an empty Entity Data Structure
     */
    public EntitySystem()
    {
        entity_list = new Entity[1000];

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
     * @return
     */
    public boolean collisionCheckEntity(Entity self, Entity other)
    {
        if( (self.position.x + self.frame.x > other.position.x) &&
                (other.position.x + other.frame.x > self.position.x) &&
                (self.position.y + self.frame.y > other.position.y) &&
                (other.position.y + other.frame.y > self.position.y) )
        {
            return true;
        }
        return false;
    }

    /**
     * Draws all Entities in the Entity System. Draws Arrows first, then everything else
     * @param batch Sprite Batch
     */
    public void drawAllEntity(Batch batch, float delta_time)
    {
        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            if(entity_list[i].type != Entity.EntityType.TILE_ARROW)
            {
                continue;
            }

            entity_list[i].draw(entity_list[i].getKey(), batch, delta_time, entity_list[i].position.x, entity_list[i].position.y);
        }

        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            if(entity_list[i].type == Entity.EntityType.TILE_ARROW)
            {
                continue;
            }

            entity_list[i].draw(entity_list[i].getKey(), batch, delta_time, entity_list[i].position.x, entity_list[i].position.y);
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
        prefs = Gdx.app.getPreferences("SaveInitialLevelState");

        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            entity = new Entity(entity_list[i].position, entity_list[i].type, entity_list[i].state);
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
     * @param level
     * @param sprite_system
     */
    public void resetAllEntities(LevelGenerator level, SpriteSystem sprite_system)
    {
        Entity temp_entity;
        Json json = new Json();
        ArrayList<Entity> save = json.fromJson(ArrayList.class, prefs.getString("SaveLevel"));
        this.freeAllEntities();

        for(Entity entity : save)
        {
            switch(entity.type)
            {
                case CAT_RACER:
                    temp_entity = new EntityCatRacer(entity.position, entity.type, entity.state, sprite_system);
                    break;
                case TILE_ARROW:
                    temp_entity = new EntityTileArrow(entity.position, entity.type, entity.state, sprite_system);
                    break;
                default:
                    temp_entity = new Entity(entity.position, entity.type, entity.state, sprite_system);
                    break;
            }
            this.newEntity(temp_entity);
        }
        level.setLevelState(LevelGenerator.LevelState.STANDBY);
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
     * Gets the Arrow on specified coordinate
     * @param coordinate the coordinate to check
     * @return Arrow that is on the coordinate. Null if there is none
     */
    public Entity getArrowOnTile(Vector2 coordinate)
    {
        for(int i = 0; i < entity_list.length; i++)
        {
            if(!entity_list[i].inuse)
            {
                continue;
            }

            if( entity_list[i].position.x == coordinate.x &&
                entity_list[i].position.y == coordinate.y &&
                entity_list[i].type == Entity.EntityType.TILE_ARROW)
            {
                return entity_list[i];
            }
        }
        return null;
    }
}
