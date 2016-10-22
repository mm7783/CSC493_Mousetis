package com.mousetis.gdx.game.desktop;

/**
 * @author Matt Mousetis
 * 
 * 
 * 
 */


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mousetis.gdx.game.MousetisGDXMain;
import com.mousetis.gdx.game.MousetisGdxGame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class Main 
{
	
		private static boolean rebuildAtlus = true;
		private static boolean drawDebugOutline = true;
	
	
	public static void main (String[] arg) 
	{
		if(rebuildAtlus)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", "../CanyonBunny-desktop/assets/images", "canyonbunny.pack");
		}
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		new LwjglApplication(new MousetisGdxGame(), cfg);
		cfg.title = "CanyonBunny";
		cfg.width = 800;
		cfg.height = 480;
		
		new LwjglApplication(new MousetisGDXMain(), cfg);
	}
	
	
}
