package com.mygdx.baseinfec.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.ui.Scaler;

/**
 * Created by FlapJack on 7/22/2017.
 *
 * Only to be used for animated sprites packed into textureatlas.
 */

public final class Animator
{
    private final float scale_width = (Gdx.graphics.getWidth() / 400);
    private final float scale_height = (Gdx.graphics.getHeight() / 600);

    private final Main main;
    private final Array<Animation> animation = new Array<Animation>();
    private ArrayMap<String, Float> region;
    private float elapsed_time = 0f;
    private float endTime = -1;
    private int playback = 0;

    private String file;

    private float width = 0;
    private float height = 0;
    private int box2dScale = 0;

    public Animator(String file, Main main)
    {
        this.file = file;
        this.main = main;
    }

    /**reference an existing region*/
    public void setRegion(ArrayMap<String, Float> region)
    {
        this.region = region;
        create();
    }

    /**used for single region atlases; Warning: calling this will */
    public void setRegion(String regionName, float frameSpeed)
    {
        region = new ArrayMap<String, Float>();
        region.put(regionName, frameSpeed);
        create(regionName);
    }

    /**adds a region to the existing region created*/
    public void addRegion(String regionName, float frameSpeed)
    {
        if(region == null)
        {
            region = new ArrayMap<String, Float>();
            Gdx.app.log("Class Animator.java", "addRegion created a new region object.");
        }
        region.put(regionName, frameSpeed);
        create(regionName);
    }

    /**Used when referencing another region*/
    private void create()
    {
        for(int n = 0; n < this.region.size; n++)
        {
            animation.add(new Animation<TextureRegion>(region.getValueAt(n) * Gdx.graphics.getDeltaTime(),
                    main.assetmanager.manager.get(this.file, TextureAtlas.class).findRegions(this.region.getKeyAt(n))));
        }

        width = main.assetmanager.manager.get(file, TextureAtlas.class).getRegions().first().packedWidth * scale_width;
        height = main.assetmanager.manager.get(file, TextureAtlas.class).getRegions().first().packedHeight * scale_height;
    }

    /**For setting region */
    private void create(String regionName)
    {
        animation.add(new Animation<TextureRegion>(region.get(regionName) * Gdx.graphics.getDeltaTime(),
                main.assetmanager.manager.get(this.file, TextureAtlas.class).findRegions(regionName)));

        if(width == 0 || height == 0)
        {
            width = main.assetmanager.manager.get(file, TextureAtlas.class).getRegions().first().packedWidth * scale_width;
            height = main.assetmanager.manager.get(file, TextureAtlas.class).getRegions().first().packedHeight * scale_height;
        }
    }

    public void findRegion(String call)
    {
        int n = 0;

        for(ObjectMap.Entry<String, Float> iter : region)
        {
            if(iter.key.equals(call))
            {
                playback = n;
                return;
            }

            n++;
        }
    }

    /**playback is the type or set of animations you want to render from the atlas.
     * The ordering is heavily dependant on the order you passed the regions*/
    public void render(Batch batch, float x, float y)
    {
        elapsed_time += Gdx.graphics.getDeltaTime();

        batch.draw((TextureRegion) animation.get(playback).getKeyFrame(elapsed_time, true),
                ((x * Scaler.PIXELS_TO_METERS) - width/2) + box2dScale,
                (y * Scaler.PIXELS_TO_METERS) - height/2,
                width, height);
    }

    public void recordEndTime()
    {
        if(animation.get(playback).isAnimationFinished(elapsed_time))
        {
            endTime = elapsed_time;
        }
    }

    public void resetTime()
    {
        endTime = -1;
        elapsed_time = 0;
    }

    public boolean ifFrameEnd()
    {
        if(endTime == elapsed_time)
            return true;

        return false;
    }

    /**For manually upscaling and downscaling resolutions*/
    public void setScale(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    public void flip(int val)
    {
        width *= -1;

        box2dScale = val;
    }

    public void setPosScale(int val)
    {
        box2dScale = val;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }
}
