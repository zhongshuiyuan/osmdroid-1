package com.huejie.osmdroid.model;

import java.io.Serializable;

public class SimpleChooseModel implements Serializable {
    public String name;
    public long code;
    public boolean isChoose;

    public SimpleChooseModel(String name) {
        this.name = name;
    }

    public SimpleChooseModel(String name, long code) {
        this.name = name;
        this.code = code;
    }

    public SimpleChooseModel(String name, boolean isChoose) {
        this.name = name;
        this.isChoose = isChoose;
    }
}
