package com.graphhopper.matching;

import com.graphhopper.util.shapes.GHPoint3D;

import java.util.Date;

public class MyGHPoint extends GHPoint3D {
    public Date time;
    public double speed;

    public MyGHPoint(double lat, double lon, double elevation, Date time , double speed) {
        super(lat, lon, elevation);
        this.time = time;
        this.speed = speed;
    }
}
