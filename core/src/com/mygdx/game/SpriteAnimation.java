package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteAnimation extends Animation
{
    float scale;

    public SpriteAnimation(float frameDuration, TextureRegion[] keyFrames)
    {
        super(frameDuration, keyFrames);
    }

    public void setScaling(float scale)
    {
        this.scale = scale;
    }

    public void draw(float stateTime, Batch batch, float x, float y)
    {
        TextureRegion region = getKeyFrame(stateTime);
        batch.draw(region, x, y, region.getRegionWidth() * scale, region.getRegionHeight() * scale);
    }
}
