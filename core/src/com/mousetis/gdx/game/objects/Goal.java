package com.mousetis.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mousetis.gdx.game.Assets.Assets;

public class Goal extends AbstractGameObject 
{
    private TextureRegion regGoal;

    /**
     * constructor for the goal class
     */
    public Goal() 
    {
        init();
    }

    /**
     * initializes the goal object's setting
     */
    private void init() 
    {
        dimension.set(3.0f, 3.0f);
        regGoal = Assets.instance.levelDecoration.goal;
        // Set bounding box for collision detection
        bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_VALUE);
        origin.set(dimension.x / 2.0f, 0.0f);
    }

    /**
     * renders the goal object 
     * @param batch
     */
    public void render(SpriteBatch batch) 
    {
        TextureRegion reg = regGoal;
        batch.draw(reg.getTexture(), position.x - origin.x,
                position.y - origin.y, origin.x, origin.y, dimension.x,
                dimension.y, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(),
                false, false);
    }
}