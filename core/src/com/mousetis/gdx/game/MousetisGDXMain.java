package com.mousetis.gdx.game;

/**
 * @author Matt Mousetis
 */


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mousetis.gdx.game.Assets.Assets;
import com.mousetis.gdx.game.screens.MenuScreen;
import com.badlogic.gdx.assets.AssetManager;

	
public class MousetisGDXMain extends Game
{
		private static final String TAG = MousetisGDXMain.class.getName();
		
		private WorldController worldController;
		private WorldRenderer worldRenderer;
		
		public void create() 
		{ 
			//set libgdx log level to DEBUG
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
			//Load assets
			Assets.instance.init(new AssetManager());
			//load preferences for audio stuff
			GamePreferences.instance.load();
			//AudioManager.instance.play(Assets.instance.music.song01);
			//initialize controller and renderer
	        setScreen(new MenuScreen(this));
		}
		/**
		@Override public void render () 
		{
			worldController.update(Gdx.graphics.getDeltaTime());
			Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			worldRenderer.render();
		}
		@Override public void resize (int width, int height) 
		{
			worldRenderer.resize(width, height);
		}
		@Override public void pause () { }
		@Override public void resume () { }
		@Override public void dispose () 
		{
			worldRenderer.dispose();
			Assets.instance.dispose();
		}
		*/
}
