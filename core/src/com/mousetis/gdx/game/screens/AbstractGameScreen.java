
package com.mousetis.gdx.game.screens;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.mousetis.gdx.game.Assets.Assets;

/**
 * @author Matt Mousetis
 * 
 */



public abstract class AbstractGameScreen implements Screen 
{

	protected Game game;

	/**
	 * constructor for the abstract game screen
	 * @param game
	 */
	public AbstractGameScreen(Game game) 
	{
		this.game = game;
	}

	/**
	 * render method for the screens
	 * @param deltaTime
	 */
	public abstract void render(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void show();

	public abstract void hide();

	public abstract void pause();

	public abstract InputProcessor getInputProcessor();

	
	public void resume() 
	{
		Assets.instance.init(new AssetManager());
	}

	
	public void dispose() 
	{
		Assets.instance.dispose();
	}

}