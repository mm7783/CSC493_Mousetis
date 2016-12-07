package com.mousetis.gdx.game;

/**
 * @author Matt Mousetis
 */

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.mousetis.gdx.game.Assets.Assets;
import com.mousetis.gdx.game.objects.Character;
import com.mousetis.gdx.game.objects.Apple;
import com.mousetis.gdx.game.objects.Ground;
import com.mousetis.gdx.game.screens.MenuScreen;


public class WorldController extends InputAdapter{
	
	private static final String TAG = WorldController.class.getName();
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;
	private boolean newHighScore;
	
	// Box2D Collisions
	public World b2world;
	
	public boolean level2;
	
	public float livesVisual;
	public float scoreVisual;
	
	private float timeLeftGameOverDelay;
	
	//rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private Game Game;
	
	public WorldController(Game game) 
	{
		init();
		this.Game = game;
		level2= false;
	}
	
	public WorldController()
	{
		init();
		level2= false;
		newHighScore= false;
	}
	
	//initiates this class
	private void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}

	//initiates the level
	private void initLevel()
	{
		score = 0;
		scoreVisual = score;
		GamePreferences.instance.load();
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.character);
		initPhysics();
	}

	//initiates the level 2
	private void initLevel2()
	{
		scoreVisual = score;
		GamePreferences.instance.load();
		level = new Level(Constants.LEVEL_02);
		cameraHelper.setTarget(level.character);
		initPhysics();
	}
	
	/**
	 * updates the world controller based on time
	 * @param deltaTime
	 */
    public void update (float deltaTime) 
    {
        handleDebugInput(deltaTime);
        handleInputGame(deltaTime);
        cameraHelper.update(deltaTime);
        level.update(deltaTime);
        testCollisions();
        
        if (!isGameOver() && isPlayerInWater()) 
        {
    		GamePreferences.instance.save();
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }
        if (livesVisual > lives)
            livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
        if (scoreVisual < score)
            scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
        


        if(score > GamePreferences.instance.Highscore3 && score < GamePreferences.instance.Highscore2)
        {
        	GamePreferences.instance.Highscore3 = score;
        }
        
        if(score > GamePreferences.instance.Highscore2 && score < GamePreferences.instance.Highscore1)
        {
        	GamePreferences.instance.Highscore2 = score;
        }
        
        if(score > GamePreferences.instance.Highscore1)
        {
        	GamePreferences.instance.Highscore1 = score;
        }
        newHighScore = true;
    }

	//handles the input
	private void handleDebugInput(float deltaTime) {
		if(Gdx.app.getType() != ApplicationType.Desktop) return;
		
		if(!cameraHelper.hasTarget(level.character))
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

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		
		cameraHelper.setPosition(x, y);
	}
	
	@Override
	public boolean keyUp (int keycode)
	{	
		if(keycode == Keys.R)
		{
			if(level2 == false)
			{	
				initLevel2();
				level2 = true;
			}
			else
			{
				init();
				level2 = false;
			}
			Gdx.app.debug(TAG, "Level Switched");
		}
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.character);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		else if(keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu();
		}
		else if (keycode == Keys.S)
		{
			score = score + 10;
		}

		else if (keycode == Keys.Q)
		{
			System.out.println(""+GamePreferences.instance.Highscore1);
			System.out.println(""+GamePreferences.instance.Highscore2);
			System.out.println(""+GamePreferences.instance.Highscore3);
		}	
		return false;
	}
	
	/**
	 * handles the input from the player
	 * @param deltaTime
	 */
	private void handleInputGame(float deltaTime)
	{
		if (cameraHelper.hasTarget(level.character))
		{
			Character player = level.character;
			if (Gdx.input.isKeyPressed(Keys.LEFT))
			{
				System.out.println("Hit left");
				player.velocity.x = -player.terminalVelocity.x;
			}
			else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				System.out.println("Hit right");
				player.velocity.x = player.terminalVelocity.x;
			}

			if (Gdx.input.isKeyPressed(Keys.SPACE))
			{
				
				player.setJumping(true);
			}
			else
			{
				player.setJumping(false);
			}
		}
	}
    
	/**
	 * tests if there is going to be a collision
	 * 
	 */
	private void testCollisions()
	{
		r1.set(level.character.position.x, level.character.position.y, level.character.bounds.width, level.character.bounds.height);
		
		//test collision : bunny head with rocks
		for (Ground rock : level.ground)
		{
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if(!r1.overlaps(r2)) continue;
				onCollisionBunnyHeadWithRock(rock);
				//important must do all collisions for valid edge testing on rocks
		}
		
		
		//test collision bunny head with feathers
		for (Apple apple : level.apples)
		{
			if(apple.collected) continue;
			r2.set(apple.position.x, apple.position.y, apple.bounds.width, apple.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionCharacterWithFireball(apple);
			break;
		}
	}
	
	/**
 	 * handles collision with the fireball
 	 * @param apple
 	 */
 	private void onCollisionCharacterWithFireball(Apple apple) 
 	{
		apple.collected = true;
		//AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score += apple.getScore();
		level.character.setFireballPowerUp(true);
		Gdx.app.log(TAG, "TURBO MODE");
		
	}

	/**
 	 * handles collision with ground
 	 * @param rock
 	 */
	private void onCollisionBunnyHeadWithRock(Ground rock) 
	{
        Character character = level.character;
        float heightDifference = Math.abs(character.position.y - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f) 
        {
            boolean hitLeftEdge = character.position.x
                    > (rock.position.x + rock.bounds.width / 2.0f);
            if (hitLeftEdge) 
            {
                character.position.x = rock.position.x + rock.bounds.width;
            } else 
            {
                character.position.x = rock.position.x - character.bounds.width;
            }
        
            return;
        }
        
        switch (character.jumpState) {
        case GROUNDED:
            break;
        case FALLING:
        case JUMP_FALLING:
            character.position.y = rock.position.y
                    + character.bounds.height
                    + character.origin.y;
            character.jumpState = Character.JUMP_STATE.GROUNDED;
            break;
        case JUMP_RISING:
            character.position.y = rock.position.y
                    + character.bounds.height
                    + character.origin.y;
            break;
        }
	}
	
	public boolean isGameOver()
	{
		return lives < 0;
	}
	
	public boolean isPlayerInWater()
	{
		return level.character.position.y < -5;
	}
	
	private void backToMenu()
	 {
	 		//switch to menu screen
	 		Game.setScreen(new MenuScreen(Game));
	 }

	public void flagForRemoval(Ground block) {
		// TODO Auto-generated method stub
		
	}
	
	private void initPhysics()
	{
		if (b2world != null)
		b2world.dispose();
		b2world = new World(new Vector2(0, -9.81f), true);
		b2world.setContactListener(new CollisionHandler(this));  // Not in the book
		Vector2 origin = new Vector2();
		for (Ground pieceOfLand : level.ground)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(pieceOfLand.position);
			bodyDef.type = BodyType.KinematicBody;
			Body body = b2world.createBody(bodyDef);
			body.setType(BodyType.DynamicBody);
			body.setUserData(pieceOfLand);
			pieceOfLand.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = pieceOfLand.bounds.width / 2.0f;
			origin.y = pieceOfLand.bounds.height / 2.0f;
			polygonShape.setAsBox(pieceOfLand.bounds.width / 2.0f, (pieceOfLand.bounds.height-0.04f) / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
		
		// For PLayer
		Character player = level.character;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(player.position);
		bodyDef.fixedRotation = true;

		Body body = b2world.createBody(bodyDef);
		body.setType(BodyType.DynamicBody);
		body.setGravityScale(0.0f);
		body.setUserData(player);
		player.body = body;

		PolygonShape polygonShape = new PolygonShape();
		origin.x = (player.bounds.width) / 2.0f;
		origin.y = (player.bounds.height) / 2.0f;
		polygonShape.setAsBox((player.bounds.width-0.7f) / 2.0f, (player.bounds.height-0.15f) / 2.0f, origin, 0);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.friction = 0.5f;
		body.createFixture(fixtureDef);
		polygonShape.dispose();
	}
	
}