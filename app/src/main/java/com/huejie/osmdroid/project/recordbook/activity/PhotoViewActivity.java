package com.huejie.osmdroid.project.recordbook.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 通过ViewPager展示照片大图
 */
public class PhotoViewActivity extends BaseActivity {
    @BindView(R.id.pager)
    ViewPager mPager;
    private List<CommonRecordBookMedia> photosList;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        ButterKnife.bind(this);
        initData();
    }


    public void initData() {
        position = getIntent().getIntExtra("position", 0);           //获取点击的是哪张照片
        photosList = (List<CommonRecordBookMedia>) getIntent().getSerializableExtra("mediaPath");   //获取所有照片路径
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
//
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return photosList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                com.bm.library.PhotoView view = new com.bm.library.PhotoView(PhotoViewActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(layoutParams);
                view.enable();
                ImageLoader.getInstance().displayImage("file://" + Util.getProjectsPath(PhotoViewActivity.this, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + photosList.get(position).mediaPath, view);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mPager.setCurrentItem(position);          //设置当前展示照片，从上个界面点击哪张照片就展示哪张
    }


}
