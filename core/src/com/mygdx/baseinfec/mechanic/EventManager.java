package com.mygdx.baseinfec.mechanic;

import com.badlogic.gdx.physics.box2d.ContactListener;
import com.mygdx.baseinfec.collision.CollisionEvent;
import com.mygdx.baseinfec.collision.HitboxID;

/**
 * Static functions used in:
 *      - class MapStage.java -> render()
 *      - class LowMob.java -> render()
 * */

public final class EventManager
{
    private final static CollisionEvent event = new CollisionEvent();

    public static ContactListener getContactListener()
    {
        return event.getContactListener();
    }

    public static boolean conditions(Object userData1, Object userData2)
    {
        return event.ifContact(userData1, userData2);
    }
}
