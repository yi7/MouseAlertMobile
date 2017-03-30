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

    public boolean checkFrontOfEntity(Entity entity)
    {
        if(collisionCheckEntity(entity) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void updateEntity(Entity entity)
    {
        if(entity.sprite == null)
        {
            return;
        }

        Entity.entityState original = entity.state;
        for(int i = 0; i < 4; i++)
        {
            switch(entity.state)
            {
                case UP:
                    entity.position.y += entity.getVelocity();
                    if(boundaryCheckEntity(entity) || checkFrontOfEntity(entity))
                    {
                        entity.state = Entity.entityState.RIGHT;
                        entity.position.y -= entity.getVelocity();
                    }
                    break;
                case RIGHT:
                    entity.position.x += entity.getVelocity();
                    if(boundaryCheckEntity(entity) || checkFrontOfEntity(entity))
                    {
                        entity.state = Entity.entityState.DOWN;
                        entity.position.x -= entity.getVelocity();
                    }
                    break;
                case DOWN:
                    entity.position.y -= entity.getVelocity();
                    if(boundaryCheckEntity(entity) || checkFrontOfEntity(entity))
                    {
                        entity.state = Entity.entityState.LEFT;
                        entity.position.y += entity.getVelocity();
                    }
                    break;
                case LEFT:
                    entity.position.x -= entity.getVelocity();
                    if(boundaryCheckEntity(entity) || checkFrontOfEntity(entity))
                    {
                        entity.state = Entity.entityState.UP;
                        entity.position.x += entity.getVelocity();
                    }
                    break;
                case FREE:
                    break;
                default:
                    break;
            }

            if(checkFrontOfEntity(entity) && entity.state != original)
            {
                continue;
            }
            else
            {
                return;
            }
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
