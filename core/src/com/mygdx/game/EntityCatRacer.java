package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class EntityCatRacer extends Entity
{
    /**
     * Constructor that creates a Racer Cat Entity
     * @param position position of the Entity
     * @param type type of the Entity
     * @param state state of the Entity
     * @param sprite_system Sprite System
     */
    public EntityCatRacer(Vector2 position, Entity.EntityType type, Entity.EntityState state, SpriteSystem sprite_system)
    {
        super(position, type, state, sprite_system);
        this.velocity = 4;
    }

    /**
     * Acts based on what Racer Cat collided with
     * @param collided_entity Entity it collided with
     * @param entity_system Entity System
     */
    @Override
    public void think(Entity collided_entity, EntitySystem entity_system)
    {
        Entity temp_entity;

        switch(collided_entity.type)
        {
            case TILE_ARROW:
                temp_entity = entity_system.getArrowOnTile(this.position);
                if(temp_entity != null)
                {
                    this.state = temp_entity.state;
                }
                break;
            default:
                break;
        }
    }
}
