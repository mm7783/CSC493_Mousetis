package com.mousetis.gdx.screens;

 import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.InputProcessor;
 import com.badlogic.gdx.Screen;
 import com.badlogic.gdx.graphics.GL20;
 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.graphics.g2d.TextureAtlas;
 import com.badlogic.gdx.scenes.scene2d.Actor;
 import com.badlogic.gdx.scenes.scene2d.Stage;
 import com.badlogic.gdx.scenes.scene2d.ui.Button;
 import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
 import com.badlogic.gdx.scenes.scene2d.ui.Image;
 import com.badlogic.gdx.scenes.scene2d.ui.Label;
 import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
 import com.badlogic.gdx.scenes.scene2d.ui.Skin;
 import com.badlogic.gdx.scenes.scene2d.ui.Slider;
 import com.badlogic.gdx.scenes.scene2d.ui.Stack;
 import com.badlogic.gdx.scenes.scene2d.ui.Table;
 import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
 import com.badlogic.gdx.scenes.scene2d.ui.Window;
 import com.badlogic.gdx.scenes.scene2d.ui.*;
 import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
 import com.badlogic.gdx.utils.Array;
 import com.badlogic.gdx.utils.viewport.StretchViewport;
 import com.mousetis.gdx.game.Assets.Assets;
 import com.mousetis.gdx.game.AudioManager;
 import com.mousetis.gdx.game.Constants;
 import com.mousetis.gdx.game.GamePreferences;
 import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
 import com.badlogic.gdx.math.Interpolation;
 import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
 import com.badlogic.gdx.scenes.scene2d.Touchable;
 
 /**
 * @author Matt Mousetis
 * 
 */

public class MenuScreen extends AbstractGameScreen
{
	private static final String TAG = MenuScreen.class.getName();
	
    // menu
    private Image imgBackground;
    private Image imgLogo;
    private Image imgInfo;
    private Image imgCoins;
    private Image imgBunny;
    private Button btnMenuPlay;
    private Button btnMenuOptions;
    // options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private SelectBox<CharacterSkin> selCharSkin;
    private Image imgCharSkin;
    private CheckBox chkShowFpsCounter;
   
    private Skin skinLibgdx;
    
    //debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;
	
    private Stage stage;
    private Skin skinCanyonBunny;
    
	/**
	 * constructor
	 * @param game
	 */
	public MenuScreen (Game game)
	{
		super(game);
	}
	
	
	/**
	 * renders the screen based on delta time
	 * @param deltaTime
	 */
	@Override
	public void render(float deltaTime)
	{
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (debugEnabled) {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }

        stage.act(deltaTime);
        stage.setDebugAll(true);
        stage.draw();
	}

