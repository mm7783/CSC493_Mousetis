package com.mousetis.gdx.game.objects;

/**
 * @author Matt Mousetis
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Animation;

public abstract class AbstractGameObject 
{
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	public Body body;
	
	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction;
	
	public Vector2 acceleration;
	public Rectangle bounds;
	
	public float stateTime;
	public Animation animation;
	
	public AbstractGameObject()
	{
		position = new Vector2();
		dimension = new Vector2(1 , 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;

		//objects current speed
		velocity = new Vector2();
		//max speed in positive and negative directions
		terminalVelocity = new Vector2(1 , 1);
		//force that slows the object down
		friction = new Vector2();
		//objects constant accelertaion
		acceleration = new Vector2();
		//objects physical body
		bounds = new Rectangle();
		
	}
	
	public void updateMotionX(float deltaTime)
	{
		if(velocity.x != 0)
		{
			if(velocity.x > 0)
			{
				velocity.x =  Math.max(velocity.x - friction.x * deltaTime, 0);
			}
			else
			{
			velocity.x =  Math.max(velocity.x + friction.x * deltaTime, 0);
			}
		}	
		//apply acceleration
		velocity.x += acceleration.x * deltaTime;
		//make sure velocity doesnt exceed its max
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
	}
	
	protected void updateMotionY(float deltaTime)
	{
		if(velocity.y != 0)
		{
			if(velocity.y > 0)
			{
				velocity.y =  Math.max(velocity.y - friction.y * deltaTime, 0);
			}
			else
			{
			velocity.x =  Math.max(velocity.y + friction.y * deltaTime, 0);
			}
		}	
		//apply acceleration
		velocity.y += acceleration.y * deltaTime;
		//make sure velocity doesnt exceed its max
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
	}
	
	
	public void update(float deltaTime)
	{
		stateTime += deltaTime;
		if(body == null)
		{
			updateMotionX(deltaTime);
			updateMotionY(deltaTime);
			//move to new position
			position.x += velocity.x * deltaTime;
			position.y += velocity.y * deltaTime;
		}else {
			position.set(body.getPosition());
			rotation = body.getAngle() * MathUtils.radiansToDegrees;
		}	
	}
	
	public abstract void render (SpriteBatch batch);
	
	/**
	 * sets  the animation for this class
	 * @param animation
	 */
	public void setAnimation(Animation animation)
	{
		this.animation = animation;
		stateTime = 0;
	}
}
