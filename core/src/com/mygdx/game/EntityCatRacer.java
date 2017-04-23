package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class EntityCatRacer extends Entity
{
    /**
     * Constructor that creates a Racer Cat Entity
     * @param position position of the Entity
     * @param subtype subtype of the Entity
     * @param state state of the Entity
     * @param sprite_system Sprite System
     */
    public EntityCatRacer(Vector2 position, Entity.EntitySubtype subtype, Entity.EntityState state, SpriteSystem sprite_system)
    {
        super(position, subtype, state, sprite_system);
        this.velocity = 4;
        this.type = EntityType.CAT;
        this.hitbox_frame = new Vector2(16, 16);
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

        switch(collided_entity.subtype)
        {
            case MOUSE_NEUTRAL:
                collided_entity.free();
                entity_system.setLevelState(LevelGenerator.LevelState.GAMEOVER);
                entity_system.setGameOverEntity(this);
                break;
            case TILE_ARROW:
                temp_entity = entity_system.getEntityOnTile(this.position, EntitySubtype.TILE_ARROW);
                if(temp_entity != null)
                {
                    this.state = temp_entity.state;
                }
                break;
            case TILE_HOME:
                temp_entity = entity_system.getEntityOnTile(this.position, EntitySubtype.TILE_HOME);
                if(temp_entity != null)
                {
                    entity_system.setLevelState(LevelGenerator.LevelState.GAMEOVER);
                    entity_system.setGameOverEntity(this);
                }
                break;
            default:
                break;
        }
    }
}
