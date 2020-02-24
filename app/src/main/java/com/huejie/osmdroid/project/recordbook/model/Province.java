package com.huejie.osmdroid.project.recordbook.model;

import java.io.Serializable;
import java.util.List;

public class Province implements Serializable {
    public String provinceName;
    public List<City> cityList;

    public boolean isCheck;
}
