package com.mygdx.baseinfec.mechanic;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.baseinfec.actors.creation.CreateBody;
import com.mygdx.baseinfec.player.Player;
import com.mygdx.baseinfec.ui.Scaler;

public class Steering
{
    private CreateBody wheel = new CreateBody(BodyDef.BodyType.KinematicBody);
    private int changeDirection = -1;

    public Steering(World world)
    {
        wheel.setData(0, 0, false);
        wheel.setID(9);
        wheel.create(world, 0, 0, 80, 80, false);
    }

    public int mechanics(float x, float y, Vector3 camera)
    {
        //wheel.body.getPosition().x = camera.position.x / Scaler.PIXELS_TO_METERS;
        //wheel.body.getPosition().y = (camera.position.y - 50) / Scaler.PIXELS_TO_METERS;
        //wheel.body.getPosition().set(camera.x / Scaler.PIXELS_TO_METERS,
        //        (camera.y - 50) / Scaler.PIXELS_TO_METERS);
        //wheel.body.getPosition().nor();


        wheel.body.setTransform(camera.x / Scaler.PIXELS_TO_METERS,
                (camera.y - 50) / Scaler.PIXELS_TO_METERS, wheel.body.getAngle());

        //Gdx.app.log("" + x, "" + y);
        //Gdx.app.log("" + wheel.body.getPosition().x , "" + wheel.body.getPosition().y);

        if((x > wheel.body.getPosition().x - 5 && x < wheel.body.getPosition().x + 5)
                && (y > wheel.body.getPosition().y - 10 && y < wheel.body.getPosition().y - 15.5f))
        {
            System.out.println("YYYYYYeeeeeee");
            wheel.body.setAngularVelocity(500);
        }

        //the touch coordinate is between the width and height of the wheel
        /**
        if((x > wheel.body.getPosition().x - 5 && x < wheel.body.getPosition().x + 5)
                && (y > wheel.body.getPosition().y + 10 && y < wheel.body.getPosition().y + 15.5f))
        {
            System.out.println(changeDirection);

            //reset click coord to keep spinning
            x = 0;
            y = 0;
            switch (changeDirection)
            {
                case 0:
                    wheel.body.setAngularVelocity(-10);
                    return changeDirection = 1;
                case 1:
                    wheel.body.setAngularVelocity(10);
                    return changeDirection = 0;
                default:
                    break;
            }

            //else
            //    body.body.setAngularVelocity(0);
        }
         */

        return -1;
    }

    public void applySteering(Player player)
    {
        //wheel.body.getPosition().x = player.getBody().getPosition().x;
        //wheel.body.getPosition().y = player.getBody().getPosition().y - 50;

        player.getBody().setAngularVelocity(wheel.body.getAngularVelocity());
    }
}
