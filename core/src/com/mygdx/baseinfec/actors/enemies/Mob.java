package com.mygdx.baseinfec.actors.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.actors.creation.CreateAnimation;
import com.mygdx.baseinfec.animator.Animator;
import com.mygdx.baseinfec.collision.FilterID;
import com.mygdx.baseinfec.collision.HitboxID;
import com.mygdx.baseinfec.mechanic.EventManager;


public final class Mob
{
    final private Array<CreateAnimation> spawned = new Array<CreateAnimation>();
    private Vector2 targetPos;
    private Boolean isSpeedSet = false;
    private Animator effect;
    private boolean ifEffect = false;
    private float x, y = 0;

    public Mob(Main main, World world, int amount)
    {
        effect = new Animator("splash_01.atlas", main);
        effect.addRegion("Anim_splash", 3.4f);
        effect.findRegion("Anim_splash");

        for(int n = 0; n < amount; n++)
        {
            spawned.add(new CreateAnimation("player4.atlas", main, BodyDef.BodyType.DynamicBody));
            spawned.get(n).addRegion("Armature_animtion0", 3.4f);
            spawned.get(n).setFilter(FilterID.enemy_category, FilterID.player_category);
            spawned.get(n).setData(0.5f, 0, false);
            spawned.get(n).setUniqueID(HitboxID.lowMob + n);
            spawned.get(n).create(world, -2000, 0, 53, 43, false);
            spawned.get(n).setRegion("Armature_animtion0");
        }
    }

    /**
     * Placed between the batch.begin() and .end(). Failure to do so will result in
     * errors and sprites not being rendered on the screen.
     * */
    public void render(Main main)
    {
        if(spawned.size != 0)
        {
            for(CreateAnimation iter : spawned)
            {
                collision(iter);

                iter.display(iter.getBody().getAngle() * MathUtils.radiansToDegrees);
            }
        }

        if(ifEffect)
        {
            //Gdx.app.error("RENDERING", "EFFECT");
            effect.recordEndTime();
            effect.render(main.batch, x, y);

            if(effect.ifFinished())
            {
                //System.out.println("STOP");
                ifEffect = false;
                effect.resetTime();
            }
        }
    }

    /**
     * @param mob -> this should be from a for loop or for range iteration.
     * Variable x and y are the iteration of spawned in the func render().
     * The boolean ifEffect triggers the collision special effect sprite.
     *
     * .setTransform(...) and .setActive(...) should send the mob to outside the
     *            playing field and have its box2D physics characteristics disabled.
     * */
    private void collision(CreateAnimation mob)
    {
        if(EventManager.conditions(HitboxID.player, mob.getBody().getUserData()))
        {
            x = mob.getX();
            y = mob.getY();
            mob.getBody().setTransform(-50, 0, 0);
            mob.setActive(false);

            ifEffect = true;
        }
    }

    /**
     * This functions similar to a path finder where the mob will go in the direction of
     * where ever the target is set to.
     * The target is coordinates are set in func. setTarget(...) below.
     * */
    public void setSpeed()
    {
        for(CreateAnimation speedIter : spawned)
        {
            speedIter.getBody().setLinearVelocity(
                    MathUtils.cos(speedIter.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * 600,
                    MathUtils.sin(speedIter.getBody().getAngle()) * Gdx.graphics.getDeltaTime() * 600);

            //speedIter.getBody().setAngularVelocity(MathUtils.atan2(target.y - speedIter.getBody().getPosition().y,
            //        target.x - speedIter.getBody().getPosition().x));


            speedIter.getBody().setTransform(speedIter.getBody().getPosition(),
                    MathUtils.atan2(targetPos.y - speedIter.getBody().getPosition().y,
                            targetPos.x - speedIter.getBody().getPosition().x));
            }
    }

    /**
     * Takes the (x, y) coordinates of the target.
     * */
    public void setTarget(Vector2 targetPos)
    {
        this.targetPos = targetPos;
    }

    /**
     * Will spawn randomly between the starting and ending values for both x and y.
     * @param amount dictates how many will be spawned. This value should not exceed the
     *               amount created in the constructor.
     * */
    public void spawnRandomly(int startX, int endX, int startY, int endY, int amount)
    {
        if(amount > spawned.size)
        {
            Gdx.app.error("Mob.java", "Amount needed to spawn exceeds the amount created!");
            return;
        }

        for (int n = 0; n < amount; n++)
        {
            spawned.get(n).setPosition(MathUtils.random(startX, endX),
                    MathUtils.random(startY, endY));
            spawned.get(n).setActive(true);
        }
    }

    /**
     * Will spawn precisely on the coordinates given.
     * @param amount dictates how many will be spawned. This value should not exceed the
     *               amount created in the constructor.
     * */
    public void spawn(float posX, float posY, int amount)
    {
        if(amount > spawned.size)
        {
            Gdx.app.error("Mob.java", "Amount needed to spawn exceeds the amount created!");
            return;
        }

        for (int n = 0; n < amount; n++)
        {
            spawned.get(n).setPosition(posX, posY);
            spawned.get(n).setActive(true);
        }
    }

    /**
     * WARNING: Untested function.
     * */
    public void remove()
    {
        spawned.get(spawned.size - 1).setSpeed(0, 0);
        spawned.get(spawned.size - 1).setActive(false);
    }
}
