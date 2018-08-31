package com.mygdx.baseinfec.actors.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.actors.creation.CreateActor;
import com.mygdx.baseinfec.actors.creation.CreateTexture;

public final class LowMob
{
    final private Array<CreateActor> storage = new Array<CreateActor>();
    final private Array<CreateActor> spawned = new Array<CreateActor>();
    private Vector2 targetPos;
    private Boolean isSpeedSet = false;

    public LowMob(Main main, World world, int amount)
    {
        for(int n = 0; n < amount; n++)
        {
            storage.add(new CreateTexture("player3.png", main, BodyDef.BodyType.DynamicBody));
            storage.get(n).setData(0.5f, 0, false);
            storage.get(n).setUniqueID("enemy" + n);
            storage.get(n).create(world, -2000, 0, 34, 24, false);
        }
    }

    public void render()
    {
        //setSpeed();

        if(spawned.size != 0)
        {
            for(CreateActor iter : spawned)
            {
                iter.display();
                //iter.setSpeed(MathUtils.random(-1000, 1000), MathUtils.random(-1000, 1000));
            }
        }
    }

    private void ai()
    {

    }

    public void setSpeed()
    {
        //if(!isSpeedSet)
            for(CreateActor speedIter : spawned)
            {
                speedIter.getBody().setLinearVelocity(MathUtils.cos(speedIter.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * 600,
                        MathUtils.sin(speedIter.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * 600);

                //speedIter.getBody().setAngularVelocity(MathUtils.atan2(target.y - speedIter.getBody().getPosition().y,
                //        target.x - speedIter.getBody().getPosition().x));


                speedIter.getBody().setTransform(speedIter.getBody().getPosition(),
                        MathUtils.atan2(targetPos.y - speedIter.getBody().getPosition().y,
                                targetPos.x - speedIter.getBody().getPosition().x));
            }

        //isSpeedSet = true;
    }

    public void setTarget(Vector2 targetPos)
    {
        /**
        for(CreateActor iter : spawned)
        {
            int n = MathUtils.random(0, targetPos.getSize() - 1);

            iter.getBody().setTransform(iter.getBody().getPosition(),
                    MathUtils.atan2(targetPos.getPosition(n).y - iter.getBody().getPosition().y,
                            targetPos.getPosition(n).x - iter.getBody().getPosition().x));
        }
         */

        this.targetPos = targetPos;
    }

    public void spawnRandomly(int startX, int endX, int startY, int endY, int amount)
    {
        if(amount == spawned.size)
            return;

        if(amount < spawned.size)       //if less enemies are being spawned
        {
            for(int n = 0; n < spawned.size; n++)
                storage.add(spawned.pop());
            spawned.clear();
        }
        else                            //more enemies spawned on the map
        {
            for (int n = 0; n < amount; n++) {
                spawned.add(storage.pop());
                spawned.get(spawned.size - 1).setPosition(MathUtils.random(startX, endX),
                        MathUtils.random(startY, endY));
                spawned.get(n).setActive(true);
            }
        }
    }

    public void spawn(float posX, float posY, int amount)
    {
        if(amount == spawned.size)
            return;

        if(amount < spawned.size)       //if less enemies are being spawned
        {
            for(int n = 0; n < spawned.size; n++)
                storage.add(spawned.pop());
            spawned.clear();
        }
        else                            //more enemies spawned on the map
        {
            for (int n = 0; n < amount; n++) {
                spawned.add(storage.pop());
                spawned.get(spawned.size - 1).setPosition(posX, posY);
                spawned.get(n).setActive(true);
            }
        }
    }

    public void remove()
    {
        storage.add(spawned.pop());
        storage.get(storage.size - 1).setSpeed(0, 0);
        storage.get(storage.size - 1).setActive(false);
    }
}
