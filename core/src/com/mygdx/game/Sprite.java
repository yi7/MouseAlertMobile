package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Sprite
{
    Texture texture;
    TextureRegion[][] textureRegion;
    int cols;
    int rows;
    float scale;

    public Sprite(Texture texture, int cols, int rows)
    {
        this.texture = texture;
        this.cols = cols;
        this.rows = rows;
        textureRegion = TextureRegion.split
        (
            texture,
            texture.getWidth() / cols,
            texture.getHeight() / rows
        );
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public float getScale()
    {
        return scale;
    }

    public TextureRegion[] generateSpriteTextureRegionUp()
    {
        TextureRegion[] temp = new TextureRegion[cols];
        int index = 0;

        for(int i = 0; i < cols; i++)
        {
            temp[index++] = textureRegion[0][i];
        }
        return temp;
    }

    public TextureRegion[] generateSpriteTextureRegionRight()
    {
        TextureRegion[] temp = new TextureRegion[cols];
        int index = 0;

        for(int i = 0; i < cols; i++)
        {
            temp[index++] = textureRegion[1][i];
        }
        return temp;
    }

    public TextureRegion[] generateSpriteTextureRegionDown()
    {
        TextureRegion[] temp = new TextureRegion[cols];
        int index = 0;

        for(int i = 0; i < cols; i++)
        {
            temp[index++] = textureRegion[2][i];
        }
        return temp;
    }

    public TextureRegion[] generateSpriteTextureRegionLeft()
    {
        TextureRegion[] temp = new TextureRegion[cols];
        int index = 0;

        for(int i = 0; i < cols; i++)
        {
            temp[index++] = textureRegion[3][i];
        }
        return temp;
    }
}
