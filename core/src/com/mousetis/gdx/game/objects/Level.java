package com.mousetis.gdx.game.objects;

/**
 * @author Matt Mousetis
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Level 
{
	public static final String TAG = Level.class.getName();

	
	/**
	 * sets the enums for the block type
	 *
	 */
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
	public Array<Rock> rocks;
	public BunnyHead bunnyHead;
	public Array<GoldCoin> goldCoins;
	public Array<Feather> feathers;
	
	//decorations
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;
	
	/**
	 * initializes the level
	 * @param filename
	 */
	
	private void init (String filename) 
	{
		//player character
		bunnyHead = null;
		//objects
		rocks = new Array <Rock>();
		goldCoins = new Array<GoldCoin>();
		feathers = new Array<Feather>();
		//load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		//scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
		{
			for(int pixelX = 0; pixelX < pixmap.getHeight(); pixelY++)
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
					obj = new Rock();
					float heightIncreaseFactor = 0.25f;
					offsetHeight = -2.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
					rocks.add((Rock)obj);
					}
					else 
					{
						rocks.get(rocks.size - 1).increaseLength(1);
					}
				}	
				

				//player spawn point				
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) 
				{
					obj = new BunnyHead();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					bunnyHead = (BunnyHead)obj;
				}
				
				//feather
				else if(BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel))
				{
					obj = new Feather();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					feathers.add((Feather)obj);
				}
				
				//gold coins
				else if(BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel))
				{
					obj = new GoldCoin();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					goldCoins.add((GoldCoin)obj);
				}
				
				//unkown object/pixel color
				else
				{
					int r = 0xff & (currentPixel >>> 24); //read color channel
					int g = 0xff & (currentPixel >>> 16);  //green color channel
					int b = 0xff & (currentPixel >>> 8); //blue color channel
					int a = 0xff & currentPixel; //alpha control
					
					Gdx.app.error(TAG, "Unknown obkect at x<" + pixelX + "> y <" + pixelY + ">: r<" + r + "> g <" + g +  "> b <" + b + "> a <"+ a + ">" );
				}
				lastPixel = currentPixel;
			}
		}
		
		//decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0,2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);
		
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
	 * @param batch
	 */
	public void render (SpriteBatch batch) 
	{
		//draw mountains
		mountains.render(batch);
		
		//draw rocks
		for(Rock rock : rocks)
			rock.render(batch);
		
		//draw gold coins
		for(GoldCoin goldCoin : goldCoins)
			goldCoin.render(batch);

		//draw feathers
		for(Feather feather : feathers)
			feather.render(batch);
		
		//draw player character
		bunnyHead.render(batch);
		
		//draw water overlay
		waterOverlay.render(batch);
		
		
		//draw clouds
		clouds.render(batch);
	}
	
	public void update (float deltaTime)
	{
		bunnyHead.update(deltaTime);
		
		//update rocks
		for(Rock rock : rocks)
			rock.update(deltaTime);
		
		//update gold coins
		for(GoldCoin goldCoin : goldCoins)
			goldCoin.update(deltaTime);

		//update feathers
		for(Feather feather : feathers)
			feather.update(deltaTime);;
		
	}
}