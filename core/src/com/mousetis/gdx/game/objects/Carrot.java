package com.mousetis.gdx.game.objects;

/**
 * @author Matt Mousetis
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mousetis.gdx.game.Assets.Assets;

public class Carrot extends AbstractGameObject 
{
    private TextureRegion regCarrot;

    /**
     * Constructor for the carrot class
     */
    public Carrot() 
    {
        init();
    }

    /**
     * initializes the setting for the carrot
     */
    private void init() 
    {
        dimension.set(0.25f, 0.5f);
        regCarrot = Assets.instance.levelDecoration.carrot;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        origin.set(dimension.x / 2, dimension.y / 2);
    }

    /**
     * renders the carrot
     * @param batch
     */
    public void render(SpriteBatch batch) 
    {
        TextureRegion reg = null;
        reg = regCarrot;
        batch.draw(reg.getTexture(), position.x - origin.x,
                position.y - origin.y, origin.x, origin.y, dimension.x,
                dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
                reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
    }
}