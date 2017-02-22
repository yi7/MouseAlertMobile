package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;

public class EntitySystem extends IteratingSystem
{
    private SpriteBatch batch;
    private Array<Entity> entitySystem;

    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<TransformComponent> transformMapper;

    public EntitySystem(SpriteBatch batch)
    {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());

        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);

        entitySystem = new Array<Entity>();

        this.batch = batch;
    }

    public void update(float deltaTime)
    {

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        entitySystem.add(entity);
    }
}
