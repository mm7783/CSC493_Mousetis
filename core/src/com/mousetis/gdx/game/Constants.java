package com.mousetis.gdx.game;

/**
 * @author Matt Mousetis
 */


public class Constants {
	
	//width of the game world
	public static final float VIEWPORT_WIDTH = 5.0f;
	
	//height of the viewport
	public static final float VIEWPORT_HEIGHT = 5.0f;
	
	//GUI width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	
	//GUI height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	
	//location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "canyonbunny.atlas";
	
	//location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";

	//Amount of extra lives at level start
	public static final int LIVES_START = 3;

	public static final float ITEM_FEATHER_POWERUP_DURATION = 0;
	
	public static final float TIME_DELAY_GAME_OVER = 3;
	
	public static final String TEXTURE_ATLAS_UI = "images/images-ui.pack";
	
	public static final String TEXTURE_LIBGDX_UI = "images/uiskin.atlas";
	
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	
	public static final String SKIN_CANYONBUNNY_UI = "images/images-ui.json";

	public static final String PREFERENCES = null;

	public static final float ITEM_POWERUP_DURATION = 0;
	
	//Number of carrots who spawn
	public static final int CARROTS_SPAWN_MAX = 100;
	
	//Spawn radius for carrots
	public static final float CARROTS_SPAWN_RADIUS = 3.5f;
	
	//delay after game finished
	public static final float TIME_DELAY_GAME_FINISHED = 6;
}
