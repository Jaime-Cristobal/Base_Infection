package com.mygdx.baseinfec.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.baseinfec.BodyEditorLoader;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.actors.creation.CreateAnimation;
import com.mygdx.baseinfec.actors.creation.CreateTexture;
import com.mygdx.baseinfec.collision.FilterID;
import com.mygdx.baseinfec.collision.HitboxID;
import com.mygdx.baseinfec.ui.Scaler;


/**
 * Created by FlapJack on 7/22/2017.
 *
 * just have a lever in hud to turn the player in a direction
 *
 * Player sprites have to be facing down in order to move with the
 * direction the player's frontal area is facing.
 */

public class Player implements GestureListener, InputProcessor
{
    private final Main main;

    private CreateAnimation fire;

    private CreateTexture actor;

    private String file;
    private float originX, originY = 0;

    private int velocity = 0;

    private GestureDetector detector = new GestureDetector(this);

    private BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("player.json"));

    public Player(Main main_p)
    {
        this.main = main_p;
        //file = "player4.atlas";
        //file = "whale_player.atlas";
        file = "player12.png";

        //actor = new CreateAnimation(file, main, BodyDef.BodyType.DynamicBody);
        //actor.addRegion("Armature_move", 3.0f);

        actor = new CreateTexture(file, main, BodyDef.BodyType.DynamicBody);

        /**
         fire = new Animator("trace.atlas", main);
         fire.addRegion("trace", 4.5f);
         fire.findRegion("trace");
         fire.setOrigin(fire.getWidth() - 1, fire.getHeight() / 2);*/

        //fire = new CreateAnimation("trace.atlas", main, BodyDef.BodyType.DynamicBody);
        //fire.addRegion("trace", 4.5f);
        //fire.setData(0, 0, false);

    }

    public void createBody(World world, float posX, float posY, float w, float h)
    {
        actor.setFilter(FilterID.player_category, (short) (FilterID.floor_category |
                FilterID.ceiling_category | FilterID.enemy_category | FilterID.platform_category
                | FilterID.coin_category | FilterID.collector_category | FilterID.bandit_category));

        actor.setData(0.1f, 0, true);
        actor.setUniqueID(HitboxID.player);
        actor.create(world, posX, posY, w, h, false);
        //actor.create(world, loader, "body01", posX, posY, w, h, false);
        //actor.setRegion("Armature_move");
    }

    /**
     * NOTE: consider taking out case 4 in the switch statement
     *
     * @param playerInput is the touch input from HUD.java.
     * @param delta is the delta time from render in MapStage.java.
     *              This is the same as Gdx.graphics.getDeltaTime
     * */
    public void display(int playerInput, float delta)
    {
        movement(playerInput, delta);

        actor.getBody().setLinearVelocity(
                MathUtils.cos(actor.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * velocity,
                MathUtils.sin(actor.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * velocity);

        //fire.render(main.batch, actor.getX() - 7, actor.getY() - 0.5f, actor.getBody().getAngle() * MathUtils.radiansToDegrees);
        //fire.renderCustomBod(main.batch, actor.getX(), actor.getY(), 0, + 20, 6,
        //        actor.getBody().getAngle() * MathUtils.radiansToDegrees);
        //fire.getBody().setTransform(actor.getBody().getPosition().x, actor.getBody().getPosition().y, actor.getBody().getAngle());
        //fire.display(actor.getBody().getAngle() * MathUtils.radiansToDegrees);
        //fire.display(actor.getBody().getAngle() * MathUtils.radiansToDegrees);

        actor.display();
        //actor.display(actor.getBody().getAngle() * MathUtils.radiansToDegrees);
        //actor.displayCustom(actor.getBody().getAngle() * MathUtils.radiansToDegrees);
    }

    /**
     * Look at the comment header in the function display(...) above for
     * parameter descriptions.
     * */
    private void movement(int input, float delta)
    {
        switch (input)
        {
            default:
                break;
            case -1:        //Stops the player from turning
                actor.getBody().setAngularVelocity(0);
                break;
            case 0:         //Goes to the right
                actor.getBody().setAngularVelocity(200 * delta);
                break;
            case 1:         //Goes to the left
                actor.getBody().setAngularVelocity(-200 * delta);
                break;
            case 2:         //Slows down the player
                if(velocity > 0)
                    velocity -= 5;
                break;
            case 3:         //Speeds up the player
                velocity += 10;
                break;
            case 4:         //Instant stop
                velocity = 0;
                break;
        }

        if(velocity > 0)    //the player will naturally slow down when not speeding up
            velocity -= 1;
        else
            velocity = 0;
    }

    public Vector2 returnCoord()
    {
        return actor.getBody().getPosition();
    }

    public int getVelocity()
    {
        return velocity;
    }

    public float getAngle()
    {
        return actor.getBody().getAngle();
    }

    /**
     * Box2d physics will no longer apply to the player when called
     * */
    public void setInactive()
    {
        actor.setActive(false);
    }

    /**If you need to reset to the original position like after a screen change*/
    public void reset()
    {
        actor.setPosition(originX, originY);
    }

    public GestureDetector getGesture()
    {
        return detector;
    }

    public InputProcessor getInputProcessor()
    {
        return this;
    }

    public float getX()
    {
        return actor.getX() * Scaler.PIXELS_TO_METERS;
    }

    public Body getBody()
    {
        return actor.getBody();
    }

    public void setFilterEmpty()
    {
        actor.setFilter(FilterID.player_category, (short) 0);
    }

    public void resetFilter()
    {
        actor.setFilter(FilterID.player_category, (short) (FilterID.floor_category |
                FilterID.ceiling_category | FilterID.enemy_category | FilterID.platform_category
                | FilterID.coin_category));
    }

    public float getY()
    {
        return actor.getY() * Scaler.PIXELS_TO_METERS;
    }

    /** @see InputProcessor#touchDown(int, int, int, int) */
    public boolean touchDown (float x, float y, int pointer, int button)
    {
        return false;
    }

    /** Called when a tap occured. A tap happens if a touch went down on the screen and was lifted again without moving outside
     * of the tap square. The tap square is a rectangular area around the initial touch position as specified on construction
     * time of the {@link GestureDetector}.
     * @param count the number of taps. */
    public boolean tap (float x, float y, int count, int button)
    {
        return false;
    }

    public boolean longPress (float x, float y)
    {
        return false;
    }

    /** Called when the user dragged a finger over the screen and lifted it. Reports the last known velocity of the finger in
     * pixels per second.
     * @param velocityX velocity on x in seconds
     * @param velocityY velocity on y in seconds */
    public boolean fling (float velocityX, float velocityY, int button)
    {
        return false;
    }

    /** Called when the user drags a finger over the screen.
     * @param deltaX the difference in pixels to the last drag event on x.
     * @param deltaY the difference in pixels to the last drag event on y. */
    public boolean pan (float x, float y, float deltaX, float deltaY)
    {

        return false;
    }

    /** Called when no longer panning. */
    public boolean panStop (float x, float y, int pointer, int button)
    {
        return false;
    }

    /** Called when the user performs a pinch zoom gesture. The original distance is the distance in pixels when the gesture
     * started.
     * @param initialDistance distance between fingers when the gesture started.
     * @param distance current distance between fingers. */
    public boolean zoom (float initialDistance, float distance)
    {
        return false;
    }

    /** Called when a user performs a pinch zoom gesture. Reports the initial positions of the two involved fingers and their
     * current positions.
     * @param initialPointer1
     * @param initialPointer2
     * @param pointer1
     * @param pointer2 */
    public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
    {
        return false;
    }

    /** Called when no longer pinching. */
    public void pinchStop (){}

    /** Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyDown (int keycode)
    {
        return false;
    }

    /** Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyUp (int keycode)
    {
        return false;
    }

    /** Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed */
    public boolean keyTyped (char character)
    {
        return false;
    }

    /** Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    /** Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
        return false;
    }
    /** Called when a finger or the mouse was dragged.
     * @param pointer the pointer for the event.
     * @return whether the input was processed */
    public boolean touchDragged (int screenX, int screenY, int pointer)
    {
        return false;
    }

    /** Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     * @return whether the input was processed */
    public boolean mouseMoved (int screenX, int screenY)
    {
        return false;
    }
    /** Called when the mouse wheel was scrolled. Will not be called on iOS.
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed. */
    public boolean scrolled (int amount)
    {
        return false;
    }
}
