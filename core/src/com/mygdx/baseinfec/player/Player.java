package com.mygdx.baseinfec.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.actors.creation.CreateAnimation;
import com.mygdx.baseinfec.actors.creation.CreateTexture;
import com.mygdx.baseinfec.collision.FilterDetector;
import com.mygdx.baseinfec.collision.FilterID;
import com.mygdx.baseinfec.ui.Scaler;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;


/**
 * Created by FlapJack on 7/22/2017.
 *
 * just have a lever in hud to turn the player in a direction
 */

public class Player implements GestureListener, InputProcessor
{
    private final Main main;

    private CreateAnimation actor;
    private ArrayMap<String, Float> region = new ArrayMap<String, Float>();
    private InputControl controls;

    private ArrayMap<String, Float> frames;

    private OrthographicCamera camera;

    private String file;
    private float originX;
    private float originY;

    private int velocity = 0;

    private int lastInput = -1;

    private float xFirstTouch = 0;
    private float yFirstTouch = 0;

    float deltaX = 0;
    float deltaY = 0;
    float rotation = 0;

    private float touchX = 0;
    private int x = 100;

    private float angle = 100;

    private GestureDetector detector = new GestureDetector(this);

    public Player(Main main_p)
    {
        this.main = main_p;
        file = "player4.atlas";

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        controls = new InputControl();

        originX = 0;
        originY = 0;

        frames = new ArrayMap<String, Float>();
        frames.put("Armature_down", 3.4f, 0);

        region.put("Armature_animtion0", 3.5f, 0);

        actor = new CreateAnimation(file, region, main, BodyDef.BodyType.DynamicBody);
    }

    public void createBody(World world, float posX, float posY, float w, float h)
    {
        actor.setFilter(FilterID.player_category, (short) (FilterID.floor_category |
                FilterID.ceiling_category | FilterID.enemy_category | FilterID.platform_category
                | FilterID.coin_category | FilterID.collector_category | FilterID.bandit_category));

        actor.setData(0.5f, 0, true);
        actor.setUniqueID(1);
        actor.create(world, posX, posY, w, h, false);
        actor.setRegion("Armature_animtion0");
        //actor.setRegion("Armature_down");
    }

    public void display(int playerInput, float delta, int rotate, int speed, Vector3 target, int direc)
    {
        switch (playerInput)
        {
            default:
                break;
            case -1:
                actor.getBody().setAngularVelocity(0);
                break;
            case 0:
                actor.getBody().setAngularVelocity(200 * delta);
                break;
            case 1:
                actor.getBody().setAngularVelocity(-200 * delta);
                break;
            case 2:
                if(velocity > 0)
                    velocity -= 5;
                break;
            case 3:
                velocity += 10;
                break;
            case 4:
                velocity = 0;
                break;
        }

        if(velocity > 0)
            velocity -= 1;
        else
            velocity = 0;

        //System.out.println(velocity);
        actor.getBody().setLinearVelocity(MathUtils.cos(actor.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * velocity,
                MathUtils.sin(actor.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * velocity);

        actor.display();
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

    public void applyJump()
    {
        actor.applyForce(0, 60);
    }

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

    public Vector2 getInputPosition()
    {
        return controls.getPosition();
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
        //if(count == 2)
        //    actor.getBody().setLinearVelocity(MathUtils.cos(actor.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * 500,
        //            MathUtils.sin(actor.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * 500);

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
        //System.out.println("CL");
        //actor.getBody().setAngularVelocity(-rotation * Gdx.graphics.getDeltaTime());

        return true;
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
        touchX = screenX / Scaler.PIXELS_TO_METERS;
        //System.out.println("touchdown");
        //System.out.println(pointer);

        //xFirstTouch = screenX;
        //yFirstTouch = screenY;
        //Gdx.app.log("" + xFirstTouch, "" + yFirstTouch);
        //System.out.println("touch");
        //rotation *= -1;     //Use this to flip direction to spin

        return true;
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
        //System.out.println("DRAGGING");
        //actor.getBody().setLinearVelocity(0, 0);
        touchX = screenX / Scaler.PIXELS_TO_METERS;

        //deltaX = (screenX / Scaler.PIXELS_TO_METERS) - actor.getX();
        //deltaY = (screenY / Scaler.PIXELS_TO_METERS) - actor.getY();
        //rotation = MathUtils.atan2(deltaY, deltaX);

        /**
        if(screenX / Scaler.PIXELS_TO_METERS < 20)
        {
            rotation *= -1;
            rotation += 0.5f;
        }
        else
            rotation -= 0.5f;
         */

        //int x = 100;

        /**
        if(screenX / Scaler.PIXELS_TO_METERS < 20)
        {
            x *= -1;
        }

        actor.getBody().setAngularVelocity(x * Gdx.graphics.getDeltaTime());
         */

        //Gdx.app.log("" + screenX / Scaler.PIXELS_TO_METERS, "" + actor.getX());

        //rotVal += 1.2f;
        //System.out.println(rotation);
        //System.out.println(screenX / Scaler.PIXELS_TO_METERS);

        //if(rotVal < 0)
        //    rotVal += 1.28;

        //actor.getBody().setAngularVelocity(screenX / Scaler.PIXELS_TO_METERS);
        //actor.getBody().setAngularVelocity(rotation * Gdx.graphics.getDeltaTime() * 100);
        //actor.getBody().setTransform(actor.getBody().getPosition(), -rotVal);

        return true;
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
