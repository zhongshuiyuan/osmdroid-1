package com.huejie.osmdroid.trajectory;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryList extends LitePalSupport {

    public long createTime;
    public String name;

    public List<Trajectory> trajectoryList = new ArrayList<>();

}
