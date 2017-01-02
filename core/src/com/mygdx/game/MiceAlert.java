package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MiceAlert extends Game
{
	public SpriteBatch batch;

	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render ()
	{
		super.render();
	}
	
	@Override
	public void dispose ()
	{
		batch.dispose();
	}
}
