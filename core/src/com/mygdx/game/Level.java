package com.mygdx.game;

public class Level
{
    private String path;
    private int arrow_limit;
    private int tilemap_width;
    private int tilemap_height;

    public Level()
    {
        this.path = "";
        this.arrow_limit = 0;
    }

    public Level(String path, int arrow_limit, int tilemap_width, int tilemap_height)
    {
        this.path = path;
        this.arrow_limit = arrow_limit;
        this.tilemap_width = tilemap_width;
        this.tilemap_height = tilemap_height;
    }

    public String getPath()
    {
        return path;
    }

    public int getArrowLimit()
    {
        return arrow_limit;
    }

    public int getTilemapWidth()
    {
        return tilemap_width;
    }

    public int getTilemapHeight()
    {
        return tilemap_height;
    }
}
