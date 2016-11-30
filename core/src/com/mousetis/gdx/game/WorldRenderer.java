package com.mousetis.gdx.game;

/**
 * @author Matt Mousetis
 */


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mousetis.gdx.game.GamePreferences;
import com.mousetis.gdx.game.Assets.Assets;
import com.badlogic.gdx.math.MathUtils;

public class WorldRenderer implements Disposable {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private OrthographicCamera cameraGUI;
	
	//constructor
	public WorldRenderer (WorldController worldController) 
	{
		this.worldController = worldController;
	}
	
	//initializes the world
	private void init() 
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		camera.setToOrtho(true); //flip y-axis
		cameraGUI.update();
	}
	
	//renders the objects
	public void render() 
	{
		renderWorld(batch);
		renderGUI(batch);
	}

	public void resize(int width, int height)
	{
		int num = 0;
		num =+ 1;
		int x;
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
	}
	
	//method is self explanatory renders the world using a given sprite batch
	private void renderWorld(SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	
	@Override
	public void dispose() 
	{
		batch.dispose();
	}
	
	
 	private void renderGUI(SpriteBatch batch) 
 	{
 		batch.setProjectionMatrix(cameraGUI.combined);
 		batch.begin();
 		//draw collected gold coins icon + text anchored to top edge
 		renderGuiScore(batch);
 		//draw collected feather icon
 		renderGuiFireballPowerup(batch);
  		//draw the extra lives icon
  		renderGuiExtraLife(batch);
  		//draw FPS text anchored to  bottom right
 		rederGuiFpsCounter(batch);
 		if(GamePreferences.instance.showFpsCounter)
 			rederGuiFpsCounter(batch);
  		//draw game over text
  		renderGuiGameOverMessage(batch);
  		batch.end();
 	}
 	
	private void renderGuiScore (SpriteBatch batch)
	{        
		float x = -15;
		float y = -15;
		float offsetX = 50;
		float offsetY = 50;
		if (worldController.scoreVisual < worldController.score) {
			long shakeAlpha = System.currentTimeMillis() % 360;
			float shakeDist = 1.5f;
			offsetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDist;
			offsetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDist;
		}
		Assets.instance.fonts.defaultBig.draw(batch, "" + (int) worldController.scoreVisual, x + 75, y + 37);

	}
	
	private void renderGuiExtraLife (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
		float y = -15;
		
		for(int i = 0; i < Constants.LIVES_START; i++)
		{
			if(worldController.lives <= i)
			{
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
				batch.draw(Assets.instance.character.head, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
				batch.setColor(1, 1, 1, 1);
			}
		}
        if (worldController.lives >= 0 && worldController.livesVisual > worldController.lives) {
            int i = worldController.lives;
            float alphaColor = Math.max(0, worldController.livesVisual - worldController.lives - 0.5f);
            float alphaScale = 0.35f * (2 + worldController.lives - worldController.livesVisual) * 2;
            float alphaRotate = -45 * alphaColor;
            batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
            batch.draw(Assets.instance.character.head, x + i * 50, y, 50, 50, 120, 100, alphaScale, -alphaScale, alphaRotate);
            batch.setColor(1, 1, 1, 1);
        }
	}
	
	private void rederGuiFpsCounter(SpriteBatch Batch)
	{
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if(fps >= 45)
		{
			//45 fps or more show up in green
			fpsFont.setColor(0, 1, 0, 1);
		}else if (fps >= 30)
		{
			//30 fps or more show up in yellow
			fpsFont.setColor(1, 1, 0, 1);
		}else
		{
			//30 fps or fewer show up in yellow
			fpsFont.setColor(1, 0, 0, 1);
		}
		fpsFont.draw(batch, "FPS: " +fps, x ,y);
		fpsFont.setColor(1, 1, 1, 1); // white
	}
	
	/**
	 * draws the game over message
	 * @param batch
	 */
    private void renderGuiGameOverMessage (SpriteBatch batch) 
    {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;
        if (worldController.isGameOver()) {
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, true);
            fontGameOver.setColor(1, 1, 1, 1);
        }
    }
    
    /**
     * draws the feather power up effects
     * @param batch
     */
    private void renderGuiFireballPowerup (SpriteBatch batch) 
    {
        float x = -15;
        float y = 30;
        float timeLeftFeatherPowerup =
                worldController.level.character.timeLeftFireballPowerUp;
        if (timeLeftFeatherPowerup > 0) {
            // Start icon fade in/out if the left power-up time
            // is less than 4 seconds. The fade interval is set
            // to 5 changes per second.
            if (timeLeftFeatherPowerup < 4) {
                if (((int)(timeLeftFeatherPowerup * 5) % 2) != 0) {
                    batch.setColor(1, 1, 1, 0.5f);
                }
            }
            batch.draw(Assets.instance.Apple.Apple, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
            Assets.instance.fonts.defaultSmall.draw(batch, "" + (int)timeLeftFeatherPowerup, x + 60, y + 57);
        }
    }
}
