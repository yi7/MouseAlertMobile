package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

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

            if(entityList[i].type != Entity.entityType.TILE_ARROW)
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

            if(entityList[i].type == Entity.entityType.TILE_ARROW)
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
                    if(entityList[i].type == Entity.entityType.TILE_ARROW)
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

    public void resetEntity(Entity entity)
    {
        Vector2 originalPosition = entity.getOriginalPosition();
        entity.position.x = originalPosition.x;
        entity.position.y = originalPosition.y;

        Entity.entityState originalState = entity.getOriginalState();
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
                entityList[i].type == Entity.entityType.TILE_ARROW)
            {
                entityList[i].setState("FREE");
            }
        }
    }

    public boolean checkAllTile(Vector2 coordinate)
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
                return true;
            }
        }
        return false;
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
                entityList[i].type == Entity.entityType.TILE_ARROW)
            {
                return true;
            }
        }
        return false;
    }

    public Entity.entityState getOppositeState(Entity.entityState state)
    {
        switch(state)
        {
            case UP:
                return Entity.entityState.DOWN;
            case RIGHT:
                return Entity.entityState.LEFT;
            case DOWN:
                return Entity.entityState.UP;
            case LEFT:
                return Entity.entityState.RIGHT;
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

    public void updateCollidedEntity(Entity entity, Entity collided_entity, Entity.entityState state)
    {
        if(collided_entity != null)
        {
            switch(entity.type)
            {
                case MOUSE_NEUTRAL:
                    if(collided_entity.type == Entity.entityType.CAT_TRACER)
                    {
                        entity.state = Entity.entityState.FREE;
                    }
                    if(collided_entity.type == Entity.entityType.TILE_BLOCK)
                    {
                        updateCollidedEntityState(entity, collided_entity, state);
                    }
                    break;
                case CAT_TRACER:
                    if(collided_entity.type == Entity.entityType.MOUSE_NEUTRAL)
                    {
                        collided_entity.state = Entity.entityState.FREE;
                    }
                    if(collided_entity.type == Entity.entityType.TILE_BLOCK)
                    {
                        updateCollidedEntityState(entity, collided_entity, state);
                    }
                    break;
                case TILE_ARROW:
                    if(checkArrowTile(collided_entity.position))
                    {
                        Entity.entityState temp = null;
                        Entity tempEntity = null;
                        switch(collided_entity.state)
                        {
                            case UP:
                                temp = Entity.entityState.UP;
                                break;
                            case RIGHT:
                                temp = Entity.entityState.RIGHT;
                                break;
                            case DOWN:
                                temp = Entity.entityState.DOWN;
                                break;
                            case LEFT:
                                temp = Entity.entityState.LEFT;
                                break;
                        }

                        collided_entity.state = entity.state;
                        step(collided_entity);
                        tempEntity = checkFrontOfEntity(collided_entity);
                        if(tempEntity != null && temp != null && (tempEntity.type == Entity.entityType.TILE_BLOCK || boundaryCheckEntity(collided_entity)))
                        {
                            backstep(collided_entity);
                            collided_entity.state = temp;
                        }
                    }
                    break;
            }
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

    public void updateCollidedEntityState(Entity entity, Entity collided_entity, Entity.entityState state)
    {
        Entity temp_entity;

        switch(state)
        {
            case UP:
                entity.position.y -= entity.getVelocity();

                entity.state = Entity.entityState.RIGHT;
                entity.position.x += entity.getVelocity();
                temp_entity = checkFrontOfEntity(entity);
                if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.entityType.TILE_BLOCK))
                {
                    entity.position.x -= entity.getVelocity();

                    entity.state = Entity.entityState.LEFT;
                    entity.position.x -= entity.getVelocity();
                    temp_entity = checkFrontOfEntity(entity);
                    if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.entityType.TILE_BLOCK))
                    {
                        entity.position.x += entity.getVelocity();
                        entity.state = Entity.entityState.DOWN;
                    }
                    entity.position.x += entity.getVelocity();
                }
                entity.position.x -= entity.getVelocity();
                break;
            case RIGHT:
                entity.position.x -= entity.getVelocity();

                entity.state = Entity.entityState.DOWN;
                entity.position.y -= entity.getVelocity();
                temp_entity = checkFrontOfEntity(entity);
                if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.entityType.TILE_BLOCK))
                {
                    entity.position.y += entity.getVelocity();

                    entity.state = Entity.entityState.UP;
                    entity.position.y += entity.getVelocity();
                    temp_entity = checkFrontOfEntity(entity);
                    if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.entityType.TILE_BLOCK))
                    {
                        entity.position.y -= entity.getVelocity();
                        entity.state = Entity.entityState.LEFT;
                    }
                    entity.position.y -= entity.getVelocity();
                }
                entity.position.y += entity.getVelocity();
                break;
            case DOWN:
                entity.position.y += entity.getVelocity();

                entity.state = Entity.entityState.LEFT;
                entity.position.x -= entity.getVelocity();
                temp_entity = checkFrontOfEntity(entity);
                if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.entityType.TILE_BLOCK))
                {
                    entity.position.x += entity.getVelocity();

                    entity.state = Entity.entityState.RIGHT;
                    entity.position.x += entity.getVelocity();
                    temp_entity = checkFrontOfEntity(entity);
                    if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.entityType.TILE_BLOCK))
                    {
                        entity.position.x -= entity.getVelocity();
                        entity.state = Entity.entityState.UP;
                    }
                    entity.position.x -= entity.getVelocity();
                }
                entity.position.x += entity.getVelocity();
                break;
            case LEFT:
                entity.position.x += entity.getVelocity();

                entity.state = Entity.entityState.UP;
                entity.position.y += entity.getVelocity();
                temp_entity = checkFrontOfEntity(entity);
                if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.entityType.TILE_BLOCK))
                {
                    entity.position.y -= entity.getVelocity();

                    entity.state = Entity.entityState.DOWN;
                    entity.position.y -= entity.getVelocity();
                    temp_entity = checkFrontOfEntity(entity);
                    if(boundaryCheckEntity(entity) || (temp_entity != null && temp_entity.type == Entity.entityType.TILE_BLOCK))
                    {
                        entity.position.y += entity.getVelocity();
                        entity.state = Entity.entityState.RIGHT;
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
                    updateCollidedEntity(entity, collided_entity, Entity.entityState.UP);
                }

                if(boundaryCheckEntity(entity))
                {
                    updateCollidedEntityState(entity, collided_entity, Entity.entityState.UP);
                }

                break;
            case RIGHT:
                entity.position.x += entity.getVelocity();

                collided_entity = checkFrontOfEntity(entity);
                if(collided_entity != null)
                {
                    updateCollidedEntity(entity, collided_entity, Entity.entityState.RIGHT);
                }

                if(boundaryCheckEntity(entity))
                {
                    updateCollidedEntityState(entity, collided_entity, Entity.entityState.RIGHT);
                }
                break;
            case DOWN:
                entity.position.y -= entity.getVelocity();

                collided_entity = checkFrontOfEntity(entity);
                if(collided_entity != null)
                {
                    updateCollidedEntity(entity, collided_entity, Entity.entityState.DOWN);
                }

                if(boundaryCheckEntity(entity))
                {
                    updateCollidedEntityState(entity, collided_entity, Entity.entityState.DOWN);
                }
                break;
            case LEFT:
                entity.position.x -= entity.getVelocity();

                collided_entity = checkFrontOfEntity(entity);
                if(collided_entity != null)
                {
                    updateCollidedEntity(entity, collided_entity, Entity.entityState.LEFT);
                }

                if(boundaryCheckEntity(entity))
                {
                    updateCollidedEntityState(entity, collided_entity, Entity.entityState.LEFT);
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
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            //ignore itself
            if(entityList[i] == entity)
            {
                continue;
            }

            //check collision
            if( (entity.position.x + entity.frameSize.x > entityList[i].position.x) &&
                (entityList[i].position.x + entityList[i].frameSize.x > entity.position.x) &&
                (entity.position.y + entity.frameSize.y > entityList[i].position.y) &&
                (entityList[i].position.y + entityList[i].frameSize.y > entity.position.y) )
            {
                return entityList[i];
            }
        }
        return null;
    }
}
