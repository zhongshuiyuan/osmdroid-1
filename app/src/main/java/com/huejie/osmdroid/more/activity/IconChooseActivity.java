package com.huejie.osmdroid.more.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.more.adapter.MarkerChooseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IconChooseActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.gridView)
    GridView gridView;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    private int[] icons = new int[]{
            R.mipmap.marker_icon_1, R.mipmap.marker_icon_2, R.mipmap.marker_icon_3, R.mipmap.marker_icon_4, R.mipmap.marker_icon_5,
            R.mipmap.marker_icon_6, R.mipmap.marker_icon_7, R.mipmap.marker_icon_8, R.mipmap.marker_icon_9, R.mipmap.marker_icon_10,
            R.mipmap.marker_icon_11, R.mipmap.marker_icon_12, R.mipmap.marker_icon_13, R.mipmap.marker_icon_14, R.mipmap.marker_icon_15,
            R.mipmap.marker_icon_16, R.mipmap.marker_icon_17, R.mipmap.marker_icon_18, R.mipmap.marker_icon_19, R.mipmap.marker_icon_20,
            R.mipmap.marker_icon_21, R.mipmap.marker_icon_22, R.mipmap.marker_icon_23, R.mipmap.marker_icon_24, R.mipmap.marker_icon_25,
            R.mipmap.marker_icon_26, R.mipmap.marker_icon_27, R.mipmap.marker_icon_28
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_choose);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("图标选择");
        MarkerChooseAdapter adapter = new MarkerChooseAdapter(this, icons);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK, new Intent().putExtra("result", icons[position]));
                finish();
            }
        });
    }
}
