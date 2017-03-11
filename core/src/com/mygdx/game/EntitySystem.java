package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class EntitySystem
{
    ArrayList<Entity> entityList;

    public EntitySystem()
    {
        entityList = new ArrayList<Entity>();
    }

    public void newEntity(Entity entity)
    {
        entityList.add(entity);
    }

    public void drawAllEntity(float deltaTime, Batch batch)
    {
        for(Entity entity : entityList)
        {
            //dx.app.log("Yokaka", entity.position.x + ", " + entity.position.y);
            entity.drawEntity(deltaTime, batch, entity.position.x, entity.position.y);
        }
        //Gdx.app.log("Yokaka", "------");
    }

    public void updateAllEntity()
    {
        for(Entity entity : entityList)
        {
            entity.updateEntity(entity);
        }
    }
}
