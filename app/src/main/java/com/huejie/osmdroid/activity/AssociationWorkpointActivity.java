package com.huejie.osmdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.basic.RecordBookType;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.model.books.InterchangeRecordBook;
import com.huejie.osmdroid.model.books.RetainingWallRecordBook;
import com.huejie.osmdroid.project.recordbook.adapter.BookAdapter;
import com.huejie.osmdroid.project.recordbook.adapter.BookNewAdapter;
import com.huejie.osmdroid.util.BookUtils;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.DictUtil;

import org.litepal.LitePal;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关联工点
 */

public class AssociationWorkpointActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.listView)
    ListView listView;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    private BookSimple result;

    @OnClick(R.id.tv_right)
    public void confirm() {
        if (null != result) {
            GeoPoint point = (GeoPoint) getIntent().getSerializableExtra("point");
            result.positionY = point.getLatitude() + "";
            result.positionX = point.getLongitude() + "";
            setResult(RESULT_OK, new Intent().putExtra("result", result));
        }
        finish();
    }

    private BookNewAdapter adapter;

    private List<RecordBookType> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association_workpoint);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("关联工点");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("确定");
        String projectId = getIntent().getStringExtra("projectId");
        DBUtil.useBaseDatabases(this);
        list = LitePal.findAll(RecordBookType.class);
        for (int i = 0; i < list.size(); i++) {
            DBUtil.useExtraDbByProjectName(this, DBUtil.getProjectNameById(this, projectId));
            list.get(i).list = LitePal.where("project_id = ?", projectId).find(BookUtils.getClassByType(list.get(i).id));
            for (int j = 0; j < list.get(i).list.size(); j++) {
                BookSimple workPoint = list.get(i).list.get(j);
                switch (list.get(i).recordBookTypeCode) {
                    case "RetainingWallRecordBook":
                        RetainingWallRecordBook retainingWallRecordBook = (RetainingWallRecordBook) workPoint;
                        retainingWallRecordBook.uniqueTag = retainingWallRecordBook.startStake + " - " + retainingWallRecordBook.endStake + " - " + DictUtil.getDictLabelByCode(AssociationWorkpointActivity.this, DictUtil.WALL_TYPE, retainingWallRecordBook.wallType);
                        break;
                    case "InterchangeRecordBook":
                        InterchangeRecordBook interchangeRecordBook = (InterchangeRecordBook) workPoint;
                        interchangeRecordBook.uniqueTag = interchangeRecordBook.crossStake + " - " + interchangeRecordBook.name;
                        break;

                }

            }
        }
        adapter = new BookNewAdapter(this, list, 1, false);
        listView.setAdapter(adapter);
        //在线项目，只能拷贝项目到本地
        adapter.setOnParentClickListener(new BookNewAdapter.OnParentClickListener() {
            @Override
            public void onParentClick(int position) {
                list.get(position).isCheck = !list.get(position).isCheck;
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onAddClick(int position) {

            }
        });
        adapter.setOnClickListener(new BookNewAdapter.OnClickListener() {
            @Override
            public void onClick(int parent, int position, BookAdapter adapter) {
                for (int i = 0; i < list.size(); i++) {
                    List<BookSimple> child = list.get(i).list;
                    for (int j = 0; j < child.size(); j++) {
                        if (i == parent && j == position) {
                            child.get(j).isCheck = true;
                            result = list.get(parent).list.get(position);
                        } else {
                            child.get(j).isCheck = false;
                        }
                    }

                }
                adapter.notifyDataSetChanged();
            }
        });

    }


}
