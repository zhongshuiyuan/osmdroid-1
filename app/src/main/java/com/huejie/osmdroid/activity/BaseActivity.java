package com.huejie.osmdroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppManager;
import com.huejie.osmdroid.util.KeyBoardUtil;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).init();
        AppManager.addActivity(this);

    }

    @Override
    protected void onPause() {
        KeyboardUtils.hideSoftInput(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LogUtils.i("abc",System.currentTimeMillis());
        super.onDestroy();
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
    }

}
