package com.huejie.osmdroid.project.recordbook.model;

import org.osmdroid.util.BoundingBox;

import java.io.Serializable;

public class City implements Serializable {
    public String cityName;
    public BoundingBox boundingBox;
    public boolean isCheck;
}
