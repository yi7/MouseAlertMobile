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

        if(sprite != null)
        {
            spriteAnimation = new SpriteAnimation(1f/4f, sprite.generateSpriteTextureRegion());
            spriteAnimation.setScale(sprite.getScale());
            spriteAnimation.setPlayMode(Animation.PlayMode.LOOP);
        }

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
            spriteAnimation.draw(deltaTime, batch, x * sprite.getScale(), y * sprite.getScale());
        }
    }

    public void updateEntity(Entity entity)
    {
        if(entity.sprite == null)
        {
            return;
        }
        /*Gdx.app.log("Yokaka", entity.position.y * 2.5 + "");
        Gdx.app.log("Yokaka", entity.frameSize.y + "");
        Gdx.app.log("Yokaka", Gdx.graphics.getHeight() + "");
        Gdx.app.log("Yoka", "---------------");*/
        Entity.entityState original = entity.state;
        switch(entity.state)
        {
            case UP:

                entity.position.y += velocity;
                if(boundaryCheckEntity(entity))
                {
                    entity.state = entityState.RIGHT;
                    entity.position.y -= velocity;
                }
                break;
            case RIGHT:
                entity.position.x += velocity;
                if(boundaryCheckEntity(entity))
                {
                    entity.state = entityState.DOWN;
                    entity.position.x -= velocity;
                }
                break;
            case DOWN:
                entity.position.y -= velocity;
                if(boundaryCheckEntity(entity))
                {
                    entity.state = entityState.LEFT;
                    entity.position.y += velocity;
                }
                break;
            case LEFT:
                entity.position.x -= velocity;
                if(boundaryCheckEntity(entity))
                {
                    entity.state = entityState.UP;
                    entity.position.x += velocity;
                }
                break;
            case FREE:
                break;
            default:
                break;
        }
    }

    public void updateCatEntity(Entity entity)
    {
        if(entity.sprite == null)
        {
            return;
        }

        switch(entity.type)
        {
            case CAT_TRACER:
                return;
            case TILE_BLOCK:

                break;
            default:
                break;
        }
    }

    public boolean boundaryCheckEntity(Entity entity)
    {
        //Gdx.app.log("Yokaka", entity.position.y * entity.sprite.getScale()+entity.frameSize.y  * entity.sprite.getScale()+ " > " + Gdx.graphics.getHeight());
        if(entity.position.y * scale + entity.frameSize.y * scale > Gdx.graphics.getHeight())
        {
            return true;
        }
        else if(entity.position.x * scale + entity.frameSize.x * scale > 576f * 2.5)
        {
            return true;
        }
        else if(entity.position.y < 0)
        {
            return true;
        }
        else if(entity.position.x < 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
