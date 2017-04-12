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
        FREE
    }

    public enum entityType
    {
        MOUSE_NEUTRAL,
        MOUSE_TRACER,
        MOUSE_HOVER,
        CAT_TRACER,
        WALL,
        TILE_BLOCK,
        TILE_HOME,
        TILE_ARROW
    }

    SpriteAnimation spriteAnimation;
    SpriteAnimation spriteAnimationUp;
    SpriteAnimation spriteAnimationRight;
    SpriteAnimation spriteAnimationDown;
    SpriteAnimation spriteAnimationLeft;

    Sprite sprite;
    Entity.entityState state;
    Entity.entityType type;
    Vector2 position;
    Vector2 frameSize;
    int velocity;
    boolean inuse;
    float scale;

    public Entity(Entity.entityType type, Sprite sprite, float scale)
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
        this.scale = scale;

        switch(type)
        {
            case CAT_TRACER:
                velocity = 4;
                break;
            case MOUSE_NEUTRAL:
                velocity = 4;
                break;
            default:
                velocity = 0;
                break;
        }
    }

    public void generateSpriteTextureRegion()
    {
        if(sprite != null)
        {
            spriteAnimationUp = new SpriteAnimation(1f/4f, sprite.generateSpriteTextureRegionUp(), sprite.getScale());
            spriteAnimationRight = new SpriteAnimation(1f/4f, sprite.generateSpriteTextureRegionRight(), sprite.getScale());
            spriteAnimationDown = new SpriteAnimation(1f/4f, sprite.generateSpriteTextureRegionDown(), sprite.getScale());
            spriteAnimationLeft = new SpriteAnimation(1f/4f, sprite.generateSpriteTextureRegionLeft(), sprite.getScale());

            //spriteAnimation = new SpriteAnimation(1f/4f, sprite.generateSpriteTextureRegion());
            //spriteAnimation.setScale(sprite.getScale());
            spriteAnimationUp.setPlayMode(Animation.PlayMode.LOOP);
            spriteAnimationRight.setPlayMode(Animation.PlayMode.LOOP);
            spriteAnimationDown.setPlayMode(Animation.PlayMode.LOOP);
            spriteAnimationLeft.setPlayMode(Animation.PlayMode.LOOP);
        }


    }

    public float getScale()
    {
        return this.scale;
    }

    public int getVelocity()
    {
        return this.velocity;
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
        if(sprite != null)
        {
            switch(state)
            {
                case UP:
                    spriteAnimationUp.draw(deltaTime, batch, x * sprite.getScale(), y * sprite.getScale());
                    break;
                case RIGHT:
                    spriteAnimationRight.draw(deltaTime, batch, x * sprite.getScale(), y * sprite.getScale());
                    break;
                case DOWN:
                    spriteAnimationDown.draw(deltaTime, batch, x * sprite.getScale(), y * sprite.getScale());
                    break;
                case LEFT:
                    spriteAnimationLeft.draw(deltaTime, batch, x * sprite.getScale(), y * sprite.getScale());
                    break;
            }
        }
    }
}
