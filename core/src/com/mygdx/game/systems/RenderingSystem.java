package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;

public class RenderingSystem extends IteratingSystem
{
    private SpriteBatch batch;
    private Array<Entity> renderQueue;

    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<TransformComponent> transformMapper;

    public RenderingSystem(SpriteBatch batch)
    {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());

        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);

        renderQueue = new Array<Entity>();

        this.batch = batch;
    }

    public void update(float deltaTime)
    {
        super.update(deltaTime);

        batch.begin();

        for(Entity entity : renderQueue)
        {
            TextureComponent textureComponent = textureMapper.get(entity);

            if(textureComponent.region == null)
            {
                continue;
            }

            TransformComponent transformComponent = transformMapper.get(entity);
            float width = textureComponent.region.getRegionWidth();
            float height = textureComponent.region.getRegionHeight();
            float originX = width * 0.5f;
            float originY = height * 0.5f;

            batch.draw( textureComponent.region,
                        transformComponent.pos.x - originX,
                        transformComponent.pos.y - originY,
                        originX,
                        originY,
                        width,
                        height,
                        transformComponent.scale.x,
                        transformComponent.scale.y,
                        MathUtils.radiansToDegrees * transformComponent.rotation);
        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        renderQueue.add(entity);
    }
}
