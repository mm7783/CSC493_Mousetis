package com.mousetis.gdx.game.objects;

/**
 * @author Matt Mousetis
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.mousetis.gdx.game.Assets.Assets;
import com.badlogic.gdx.Gdx;
import com.mousetis.gdx.game.AudioManager;
import com.mousetis.gdx.game.Constants;
import com.mousetis.gdx.game.GamePreferences;
import com.mousetis.gdx.screens.CharacterSkin;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Animation;

public class BunnyHead extends AbstractGameObject
{

	public static final String TAG = BunnyHead.class.getName();
	
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;
	
	public ParticleEffect dustParticles = new ParticleEffect();
	
	public enum VIEW_DIRECTION {LEFT, RIGHT}
	
	public enum JUMP_STATE{GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING}
	
	private TextureRegion regHead;
	
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFeatherPowerUp;
	public float timeLeftFeatherPowerUp;
	
	public Animation aniNormal;
	public Animation aniCopterTransform;
	public Animation aniCopterTransformBack;
	public Animation aniCopterRotate;
	
	/**
	 * initializes the bunny head
	 *
	 */
	public BunnyHead ()
	{
		init();
	}
	
	/**
	 * initializes the bunny head and its settings
	 * 
	 */
	private void init() 
	{
		aniNormal = Assets.instance.bunny.animNormal;
		aniCopterTransform = Assets.instance.bunny.animCopterTransform;
		aniCopterTransformBack = Assets.instance.bunny.animCopterTransformBack;
		aniCopterRotate = Assets.instance.bunny.animCopterRotate;
		setAnimation(aniNormal);
		
		dimension.set(0.5f, 0.5f);
		regHead = Assets.instance.bunny.head;
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
		hasFeatherPowerUp = false;
		timeLeftFeatherPowerUp = 0;
		
		//Particles
		dustParticles.load(Gdx.files.internal("particles/dust.pfx"), Gdx.files.internal("particles"));
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
			{	
				AudioManager.instance.play(Assets.instance.sounds.jump);
				jumpState = JUMP_STATE.JUMP_FALLING;
				break;
			}
		case FALLING: //falling down
		case JUMP_FALLING: //falling down after jump	
			if(jumpKeyPressed && hasFeatherPowerUp)
			{
				AudioManager.instance.play(Assets.instance.sounds.jumpWithFeather, 1, MathUtils.random(1.0f, 1.1f));
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
		
		if(timeLeftFeatherPowerUp > 0)
		{
			if(animation == aniCopterTransformBack)
			{
				setAnimation(aniCopterTransform);
			}
			timeLeftFeatherPowerUp -= deltaTime;
			if(timeLeftFeatherPowerUp < 0)
			{
				//disable powerup
				timeLeftFeatherPowerUp = 0;
				setFeatherPowerUp(false);
				setAnimation(aniCopterTransformBack);
			}
		}
		
		dustParticles.update(deltaTime);
		
        // Change animation state according to feather power-up
        if (hasFeatherPowerUp) {
            if (animation == aniNormal) {
                setAnimation(aniCopterTransform);
            } else if (animation == aniCopterTransform) {
                if (animation.isAnimationFinished(stateTime))
                    setAnimation(aniCopterRotate);
            }
        } else {
            if (animation == aniCopterRotate) {
                if (animation.isAnimationFinished(stateTime))
                    setAnimation(aniCopterTransformBack);
            } else if (animation == aniCopterTransformBack) {
                if (animation.isAnimationFinished(stateTime))
                    setAnimation(aniNormal);
            }
        }
	}
		
	/**
	 * sets what the feather does when picked up
	 * @param boolean picked up
	 */
	public void setFeatherPowerUp (boolean pickedUp)
	{
		hasFeatherPowerUp = pickedUp;
		if(pickedUp)
		{
			timeLeftFeatherPowerUp = Constants.ITEM_FEATHER_POWERUP_DURATION;
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
        {	
            dustParticles.allowCompletion();
        	super.updateMotionY(deltaTime);
        }    
    }
	
	public void hasFeatherPowerUp () {}
	
	/**
	 * render draws the bunny head
	 * @param spritebatch
	 */
	
    @Override
    public void render (SpriteBatch batch) 
    {
        TextureRegion reg;

        // Draw Particles
        dustParticles.draw(batch);

        // Apply Skin Color
        batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());

        float dimCorrectionX = 0;
        float dimCorrectionY = 0;
        if (animation != aniNormal) {
            dimCorrectionX = 0.05f;
            dimCorrectionY = 0.2f;
        }

        // Set special color when game object has a feather power-up
        if (hasFeatherPowerUp) {
            batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
        }
        // Draw image
        reg = animation.getKeyFrame(stateTime, true);
        batch.draw(reg.getTexture(), position.x, position.y, origin.x,
                origin.y, dimension.x + dimCorrectionX,
                dimension.y + dimCorrectionY, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,
                false);
        // Reset color to white
        batch.setColor(1, 1, 1, 1);
    }
}
