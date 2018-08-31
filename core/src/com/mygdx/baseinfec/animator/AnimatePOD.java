package com.mygdx.baseinfec.animator;

import com.badlogic.gdx.utils.ArrayMap;

/**
 * Created by seacow on 12/14/2017.
 */

public class AnimatePOD
{
    public static ArrayMap<String, Float> region;

    static public void create()
    {
        region = new ArrayMap<String, Float>();
        region.put("Armature_fly", 2.0f, 0);
    }
}
