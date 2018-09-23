package com.mygdx.baseinfec;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.baseinfec.actors.creation.CreateBody;
import com.mygdx.baseinfec.animator.Animator;
import com.mygdx.baseinfec.collision.FilterID;
import com.mygdx.baseinfec.mechanic.Bases;
import com.mygdx.baseinfec.mechanic.Builder;
import com.mygdx.baseinfec.actors.enemies.Mob;
import com.mygdx.baseinfec.actors.HUD;
import com.mygdx.baseinfec.actors.creation.CreateAnimation;
import com.mygdx.baseinfec.mechanic.EventManager;
import com.mygdx.baseinfec.player.Player;
import com.mygdx.baseinfec.ui.Scaler;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class MapStage implements Screen
{
    final private float width = Gdx.graphics.getWidth();
    final private float height = Gdx.graphics.getHeight();

    final private Main main;
    final private World world = new World(new Vector2(0, 0), true);
    final private RayHandler ray = new RayHandler(world);
    final private OrthographicCamera worldCamera = new OrthographicCamera(width, height);
    final private ExtendViewport viewport = new ExtendViewport(width, height, worldCamera);
    final private InputMultiplexer inputGroup = new InputMultiplexer();      //for multiple input processors needed

    final private PointLight light = new PointLight(ray, 500, Color.RED, 50, -5, 10);
    final private ConeLight cone = new ConeLight(ray, 500, Color.BLUE, 150, 20, 50,
            90, 350);

    private float dayLight = 3;
    private int day = 1;

    final private Player player;
    final private HUD hud;
    final private CreateAnimation fx;
    final private ArrayMap<String, Float> region = new ArrayMap<String, Float>();
    final private Builder builder;
    final private Bases base;
    final private Mob mob;
    final private CreateAnimation rock_01;
    private float angle = 0;

    private BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("newphys.json"));

    final private float lerp  = 0.1f;
    private Vector3 camPosition;

    private Array<CreateBody> sides = new Array<CreateBody>();

    final private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private Matrix4 debugMatrix;

    public MapStage(Main main)
    {
        this.main = main;

        player = new Player(main);
        //player.createBody(world, 200, 550, 78, 43);
        player.createBody(world, 200, 330, 27, 38);

        hud = new HUD(main, world, viewport);

        region.put("particle_red", 5f);

        fx = new CreateAnimation("fx1.atlas", region, main, BodyDef.BodyType.DynamicBody);
        fx.setData(0.1f, 0, true);
        fx.setUniqueID(3);
        fx.create(world, 270, 520, 128, 128, true);
        fx.setRegion("particle_red");

        //inputGroup.addProcessor(hud.getInputProcessor());
        inputGroup.addProcessor(hud.getStage());
        inputGroup.addProcessor(player.getGesture());
        inputGroup.addProcessor(player.getInputProcessor());
        Gdx.input.setInputProcessor(inputGroup);

        builder = new Builder(main, world);
        base = new Bases(main, world);
        mob = new Mob(main, world, 20);

        for(int n = 0; n < 4; n++)
        {
            sides.add(new CreateBody(BodyDef.BodyType.StaticBody));
            sides.get(n).setData(1, 0, true);
            sides.get(n).setFilter(FilterID.floor_category, (short) (FilterID.player_category | FilterID.platform_category));
        }

        sides.get(0).create(world, 800, 0, 2500, 20, false);       //bottom blocker
        sides.get(1).create(world, 700, 2050, 2500, 20, false);    //top
        sides.get(2).create(world, -100, 1200, 20, 2500, false);   //left
        sides.get(3).create(world, 1950, 1000, 20, 2500, false);   //right


        for(CreateBody iter : sides)
            iter.setActive(true);

        rock_01 = new CreateAnimation("rock_01.atlas", main, BodyDef.BodyType.DynamicBody);
        rock_01.addRegion("Armature_wave", 3.5f);
        rock_01.setData(1, 0, false);
        rock_01.setFilter(FilterID.platform_category, (short)(FilterID.player_category | FilterID.floor_category));
        rock_01.setUniqueID(5);
        rock_01.create(world, loader, "rock", 80, 90, 763, 729, false);
        rock_01.setSpeed(10, 10);
    }

    /** Called when this screen becomes the current screen for a {@link Game}. */
    public void show ()
    {
        //camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ray.setShadows(true);
        ray.setCulling(true);
        ray.setAmbientLight(dayLight);
        ray.setBlur(true);
        ray.setBlurNum(2);

        //player.getBody().setUserData(1);

        mob.spawnRandomly(50, 300, 350, 700, 10);
        mob.setTarget(base.getRandomPos());

        world.setContactListener(EventManager.getContactListener());
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta)
    {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);      //allows box2D objects to move
        main.batch.setProjectionMatrix( viewport.getCamera().combined);                    //allows camera to move
        ray.setCombinedMatrix(viewport.getCamera().combined);

        debugMatrix = main.batch.getProjectionMatrix().cpy().scale(Scaler.PIXELS_TO_METERS, Scaler.PIXELS_TO_METERS, 0);

        camMovement(delta);
        viewport.getCamera().update();

        main.batch.begin();
        main.batch.draw((Texture)main.assetmanager.getFile("map58.png"), -300, -350, 3072, 3072);
        mob.setSpeed();
        mob.render(main);
        player.display(hud.getInput(), delta);
        fx.display();
        builder.render();
        base.render();

        if(angle > 180)
            angle = 0;

        angle += 0.05f;

        rock_01.getBody().setAngularVelocity(0.1f);
        rock_01.displayCustom(rock_01.getBody().getAngle() * MathUtils.radiansToDegrees,
                385, 365, 512);

        hud.renderSprite(main);
        main.batch.end();

        //System.out.println(dayLight);
        /**
        if((int)dayLight == -3)     //night time
        {
            day = 1;
            //light.setDistance();
        }
        else if((int)dayLight == 3)     //day time
            day = -1;

        dayLight += 0.1f * delta * day;
        ray.setAmbientLight(dayLight);
         */

        if(builder.setOnMap(hud.getInput(), player.getBody().getPosition()))
        {
            Gdx.app.log("OBJECT", "DROPPED");
        }

        //hud.resetInput();

        //light.update();
        ray.updateAndRender();
        hud.render(main, camPosition, player.getX(), player.getY(), player.getVelocity());

        debugRenderer.render(world, debugMatrix);
    }

    /** @see ApplicationListener#resize(int, int) */
    public void resize (int width, int height)
    {
        viewport.update(width, height, false);
    }

    /** @see ApplicationListener#pause() */
    public void pause ()
    {

    }

    /** @see ApplicationListener#resume() */
    public void resume ()
    {

    }

    /** Called when this screen is no longer the current screen for a {@link Game}. */
    public void hide ()
    {

    }

    /** Called when this screen should release all resources. */
    public void dispose ()
    {
        hud.dispose();
        //ray.removeAll();
        //light.dispose();
        ray.dispose();
        world.dispose();

        Gdx.app.log("MapStage.java:", "Fully Disposed");
    }

    public void camMovement(float delta)
    {
        camPosition =  viewport.getCamera().position;

        camPosition.x += (player.getX() - camPosition.x) * lerp * delta * 100;
        camPosition.y += (player.getY() - camPosition.y - 50) * lerp * delta * 100;
    }
}
