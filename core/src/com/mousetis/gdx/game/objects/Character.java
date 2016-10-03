package com.mousetis.gdx.game.objects;
 
 /**
  * @author Matt Mousetis
  */
 
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.graphics.g2d.TextureRegion;
 import com.mousetis.gdx.game.Assets.Assets;

import javafx.geometry.Bounds;

import com.badlogic.gdx.Gdx;
 import com.mousetis.gdx.game.Constants;
 
 public class Character extends AbstractGameObject
 {
 
 	public static final String TAG = Character.class.getName();
 	
 	private final float JUMP_TIME_MAX = 0.3f;
 	private final float JUMP_TIME_MIN = 0.1f;
 	private float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;
 	
 	public enum VIEW_DIRECTION {LEFT, RIGHT}
 	
 	public enum JUMP_STATE{GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING}
 	
 	private TextureRegion regHead;
 	
 	public VIEW_DIRECTION viewDirection;
 	public float timeJumping;
 	public JUMP_STATE jumpState;
 	public boolean hasFireballPowerUp;
 	public float timeLeftFireballPowerUp;
 	
 	/**
 	 * initializes the bunny head
 	 *
 	 */
 	public Character ()
 	{
 		init();
 	}
 	
 	/**
 	 * initializes the bunny head and its settings
 	 * 
 	 */
 	private void init() 
 	{
		dimension.set(0.5f, 0.5f);
 		regHead = Assets.instance.character.head;
 		//center image on game object
 		origin.set(dimension.x / 2, dimension.y /2);
 		//bounding box for collision detection
 		bounds.set(0, 0, dimension.x, dimension.y);
 		//set physics values
 		terminalVelocity.set(3.0f, 4.0f);
 		friction.set(12.0f, 0.0f);
 		acceleration.set(0.0f, -25.0f);
 		//view direction
 		viewDirection = VIEW_DIRECTION.RIGHT;
 		//Jump State
 		jumpState = JUMP_STATE.FALLING;
 		timeJumping = 0;
 		//Power-ups
 		hasFireballPowerUp = false;
		timeLeftFireballPowerUp = 0;
 	}
 
 	/**
 	 * handles the jump states and its transitions
 	 * @param jumpkeypressed
 	 */
 	public void setJumping (boolean jumpKeyPressed) 
 	{
 		switch (jumpState)
 		{
 		case GROUNDED:
 			if(!jumpKeyPressed)
 			{
 				//start counting jump time from the beginning
 				timeJumping = 0;
 				jumpState = JUMP_STATE.JUMP_RISING;
 			}
 		break;
 		case JUMP_RISING: //rising in the air
 			if(!jumpKeyPressed)
 			jumpState = JUMP_STATE.JUMP_FALLING;
 			break;
 		case FALLING: //falling down
 		case JUMP_FALLING: //falling down after jump	
 			if(jumpKeyPressed && hasFireballPowerUp)
 			{
 				timeJumping = JUMP_TIME_OFFSET_FLYING;
 				jumpState = JUMP_STATE.JUMP_RISING;
 			}	
 			break;
 		}	
 	}
 	
 	/**
 	 * updates the bunny head based on time
 	 * @param deltaTime
	 */
 	@Override
 	public void update (float deltaTime)
 	{
 		super.update(deltaTime);
 		if(velocity.x != 0)
 		{
 			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
 		}
 		
 		if(timeLeftFireballPowerUp > 0)
 		{
 			timeLeftFireballPowerUp -= deltaTime;
 			if(timeLeftFireballPowerUp < 0)
 			{
 				//disable powerup
 				timeLeftFireballPowerUp = 0;
 				setFireballPowerUp(false);
 			}
 		}
	}
 		
 	/**
 	 * sets what the feather does when picked up
 	 * @param boolean picked up
 	 */
 	public void setFireballPowerUp (boolean pickedUp)
 	{
 		hasFireballPowerUp = pickedUp;
 		if(pickedUp)
 		{
 			timeLeftFireballPowerUp = Constants.ITEM_POWERUP_DURATION;
 			JUMP_TIME_OFFSET_FLYING = JUMP_TIME_OFFSET_FLYING * 2;
 			
 		}
 		
 	}
 	
 	/**
 	 * updates the motion of the bunny head on the Y axis
 	 * @param deltaTime
 	 */
     @Override
     protected void updateMotionY (float deltaTime) {
         switch (jumpState) {
             case GROUNDED:
                 jumpState = JUMP_STATE.FALLING;
                 break;
             case JUMP_RISING:
                 // Keep track of jump time
                 timeJumping += deltaTime;
                 // Jump time left?
                 if (timeJumping <= JUMP_TIME_MAX) {
                     // Still jumping
                     velocity.y = terminalVelocity.y;
                 }
                 break;
              case FALLING:
                 break;
             case JUMP_FALLING:
                 // Add delta times to track jump time
                 timeJumping += deltaTime;
                 // Jump to minimal height if jump key was pressed too short
                 if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
                     // Still jumping
                     velocity.y = terminalVelocity.y;
                 }
         }
         if (jumpState != JUMP_STATE.GROUNDED)
             super.updateMotionY(deltaTime);
     }
 	
 	public void hasFeatherPowerUp (boolean pickedUp) 
 	{
 		if(pickedUp)
 		{
 			timeLeftFireballPowerUp = Constants.ITEM_FEATHER_POWERUP_DURATION;
 			JUMP_TIME_OFFSET_FLYING = JUMP_TIME_OFFSET_FLYING *2;
 			terminalVelocity.x =terminalVelocity.x * 2;
 			terminalVelocity.y =terminalVelocity.y * 2;
 		}
 		
 	}
 	
 	/**
 	 * render draws the character
 	 * @param spritebatch
 	 */
 	
     @Override
     public void render (SpriteBatch batch) 
     {
         TextureRegion reg = null;
         
         //set special color when game object has the feather
         if(hasFireballPowerUp)
         {
         	batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
         }
         
         //draws the image
         reg = regHead;
         batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(), false, false);
 		
         //reset color to white
        batch.setColor(1, 1, 1 ,1);
     }
 }