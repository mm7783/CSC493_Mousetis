package com.mousetis.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mousetis.gdx.game.Assets.Assets;

public class Ground extends AbstractGameObject
{
	private TextureRegion regEdge;
	private TextureRegion regMiddle;
	
	private int length;
	
	public Ground ()
	{
		init();
	}
	
	//initalizes the object length texture to use ect.
	private void init ()
	{
		dimension.set(1, 1.5f);
		regEdge = Assets.instance.ground.ground;
		regMiddle = Assets.instance.ground.ground;
		
		//start length of this rock
		setLength(1);
	}

	//sets the length of the object
	private void setLength(int i) 
	{
		this.length = length;
	}

	//increases the length of the object
	public void increaseLength (int amount)
	{
		setLength(length + amount);
	}
	
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