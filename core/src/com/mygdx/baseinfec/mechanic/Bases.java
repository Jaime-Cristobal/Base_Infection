package com.mygdx.baseinfec.mechanic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.actors.creation.CreateActor;
import com.mygdx.baseinfec.actors.creation.CreateTexture;
import com.mygdx.baseinfec.collision.FilterID;

public class Bases
{
    final private Array<CreateActor> base = new Array<CreateActor>();
    private int health = 100;
    private int armor = 0;
    private int infection = 0;      //infection at 50 starts to spawn enemies
    private int energy = 100;

    public Bases(Main main, World world)
    {
        for(int n = 0; n < 4; n++)
        {
            base.add(new CreateTexture("base.png", main, BodyDef.BodyType.DynamicBody));
            base.get(n).setData(1, 0, false);
            base.get(n).setUniqueID("base" + n);
            base.get(n).create(world, MathUtils.random(800, 3200), MathUtils.random(800, 3200),
                    149, 134, false);
            base.get(n).setNoGravity();
        }
    }

    public void render()
    {
        for(CreateActor iter : base)
            iter.display();
    }

    public void changeHealth(int value)
    {
        health += value;
    }

    public void changeArmor(int value)
    {
        armor += value;
    }

    public void changeInfection(int value)
    {
        infection += value;
    }

    public void changeEnergy(int value)
    {
        energy += value;
    }

    public int getHealth()
    {
        return health;
    }

    public int getArmor()
    {
        return armor;
    }

    public int getInfection()
    {
        return infection;
    }

    public int getEnergy()
    {
        return energy;
    }

    public int getSize()
    {
        return base.size;
    }

    public Vector2 getRandomPos()
    {
        return base.get(MathUtils.random(0,3)).getBody().getPosition();
    }

    public Vector2 getPosition(int index)
    {
        return base.get(index).getBody().getPosition();
    }
}
