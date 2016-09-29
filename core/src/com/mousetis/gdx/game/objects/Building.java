package com.mousetis.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mousetis.gdx.game.Assets.Assets;
import com.badlogic.gdx.math.MathUtils;

public class Building extends AbstractGameObject
{

	private TextureRegion building;
	
	private int length;
	
	public Building (int length)
	{
		this.length = length;
		init();
	}
	
	//method that initializes the building sets its origin length texture ect.
	private void init() 
	{
		dimension.set(10,  2);
		
		building = Assets.instance.levelDecoration.building;
		
		//shift mountain and extend length
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
		
	}

	//method that draws the buildings
	private void drawBuilding (SpriteBatch batch, float offsetX, float offsetY, float tintColor)
	{
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		
		//mountains span the whole level
		int buildingLength = 0;
		buildingLength += MathUtils.ceil(length / (2 * dimension.x));
		buildingLength += MathUtils.ceil(0.5f + offsetX);
		
		for (int i = 0; i < buildingLength; i++)
		{
			//draw the building
			reg = building;
			batch.draw(reg.getTexture(), position.x + xRel, position.y + yRel, origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			
		}
		
		//reset Color to white
		batch.setColor(1, 1, 1, 1);
	}
	
	
	@Override
	public void render(SpriteBatch batch) 
	{
		//distant mountains (dark grey)
		drawBuilding(batch, 0.5f, 0.5f, 0.5f);
		//distant mountains grey
		drawBuilding(batch, 0.25f, 0.25f, 0.7f);
		//distant mountains (light grey)
		drawBuilding(batch, 0.0f, 0.0f, 0.9f);
		
	}

}