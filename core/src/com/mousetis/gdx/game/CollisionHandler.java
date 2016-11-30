package com.mousetis.gdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.ObjectMap;
import com.mousetis.gdx.game.objects.Ground;
import com.mousetis.gdx.game.WorldController;
import com.mousetis.gdx.game.Assets.Assets;
import com.mousetis.gdx.game.objects.AbstractGameObject;
import com.mousetis.gdx.game.objects.Character.JUMP_STATE;
import com.mousetis.gdx.game.objects.Character;

public class CollisionHandler implements ContactListener
{
    private ObjectMap<Short, ObjectMap<Short, ContactListener>> listeners;

    private WorldController world;

    /**
     * constructor for collision handler
     * @param w
     */
    public CollisionHandler(WorldController w)
    {
    	world = w;
        listeners = new ObjectMap<Short, ObjectMap<Short, ContactListener>>();
    }

    /**
     * adds a listener to a object
     * @param categoryA
     * @param categoryB
     * @param listener
     */
    public void addListener(short categoryA, short categoryB, ContactListener listener)
    {
        addListenerInternal(categoryA, categoryB, listener);
        addListenerInternal(categoryB, categoryA, listener);
    }

    /**
     * initiates the contact between fixtures
     */
    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        //Gdx.app.log("CollisionHandler-begin A", "begin");

       // processContact(contact);

        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.beginContact(contact);
        }
    }

    /**
     * ends the contact between the objects
     */
    @Override
    public void endContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

       // Gdx.app.log("CollisionHandler-end A", "end");
        processContact(contact);

        // Gdx.app.log("CollisionHandler-end A", fixtureA.getBody().getLinearVelocity().x+" : "+fixtureA.getBody().getLinearVelocity().y);
        // Gdx.app.log("CollisionHandler-end B", fixtureB.getBody().getLinearVelocity().x+" : "+fixtureB.getBody().getLinearVelocity().y);
        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.endContact(contact);
        }
    }

    /**
     * determines the contact before the collision
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.preSolve(contact, oldManifold);
        }
    }

    /**
     * solves the contract after collision
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.postSolve(contact, impulse);
        }
    }

    /**
     * adds a listener for a type of collision
     * @param categoryA
     * @param categoryB
     * @param listener
     */
    private void addListenerInternal(short categoryA, short categoryB, ContactListener listener)
    {
        ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
        if (listenerCollection == null)
        {
            listenerCollection = new ObjectMap<Short, ContactListener>();
            listeners.put(categoryA, listenerCollection);
        }
        listenerCollection.put(categoryB, listener);
    }

    /**
     * constructor for the contact listener
     * @param categoryA
     * @param categoryB
     * @return
     */
    private ContactListener getListener(short categoryA, short categoryB)
    {
        ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
        if (listenerCollection == null)
        {
            return null;
        }
        return listenerCollection.get(categoryB);
    }

    /**
     * processes a type of contact
     * @param contact
     */
    private void processContact(Contact contact)
    {
    	Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        AbstractGameObject objA = (AbstractGameObject)fixtureA.getBody().getUserData();
        AbstractGameObject objB = (AbstractGameObject)fixtureB.getBody().getUserData();

        if (objA instanceof Character)
        {
        	processPlayerContact(fixtureA, fixtureB);
        }
        else if (objB instanceof Character)
        {
        	processPlayerContact(fixtureB, fixtureA);
        }
    }

    /**
     * handles the collision between the player and an object
     * @param playerFixture
     * @param objFixture
     */
    private void processPlayerContact(Fixture playerFixture, Fixture objFixture)
    {
    	if (objFixture.getBody().getUserData() instanceof Ground)
    	{
    		Character character = (Character)playerFixture.getBody().getUserData();
    		character.acceleration.y = 0;
    	    character.velocity.y = 0;
    	    character.jumpState = JUMP_STATE.GROUNDED;
    	    playerFixture.getBody().setLinearVelocity(character.velocity);
    	}
    	else if (objFixture.getBody().getUserData() instanceof Ground)
    	{
    		// Remove the block update the player's score by 1.
    		world.score++;
    		/**
    		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
    		AudioManager.instance.play(Assets.instance.sounds.jump);
    		AudioManager.instance.play(Assets.instance.sounds.liveLost);
			*/
    		Ground block = (Ground)objFixture.getBody().getUserData();
    		world.flagForRemoval(block);
    	}
    }

}