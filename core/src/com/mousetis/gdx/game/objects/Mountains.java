package com.mousetis.gdx.game.objects;

/**
 * @author Matt Mousetis
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mousetis.gdx.game.Assets.Assets;
import com.badlogic.gdx.math.MathUtils;

public class Mountains extends AbstractGameObject
{

	private TextureRegion regMountainLeft;
	private TextureRegion regMountainRight;
	
	private int length;
	
	public Mountains (int length)
	{
		this.length = length;
		init();
	}
	
	private void init() 
	{
		dimension.set(10,  2);
		
		regMountainLeft = Assets.instance.levelDecoration.mountainLeft;
		regMountainRight = Assets.instance.levelDecoration.mountainRight;
		
		//shift mountain and extend length
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
		
	}

	private void drawMountain (SpriteBatch batch, float offsetX, float offsetY, float tintColor)
	{
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		
		//mountains span the whole level
		int mountainLength = 0;
		mountainLength += MathUtils.ceil(length / (2 * dimension.x));
		mountainLength += MathUtils.ceil(0.5f + offsetX);
		
		for (int i = 0; i < mountainLength; i++)
		{
			//mountain left
			reg = regMountainLeft;
			batch.draw(reg.getTexture(), position.x + xRel, position.y + yRel, origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			
		
		
			//mountain right
			reg = regMountainRight;
			batch.draw(reg.getTexture(), position.x + xRel, position.y + yRel, origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;
			
		}
		
		//reset Color to white
		batch.setColor(1, 1, 1, 1);
	}
	
	
	@Override
	public void render(SpriteBatch batch) 
	{
		//distant mountains (dark grey)
		drawMountain(batch, 0.5f, 0.5f, 0.5f);
		//distant mountains grey
		drawMountain(batch, 0.25f, 0.25f, 0.7f);
		//distant mountains (light grey)
		drawMountain(batch, 0.0f, 0.0f, 0.9f);
		
	}

}
