package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class EntityTileArrow extends Entity
{
    /**
     * Constructor that creates a Tile Arrow Entity
     * @param position position of the Entity
     * @param subtype type of the Entity
     * @param state state of the Entity
     * @param sprite_system Sprite System
     */
    public EntityTileArrow(Vector2 position, Entity.EntitySubtype subtype, Entity.EntityState state, SpriteSystem sprite_system)
    {
        super(position, subtype, state, sprite_system);
    }

    /**
     * Acts based on what Arrow Tile collided with
     * @param collided_entity Entity it collided with
     * @param entity_system Entity System
     */
    @Override
    public void think(Entity collided_entity, EntitySystem entity_system)
    {
        Entity temp_entity;

        switch(collided_entity.subtype)
        {
            case CAT_RACER:
                temp_entity = entity_system.getEntityOnTile(collided_entity.position, EntitySubtype.TILE_ARROW);
                if(temp_entity != null)
                {
                    collided_entity.state = this.state;
                    this.free();
                }
                break;
            case MOUSE_NEUTRAL:
                temp_entity = entity_system.getEntityOnTile(collided_entity.position, EntitySubtype.TILE_ARROW);
                if(temp_entity != null)
                {
                    collided_entity.state = this.state;
                }
                break;
            default:
                break;
        }
    }
}
