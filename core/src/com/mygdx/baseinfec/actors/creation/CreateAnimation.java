package com.mygdx.baseinfec.actors.creation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.animator.Animator;
import com.mygdx.baseinfec.ui.Scaler;

/**
 * Created by seacow on 12/17/2017.
 */

public class CreateAnimation implements CreateActor
{
    final private Main main;

    private CreateBody box2dBody;
    private Animator animate;

    private String file;
    private float x;
    private float y;
    private float width;
    private float height;

    private CreateAnimation(String name, Main main)
    {
        this.main = main;
        file = name;

        x = 0;
        y = 0;
    }

    public CreateAnimation(String file, ArrayMap<String, Float> region, Main main, BodyDef.BodyType type)
    {
        this(file, main);

        box2dBody = new CreateBody(type);
        animate = new Animator(this.file, region, main.assetmanager.manager);
    }

    /**Use this with a contact filtering class*/
    public void setFilter(short category, short mask)
    {
        box2dBody.setFilter(category, mask);
    }

    /**Manually set the body's density(heaviness) and restitution(bounciness)
     * values.
     * The default values are density = 0 and restitution = 0.1f*/
    public void setData(float density, float resitution, boolean lockRotation)
    {
        box2dBody.setData(density, resitution, lockRotation);
    }

    /**For IDing a container of objects containing the same name file. Have the same
     * name files means they all have same UserData ID*/
    public void setUniqueID(Object ID)
    {
        box2dBody.setID(ID);
    }

    public void create(World world, float xPos, float yPos, float w, float h, boolean isSensor)
    {
        animate.setScale(w, h);
        width = animate.getWidth();
        height = animate.getHeight();

        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.create(world, x, y, width / Scaler.scaleX, height / Scaler.scaleY, isSensor);
        box2dBody.body.setActive(true);
    }

    public void display()
    {
        animate.render(main.batch, box2dBody.body.getPosition().x, box2dBody.body.getPosition().y);
    }

    /**false sets the box2D body to sleep (unmovable)*/
    public void setActive(boolean active)
    {
        box2dBody.body.setActive(active);
    }

    public boolean isActive()
    {
        return box2dBody.body.isActive();
    }

    /**speed is set on a x and y axis plane*/
    public void setSpeed(float xSpeed, float ySpeed)
    {
        box2dBody.body.setLinearVelocity(xSpeed * Gdx.graphics.getDeltaTime(),
                ySpeed * Gdx.graphics.getDeltaTime());
    }

    public void applyForce(float xVal, float yVal)
    {
        box2dBody.body.applyLinearImpulse(xVal, yVal, getX(), getY(), true);
    }

    /**sets the position to whatever the values of xPos and yPos are*/
    public void setPosition(float xPos, float yPos)
    {
        box2dBody.body.setTransform(xPos / Scaler.PIXELS_TO_METERS,
                yPos / Scaler.PIXELS_TO_METERS, box2dBody.body.getAngle());
    }

    public float getX()
    {
        return box2dBody.body.getPosition().x;
    }

    public float getY()
    {
        return box2dBody.body.getPosition().y;
    }

    public float getVelX()
    {
        return box2dBody.body.getLinearVelocity().x;
    }

    public float getVelY()
    {
        return box2dBody.body.getLinearVelocity().y;
    }

    public float getWidth()
    {
        return animate.getWidth();
    }

    public float getHeight()
    {
        return animate.getHeight();
    }

    public Body getBody()
    {
        return box2dBody.body;
    }

    /**Changes the animation played on the screen*/
    public void setRegion(String key)
    {
        animate.findRegion(key);
    }

    public void flip()
    {
        animate.flip(0);
    }

    public void setNoGravity()
    {
        box2dBody.body.setGravityScale(0);
    }

    public void resetUserData()
    {
        box2dBody.resetUserData();
    }
}
