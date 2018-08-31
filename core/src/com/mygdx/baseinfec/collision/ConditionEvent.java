package com.mygdx.baseinfec.collision;

/**
 * Created by seacow on 11/8/2017.
 */

public class ConditionEvent
{
    public void playerCollision(FilterDetector contact)
    {
        if(contact.feedback(FilterID.player_category, FilterID.enemy_category))
            System.out.println("They're colliding!");
    }
}
