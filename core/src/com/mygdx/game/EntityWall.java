package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class EntityWall extends Entity
{
    /**
     * Constructor that creates a Wall Entity
     * @param position position of the Entity
     * @param subtype subtype of the Entity
     * @param state state of the Entity
     * @param sprite_system Sprite System
     */
    public EntityWall(Vector2 position, Entity.EntitySubtype subtype, Entity.EntityState state, SpriteSystem sprite_system)
    {
        super(position, subtype, state, sprite_system);
        this.type = EntityType.WALL;
        if(subtype == EntitySubtype.VWALL_NEUTRAL)
        {
            this.hitbox_frame = new Vector2(48, 64);
        }
        else
        {
            this.hitbox_frame = new Vector2(64, 48);
        }
    }
}
