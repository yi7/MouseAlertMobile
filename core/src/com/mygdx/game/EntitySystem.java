package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.google.gwt.json.client.JSONArray;

import java.util.ArrayList;

import static java.awt.SystemColor.text;

public class EntitySystem
{
    Entity[] entityList;

    public EntitySystem()
    {
        entityList = new Entity[1000];

        for(int i = 0; i < entityList.length; i++)
        {
            entityList[i] = new Entity(null, null, 0);
        }
    }

    public void newEntity(Entity entity)
    {
        for(int i = 0; i < entityList.length; i++)
        {
            if(entityList[i].inuse)
            {
                continue;
            }

            entityList[i] = entity;
            break;
        }
    }

    public void freeEntity(Entity entity)
    {
        entity.inuse = false;
        entity.sprite = null;
    }

    public void drawAllEntity(float deltaTime, Batch batch)
    {
        for(int i = 0; i < entityList.length; i++) //Draws Arrows First
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            if(entityList[i].type != Entity.EntityType.TILE_ARROW)
            {
                continue;
            }

            entityList[i].drawEntity(deltaTime, batch, entityList[i].position.x, entityList[i].position.y);
        }

        for(int i = 0; i < entityList.length; i++) //Draws
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            if(entityList[i].type == Entity.EntityType.TILE_ARROW)
            {
                continue;
            }

