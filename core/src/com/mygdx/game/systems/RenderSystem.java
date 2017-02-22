package com.mygdx.game.systems;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class RenderSystem
{
    float scale;

    public void tilemapSetScale(float scale)
    {
        this.scale = scale;
    }

    public void tilemapRenderObject(MapObjects objects, Batch batch, float stateTime)
    {
        for(MapObject object : objects)
        {
            if(object instanceof TextureMapObject)
            {
                TextureMapObject textureObject = (TextureMapObject) object;
                batch.draw(
                    textureObject.getTextureRegion(),
                    textureObject.getX() * scale,
                    textureObject.getY() * scale,
                    textureObject.getTextureRegion().getRegionWidth() * scale,
                    textureObject.getTextureRegion().getRegionHeight() * scale
                );
            }
        }
    }
}
