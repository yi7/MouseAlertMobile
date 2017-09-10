package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains Sprite Sheet
 */
public class SpriteSystem
{
    private LevelGenerator level_generator;     /**<Used to get phone dimensions*/

    private Texture texture;                    /**<Sprite Sheet*/
    private int cols;                           /**<Sprite Sheet columns*/
    private int rows;                           /**<Sprite Sheet rows*/
    private TextureRegion[][] sprite_sheet;     /**<Split the Sprite Sheet into a cols x rows grid*/
    private Animation[] sprite_animation_list;  /**<Data Structure which contains all Sprite Animation*/
    private float scale;                        /**<Scale of the Sprite*/

    /**
     * Constructor that initializes a new SpriteSystem
     * @param texture  SpriteSystem Sheet
     * @param cols     SpriteSystem Sheet columns
     * @param rows     SpriteSystem Sheet rows
     */
    public SpriteSystem(Texture texture, int cols, int rows)
    {
        TextureRegion[] temp;

        this.texture = texture;
        this.cols = cols;
        this.rows = rows;
        sprite_sheet = TextureRegion.split
        (
                texture,
                texture.getWidth() / cols,
                texture.getHeight() / rows
        );
        sprite_animation_list = new Animation[rows];

        for(int i = 0; i < rows; i++)
        {
            temp = new TextureRegion[cols];
            for(int j = 0; j < cols; j++)
            {
                temp[j] = sprite_sheet[i][j];
            }

            sprite_animation_list[i] = new Animation(1f/4f, temp);
            sprite_animation_list[i].setPlayMode(Animation.PlayMode.LOOP);
        }
    }

    /**
     * Sets the LevelGenerator, used for level dimension and scale
     * @param level_generator LevelGenerator to set
     */
    public void setLevelGenerator(LevelGenerator level_generator)
    {
        this.level_generator = level_generator;
        this.scale = level_generator.phone_scale;
    }

    /**
     * Gets the LevelGenerator, used for level dimension and scale
     * @return LevelGenerator to retrieve
     */
    public LevelGenerator getLevelGenerator()
    {
        return level_generator;
    }

    /**
     * Method that animates the sprite onto the screen
     * @param key   Determines which Sprite animation to draw
     * @param batch Sprite Batch
     * @param delta_time game time
     * @param x     X position
     * @param y     Y position
     */
    public void draw(int key, Batch batch, float delta_time, float x, float y)
    {
        TextureRegion region = sprite_animation_list[key].getKeyFrame(delta_time, true);
        batch.draw(region, x * scale, y * scale, region.getRegionWidth() * scale, region.getRegionHeight() * scale);
    }

    /**
     * Method that draws the sprite onto the screen based on scale
     * @param key Determines which Sprite to draw
     * @param batch Sprite Batch
     * @param delta_time game time
     * @param x X position
     * @param y Y position
     * @param width frame width
     * @param height frame height
     */
    public void draw(int key, Batch batch, float delta_time, float x, float y, float width, float height)
    {
        TextureRegion region = sprite_animation_list[key].getKeyFrame(delta_time, true);
        batch.draw(region, x * scale, y * scale, width * scale, height * scale);
    }
}
