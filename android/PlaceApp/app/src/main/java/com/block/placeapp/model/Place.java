package com.block.placeapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Place implements Serializable {

    public String name;
    public String vicinity;

    public Geometry geometry;

    // inner class
    public class Geometry implements Serializable {
        public Location location;


        public class Location implements Serializable {
            public double lat;
            public double lng;
        }
    }

}
