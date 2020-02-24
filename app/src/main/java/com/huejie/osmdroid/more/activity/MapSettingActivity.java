package com.huejie.osmdroid.more.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.util.Config;

import org.osmdroid.config.Configuration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapSettingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_maxZoom)
    TextView tv_maxZoom;
    @BindView(R.id.tv_cache)
    TextView tv_cache;
    @BindView(R.id.tv_jwdgs)
    TextView tv_jwdgs;
    @BindView(R.id.tv_cdgs)
    TextView tv_cdgs;


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.iv_zoom_jia)
    public void zoomJia() {
        String zoom = tv_maxZoom.getText().toString().trim();
        if (!TextUtils.isEmpty(zoom)) {
            Integer z = Integer.valueOf(zoom);
            if (z < MAXZOOM) {
                tv_maxZoom.setText((z + 1) + "");
                AppContext.sp.putInt(Config.ZDJB, (z + 1));
            }
        }

    }

    @OnClick(R.id.iv_zoom_jian)
    public void zoomJian() {
        String zoom = tv_maxZoom.getText().toString().trim();
        if (!TextUtils.isEmpty(zoom)) {
            Integer z = Integer.valueOf(zoom);
            if (z > 1) {
                tv_maxZoom.setText((z - 1) + "");
                AppContext.sp.putInt(Config.ZDJB, (z - 1));
            }
        }
    }

    @OnClick(R.id.iv_cache_jia)
    public void cacheJia() {
        switch (cacheSize) {
            case 512:
                tv_cache.setText("1024M");
                AppContext.sp.putInt(Config.HCDX, 1024);
                cacheSize = 1024;
                Configuration.getInstance().setTileFileSystemCacheMaxBytes(1024L * 1024 * 1024);
                break;
            case 1024:
                tv_cache.setText("2048M");
                AppContext.sp.putInt(Config.HCDX, 2048);
                cacheSize = 2048;
                Configuration.getInstance().setTileFileSystemCacheMaxBytes(2048L * 1024 * 1024);
                break;
        }

    }

    @OnClick(R.id.iv_cache_jian)
    public void cacheJian() {
        switch (cacheSize) {
            case 2048:
                tv_cache.setText("1024M");
                AppContext.sp.putInt(Config.HCDX, 1024);
                cacheSize = 1024;
                Configuration.getInstance().setTileFileSystemCacheMaxBytes(1024L * 1024 * 1024);
                break;
            case 1024:
                tv_cache.setText("512M");
                AppContext.sp.putInt(Config.HCDX, 512);
                cacheSize = 512;
                Configuration.getInstance().setTileFileSystemCacheMaxBytes(512L * 1024 * 1024);
                break;
        }

    }


    public static final int MAXZOOM = 28;
    public static final int MINZOOM = 1;
    private int cacheSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_setting);
        initView();

    }

    private void initView() {
        ButterKnife.bind(this);
        tv_title.setText("地图参数设置");
        tv_jwdgs.setText(AppContext.sp.getString(Config.JWDGS));
        tv_cdgs.setText(AppContext.sp.getString(Config.CDDWGS));
        tv_maxZoom.setText(AppContext.sp.getInt(Config.ZDJB) + "");
        cacheSize = AppContext.sp.getInt(Config.HCDX);
        tv_cache.setText(cacheSize + "M");

    }
}
