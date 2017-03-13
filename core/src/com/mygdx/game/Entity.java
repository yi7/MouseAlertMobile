package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        CAT_TRACER,
        WALL,
        TILE_BLOCK,
        TILE_HOME;
    }

    SpriteAnimation spriteAnimation;
    Sprite sprite;
    Entity.entityState state;
    Entity.entityType type;
    Vector2 position;
    Vector2 frameSize;
    int velocity;
    boolean inuse;

    public Entity(Entity.entityType type, Sprite sprite)
    {
        if(type == null)
        {
            this.inuse = false;
            return;
        }
        else
        {
            this.inuse = true;
        }

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

    public void setFrameSize(float x, float y)
    {
        Vector2 frameSize = new Vector2();
        frameSize.set(x, y);
        this.frameSize = frameSize;
    }

    public void setPosition(float x, float y)
    {
        Vector2 position = new Vector2();
        position.set(x, y);
        this.position = position;
    }

    public void setState(String passedState)
    {
        if(passedState.equals("UP"))
        {
            this.state = entityState.UP;
        }
        else if(passedState.equals("RIGHT"))
        {
            this.state = entityState.RIGHT;
        }
        else if(passedState.equals("DOWN"))
        {
            this.state = entityState.DOWN;
        }
        else if(passedState.equals("LEFT"))
        {
            this.state = entityState.LEFT;
        }
    }

    public void drawEntity(float deltaTime, Batch batch, float x, float y)
    {
        spriteAnimation.draw(deltaTime, batch, x * sprite.getScale(), y * sprite.getScale());
    }

    public void updateEntity(Entity entity)
    {
        switch(entity.state)
        {
            case UP:
                Gdx.app.log("Yokaka", entity.position.y + "");
                Gdx.app.log("Yokaka", entity.frameSize.y + "");
                Gdx.app.log("Yokaka", Gdx.graphics.getWidth() + "");
                Gdx.app.log("Yokaka", "---------------");
                entity.position.y += velocity;
                break;
            case RIGHT:
                entity.position.x += velocity;
                break;
            case DOWN:
                entity.position.y -= velocity;
                break;
            case LEFT:
                entity.position.x -= velocity;
                break;
            case FREE:
                break;
            default:
                break;
        }
    }
}
