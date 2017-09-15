package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class CustomListener extends ActorGestureListener
{
    private MiceAlert game;
    private Level level;
    private LevelMenu menu;

    public CustomListener(MiceAlert game, Level level, LevelMenu menu)
    {
        this.game = game;
        this.level = level;
        this.menu = menu;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int pointer, int button)
    {
        game.setScreen(new LevelGenerator(game, level, menu));
    }
}
