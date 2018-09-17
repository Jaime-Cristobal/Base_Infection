package com.mygdx.baseinfec.actors.creation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.baseinfec.ui.Scaler;

/**
 * Created by seacow on 10/27/2017.
 */

public class CreateBody
{
    private short category = 0;
    private short mask = 0;

    public Body body;
    private BodyDef.BodyType type;
    private BodyDef bodydef = new BodyDef();
    private PolygonShape shape;
    private FixtureDef fixDef = new FixtureDef();

    private float density = 0;
    private float restitution = 0;

    private Object ID = null;

    public CreateBody(BodyDef.BodyType type)
    {
        this.type = type;
    }

    /**Set filters for collision*/
    public void setFilter(short category, short mask)
    {
        this.category = category;
        this.mask = mask;
    }

    /**Manually set the body's data such as density(weight) and restitution(bounciness)
     * Call before calling create() for it to take effect*/
    public void setData(float density, float restitution, boolean rotationLock)
    {
        this.density = density;
        this.restitution = restitution;

        bodydef.fixedRotation = rotationLock;
    }

    /**This is for cases where multiple objects will have the same name file and want
     * to distinguish the UserData inside the fixtures with a unique set of ID*/
    public void setID(Object ID)
    {
        this.ID = ID;
    }

    /**Bodydef.BodyType sets whether the body tpye is static, kinematic, or dynamic
     *
     * You still have to fix the resolution scaling for data*/
    public void create(World world, float x, float y, float w, float h, boolean isSensor)
    {
        bodydef.type = type;
        bodydef.position.set(x / Scaler.PIXELS_TO_METERS,
                y / Scaler.PIXELS_TO_METERS);                  //box collision at the same dimension as the sprite

        body = world.createBody(bodydef);

        shape = new PolygonShape();
        shape.setAsBox(w / 2 / Scaler.PIXELS_TO_METERS * Scaler.scaleX,
                h / 2 / Scaler.PIXELS_TO_METERS * Scaler.scaleY);      //box collision at the same dimension as the sprite

        fixDef.shape = shape;
        fixDef.restitution = restitution;
        fixDef.density = density;
        fixDef.isSensor = isSensor;
        fixDef.filter.categoryBits = category;       //short something = CATEGORY
        fixDef.filter.maskBits = mask;

        if(ID == null)
        {
            Gdx.app.error("Class CreateBody.java", "There is no UserData ID!");
        }

        body.createFixture(fixDef).setUserData(ID);

        body.setUserData(ID);

        body.setActive(false);      //will not move if the body is not active.

        shape.dispose();
    }

    public void createCircle(World world, float x, float y, float w, float h, boolean isSensor)
    {

    }

    /**UserData will get resetted every time a map is called
     *
     * Has to be called in show(). Failure to do so will result in a null UserData in
     * class event.*/
    public void resetUserData()
    {
        body.setUserData(ID);
    }

    public Filter getFilter() {
        return fixDef.filter;
    }

    public void setActive(boolean active)
    {
        body.setActive(active);
    }
}
