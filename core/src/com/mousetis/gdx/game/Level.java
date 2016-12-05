
package com.mousetis.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mousetis.gdx.game.objects.Ground;
import com.mousetis.gdx.game.objects.AbstractGameObject;
import com.mousetis.gdx.game.objects.Apple;
import com.mousetis.gdx.game.objects.Character;
import com.mousetis.gdx.game.objects.GoldCoin;

public class Level 
{
	public Character character;
	
	private static final String TAG = WorldController.class.getName();
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;
	public Array<GoldCoin> goldcoins;
	
	//rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	public enum BLOCK_TYPE 
	{
		EMPTY(0,0,0), //black
		ROCK(0, 255, 0), //green
		PLAYER_SPAWNPOINT(255, 255, 255), //white
		ITEM_FEATHER(255, 0 , 255), 	// purple
		ITEM_GOLD_COIN(255,255,0); 		//yellow
		
		private int color;
		private BLOCK_TYPE (int r, int g, int b)
		{
			color = r << 24 | g << 16 | b << 8 | 0xff; 
		}
		
		public boolean sameColor(int color)
		{
			return this.color == color;
		}
		
		public int getColor()
		{
			return color;
		}
	}
	
	//objects
	public Array<Ground> ground;
	public Array<Apple> apples;
	
	
	
	//method that takes in the color data in order to determine what to draw there
	private void init (String filename) 
	{
		//character
		character = null;
		
		//objects
		ground = new Array <Ground>();
		apples = new Array<Apple>();
		goldcoins = new Array<GoldCoin>();
		
		//load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		//scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
		{
			for(int pixelX = 0; pixelX < pixmap.getHeight(); pixelX++)
			{
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				//height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
				//get color of current pixel as 32 - bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				//find matching color value to indentify block type at (x,y)
				//point and create the corresponding game object if there is a match
				
				//empty space
				if(BLOCK_TYPE.EMPTY.sameColor(currentPixel))
				{
					//do nothing
				}	
				else if(BLOCK_TYPE.ROCK.sameColor(currentPixel))
				{
					if(lastPixel != currentPixel)
					{
					obj = new Ground();
					float heightIncreaseFactor = 0.25f;
					offsetHeight = -2.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
					ground.add((Ground)obj);
					}
					else 
					{
						ground.get(ground.size - 1).increaseLength(1);
					}
				}	
				

				//player spawn point				
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) 
				{
					obj = new Character();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					character = (Character)obj;
				}
				
				//feather
				else if(BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel))
				{
					obj = new Apple();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					apples.add((Apple)obj);
				}
				
                // gold coin
                else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
                    obj = new GoldCoin();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    goldcoins.add((GoldCoin) obj);
                }
				
				//unkown object/pixel color
				else
				{
					int r = 0xff & (currentPixel >>> 24); //read color channel
					int g = 0xff & (currentPixel >>> 16);  //green color channel
					int b = 0xff & (currentPixel >>> 8); //blue color channel
					int a = 0xff & currentPixel; //alpha control
					
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y <" + pixelY + ">: r<" + r + "> g <" + g +  "> b <" + b + "> a <"+ a + ">" );
				}
				lastPixel = currentPixel;
			}
		}
		
		//free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "Level '"+ filename +"' loaded");
	}

	/**
 	 * constructor for the level
 	 * @param filename
 	 */
	public Level(String filename)
	{
		init(filename);
	}
	

	
 	/**
 	 * renders the level
 	 * @param spritebatch
 	 */
	public void render (SpriteBatch batch) 
	{
		
		//draw rocks
		for(Ground G : ground)
			G.render(batch);
		
		//draw feathers
		for(Apple apple : apples)
			apple.render(batch);
		
        // Draw Gold Coins
        for (GoldCoin goldCoin : goldcoins)
            goldCoin.render(batch);
		
		//draw player character
		character.render(batch);
		
	}

	public void update(float deltaTime) 
	{
		
		//draw rocks
		for(Ground G : ground)
			G.update(deltaTime);
		
		//draw feathers
		for(Apple apple : apples)
			apple.update(deltaTime);
		
        // Draw Gold Coins
        for (GoldCoin goldCoin : goldcoins)
            goldCoin.update(deltaTime);
		
		//draw player character
		character.update(deltaTime);
		
	}
	
}