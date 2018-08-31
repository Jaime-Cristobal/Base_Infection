package com.mygdx.baseinfec.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.baseinfec.Main;
import com.mygdx.baseinfec.mechanic.Steering;
import com.mygdx.baseinfec.actors.creation.CreateBody;
import com.mygdx.baseinfec.ui.Scaler;

public class HUD implements InputProcessor
{
    final private World world;
    final private Stage stage;

    final Steering wheel;

    private Table table;
    private Label score;

    private CreateBody body;
    private CreateBody boxBody;
    private Vector3 touchPos;
    private Body touchPoint;
    private int x, y;
    private MouseJoint mouseJoint;
    private boolean changeDir;
    private int changeDirection;

    private Vector3 camPosition;
    private OrthographicCamera camera;

    private Array<TextButton> controlPad;

    private int input = -1;

    final private Image box;
    private int rotate = 0;
    private int speed = 0;

    private Vector3 target = new Vector3();

    int lastX = 0;
    int lastY = 0;

    private int n = 0;
    private boolean click = false;
    private int direc = 1; //-1 is to the left, 1 is to the right
    private boolean onHold = false;

    private int magnitude = 0;

    public HUD(final Main main, World world, Viewport viewport)
    {
        body = new CreateBody(BodyDef.BodyType.KinematicBody);
        body.setData(0.1f, 0, false);
        body.setID(3);
        body.create(world, 0, 0, 100,50, true);
        body.body.setActive(true);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        touchPos = new Vector3();
        this.world = world;

        x = 0;
        y = 0;

        changeDir = false;

        changeDirection = 0;

        score = new Label("Speed: 0 KPH", main.skin);
        controlPad = new Array<TextButton>();
        controlPad.add(new TextButton("L", main.skin));  //0
        controlPad.add(new TextButton("R", main.skin)); //1
        controlPad.add(new TextButton("Brake", main.skin)); //2
        controlPad.add(new TextButton("GO", main.skin)); //3
        controlPad.add(new TextButton("E BRAKE", main.skin)); //4
        controlPad.add(new TextButton("BOX", main.skin)); //5
        controlPad.add(new TextButton("FORT", main.skin)); //6
        for(TextButton iter : controlPad)
            iter.setTransform(true);

        //stage = new Stage(viewport);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        table = new Table();
        table.setTransform(true);
        table.setFillParent(true);
        stage.addActor(table);

        box = new Image((Texture) main.assetmanager.getFile("player3.png"));
        box.setSize(50, 15);
        //stage.addActor(box);

        boxBody = new CreateBody(BodyDef.BodyType.DynamicBody);
        boxBody.setData(0.5f, 0, false);
        boxBody.setID(5);
        boxBody.create(world, box.getOriginX(), box.getOriginY(), 70, 70, false);

        //table.add(box).fillX().uniformX().center().right().pad(0, -0, -900, 0);
        table.row();
        table.add(score).fillX().uniformX().center().right().pad(0, 30, -50, 30);
        table.add(controlPad.get(0)).center().right().pad(0, -250, -1010, 250); //L
        table.add(controlPad.get(1)).center().right().pad(0, -210, -1010, 210); //R
        table.add(controlPad.get(2)).center().right().pad(0, -270, -1100, 270); //Brake
        table.add(controlPad.get(3)).center().right().pad(0, -260, -1000, 260); //GO
        table.add(controlPad.get(4)).center().right().pad(0, -260, -1000, 260); //E BRAKE

        wheel = new Steering(world);

        box.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                //box.setOrigin(box.getWidth()/2, box.getHeight()/2);
                click = true;

                direc *= -1;

                //box.rotateBy(40);


               // if(rotate == 0)
                //    rotate = 1;

                //rotate *= -1;

                System.out.println("JABAWOK");
            }
        });

        controlPad.get(0).addListener(new InputListener()
        {
            //Creates a button hold
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = true;
                input = 0;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = false;
                input = -1;
            }

        });

        controlPad.get(1).addListener(new InputListener()
        {
            //Creates a button hold
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = true;
                input = 1;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = false;
                input = -1;
            }

        });

        controlPad.get(2).addListener(new InputListener()
        {
            //Creates a button hold
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = true;
                input = 2;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = false;
                input = -1;
            }

        });

        controlPad.get(3).addListener(new InputListener()
        {
            //Creates a button hold
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = true;
                input = 3;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = false;
                input = -1;
            }

        });

        controlPad.get(4).addListener(new InputListener()      //E Brake
        {
            //Creates a button hold
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = true;
                input = 4;
                for(TextButton itr : controlPad)
                    itr.setColor(Color.RED);
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                onHold = false;
                input = -1;
            }

        });
    }

    public void rotate(int val)
    {
        body.body.setAngularVelocity(val);
    }

    public void renderSprite(Main main)
    {
        //steer.draw(main.batch);
    }

    public void render(Main main, Vector3 pos, float posX, float posY, int speed)
    {
        //body.body.setTransform(posX / Scaler.PIXELS_TO_METERS, (posY - 200) / Scaler.PIXELS_TO_METERS,
        //        body.body.getAngle());

        //System.out.println(input);

        if(click)
        {
            switch (direc)
            {
                default:
                    //click = false;
                    break;
                case -1:
                    if(box.getRotation() != 180)
                        n += 20;
                    else
                        click = false;
                    break;
                case 1:
                    if(box.getRotation() != 0)
                        n -= 20;
                    else
                        click = false;
                    break;
            }

            /**
            if(box.getRotation() != 180)
                n += 20;
            else if(box.getRotation() == 180)
                n -= 20;
            else
                click = false;
             */

            box.setOrigin(30, box.getHeight()/2);
            box.setRotation(n);
        }

        main.batch.setProjectionMatrix(stage.getCamera().combined);
        //wheel.mechanics(x, y, pos);

        if(speed > 20)
            speed /= 10;
        score.setText(speed + " KPH");

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        //body.body.getPosition().set(posX / Scaler.PIXELS_TO_METERS, (posY - 200) / Scaler.PIXELS_TO_METERS);
    }

    public int getDirection()
    {
        return direc;
    }

    public InputProcessor getInputProcessor()
    {
        return this;
    }

    public Stage getStage()
    {
        return stage;
    }

    public float getAnglularVel()
    {
        return body.body.getAngularVelocity();
    }

    public void resetInput()
    {
        input = -1;
    }

    public int getRotate()
    {
        return rotate;
    }

    public int getSpeed()
    {
        return speed;
    }

    public Vector3 getTarget()
    {
        return target;
    }

    public int getInput()
    {
        /**
        //up
        controlPad.get(0).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                input = 0;
            }
        });

        //down
        controlPad.get(1).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                input = 1;
            }
        });

        //left
        controlPad.get(2).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                input = 2;
            }
        });

        //right
        controlPad.get(3).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                input = 3;
            }
        });

        //Parking Mode
        controlPad.get(4).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                input = 4;
            }
        });

        //Drop a box
        controlPad.get(5).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                input = 5;
            }
        });

        //fort
        controlPad.get(6).addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                input = 6;
            }
        });
         */

        return input;
    }

    public boolean motionTouch(Camera camera)
    {
        //Gdx.app.log("" + x, "" + y);

        //Gdx.app.log("" + body.body.getPosition().x, "" + body.body.getPosition().y);

        //the touch coordinate is between the width and height of the wheel
        if((x > body.body.getPosition().x - 5 && x < body.body.getPosition().x + 5)
                && (y > body.body.getPosition().y + 10 && y < body.body.getPosition().y + 15.5f))
        {
            System.out.println(changeDirection);

            //reset click coord to keep spinning
            x = 0;
            y = 0;
            switch (changeDirection)
            {
                case 0:
                    body.body.setAngularVelocity(-10);
                    changeDirection = 1;
                    return false;
                case 1:
                    body.body.setAngularVelocity(10);
                    changeDirection = 0;
                    return false;
            }

            //else
            //    body.body.setAngularVelocity(0);

            return false;
        }
        else
        {
            //body.body.setAngularVelocity(0);
        }

        changeDir = false;

        return true;

        /**
        QueryCallback callback = new QueryCallback()
        {
            @Override
            public boolean reportFixture(Fixture fixture)
            {
                // if the hit point is inside the fixture of the body
                // we report it
                if (fixture.testPoint(touchPos.x, touchPos.y))
                {
                    touchPoint = fixture.getBody();
                    return false;
                } else
                    return true;
            }
        };


        // translate the mouse coordinates to world coordinates
        camera.unproject(touchPos.set(x, y, 0));
        // ask the world which bodies are within the given
        // bounding box around the mouse pointer
        touchPoint = null;
        world.QueryAABB(callback, touchPos.x - 0.0001f, touchPos.y - 0.0001f,
                touchPos.x + 0.0001f, touchPos.y + 0.0001f);

        if (touchPoint == body.body) touchPoint = null;

        // ignore kinematic bodies, they don't work with the mouse joint
        if (touchPoint != null && touchPoint.getType() == BodyDef.BodyType.KinematicBody)
        {
            return false;
        }

        // if we hit something we create a new mouse joint
        // and attach it to the hit body.
        if (touchPoint != null)
        {
            MouseJointDef def = new MouseJointDef();
            def.bodyA = body.body;
            def.bodyB = touchPoint;
            def.collideConnected = true;
            def.target.set(touchPos.x, touchPos.y);
            def.maxForce = 1000.0f * touchPoint.getMass();

            mouseJoint = (MouseJoint)world.createJoint(def);
            touchPoint.setAwake(true);
        }

        return true;
         */
    }

    public void dispose()
    {
        stage.dispose();
    }

    /** Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyDown (int keycode)
    {
        return false;
    }

    /** Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyUp (int keycode)
    {
        return false;
    }

    /** Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed */
    public boolean keyTyped (char character)
    {
        return false;
    }

    /** Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
        x = screenX / (int)Scaler.PIXELS_TO_METERS;
        y = (Gdx.graphics.getHeight() - screenY) / (int)Scaler.PIXELS_TO_METERS; //height - screenY makes (0,0) as bottom left
        changeDir = true;

        return false;
    }

    /** Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    /** Called when a finger or the mouse was dragged.
     * @param pointer the pointer for the event.
     * @return whether the input was processed */
    public boolean touchDragged (int screenX, int screenY, int pointer)
    {
        box.setOrigin(box.getWidth()/2, box.getHeight()/2);
        //box.rotateBy(2 * rotate);
        //System.out.println(screenX / Scaler.PIXELS_TO_METERS);
        //box.rotateBy(screenX / Scaler.PIXELS_TO_METERS * Gdx.graphics.getDeltaTime());

        target.set(screenX, screenY, 0);
        stage.getCamera().unproject(target);

        //box.rotateBy(MathUtils.atan2(target.y, target.x));

        //box.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2(-target.y, -target.x));

        //System.out.println(MathUtils.atan2(screenY - lastY, screenX - lastX));
        //box.setRotation(-screenX);
        //box.setRotation(MathUtils.atan2(screenY - lastY, screenX - lastX));

        //lastX = screenX;
        //lastY = screenY;

        /**
        if(screenX / Scaler.PIXELS_TO_METERS > 17)
        {
            //System.out.println("K");
            box.rotateBy(35 * Gdx.graphics.getDeltaTime());
        }
        else
        {
            box.rotateBy(-35 * Gdx.graphics.getDeltaTime());
        }*/

        return false;
    }

    /** Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     * @return whether the input was processed */
    public boolean mouseMoved (int screenX, int screenY)
    {
        return false;
    }

    /** Called when the mouse wheel was scrolled. Will not be called on iOS.
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed. */
    public boolean scrolled (int amount)
    {
        return false;
    }
}
