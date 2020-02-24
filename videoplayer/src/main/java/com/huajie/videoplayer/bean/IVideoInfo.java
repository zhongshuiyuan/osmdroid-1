package com.huajie.videoplayer.bean;

import android.net.Uri;

import java.io.Serializable;

/**
 * 视频数据类请实现本接口
 */
public interface IVideoInfo extends Serializable {

    /**
     * 视频标题
     */
    String getVideoTitle();

    /**
     * 视频播放路径 url / file path
     */
    Uri getVideoUri();

}
