package com.huejie.osmdroid.project.recordbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
import com.huejie.osmdroid.project.recordbook.adapter.PositionAdapter;
import com.huejie.osmdroid.util.DBUtil;

import org.litepal.LitePal;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoosePositionByPictureActivity extends BaseActivity {
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    private PositionAdapter adapter;
    private BookSimple book;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void confirm() {
        book.positionY = chooseBean.gpsLatitude + "";
        book.positionX = chooseBean.gpsLongitude + "";
        if (book.saveOrUpdate("project_id = ? and id = ?", book.projectId + "", book.id + "")) {
            setResult(RESULT_OK, new Intent().putExtra("result", (Serializable) new GeoPoint(Double.valueOf(chooseBean.gpsLatitude), Double.valueOf(chooseBean.gpsLongitude))));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_position_by_picture);
        ButterKnife.bind(this);
        initView();
    }

    private List<CommonRecordBookMedia> list = new ArrayList<>();
    private CommonRecordBookMedia chooseBean;

    private void initView() {
        tv_title.setText("图片选择");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("确定");
        book = (BookSimple) getIntent().getSerializableExtra("book");


        DBUtil.useExtraDbByProjectName(this, getIntent().getStringExtra("projectName"));
        list.addAll(LitePal.where("record_book_id = ? and record_book_type_id = ? and media_type = ?", book.id + "", book.recordBookTypeId + "", getIntent().getStringExtra("mediaType")).find(CommonRecordBookMedia.class));
        adapter = new PositionAdapter(this, list);
        adapter.setOnClickListener(new PositionAdapter.OnClick() {
            @Override
            public void onClick(int position) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).isSelect = i == position;
                }
                chooseBean = list.get(position);
                adapter.notifyDataSetChanged();
            }
        });
        gridView.setAdapter(adapter);

    }
}
