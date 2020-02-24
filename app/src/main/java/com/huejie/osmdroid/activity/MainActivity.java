package com.huejie.osmdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckedTextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.edit.EditFragment;
import com.huejie.osmdroid.map.MapFragment;
import com.huejie.osmdroid.more.MoreFragment;
import com.huejie.osmdroid.project.ProjectFragment;
import com.huejie.osmdroid.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.ctv_map)
    CheckedTextView ctv_map;
    @BindView(R.id.ctv_edit)
    CheckedTextView ctv_edit;
    @BindView(R.id.ctv_project)
    CheckedTextView ctv_project;
    @BindView(R.id.ctv_more)
    CheckedTextView ctv_more;

    public int current = 0;

    @OnClick(R.id.ctv_map)
    public void mapClick() {
        if (current != 0) {
            ctv_map.setChecked(true);
            ctv_edit.setChecked(false);
            ctv_project.setChecked(false);
            ctv_more.setChecked(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_layout, MapFragment.newInstance("", "")).commit();
            current = 0;
        }
    }

    @OnClick(R.id.ctv_edit)
    public void editClick() {
        if (current != 1) {
            ctv_map.setChecked(false);
            ctv_edit.setChecked(true);
            ctv_project.setChecked(false);
            ctv_more.setChecked(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_layout, EditFragment.newInstance("", "")).commit();
            current = 1;
        }
    }

    @OnClick(R.id.ctv_project)
    public void projectClick() {
        if (AppContext.sp.getBoolean(Config.SP.IS_LOGIN)) {
            if (current != 2) {
                ctv_map.setChecked(false);
                ctv_edit.setChecked(false);
                ctv_project.setChecked(true);
                ctv_more.setChecked(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_layout, ProjectFragment.newInstance("", "")).commit();
                current = 2;
            }
        } else {
            ToastUtils.showShortToast("请先登录");
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 100);
        }
    }

    @OnClick(R.id.ctv_more)
    public void moreClick() {
        if (current != 3) {
            ctv_map.setChecked(false);
            ctv_edit.setChecked(false);
            ctv_project.setChecked(false);
            ctv_more.setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_layout, MoreFragment.newInstance("", "")).commit();
            current = 3;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_layout, new MapFragment()).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                ctv_map.setChecked(false);
                ctv_edit.setChecked(false);
                ctv_project.setChecked(true);
                ctv_more.setChecked(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_layout, ProjectFragment.newInstance("", "")).commit();
                current = 2;
            }
        }

    }
}
