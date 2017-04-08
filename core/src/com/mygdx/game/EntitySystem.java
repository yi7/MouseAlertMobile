package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

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
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            entityList[i].drawEntity(deltaTime, batch, entityList[i].position.x, entityList[i].position.y);
        }
    }

    public void updateAllEntity()
    {
        Entity entity;
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }
            //entityList[i].updateEntity(entityList[i]);

            //entity = this.collisionCheckEntity(entityList[i]);
            updateEntity(entityList[i]);
            /*if(entity != null)
            {
                Gdx.app.log("Yokaka", "Test");

                switch(entity.type)
                {
                    case CAT_TRACER:
                        //entity.updateCatEntity(entityList[i]);
                        break;
                    case WALL:
                        break;
                    default:
                        break;
                }
            }*/
        }
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
                default:
                    break;
            }
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

        Entity.entityState state_original = entity.state;
        Entity.entityState state_opposite = getOppositeState(entity.state);
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

                if(entity != null && boundaryCheckEntity(entity))
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

                if(entity != null && boundaryCheckEntity(entity))
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

                if(entity != null && boundaryCheckEntity(entity))
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

                if(entity != null && boundaryCheckEntity(entity))
                {
                    updateCollidedEntityState(entity, collided_entity, Entity.entityState.LEFT);
                }
                break;
            case FREE:
                freeEntity(entity);
                break;
            default:
                break;
        }
    }

    public boolean boundaryCheckEntity(Entity entity)
    {
        //Gdx.app.log("Yokaka", entity.position.y * entity.sprite.getScale()+entity.frameSize.y  * entity.sprite.getScale()+ " > " + Gdx.graphics.getHeight());
        if(entity.position.y * entity.getScale() + entity.frameSize.y * entity.getScale() > Gdx.graphics.getHeight())
        {
            return true;
        }
        else if(entity.position.x * entity.getScale() + entity.frameSize.x * entity.getScale() > 576f * 2.5)
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
