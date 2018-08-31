package com.mygdx.baseinfec.actors.creation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.actors.Render;
import com.mygdx.baseinfec.ui.Scaler;

/**
 * Created by seacow on 12/17/2017.
 */

public class CreateTexture implements CreateActor
{
    final private Main main;

    public CreateBody box2dBody;
    private Render renderer;
    private Sprite sprite;

    private String file;
    private float x;
    private float y;
    private float width;
    private float height;
    private float rotation;

    private CreateTexture(String name, Main main)
    {
        this.main = main;
        file = name;

        x = 0;
        y = 0;

        rotation = 0;
    }

    /**for empty/no texture box2d bodies - mostly for invisible platforms/walls*/
    public CreateTexture(BodyDef.BodyType type)
    {
        this(null, null);

        box2dBody = new CreateBody(type);
        renderer = new Render();

        file = null;
        sprite = null;
    }

    /**for texture sprites*/
    public CreateTexture(String file, Main main, BodyDef.BodyType type)
    {
        this(file, main);

        box2dBody = new CreateBody(type);
        renderer = new Render();

        sprite = new Sprite((Texture) main.assetmanager.manager.get(this.file));
    }

    public CreateTexture(String file, Main main, BodyDef.BodyType type, float rotation)
    {
        this(file, main, type);
        this.rotation = rotation;
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
        width = w / Scaler.scaleX;
        height = h / Scaler.scaleY;

        x = xPos + (width / 2);
        y = yPos + (height / 2);

        box2dBody.create(world, x, y, width, height, isSensor);
        box2dBody.body.setActive(true);

        box2dBody.body.setAngularVelocity(rotation * Gdx.graphics.getDeltaTime());
    }

    /**Only render if the file isn't null, empty box2D bodies do not need to rendered*/
    public void display()
    {
        sprite.setRotation((float)Math.toDegrees(box2dBody.body.getAngle()));
        renderer.render(main, sprite, box2dBody.body, width ,
                height);
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

    /**Sets the width as a negative value, flipping the rendered image to
     * the opposite side*/
    public void flip()
    {
        width *= -1;
    }

    public float getX()
    {
        return box2dBody.body.getPosition().x;
    }

    public float getY()
    {
        return box2dBody.body.getPosition().y;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getVelX()
    {
        return box2dBody.body.getLinearVelocity().x;
    }

    public float getVelY()
    {
        return box2dBody.body.getLinearVelocity().y;
    }

    public Body getBody()
    {
        return box2dBody.body;
    }

    public void setNoGravity()
    {
        box2dBody.body.setGravityScale(0f);
    }

    public void resetUserData()
    {
        box2dBody.resetUserData();
    }
}
