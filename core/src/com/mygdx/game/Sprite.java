package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
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
    int cols;
    int rows;
    float scale;

    public Sprite(Texture texture, int cols, int rows)
    {
        this.texture = texture;
        this.cols = cols;
        this.rows = rows;
    }

    public void setScaling(float scale)
    {
        this.scale = scale;
    }

    public TextureRegion[] generateSpriteTextureRegion()
    {
        TextureRegion[][] temp = TextureRegion.split
        (
            texture,
            texture.getWidth() / cols,
            texture.getHeight() / 4 / rows
        );
        TextureRegion[] textureRegion = new TextureRegion[cols * rows];
        int index = 0;
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                textureRegion[index++] = temp[i][j];
            }
        }
        return textureRegion;
    }

    public ArrayList<Vector2> getSpawnPosition(MapObjects mapObjects)
    {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        for (MapObject object : mapObjects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                Vector2 position = new Vector2();
                position.set
                        (
                                rect.getX() * scale,
                                rect.getY() * scale
                        );
                positions.add(position);
            }
        }
        return positions;
    }
}
