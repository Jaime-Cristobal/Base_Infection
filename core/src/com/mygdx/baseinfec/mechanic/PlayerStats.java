package com.mygdx.baseinfec.mechanic;

import com.badlogic.gdx.Gdx;

public final class PlayerStats
{
    /**
     * This is mainly for stat tracking purposes on the HUD screen
     *
     * velocity -> for speed tracking purposes
     * energy -> fuel needed to run the player
     * health -> overall health tracker
     *          -> overheating will cause in lost health
     * temperature -> tracks if player is overheating
     * construcPoints -> points that players have available to construct objects in the map
     *
     * score -> initial score shown in the HUD
     * bonus -> hidden points added
     *
     * */
    static private int velocity, energy, health, construcPoints = 0;
    static private int temperature = 0;
    static private int score, bonus = 0;
    static private int numOfBases = 0;

    /**Player stats purposes*/
    static public void setStats(int velocity, int energy, int health, int construcPoints)
    {
        if(velocity < 0 && energy < 0 && health < 0 && construcPoints < 0) {
            Gdx.app.error("Class PlayerStats.java", "statistic values cannot be below 0!");
        }

        PlayerStats.velocity = velocity;
        PlayerStats.energy = energy;
        PlayerStats.health = health;
        PlayerStats.construcPoints = construcPoints;
    }

    static public void addStats(int velocity, int energy, int health, int construcPoints)
    {
        if(PlayerStats.velocity + velocity < 0)
            PlayerStats.velocity = 0;
        else
            PlayerStats.velocity += velocity;

        if(PlayerStats.energy + energy < 0)
            PlayerStats.energy = 0;
        else
            PlayerStats.energy += energy;

        if(PlayerStats.health + health < 0)
            PlayerStats.health = 0;
        else
            PlayerStats.health += health;

        if(PlayerStats.construcPoints + construcPoints < 0)
            PlayerStats.construcPoints = 0;
        else
            PlayerStats.construcPoints += construcPoints;
    }

    static public void setTemperature(int celcius)
    {
        temperature = celcius;
    }

    static public void addTemperature(int celcius)
    {
        temperature += celcius;
    }

    static public int getVelocity()
    {
        if(velocity > 20)
            return velocity / 10;

        return velocity;
    }

    static public int getEnergy()
    {
        return energy;
    }

    static public int getTemperature()
    {
        return temperature;
    }

    static public int getConstrucPoints()
    {
        return construcPoints;
    }

    /**Scoring purposes*/
    static public void addScore(int value)
    {
        if(score + value < 0)
            score = 0;
        else
            score += value;
    }

    static public int getIniScore()
    {
        return score;
    }

    static public int getFinalScore()
    {
        return score + bonus;
    }

    /**for bases tracking purposes*/
    static public void addNumOfBases(int value)
    {
        if(numOfBases + value < 0)
            numOfBases = 0;
        else
            numOfBases += value;
    }

    static public int getNumOfBases()
    {
        return numOfBases;
    }
}