package com.mygdx.baseinfec.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.actors.creation.CreateActor;
import com.mygdx.baseinfec.actors.creation.CreateTexture;

/**
 * Created by seacow on 12/17/2017.
 *
 * This will handle the overall movement
 */

public class TextureActor implements GameActor
{
    final private Main main;
    private Array<CreateActor> actors;
    private Movement movement;
    private BodyDef.BodyType bodyType;

    private String file;
    private float xMin;
    private float xMax;
    private float yMin;
    private float yMax;

    private short objectHex;
    private short playerHex;

    private float density;
    private float resitution;

    public TextureActor(String file, Main main)
    {
        this.main = main;
        this.file = file;
        actors = new Array<CreateActor>();

        objectHex = 0x0000;
        playerHex = 0x0000;

        movement = new Movement();
        movement.setDirection(0, 1);

        bodyType = BodyDef.BodyType.DynamicBody;

        density = 0;
        resitution = 0.1f;
    }

    /**Please use these to randomize by using the LibGdx MathUtensil.random*/
    public void setSpawn(float xmin, float xmax, float ymin, float ymax)
    {
        xMin = xmin;
        xMax = xmax;
        yMin = ymin;
        yMax = ymax;

        movement.setSpawn(xMin, xMax, yMin, yMax);
    }

    /**Manually set the body's density(heaviness) and restitution(bounciness)
     * values.
     * The default values are density = 0 and restitution = 0.1f*/
    public void setData(float dens, float resti, BodyDef.BodyType type)
    {
        density = dens;
        resitution = resti;
        bodyType = type;
    }

    public void setFilter(short hex1, short hex2)
    {
        objectHex = hex1;
        playerHex = hex2;
    }

    /**Because we like controlling when our objects respawn*/
    public void setLimit(float value, boolean isVertical)
    {
        movement.setLimit(value);
        if(isVertical)
        {
            movement.setDirection(1, 0);
        }
    }

    /***
     * DO NOT CALL this without calling setSpawn, setFilter, AND setResolution
     * beforehand. If you do, you are a bad mannnnnnnnn.*/
    public void create(World world, Object ID, float width, float height, int amount, boolean isSensor)
    {
        for(int n = 0; n < amount; n++)
        {
            actors.add(new CreateTexture(file, main, bodyType));  //This creates ensures there's an actor
            actors.get(n).setFilter(objectHex, playerHex);
            actors.get(n).setData(density, resitution, true);
            actors.get(n).setUniqueID(ID);
            actors.get(n).create(world, MathUtils.random(xMin, xMax),
                    MathUtils.random(yMin, yMax), width, height, isSensor);
        }
    }

    public void displayAll(float speed)
    {
        for(CreateActor iter : actors)
        {
            iter.display();
            movement.move(iter, speed);
        }
    }

    public void display(float speed, int amount)
    {
        int n = 0;

        for(CreateActor iter : actors)
        {
            if(n == amount)
                return;

            iter.display();
            movement.move(iter, speed);

            n++;
        }
    }

    public void setNoGravity()
    {
        for(CreateActor iter : actors)
        {
            iter.setNoGravity();
        }
    }

    public Object getUserData()
    {
        return actors.get(0).getBody().getUserData();
    }
}
