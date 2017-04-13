package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public class TilemapSystem
{
    final int TOTAL_TILES = 63;
    final int TILEMAP_WIDTH = 9;
    final int TILEMAP_HEIGHT = 7;
    HashMap<Integer, Vector2> positionMap;
    float scale;

    public TilemapSystem()
    {
        int mapX, mapY;
        int index = 0;
        Vector2 position;
        positionMap = new HashMap<Integer, Vector2>();

        for(int i = TILEMAP_HEIGHT; i > 0; i--)
        {
            for(int j = 0; j < TILEMAP_WIDTH; j++)
            {
                position = new Vector2(j * 64, (i-1) * 64);
                Gdx.app.log("Yokaka", j*64 + ",. " + (i-1)*64);
                positionMap.put(index++, position);
            }
        }
    }

    public Vector2 getMapCoordinate(int position)
    {
        return positionMap.get(position);
    }

    public void tilemapSetScale(float scale)
    {
        this.scale = scale;
    }

    public void tilemapRenderObject(MapObjects objects, Batch batch, float deltaTime)
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
