package com.huejie.osmdroid.project.recordbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.model.basic.RecordBookType;
import com.huejie.osmdroid.project.recordbook.adapter.ChooseWorkPointAdapter;
import com.huejie.osmdroid.util.DBUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseWorkPointActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.listView)
    ListView listView;

    List<RecordBookType> list = new ArrayList<>();

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_work_point);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("记录簿选择");
        DBUtil.useBaseDatabases(this);
        list.addAll(LitePal.findAll(RecordBookType.class));

        ChooseWorkPointAdapter adapter = new ChooseWorkPointAdapter(this, list);
        adapter.setOnClickListener(new ChooseWorkPointAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                setResult(RESULT_OK, new Intent().putExtra("result", list.get(position)).putExtra("point", getIntent().getSerializableExtra("point")));
                finish();

            }
        });
        listView.setAdapter(adapter);

    }
}
