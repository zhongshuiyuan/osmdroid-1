package com.huajie.videoplayer.listener;

public interface OnVideoControlListener {

    /**
     * 返回
     */
    void onBack();

    /**
     * 全屏
     */
    void onFullScreen();

    /**
     * 错误后的重试
     *
     * @param errorStatus 当前错误状态
     */
    void onRetry(int errorStatus);

}
