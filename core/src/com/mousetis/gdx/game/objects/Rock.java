package com.mousetis.gdx.game.objects;

/**
 * @author Matt Mousetis
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mousetis.gdx.game.Assets.Assets;

public class Rock extends AbstractGameObject
{
	private TextureRegion regEdge;
	private TextureRegion regMiddle;
	
	private int length;
	
	/**
	 * constructor for the rock class
	 * 
	 */
	public Rock ()
	{
		init();
	}
	
	/**
	 * sets a the initial values for the rock
	 * 
	 */
	private void init ()
	{
		dimension.set(1, 1.5f);
		regEdge = Assets.instance.rock.edge;
		regMiddle = Assets.instance.rock.middle;
		
		//start length of this rock
		setLength(1);
	}

	/**
	 * sets the length of the rock
	 * @param i
	 */
	private void setLength(int i) 
	{
		this.length = length;
		//update bounding box for collection detection
		bounds.set(0 , 0, dimension.x * length, dimension.y);
	}

	/**
	 * increases the length of the rock by an amount
	 * @param amount
	 */
	public void increaseLength (int amount)
	{
		setLength(length + amount);
	}
	
	/**
	 * draws the rock
	 * @param batch
	 */
	@Override
	public void render(SpriteBatch batch) 
	{
		TextureRegion reg = null;
		
		float relX= 0;
		float relY = 0;
		
		//draw left edge
		reg = regEdge;
		relX -= dimension.x / 4;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
		//draw middle
		reg = regMiddle;
		relX = 0;
		for(int i =0; i < length; i++)
		{
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}

		//draw right edge
		reg = regEdge;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}
}
