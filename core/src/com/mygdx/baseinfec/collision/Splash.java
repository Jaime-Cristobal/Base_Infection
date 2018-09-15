package com.mygdx.baseinfec.collision;

import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.animator.Animator;
import com.mygdx.baseinfec.ui.Scaler;

/**
 * Created by seacow on 2/17/2018.
 */

public class Splash
{
    final private Main main;
    final private Animator splash;
    final private ArrayMap<String, Float> region;

    private Object playerData;
    private Object platformData;

    private boolean animate;
    private boolean stopAnimate;
    private FilterDetector detector;

    public Splash(Main mainParam)
    {
        this.main = mainParam;

        region = new ArrayMap<String, Float>();
        region.put("Armature_splash", 3.7f);

        splash = new Animator("splash.atlas", main);
        splash.setRegion(region);
        splash.setScale(44, 21);
        splash.findRegion("Armature_splash");

        animate = false;
        stopAnimate = true;
    }

    public void setDetector(FilterDetector detector)
    {
        this.detector = detector;
    }

    public void render(float playerPosX, float playerPosY)
    {
        splash.recordEndTime();
        //collision();
        //splash.outValues();


        /**
        if(splash.ifFrameEnd() && animate)
        {
            System.out.println("GARARAR");
            animate = false;
            splash.resetTime();
            //stopAnimate = true;
        }*/
        //if(animate && !splash.ifFrameEnd() && !stopAnimate)
        if(detector.feedback(FilterID.player_category, FilterID.platform_category))
        {
            animate = true;
        }

        if(animate && !splash.ifFrameEnd())
        {
            //System.out.println("It's happening");
            splash.render(main.batch, playerPosX / Scaler.PIXELS_TO_METERS, playerPosY / Scaler.PIXELS_TO_METERS - 1.5f);
            //canAnimate = false;
        }
        else
        {
            animate = false;
            splash.resetTime();
        }

        /**
        if(animate && !splash.ifFrameEnd() && !stopAnimate)
        {
            System.out.println("It's happening");
            splash.render(main.batch, playerPosX / Scaler.PIXELS_TO_METERS, playerPosY / Scaler.PIXELS_TO_METERS - 1.2f);
            //canAnimate = false;
        }*/

        //System.out.println(stopAnimate);
        //System.out.println(animate);
    }

    public void setData(Object player, Object platform)
    {
        playerData = player;
        platformData = platform;
    }

    public void collision()
    {
        if(detector.feedback(FilterID.player_category, FilterID.platform_category) && stopAnimate)
        {
            System.out.println("Contact initiated");
            if(stopAnimate)
            {
                animate = true;
                stopAnimate = false;
            }
        }
        else
        {
            if(splash.ifFrameEnd())
            {
                splash.resetTime();
                stopAnimate = true;
                animate = false;
            }
        }
    }
}
