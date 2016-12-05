package com.mousetis.gdx.game.objects;

/**
 * @author Matt Mousetis
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mousetis.gdx.game.Assets.Assets;

public class Apple extends AbstractGameObject
{
	private TextureRegion regApple;
	
	public boolean collected;
	
	public Apple()
	{
		init();
	}

	//initalizes the feather
	private void init() 
	{
		dimension.set(0.5f, 0.5f);
		
		regApple = Assets.instance.Apple.Apple;
		
		//set the bounding box for the feather
		bounds.set(0, 0, dimension.x, dimension.y);
		
		collected = false;
	}

	//renders the feather
	@Override
	public void render(SpriteBatch batch) 
	{
		if(collected) return;
		
		TextureRegion reg = null;
		reg = regApple;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(), false, false);
		
		
	}

	//returns the amount of points gained from collecting the feather
	public int getScore()
	{
		return 250;
	}
}