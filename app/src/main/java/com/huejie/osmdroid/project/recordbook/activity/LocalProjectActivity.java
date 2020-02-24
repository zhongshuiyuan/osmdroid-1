package com.huejie.osmdroid.project.recordbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.model.SimpleProject;
import com.huejie.osmdroid.project.adapter.ChooseProjectAdapter;
import com.huejie.osmdroid.util.DBUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalProjectActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycleView)
    LRecyclerView recycleView;

    private List<SimpleProject> list = new ArrayList<>();
    private ChooseProjectAdapter adapter;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_project);
        ButterKnife.bind(this);
        initView();
    }

    private long projectId;

    private void initView() {
        tv_title.setText("切换项目");
        projectId = getIntent().getLongExtra("projectId", -1);
        //先获取本地数据库的数据
        adapter = new ChooseProjectAdapter(this, list, false);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recycleView.setAdapter(mLRecyclerViewAdapter);
//        recycleView.setRefreshHeader(null);
        //设置底部刷新样式及颜色
        recycleView.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.white);
        recycleView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //设置不能下拉刷新
        recycleView.setPullRefreshEnabled(false);
        // recycleView.setLoadMoreEnabled(false);
        recycleView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        recycleView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getLocalData();


            }
        });
        adapter.setOnClickListener(new ChooseProjectAdapter.OnClickListener() {

            @Override
            public void onClick(int position) {
                setResult(RESULT_OK, new Intent().putExtra("result", list.get(position)));
                finish();
            }
        });
        getLocalData();
    }

    private int page = 0;

    private void getLocalData() {
        DBUtil.useProjectDatabases(this);
        int total = LitePal.count(SimpleProject.class);
        list.addAll(LitePal.limit(15).offset(15 * (page)).find(SimpleProject.class));
        for (int i = 0; i < list.size(); i++) {
            SimpleProject project = list.get(i);
            project.isCheck = project.projectId == projectId;
        }
        adapter.notifyDataSetChanged();
        page++;
        recycleView.setNoMore(list.size() >= total);
        recycleView.setLoadMoreEnabled(list.size() < total);
    }
}
