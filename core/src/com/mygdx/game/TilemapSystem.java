package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

/**
 * The core data structure for Tile System
 */
public class TilemapSystem
{
    public final int tile_frame_size = 64;             /**<Size of Tile*/
    public final int tilemap_height = 7;               /**<Number of tiles that make up the height*/
    public final int tilemap_width = 9;                /**<Number of tiles that make up the width*/
    private HashMap<Integer, Vector2> coordinate_list;  /**<Data Structure which contains all coordinates*/

    /**
     * Constructor that initializes coordinate Data Structure
     */
    public TilemapSystem()
    {
        int index = 0;
        Vector2 position;
        coordinate_list = new HashMap<Integer, Vector2>();

        for(int i = tilemap_height; i > 0; i--)
        {
            for(int j = 0; j < tilemap_width; j++)
            {
                position = new Vector2((j * tile_frame_size), ((i-1) * tile_frame_size));
                coordinate_list.put(index++, position);
            }
        }
    }

    /**
     * Gets the sprite_frame size of a tile
     * @return Size of tile
     */
    public int getTileFrameSize()
    {
        return tile_frame_size;
    }

    /**
     * Gets the coordinate of a tile
     * @param position position of the tile on the Level
     * @return coordinate of the tile in position
     */
    public Vector2 getMapCoordinate(int position)
    {
        return coordinate_list.get(position);
    }
}
