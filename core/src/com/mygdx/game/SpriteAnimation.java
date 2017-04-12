package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteAnimation extends Animation
{
    float scale;

    public SpriteAnimation(float frameDuration, TextureRegion[] keyFrames, float scale)
    {
        super(frameDuration, keyFrames);
        this.scale = scale;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public void draw(float deltaTime, Batch batch, float x, float y)
    {
        TextureRegion region = getKeyFrame(deltaTime);
        batch.draw(region, x, y, region.getRegionWidth() * scale, region.getRegionHeight() * scale);
    }
}
