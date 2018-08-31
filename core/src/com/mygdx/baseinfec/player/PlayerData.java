package com.mygdx.baseinfec.player;

import com.badlogic.gdx.utils.ArrayMap;

/**
 * Created by seacow on 9/9/2017.
 */

public class PlayerData
{
    public final static String file = "player.atlas";

    public final static ArrayMap<String, Float> frames = new ArrayMap<String, Float>(3);

    public PlayerData()
    {
        frames.put("Armature_run", 1.5f, 0);
        frames.put("Armature_jump", 4.0f, 1);
        frames.put("Armature_idle", 5.3f, 2);
        frames.put("Armature_pistol", 5.0f, 3);
    }
}
