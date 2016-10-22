package com.mousetis.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mousetis.gdx.game.Assets.Assets;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Building extends AbstractGameObject
{

	private TextureRegion building;
	
	private TextureRegion regBuildingLeft;
	private TextureRegion regBuildingRight;
	
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

	//draws the buildings
    private void drawBuilding(SpriteBatch batch, float offsetX, float offsetY, float tintColor, float parallaxSpeedX) {
        TextureRegion reg;
        batch.setColor(tintColor, tintColor, tintColor, 1);
        float xRel = dimension.x * offsetX;
        float yRel = dimension.y * offsetY;
        // mountains span the whole level
        int mountainLength = 0;
        mountainLength += MathUtils.ceil(length / (2 * dimension.x) * (1 - parallaxSpeedX));
        mountainLength += MathUtils.ceil(length / (2 * dimension.x));
        mountainLength += MathUtils.ceil(0.5f + offsetX);
        for (int i = 0; i < mountainLength; i++) {
            // mountain left
            reg = regBuildingLeft;
            batch.draw(reg.getTexture(), origin.x + xRel + position.x * parallaxSpeedX, origin.y + yRel + position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),reg.getRegionWidth(), reg.getRegionHeight(),false, false);

            xRel += dimension.x;
            // mountain right
            batch.draw(reg.getTexture(),origin.x + xRel + position.x * parallaxSpeedX,origin.y + yRel + position.y,origin.x, origin.y,dimension.x, dimension.y,scale.x, scale.y,rotation,reg.getRegionX(), reg.getRegionY(),reg.getRegionWidth(), reg.getRegionHeight(),false, false);
            xRel += dimension.x;
        }
        // reset color to white
        batch.setColor(1, 1, 1, 1);
    }
	
	@Override
	public void render(SpriteBatch batch) 
	{
		//distant mountains (dark grey)
		drawBuilding(batch, 0.5f, 0.5f, 0.5f, 0.8f);
		//distant mountains grey
		drawBuilding(batch, 0.25f, 0.25f, 0.7f, 0.5f);
		//distant mountains (light grey)
		drawBuilding(batch, 0.0f, 0.0f, 0.9f, 0.3f);
		
	}

	public void updateScrollPosition(Vector2 camPosition) 
	{
		position.set(camPosition.x, position.y);
	}

}