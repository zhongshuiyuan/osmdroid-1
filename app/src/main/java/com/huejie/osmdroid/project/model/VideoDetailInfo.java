package com.huejie.osmdroid.project.model;


import android.net.Uri;

import com.huajie.videoplayer.bean.IVideoInfo;

public class VideoDetailInfo implements IVideoInfo {

    public String title;
    public Uri videoPath;

    @Override
    public String getVideoTitle() {
        return title;
    }

    @Override
    public Uri getVideoUri() {
        return videoPath;
    }
}
