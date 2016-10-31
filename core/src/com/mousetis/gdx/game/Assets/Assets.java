package com.mousetis.gdx.game.Assets;

/**
 * @author Matt Mousetis
 */


	import com.badlogic.gdx.Gdx;
	import com.badlogic.gdx.assets.AssetDescriptor;
	import com.badlogic.gdx.assets.AssetErrorListener;
	import com.badlogic.gdx.assets.AssetManager;
	import com.badlogic.gdx.graphics.g2d.BitmapFont;
	import com.badlogic.gdx.graphics.g2d.TextureAtlas;
	import com.badlogic.gdx.utils.Disposable;
	import com.mousetis.gdx.game.Constants;
	import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
	import com.badlogic.gdx.graphics.Texture;
	import com.badlogic.gdx.graphics.Texture.TextureFilter;
	import com.badlogic.gdx.audio.Music;
	import com.badlogic.gdx.audio.Sound;

public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName();
	
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	public AssetFonts fonts;
	
	//singleton: prevent instantiation from other classes
	private Assets() {}
	
	public AssetCharacter character;
	public AssetGround ground;
	public AssetFireball fireball;
	public AssetLevelDecoration levelDecoration;
	public AssetSounds sounds;
	public AssetMusic music;
	
	
	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager;
		//set asset manager error handler
		assetManager.setErrorListener(this);
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		//load sounds
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("sounds/jump_with_feather.wav", Sound.class);
		assetManager.load("sounds/pickup_coin.wav", Sound.class);
		assetManager.load("sounds/pickup_feather.wav", Sound.class);
		assetManager.load("sounds/live_lost.wav", Sound.class);
		//load music
		assetManager.load("music/keith303_-_brand_new_highscore.mp3", Music.class);
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: "+ assetManager.getAssetNames().size);
		
		for(String a : assetManager.getAssetNames())
		{
			Gdx.app.debug(TAG, "asset: "+ a);
		}
		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		//enable texture filtering for pixel smoothing
		for(Texture t: atlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		//create game resource objects
		fonts = new AssetFonts();
		character = new AssetCharacter(atlas);
		ground = new AssetGround(atlas);
		fireball = new AssetFireball(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}
	
	//defines the character asset
	public class AssetCharacter
	{
		public final AtlasRegion head;
		
		public AssetCharacter (TextureAtlas atlas)
		{
			head = atlas.findRegion("character Sprite");
		}
	}
	
	//defines the ground asset
	public class AssetGround
	{
		public final AtlasRegion ground;
		
		public AssetGround (TextureAtlas atlas)
		{
			ground = atlas.findRegion("Cobblestone");
		}
	}
	
	//defines the fireball asset
	public class AssetFireball
	{
		public final AtlasRegion fireball;
		
		public AssetFireball (TextureAtlas atlas)
		{
			fireball = atlas.findRegion("Fireball");
		}
		
	}
	
	public class AssetFonts
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts()
		{
			//create three fonts using LibGdx's 15 px bit map font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			//create three fonts using LibGdx's 15 px bit map font
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			//create three fonts using LibGdx's 15 px bit map font
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
		
			//set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			
			//enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}
	
	
	public class AssetLevelDecoration
	{
		public final AtlasRegion character;
		public final AtlasRegion ground;
		public final AtlasRegion fireball;
		public final AtlasRegion building;
		
		public AssetLevelDecoration (TextureAtlas atlas)
		{
			character = atlas.findRegion("character Sprite");
			ground = atlas.findRegion("Cobblestone");
			fireball = atlas.findRegion("Fireball");
			building = atlas.findRegion("Office Building");
		}
	}
	
	public void error(String filename, Class type, Throwable throwable) 
	{
		Gdx.app.error(TAG, "Couldn't laod asset '" + filename + "'", (Exception)throwable);
		
	}

	@Override
	public void dispose() 
	{
		assetManager.dispose();
		fonts.defaultBig.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultSmall.dispose();
	}
	
	@Override
	public void error(AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '"+ asset.fileName + "'", (Exception)throwable);
	}
	
	/**
	 * Handles the sound assets
	 *
	 */
    public class AssetSounds 
    {
        public final Sound jump;
        public final Sound jumpWithFeather;
        public final Sound pickupCoin;
        public final Sound pickupFeather;
        public final Sound liveLost;
        public AssetSounds (AssetManager am) {
            jump = am.get("sounds/jump.wav", Sound.class);
            jumpWithFeather = am.get("sounds/jump_with_feather.wav", Sound.class);
            pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
            pickupFeather = am.get("sounds/pickup_feather.wav", Sound.class);
            liveLost = am.get("sounds/live_lost.wav", Sound.class);
        }
    }
    
    /**
     * Handles the music assets
     *
     */
    public class AssetMusic
    {
        public final Music song01;
        public AssetMusic (AssetManager am) {
            song01 = am.get("music/keith303_-_brand_new_highscore.mp3", Music.class);
        }
    }
	
}