            entityList[i].drawEntity(deltaTime, batch, entityList[i].position.x, entityList[i].position.y);
        }
    }

    public void updateAllEntity(LevelGenerator level)
    {
        Entity entity;
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            updateEntityVelocity(entityList[i], level.levelState);
            switch(level.levelState)
            {
                case PLAY:
                    updateEntity(entityList[i]);
                    //updateEntityOnArrow(entityList[i]);
                    break;
                case RESET:
                    resetEntity(entityList[i]);
                    if(entityList[i].type == Entity.EntityType.TILE_ARROW)
                    {
                        freeEntity(entityList[i]);
                    }
                    break;
                default:
                    break;
            }
        }

        if(level.levelState == LevelGenerator.LevelState.RESET)
        {
            level.setLevelState(LevelGenerator.LevelState.STANDBY);
        }
    }

    public void freeAllEntity()
    {
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            freeEntity(entityList[i]);
        }
    }

    public void updateEntityVelocity(Entity entity, LevelGenerator.LevelState levelState)
    {
        int velocity = 0;
        if(levelState == LevelGenerator.LevelState.PLAY)
        {
            switch(entity.type)
            {
                case CAT_TRACER:
                    velocity = 4;
                    break;
                case MOUSE_NEUTRAL:
                    velocity = 4;
                    break;
                default:
                    velocity = 0;
                    break;
            }
        }
        entity.velocity = velocity;
    }

    public void saveInitialState()
    {
        SaveEntity saveEntity;
        Json json = new Json();
        ArrayList<SaveEntity> saveList = new ArrayList<SaveEntity>();
        Preferences prefs = Gdx.app.getPreferences("SaveInitialLevelState");

        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            saveEntity = new SaveEntity(entityList[i].position, entityList[i].type, entityList[i].state);
            saveList.add(saveEntity);
        }

        String jsonString = json.prettyPrint(saveList);
        prefs.putString("SaveFile", jsonString);
        prefs.flush();
        //SaveEntity saveEntity = new SaveEntity()

        Gdx.app.log("Yokaka", prefs.getString("SaveFile"));
        Gdx.app.log("Yokaka", "TEST");

        Json jsontest = new Json();
        ArrayList<SaveEntity> save = json.fromJson(ArrayList.class, prefs.getString("SaveFile"));

        for(SaveEntity s : save)
        {
            Gdx.app.log("Yokaka", s.position.x + ", " + s.position.y);
        }
        /*
        //adding to the preferences:
        hsMap.put("freeMode", json.toJson( hsFreeMode ) );
        //and then:
        Gdx.app.log("data print", "the free mode value is " + ( (String[])json.fromJson(ArrayList.class, String[].class, (String)data.get("freeMode") ).get(0) )[0] );
        */
    }

    public void resetEntity(Entity entity)
    {
        Vector2 originalPosition = entity.getOriginalPosition();
        entity.position.x = originalPosition.x;
        entity.position.y = originalPosition.y;

        Entity.EntityState originalState = entity.getOriginalState();
        entity.state = originalState;
    }

    public Entity checkFrontOfEntity(Entity entity)
    {
        Entity temp_entity;

        temp_entity = collisionCheckEntity(entity);
        if(temp_entity != null)
        {
            return temp_entity;
        }
        else
        {
            return null;
        }
    }

    public void tapTile(Vector2 tapped_coordinate)
    {
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            if( entityList[i].position.x == tapped_coordinate.x &&
                entityList[i].position.y == tapped_coordinate.y &&
                entityList[i].type == Entity.EntityType.TILE_ARROW)
            {
                entityList[i].setState("FREE");
            }
        }
    }

    public Entity checkAllTile(Vector2 coordinate)
    {
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            if( entityList[i].position.x == coordinate.x &&
                entityList[i].position.y == coordinate.y)
            {
                return entityList[i];
            }
        }
        return null;
    }

    public boolean checkArrowTile(Vector2 coordinate)
    {
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            if( entityList[i].position.x == coordinate.x &&
                entityList[i].position.y == coordinate.y &&
                entityList[i].type == Entity.EntityType.TILE_ARROW)
            {
                return true;
            }
        }
        return false;
    }

    public boolean checkHomeTile(Vector2 coordinate)
    {
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            if( entityList[i].position.x == coordinate.x &&
                    entityList[i].position.y == coordinate.y &&
                    entityList[i].type == Entity.EntityType.TILE_HOME)
            {
                return true;
            }
        }
        return false;
    }

    public Entity.EntityState getOppositeState(Entity.EntityState state)
    {
        switch(state)
        {
            case UP:
                return Entity.EntityState.DOWN;
            case RIGHT:
                return Entity.EntityState.LEFT;
            case DOWN:
                return Entity.EntityState.UP;
            case LEFT:
                return Entity.EntityState.RIGHT;
        }
        return null;
    }

    /*public void updateEntityOnArrow(Entity entity)
    {
        Entity arrowEntity;
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            if( entityList[i].type == Entity.entityType.TILE_ARROW ||
                entityList[i].type == Entity.entityType.TILE_BLOCK ||
                entityList[i].type == Entity.entityType.TILE_HOME)
            {
                continue;
            }

            arrowEntity = checkArrowTile(entityList[i].position);
            if(arrowEntity != null)
            {
                entity.state = arrowEntity.state;
            }
        }
    }*/

    public void updateCollidedEntity(Entity entity, Entity collided_entity, Entity.EntityState state)
    {
        if(collided_entity != null)
        {
            switch(entity.type)
            {
                case MOUSE_NEUTRAL:
                    if(collided_entity.type == Entity.EntityType.CAT_TRACER)
                    {
                        entity.state = Entity.EntityState.FREE;
                    }
                    if(collided_entity.type == Entity.EntityType.TILE_BLOCK)
                    {
                        updateCollidedEntityState(entity, collided_entity, state);
                    }
                    break;
                case CAT_TRACER:
                    if(collided_entity.type == Entity.EntityType.TILE_ARROW && checkArrowTile(entity.position))
                    {
                        stepOnArrow(collided_entity, entity);
                    }
                    if(collided_entity.type == Entity.EntityType.MOUSE_NEUTRAL)
                    {
                        collided_entity.state = Entity.EntityState.FREE;
                    }
                    if(collided_entity.type == Entity.EntityType.TILE_BLOCK)
                    {
                        updateCollidedEntityState(entity, collided_entity, state);
                    }
                    break;
                case TILE_ARROW:
                    if(checkArrowTile(collided_entity.position))
                    {
                        stepOnArrow(entity, collided_entity);
                    }
                    break;
                case TILE_HOME:
                    if(checkHomeTile(collided_entity.position))
                    {
                        collided_entity.state = Entity.EntityState.FREE;
                    }
                    break;
            }
        }
    }

    public void stepOnArrow(Entity arrow, Entity animal)
    {
        Entity.EntityState temp = null;
        Entity tempEntity = null;
        switch(animal.state)
        {
            case UP:
                temp = Entity.EntityState.UP;
                break;
            case RIGHT:
                temp = Entity.EntityState.RIGHT;
                break;
            case DOWN:
                temp = Entity.EntityState.DOWN;
                break;
            case LEFT:
                temp = Entity.EntityState.LEFT;
                break;
        }

        /*switch(arrow.state)
        {
            case UP:
                Gdx.app.log("Yokaka", "UPUP");
                break;
            case RIGHT:
                Gdx.app.log("Yokaka", "RIRIR");
                break;
            case DOWN:
                Gdx.app.log("Yokaka", "DWDW");
                break;
            case LEFT:
                Gdx.app.log("Yokaka", "LELE");
                break;
        }*/

        animal.state = arrow.state;
        step(animal);
        tempEntity = checkFrontOfEntity(animal);
        if(tempEntity != null && temp != null && (tempEntity.type == Entity.EntityType.TILE_BLOCK || boundaryCheckEntity(animal)))
        {
            backstep(animal);
            animal.state = temp;
        }
    }

    public void step(Entity entity)
    {
        switch(entity.state)
        {
            case UP:
                entity.position.y += entity.getVelocity();
                break;
            case RIGHT:
                entity.position.x += entity.getVelocity();
                break;
            case DOWN:
                entity.position.y -= entity.getVelocity();
                break;
            case LEFT:
                entity.position.x -= entity.getVelocity();
                break;
        }
    }

    public void backstep(Entity entity)
    {
        switch(entity.state)
        {
            case UP:
                entity.position.y -= entity.getVelocity();
                break;
            case RIGHT:
                entity.position.x -= entity.getVelocity();
                break;
            case DOWN:
                entity.position.y += entity.getVelocity();
                break;
            case LEFT:
                entity.position.x += entity.getVelocity();
                break;
        }
    }

    public void updateCollidedEntityState(Entity entity, Entity collided_entity, Entity.EntityState state)
    {
        Entity temp_entity;

        switch(state)
        {
            case UP:
                entity.position.y -= entity.getVelocity();

                entity.state = Entity.EntityState.RIGHT;
                entity.position.x += entity.getVelocity();
                temp_entity = checkFrontOfEntity(entity);
                if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK))
                {
                    entity.position.x -= entity.getVelocity();

                    entity.state = Entity.EntityState.LEFT;
                    entity.position.x -= entity.getVelocity();
                    temp_entity = checkFrontOfEntity(entity);
                    if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK))
                    {
                        entity.position.x += entity.getVelocity();
                        entity.state = Entity.EntityState.DOWN;
                    }
                    entity.position.x += entity.getVelocity();
                }
                entity.position.x -= entity.getVelocity();
                break;
            case RIGHT:
                entity.position.x -= entity.getVelocity();

                entity.state = Entity.EntityState.DOWN;
                entity.position.y -= entity.getVelocity();
                temp_entity = checkFrontOfEntity(entity);
                if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK))
                {
                    entity.position.y += entity.getVelocity();

                    entity.state = Entity.EntityState.UP;
                    entity.position.y += entity.getVelocity();
                    temp_entity = checkFrontOfEntity(entity);
                    if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK))
                    {
                        entity.position.y -= entity.getVelocity();
                        entity.state = Entity.EntityState.LEFT;
                    }
                    entity.position.y -= entity.getVelocity();
                }
                entity.position.y += entity.getVelocity();
                break;
            case DOWN:
                entity.position.y += entity.getVelocity();

                entity.state = Entity.EntityState.LEFT;
                entity.position.x -= entity.getVelocity();
                temp_entity = checkFrontOfEntity(entity);
                if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK))
                {
                    entity.position.x += entity.getVelocity();

                    entity.state = Entity.EntityState.RIGHT;
                    entity.position.x += entity.getVelocity();
                    temp_entity = checkFrontOfEntity(entity);
                    if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK))
                    {
                        entity.position.x -= entity.getVelocity();
                        entity.state = Entity.EntityState.UP;
                    }
                    entity.position.x -= entity.getVelocity();
                }
                entity.position.x += entity.getVelocity();
                break;
            case LEFT:
                entity.position.x += entity.getVelocity();

                entity.state = Entity.EntityState.UP;
                entity.position.y += entity.getVelocity();
                temp_entity = checkFrontOfEntity(entity);
                if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK))
                {
                    entity.position.y -= entity.getVelocity();

                    entity.state = Entity.EntityState.DOWN;
                    entity.position.y -= entity.getVelocity();
                    temp_entity = checkFrontOfEntity(entity);
                    if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.EntityType.TILE_BLOCK))
                    {
                        entity.position.y += entity.getVelocity();
                        entity.state = Entity.EntityState.RIGHT;
                    }
                    entity.position.y += entity.getVelocity();
                }
                entity.position.y -= entity.getVelocity();
                break;
            default:
                break;
        }
    }

    public void updateEntity(Entity entity)
    {
        if(entity.sprite == null)
        {
            return;
        }

        Entity collided_entity;

        switch(entity.state)
        {
            case UP:
                entity.position.y += entity.getVelocity();

                collided_entity = checkFrontOfEntity(entity);
                if(collided_entity != null)
                {
                    updateCollidedEntity(entity, collided_entity, Entity.EntityState.UP);
                }

                if(boundaryCheckEntity(entity))
                {
                    updateCollidedEntityState(entity, collided_entity, Entity.EntityState.UP);
                }

                break;
            case RIGHT:
                entity.position.x += entity.getVelocity();

                collided_entity = checkFrontOfEntity(entity);
                if(collided_entity != null)
                {
                    updateCollidedEntity(entity, collided_entity, Entity.EntityState.RIGHT);
                }

                if(boundaryCheckEntity(entity))
                {
                    updateCollidedEntityState(entity, collided_entity, Entity.EntityState.RIGHT);
                }
                break;
            case DOWN:
                entity.position.y -= entity.getVelocity();

                collided_entity = checkFrontOfEntity(entity);
                if(collided_entity != null)
                {
                    updateCollidedEntity(entity, collided_entity, Entity.EntityState.DOWN);
                }

                if(boundaryCheckEntity(entity))
                {
                    updateCollidedEntityState(entity, collided_entity, Entity.EntityState.DOWN);
                }
                break;
            case LEFT:
                entity.position.x -= entity.getVelocity();

                collided_entity = checkFrontOfEntity(entity);
                if(collided_entity != null)
                {
                    updateCollidedEntity(entity, collided_entity, Entity.EntityState.LEFT);
                }

                if(boundaryCheckEntity(entity))
                {
                    updateCollidedEntityState(entity, collided_entity, Entity.EntityState.LEFT);
                }
                break;
            case FREE:
                freeEntity(entity);
                break;
        }
    }

    public boolean boundaryCheckEntity(Entity entity)
    {
        //Gdx.app.log("Yokaka", entity.position.x * entity.sprite.getScale()+entity.frameSize.x  * entity.sprite.getScale()+ " > " + Gdx.graphics.getWidth());
        float pointX = entity.position.x * entity.getScale();
        float pointY = entity.position.y * entity.getScale();
        float frameX = entity.frameSize.x * entity.getScale();
        float frameY = entity.frameSize.y * entity.getScale();

        //64: Tile frame size, 9: Tiles per line
        float mapWidth = 64 * 9 * entity.getScale();

        if(pointY + frameY > Gdx.graphics.getHeight())
        {
            return true;
        }
        else if(pointX + frameX > mapWidth)
        {
            return true;
        }
        else if(entity.position.y < 0)
        {
            return true;
        }
        else if(entity.position.x < 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Entity collisionCheckEntity(Entity entity)
    {
        Entity temp;

        for(int i = 0; i < entityList.length; i++)
        {
            temp = entityList[i];
            if(!temp.inuse)
            {
                continue;
            }

            //ignore itself
            if(temp == entity)
            {
                continue;
            }

            //if same type, not gonna do anything anyways so skip
            if(temp.type == entity.type)
            {
                continue;
            }

            //check collision
            if( (entity.position.x + entity.frameSize.x > temp.position.x) &&
                (temp.position.x + temp.frameSize.x > entity.position.x) &&
                (entity.position.y + entity.frameSize.y > temp.position.y) &&
                (temp.position.y + temp.frameSize.y > entity.position.y) )
            {
                return temp;
            }
        }
        return null;
    }
}
