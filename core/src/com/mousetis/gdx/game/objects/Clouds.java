package com.mousetis.gdx.game.objects;

/**
 * @author Matt Mousetis
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mousetis.gdx.game.Assets.Assets;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class Clouds extends AbstractGameObject
{
	private float length;
	
	private Array<TextureRegion> regClouds;
	private Array<Cloud> clouds;
	

	private class Cloud extends AbstractGameObject
	{
		private TextureRegion regCloud;
		
		public Cloud () {}

		/**
		 *  Sets the texture region for the cloud
		 *  @param region
		 */
		public void setRegion (TextureRegion region)
		{
			regCloud = region;
		}
		
		/**
		 * renders the cloud
		 * @param batch
		 */
		@Override
		public void render(SpriteBatch batch)
		{
			TextureRegion reg = regCloud;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			
		}
		
	}
	/**
	 * constructor for clouds
	 * @param length
	 */
	public Clouds (float length)
	{
		this.length = length;
		init();
	}
	
	/**
	 * initializes the clouds
	 */
	private void init()
	{
		dimension.set(3.0f, 1.5f);
		
		regClouds = new Array<TextureRegion>();
		regClouds.add(Assets.instance.levelDecoration.cloud01);
		regClouds.add(Assets.instance.levelDecoration.cloud02);
		regClouds.add(Assets.instance.levelDecoration.cloud03);
		
		int distFac = 5;
		int numClouds = (int)(length/distFac);
		clouds = new Array<Cloud>(2 * numClouds);
		for(int i = 0; i < numClouds; i++)
		{
			Cloud cloud = spawnCloud();
			cloud.position.x = i * distFac;
			clouds.add(cloud);
		}
	}
	
	/**
	 * spawns the cloud
	 * @return cloud
	 */
	private Cloud spawnCloud() 
	{
		Cloud cloud = new Cloud();
		cloud.dimension.set(dimension);
		//select random cloud image
		cloud.setRegion(regClouds.random());
		//position
		Vector2 pos = new Vector2();
		pos.x = length + 10; //position after end of level
		pos.y += 1.75; //base position
		pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1); //rnadom additional position
		cloud.position.set(pos);
		//speed
		Vector2 speed = new Vector2();
		speed.x += 0.5f; //base speed
		speed.x += MathUtils.random(0.0f, 0.75f);
		cloud.terminalVelocity.set(speed);
		speed.x *= -1;//move left
		cloud.velocity.set(speed);
		return cloud;
	}

	/**
	 * renders the array of the clouds
	 */
	@Override
	public void render(SpriteBatch batch) 
	{
		for (Cloud cloud : clouds)
		{
			cloud.render(batch);
		}
		
	}
	
	/**
	 * updates the cloud
	 */
    @Override
    public void update (float deltaTime) 
    {
        for (int i = clouds.size - 1; i>= 0; i--) 
        {
            Cloud cloud = clouds.get(i);
            cloud.update(deltaTime);
            if (cloud.position.x< -10) 
            {
            // cloud moved outside of world.
            // destroy and spawn new cloud at end of level.
                clouds.removeIndex(i);
                clouds.add(spawnCloud());
            }
        }
    }

}
