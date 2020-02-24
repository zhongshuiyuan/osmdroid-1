package com.huejie.osmdroid.event;

import com.huejie.osmdroid.model.CommonProjectMedia;

import java.util.ArrayList;

public class OverlayChangeEvent {

    public ArrayList<CommonProjectMedia> chooseList;

    public OverlayChangeEvent(ArrayList<CommonProjectMedia> chooseList) {
        this.chooseList = chooseList;
    }


}