	/**
	 * resizes the image when necessary
	 * @param width height
	 */
	@Override
	public void resize(int width, int height) 
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isTouched())
			game.setScreen(new GameScreen(game));
		
		stage.getViewport().update(width, height, true);
	}

	/**
	 * adds to the stage what needs to be displayed when the menu is pulled up
	 * 
	 */
	@Override
	public void show() 
	{
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
	}

	/**
	 * hides what needs to be hidden when the menu is pulled pup
	 * 
	 */
	@Override
	public void hide() 
	{
		stage.dispose();
		skinCanyonBunny.dispose();
		skinLibgdx.dispose();
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputProcessor getInputProcessor()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * rebuilds the stage when the menu is exited
	 */
    private void rebuildStage() 
    {
        skinCanyonBunny = new Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        Skin skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_LIBGDX_UI));

        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();

		// assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);
    }
    
    /**
     * builds the options window layer
     * @return win options
     */
	private Table buildOptionsWindowLayer() 
	{
	      winOptions = new Window("Options", skinLibgdx);
	        // + Audio Settings: Sound/Music CheckBox and Volume Slider
	        winOptions.add(buildOptWinAudioSettings()).row();
	        // + Character Skin: Selection Box (White, Gray, Brown)
	        winOptions.add(buildOptWinSkinSelection()).row();
	        // + Debug: Show FPS Counter
	        winOptions.add(buildOptWinDebug()).row();
	        // + Separator and Buttons (Save, Cancel)
	        winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

	        // Make options window slightly transparent
	        winOptions.setColor(1, 1, 1, 0.8f);
	        //hide options window by default
	        showOptionsWindow(false, false);
	        // Hide options window by default
	        winOptions.setVisible(false);
	        if (debugEnabled)
	            winOptions.debug();
	        // Let TableLayout recalculate widget sizes and positions
	        winOptions.pack();
	        // Move options window to bottom right corner
	        winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
		return winOptions;
	}


	/**
	 * builds the controls into the new menu
	 * 
	 */
	private Table buildControlsLayer() 
	{
        Table layer = new Table();
        layer.right().bottom();
        // + Play Button
        btnMenuPlay = new Button(skinCanyonBunny, "play");
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }


			}
        );
        layer.row();
        // + Options Button
        btnMenuOptions = new Button(skinCanyonBunny, "options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }


        });
        if (debugEnabled)
            layer.debug();
        return layer;
	}

	/**
	 * sets the game screen when the play button is clicked
	 * 
	 */
	private void onPlayClicked() 
	{
		game.setScreen(new GameScreen(game));
	}
	
	private void onOptionsClicked()
	{
		loadSettings();
		showMenuButtons(false);
		showOptionsWindow(true, true);
	}
	
	/**
	 * builds the logo layer in the men UI
	 * 
	 */
	private Table buildLogosLayer() 
	{
		   Table layer = new Table();
	        layer.left().top();
	        // + Game Logo
	        imgLogo = new Image(skinCanyonBunny, "logo");
	        layer.add(imgLogo);
	        layer.row().expandY();
	        // + Info Logos
	        imgInfo = new Image(skinCanyonBunny, "info");
	        layer.add(imgInfo).bottom();
	        if (debugEnabled)
	            layer.debug();
	        return layer;
	}


	/**
	 * builds the objects layer of the menu
	 * 
	 */
    private Table buildObjectsLayer() {
        Table layer = new Table();
        // + Coins
        imgCoins = new Image(skinCanyonBunny, "coins");
        layer.addActor(imgCoins);
        imgCoins.setOrigin(imgCoins.getWidth() / 2,
                imgCoins.getHeight() / 2);
        imgCoins.addAction(sequence(
                moveTo(135, -20),
                scaleTo(0, 0),
                fadeOut(0),
                delay(2.5f),
                parallel(moveBy(0, 100, 0.5f, Interpolation.swingOut),
                        scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
                        alpha(1.0f, 0.5f))));
        // + Bunny
        imgBunny = new Image(skinCanyonBunny, "bunny");
        layer.addActor(imgBunny);
        imgBunny.addAction(sequence(moveTo(655, 510),
                delay(4.0f),
                moveBy(-70, -100, 0.5f, Interpolation.fade),
                moveBy(-100, -50, 0.5f, Interpolation.fade),
                moveBy(-150, -300, 1.0f, Interpolation.elasticIn)));
        return layer;
    }

	private Table buildBackgroundLayer() {
		Table layer = new Table();
		return layer;
	}
	
	/**
	 * loads the settings for the menu screen
	 */
    private void loadSettings() 
    {
        GamePreferences prefs = GamePreferences.instance;
        prefs.load();
        chkSound.setChecked(prefs.sound);
        sldSound.setValue(prefs.volSound);
        chkMusic.setChecked(prefs.music);
        sldMusic.setValue(prefs.volMusic);
        selCharSkin.setSelectedIndex(prefs.charSkin);
        onCharSkinSelected(prefs.charSkin);
        chkShowFpsCounter.setChecked(prefs.showFpsCounter);
    }
    



	/**
     * saves the settings for the menu screen
     */
    private void saveSettings() 
    {
        GamePreferences prefs = GamePreferences.instance;
        prefs.sound = chkSound.isChecked();
        prefs.volSound = sldSound.getValue();
        prefs.music = chkMusic.isChecked();
        prefs.volMusic = sldMusic.getValue();
        prefs.charSkin = selCharSkin.getSelectedIndex();
        prefs.showFpsCounter = chkShowFpsCounter.isChecked();
        prefs.save();
    }
    
	/**
     * handles when character skin is selected
     */
    private void onCharSkinSelected(int index) 
    {
    	CharacterSkin skin = CharacterSkin.values()[index];
		imgCharSkin.setColor(skin.getColor());
	}
    
	/**
     * handles what happens when the save button is clicked
     */
    private void onSaveClicked()
    {
    	saveSettings();
    	onCancelClicked();
    	AudioManager.instance.onSettingsUpdated();
    }

	/**
     * handles what happens when the cancel button is clicked
     */
	private void onCancelClicked() 
	{
		showMenuButtons(true);
		showOptionsWindow(false, true);
		AudioManager.instance.onSettingsUpdated();
	}
	
    /**
     * builds the audio settings in the options window
     * @return table
     */
	private Table buildOptWinAudioSettings() 
    {
        Table tbl = new Table();
        // + Title: "Audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Sound" label, sound volume slider
        chkSound = new CheckBox("", skinLibgdx);
        tbl.add(chkSound);
        tbl.add(new Label("Sound", skinLibgdx));
        sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldSound);
        tbl.row();
        // + Checkbox, "Music" label, music volume slider
        chkMusic = new CheckBox("", skinLibgdx);
        tbl.add(chkMusic);
        tbl.add(new Label("Music", skinLibgdx));
        sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;
    }
	
	/**
	 * builds the skin selection options in skin selection menu
	 * @return table
	 */
    private Table buildOptWinSkinSelection() 
    {
        Table tbl = new Table();
        // + Title: "Character Skin"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Character Skin", skinLibgdx, "default-font", Color.ORANGE)).colspan(2);
        tbl.row();
        // + Drop down box filled with skin items
        selCharSkin = new SelectBox<CharacterSkin>(skinLibgdx);
        
        selCharSkin.setItems(CharacterSkin.values());

        selCharSkin.addListener(new ChangeListener() 
        {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCharSkinSelected(((SelectBox<CharacterSkin>) actor).getSelectedIndex());
            }
        });
        tbl.add(selCharSkin).width(120).padRight(20);
        // + Skin preview image
        imgCharSkin = new Image(Assets.instance.bunny.head);
        tbl.add(imgCharSkin).width(50).height(50);
        return tbl;
    }
    
    /**
     * builds the options window debug
     * @return
     */
    private Table buildOptWinDebug() 
    {
        Table tbl = new Table();
        // + Title: "Debug"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Show FPS Counter" label
        chkShowFpsCounter = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Show FPS Counter", skinLibgdx));
        tbl.add(chkShowFpsCounter);
        tbl.row();
        return tbl;
    }
    
    /**
     * builds the options window buttons
     * @return table
     */
    private Table buildOptWinButtons() 
    {
        Table tbl = new Table();
        // + Separator
        Label lbl;
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
        tbl.row();
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.5f, 0.5f, 0.5f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();
        // + Save Button with event handler
        btnWinOptSave = new TextButton("Save", skinLibgdx);
        tbl.add(btnWinOptSave).padRight(30);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
        // + Cancel Button with event handler
        btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
        tbl.add(btnWinOptCancel);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCancelClicked();
            }
        });
        return tbl;
    }
    
    /**
     * displays the menu buttons
     * @param visible
     */
    private void showMenuButtons(boolean visible) {
        float moveDuration = 1.0f;
        Interpolation moveEasing = Interpolation.swing;
        float delayOptionsButton = 0.25f;
        float moveX = 300 * (visible ? -1 : 1);
        float moveY = 0 * (visible ? -1 : 1);
        final Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
        btnMenuPlay.addAction(moveBy(moveX, moveY, moveDuration, moveEasing));
        btnMenuOptions.addAction(sequence(delay(delayOptionsButton),
                moveBy(moveX, moveY, moveDuration, moveEasing)));
        SequenceAction seq = sequence();
        if (visible)
            seq.addAction(delay(delayOptionsButton + moveDuration));
        seq.addAction(run(new Runnable() {
            public void run() {
                btnMenuPlay.setTouchable(touchEnabled);
                btnMenuOptions.setTouchable(touchEnabled);
            }
        }));
        stage.addAction(seq);
    }
    
    /**
     * shows the option window when clicked
     * @param visible
     * @param animated
     */
    private void showOptionsWindow(boolean visible, boolean animated) 
    {
        float alphaTo = visible ? 0.8f : 0.0f;
        float duration = animated ? 1.0f : 0.0f;
        Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
        winOptions.addAction(sequence(
                touchable(touchEnabled),
                alpha(alphaTo, duration)));
    }
}
