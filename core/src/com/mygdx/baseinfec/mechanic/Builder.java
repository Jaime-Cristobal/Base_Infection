package com.mygdx.baseinfec.mechanic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.actors.creation.CreateTexture;
import com.mygdx.baseinfec.ui.Scaler;

public class Builder
{
    final private Array<CreateTexture> boxStorage = new Array<CreateTexture>(20);
    final private Array<CreateTexture> box = new Array<CreateTexture>(20);

    final private Array<CreateTexture> fortStorage = new Array<CreateTexture>(3);
    final private Array<CreateTexture> fort = new Array<CreateTexture>(3);

    public Builder(Main main, World world)
    {
        for(int n = 0; n < 20; n++)
        {
            boxStorage.add(new CreateTexture("box.png", main, BodyDef.BodyType.DynamicBody));
            boxStorage.get(n).setData(1, 0, false);
            boxStorage.get(n).setUniqueID("box" + n);
            boxStorage.get(n).create(world, -1000, 0, 29, 24, false);
            boxStorage.get(n).setNoGravity();
        }

        for(int n = 0; n < 3; n++)
        {
            fortStorage.add(new CreateTexture("fort.png", main, BodyDef.BodyType.DynamicBody));
            fortStorage.get(n).setData(1, 0, false);
            fortStorage.get(n).setUniqueID("fort" + n);
            fortStorage.get(n).create(world, -1000, 0, 165, 169, false);
            fortStorage.get(n).setNoGravity();
        }
    }

    public void render()
    {
        if(box.size != 0 || fort.size != 0)
        {
            for (CreateTexture iter : box)
                iter.display();
            for (CreateTexture iter1: fort)
                iter1.display();
        }
    }

    public boolean setOnMap(int inputVal, Vector2 playerPos)
    {
        if(inputVal == 5 && boxStorage.size != 0)
        {
            //Gdx.app.log("BOX", "DROPPED");

            //box.add(boxStorage.get(boxStorage.size - 1));
            box.add(boxStorage.pop());
            box.get(box.size - 1).setPosition(playerPos.x * Scaler.PIXELS_TO_METERS, playerPos.y * Scaler.PIXELS_TO_METERS);
            //box.get(boxStorage.size - 1).setPosition(playerPos.x, playerPos.y);
            //boxStorage.removeIndex(boxStorage.size - 1);

            return true;
        }

        if(inputVal == 6 && fortStorage.size != 0)
        {
            if(fort.size != 0)
                for(CreateTexture iter : fort)
                {
                    //Makes sure forts aren't too close to each other
                    if(iter.getBody().getPosition().x + 20 > playerPos.x && iter.getBody().getPosition().x - 20 < playerPos.x
                            && iter.getBody().getPosition().y + 20 > playerPos.y && iter.getBody().getPosition().y - 20 < playerPos.y)
                        return false;
                }

            fort.add(fortStorage.pop());    //removes from storage, adds to playing map
            fort.get(fort.size - 1).setPosition(playerPos.x * Scaler.PIXELS_TO_METERS, playerPos.y * Scaler.PIXELS_TO_METERS);

            return true;
        }


        return false;
    }

    public boolean removeFromMap()
    {
        return false;
    }
}
