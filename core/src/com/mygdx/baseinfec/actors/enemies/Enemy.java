package com.mygdx.baseinfec.actors.enemies;

import com.badlogic.gdx.math.Vector2;

public interface Enemy
{
    void render();

    void ai();

    void setTarget(Vector2 target);

    void spawnRandomly(int startX, int endX, int startY, int endY, int amount);

    void spawn(float posX, float posY, int amount);
}
