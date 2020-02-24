package com.huejie.osmdroid.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.huejie.osmdroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModelActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
    }
}
