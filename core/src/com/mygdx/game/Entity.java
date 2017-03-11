package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Entity
{
    public enum entityState
    {
        UP,
        RIGHT,
        DOWN,
        LEFT,
        FREE;
    }

    public enum entityType
    {
        MOUSE_NEUTRAL,
        MOUSE_TRACER,
        MOUSE_HOVER,
        CAT_TRACER;
    }

    SpriteAnimation spriteAnimation;
    Sprite sprite;
    Entity.entityState state;
    Entity.entityType type;
    Vector2 position;
    int velocity;

    public Entity(Entity.entityType type, Sprite sprite)
    {
        this.type = type;
        this.sprite = sprite;
        spriteAnimation = new SpriteAnimation(1f/4f, sprite.generateSpriteTextureRegion());
        spriteAnimation.setScale(sprite.getScale());
        spriteAnimation.setPlayMode(Animation.PlayMode.LOOP);

        switch(type)
        {
            case CAT_TRACER:
                velocity = 8;
                break;
            default:
                velocity = 4;
                break;
        }
    }

    public void setPosition(float x, float y)
    {
        Vector2 position = new Vector2();
        position.set(x, y);
        this.position = position;
    }

    public void setState(String passedState)
    {
        if(passedState == "UP")
        {
            this.state = entityState.UP;
        }
        else if(passedState == "RIGHT")
        {
            this.state = entityState.RIGHT;
        }
        else if(passedState == "DOWN")
        {
            this.state = entityState.DOWN;
        }
        else if(passedState == "LEFT")
        {
            this.state = entityState.LEFT;
        }
    }

    public void drawEntity(float deltaTime, Batch batch, float x, float y)
    {
        spriteAnimation.draw(deltaTime, batch, x * sprite.getScale(), y * sprite.getScale());
    }

    public void updateEntity()
    {
        
    }
}
