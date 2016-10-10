package com.mousetis.gdx.game;

/**
 * @author Matt Mousetis
 */


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.mousetis.gdx.game.objects.Level;
import com.badlogic.gdx.math.Rectangle;
import com.mousetis.gdx.game.objects.Rock;
import com.mousetis.gdx.game.objects.BunnyHead;
import com.mousetis.gdx.game.objects.Feather;
import com.mousetis.gdx.game.objects.GoldCoin;
import com.badlogic.gdx.Game;
import com.mousetis.gdx.screens.MenuScreen;

public class WorldController extends InputAdapter{
	
	private static final String TAG = WorldController.class.getName();
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;
	private Game game;
	
	//rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	private float timeLeftGameOverDelay;
	
	/**
	 * constructor for the world controller
	 * @param game 
	 * 
	 */
	public WorldController(Game game) 
	{
		this.game = game;
		init();
		
	}

	public WorldController() 
	{
		init();
		// TODO Auto-generated constructor stub
	}

	/**
	 * initializes the level
	 * 
	 */
	public void initLevel()
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
	}
	
	private void backToMenu()
	{
		//switch to menu screen
		game.setScreen(new MenuScreen(game));
	}
	
	/**
	 * initializes the world controller
	 * 
	 */
	private void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		initLevel();
	}

	/**
	 * creates a procedural pixmap
	 * @param width @param height
	 */
	private Pixmap createProceduralPixmap(int width, int height) {
		
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(1,0,0,0.5f);
		pixmap.fill();
		pixmap.setColor(1,1,0,1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.setColor(0,1,1,1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}

	/**
	 * updates the world controller based on time
	 * @param deltaTime
	 */
	public void update (float deltaTime) 
	{
		handleDebugInput(deltaTime);
		if(isGameOver())
		{
			timeLeftGameOverDelay -= deltaTime;
			if(timeLeftGameOverDelay < 0) init();
			if(timeLeftGameOverDelay < 0) backToMenu();
		}else
		{
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if(!isGameOver() && isPlayerInWater())
		{
			lives --;
			if(isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
	}

	/**
	 * handles the input from the player
	 * @param deltaTime
	 */
	private void handleDebugInput(float deltaTime) 
	{
		if(Gdx.app.getType() != ApplicationType.Desktop) return;
		
		
		if(!cameraHelper.hasTarget(level.bunnyHead))
		{
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
		
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
		
			if(Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
		
			if(Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
		
			if(Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
		
			if(Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
		}
		
		float camZoomSpeed = 1*deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
		
		if(Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		
		if(Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		
		if(Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
	}

	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		
		cameraHelper.setPosition(x, y);
	}
	
	/**
	 * handles what happens when a keys is pressed
	 * @param keycode
	 */
	
	@Override
	public boolean keyUp (int keycode)
	{
		if(keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG, "Game world reset");
		}
		//toggle camera follow
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.bunnyHead);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		//back to menu
		else if(keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu();
		}
		
		return false;
	}
	
	/**
	 * handles when the bunny collides with a rock
	 * @param rock
	 */
    private void onCollisionBunnyHeadWithRock(Rock rock) 
    {
        BunnyHead bunnyHead = level.bunnyHead;
        float heightDifference = Math.abs(bunnyHead.position.y
                - (rock.position.y
                + rock.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitLeftEdge = bunnyHead.position.x
                    > (rock.position.x
                    + rock.bounds.width / 2.0f);
            if (hitLeftEdge) {
                bunnyHead.position.x = rock.position.x
                        + rock.bounds.width;
            } else {
                bunnyHead.position.x = rock.position.x
                        - bunnyHead.bounds.width;
            }
            return;
        }

        switch (bunnyHead.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                bunnyHead.position.y = rock.position.y
                        + bunnyHead.bounds.height
                        + bunnyHead.origin.y;
                bunnyHead.jumpState = BunnyHead.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                bunnyHead.position.y = rock.position.y
                        + bunnyHead.bounds.height
                        + bunnyHead.origin.y;
                break;
        }
    }

	/**
	 * handles collision between gold coin and bunny
	 * @param goldcoin
	 */
	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin)
	{
		goldcoin.collected = true;
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold Coin collected");
	};
	
	/**
	 * handles collision between feathers and the bunny
	 * @param feather
	 */
	private void onCollisionBunnyWithFeather(Feather feather) 
	{
		feather.collected = true;
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerUp(true);
		Gdx.app.log(TAG, "Gold Coin collected");
	};
	
	/**
	 * tests if there is going to be a collision
	 * 
	 */
	private void testCollisions()
	{
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
		
		//test collision : bunny head with rocks
		for (Rock rock : level.rocks)
		{
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if(!r1.overlaps(r2)) continue;
				onCollisionBunnyHeadWithRock(rock);
				//important must do all collisions for valid edge testing on rocks
		}
		
		//test collision : bunny head with gold coins
		for(GoldCoin goldcoin : level.goldCoins)
		{
			if(goldcoin.collected) continue;
			r2.set(goldcoin.position.x, goldcoin.position.y, goldcoin.bounds.width, goldcoin.bounds.height);
			if(!r1.overlaps(r2)) continue;
			onCollisionBunnyWithGoldCoin(goldcoin);
			break;
		}
		
		//test collision bunny head with feathers
		for (Feather feather : level.feathers)
		{
			if(feather.collected) continue;
			r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithFeather(feather);
			break;
		}
	}
	
	/**
	 * handles the input from the player
	 * @param deltaTime
	 */
    private void handleInputGame(float deltaTime) 
    {
        if (cameraHelper.hasTarget(level.bunnyHead)) {
            // Player Movement
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
            } else {
                // Execute auto-forward movement on non-desktop platform
                if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
                    level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
                }
            }
            // Bunny Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                level.bunnyHead.setJumping(true);
            } else {
                level.bunnyHead.setJumping(false);
            }
        }
    }
    
   /**
    * tests if the player has run out of lives 
    * 
    */
    public boolean isGameOver()
    {
    	return lives < 0;
    }
    
    /**
     * 
     * tests if the player is in the water
     */
    public boolean isPlayerInWater()
    {
    	return level.bunnyHead.position.y < -5;
    }
}
