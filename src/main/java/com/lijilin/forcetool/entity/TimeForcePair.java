package com.lijilin.forcetool.entity;

/**
 * @author lijilin
 * @date 2023/5/26 9:01
 */
public class TimeForcePair
{
    public double[] times;
    public double[] forces;

    public TimeForcePair(double[] times, double[] forces) {
        this.times = times;
        this.forces = forces;
    }
}
