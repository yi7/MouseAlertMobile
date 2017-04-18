package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Entity
{
    public enum EntityState
    {
        UP,
        RIGHT,
        DOWN,
        LEFT,
        FREE
    }

    public enum EntityType
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
    EntityState state;
    EntityState originalState;
    Entity.EntityType type;
    Vector2 position;
    Vector2 originalPosition;
    Vector2 frameSize;
    int velocity;
    boolean inuse;
    float scale;

    boolean arrowUpdateFlag;

    public Entity(Entity.EntityType type, Sprite sprite, float scale)
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

        this.velocity = 0;

        arrowUpdateFlag = false;
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

    public EntityState getState()
    {
        return this.state;
    }

    public void setVelocity(Entity entity)
    {
        this.velocity = velocity;
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

        Vector2 originalPosition = new Vector2();
        originalPosition.set(x, y);
        this.originalPosition = originalPosition;
    }

    public Vector2 getOriginalPosition()
    {
        return this.originalPosition;
    }

    public EntityState getOriginalState()
    {
        return this.originalState;
    }

    public void setState(String passedState)
    {
        if(passedState.equals("UP"))
        {
            this.state = EntityState.UP;
        }
        else if(passedState.equals("RIGHT"))
        {
            this.state = EntityState.RIGHT;
        }
        else if(passedState.equals("DOWN"))
        {
            this.state = EntityState.DOWN;
        }
        else if(passedState.equals("LEFT"))
        {
            this.state = EntityState.LEFT;
        }
        else if(passedState.equals("FREE"))
        {
            this.state = EntityState.FREE;
        }
    }

    public void setState(EntityState passedState)
    {
        this.state = passedState;
    }

    public void setOriginalState(String passedState)
    {
        if(passedState.equals("UP"))
        {
            this.originalState = EntityState.UP;
        }
        else if(passedState.equals("RIGHT"))
        {
            this.originalState = EntityState.RIGHT;
        }
        else if(passedState.equals("DOWN"))
        {
            this.originalState = EntityState.DOWN;
        }
        else if(passedState.equals("LEFT"))
        {
            this.originalState = EntityState.LEFT;
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
