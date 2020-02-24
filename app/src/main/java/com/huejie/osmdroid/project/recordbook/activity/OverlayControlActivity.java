package com.huejie.osmdroid.project.recordbook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.event.OverlayChangeEvent;
import com.huejie.osmdroid.model.CommonProjectMedia;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.project.recordbook.adapter.OverlayAdapter;
import com.huejie.osmdroid.util.DBUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OverlayControlActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.gridView)
    GridView gridView;
    private OverlayAdapter adapter;
    private BookSimple book;
    private List<CommonProjectMedia> list = new ArrayList<>();

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void complete() {
        ArrayList<CommonProjectMedia> temList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CommonProjectMedia mult = list.get(i);
            if (mult.isChoose) {
                temList.add(mult);
            }
        }
        EventBus.getDefault().post(new OverlayChangeEvent(temList));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_control);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("图层");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("确定");
        book = (BookSimple) getIntent().getSerializableExtra("book");
        ArrayList<CommonProjectMedia> multList = (ArrayList<CommonProjectMedia>) getIntent().getSerializableExtra("data");
        DBUtil.useExtraDbByProjectName(this, getIntent().getStringExtra("projectName"));
        list.addAll(LitePal.where("project_id = ?", book.projectId + "").find(CommonProjectMedia.class));
        if (null != multList && null != list) {
            for (int i = 0; i < list.size(); i++) {
                CommonProjectMedia mult = list.get(i);
                for (int j = 0; j < multList.size(); j++) {
                    CommonProjectMedia cmult = multList.get(j);
                    if (mult.mediaPath.equals(cmult.mediaPath)) {
                        //路径一样，说明选中了
                        mult.isChoose = true;
                    }
                }
            }
        }
        adapter = new OverlayAdapter(this, list);
        gridView.setAdapter(adapter);
        adapter.setOnClickListener(new OverlayAdapter.OnClick() {
            @Override
            public void onClick(int position) {
                list.get(position).isChoose = !list.get(position).isChoose;
                adapter.notifyDataSetChanged();

            }
        });
    }
}
