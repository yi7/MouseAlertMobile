package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class SaveEntity
{
    Vector2 position;
    Entity.EntityType type;
    Entity.EntityState state;

    public SaveEntity()
    {

    }

    public SaveEntity(Vector2 position, Entity.EntityType type, Entity.EntityState state)
    {
        this.position = position;
        this.type = type;
        this.state = state;
    }
}
