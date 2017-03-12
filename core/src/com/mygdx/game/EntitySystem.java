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
            entityList[i] = new Entity(null, null);
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
        for(int i = 0; i < entityList.length; i++)
        {
            if(!entityList[i].inuse)
            {
                continue;
            }

            entityList[i].updateEntity(entityList[i]);
        }
    }
}
