package com.huejie.osmdroid.project.recordbook.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.WindowManager;

import com.blankj.utilcode.utils.FileUtils;
import com.huajie.videoplayer.BDVideoView;
import com.huajie.videoplayer.listener.SimpleOnVideoControlListener;
import com.huajie.videoplayer.utils.DisplayUtils;
import com.huejie.osmdroid.BuildConfig;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.project.model.VideoDetailInfo;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaPlayerActivity extends BaseActivity {
    @BindView(R.id.videoPlayer)
    BDVideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        ButterKnife.bind(this);
        String path = getIntent().getStringExtra("mediaPath");
        VideoDetailInfo info = new VideoDetailInfo();
        info.title = FileUtils.getFileName(path);
        Uri uri = getUriForFile(this, new File(path));
        info.videoPath = uri;
        videoView.setOnVideoControlListener(new SimpleOnVideoControlListener() {

            @Override
            public void onRetry(int errorStatus) {
                // TODO: 2017/6/20 调用业务接口重新获取数据
                // get info and call method "videoView.startPlayVideo(info);"
            }

            @Override
            public void onBack() {
                onBackPressed();
            }

            @Override
            public void onFullScreen() {
                DisplayUtils.toggleScreenOrientation(MediaPlayerActivity.this);
            }
        });
        videoView.startPlayVideo(info);

    }

    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果SDK版本>=24，即：Build.VERSION.SDK_INT >= 24
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        videoView.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        videoView.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        videoView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!DisplayUtils.isPortrait(this)) {
            if (!videoView.isLock()) {
                DisplayUtils.toggleScreenOrientation(this);
            }
        } else {
            super.onBackPressed();
        }
    }
}
