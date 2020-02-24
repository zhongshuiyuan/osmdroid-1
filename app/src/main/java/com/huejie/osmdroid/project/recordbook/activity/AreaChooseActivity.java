package com.huejie.osmdroid.project.recordbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.project.recordbook.adapter.AreaAdapter;
import com.huejie.osmdroid.project.recordbook.model.City;
import com.huejie.osmdroid.project.recordbook.model.Province;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AreaChooseActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    private AreaAdapter adapter;
    List<Province> provinceList;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void comfirm() {
        ArrayList<City> result = new ArrayList<>();
        for (int i = 0; i < provinceList.size(); i++) {
            List<City> cList = provinceList.get(i).cityList;
            for (int j = 0; j < cList.size(); j++) {
                City city = cList.get(j);
                if (city.isCheck) {
                    result.add(city);
                }
            }
        }
        setResult(RESULT_OK, new Intent().putExtra("data", result));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_choose);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("区域选择");
        provinceList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Province province = new Province();
            province.provinceName = "湖北省" + i;
            province.cityList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                City city = new City();
                city.cityName = "武汉" + j;
                province.cityList.add(city);
            }
            provinceList.add(province);

        }


        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AreaAdapter(this, provinceList);
        recycleView.setAdapter(adapter);
        adapter.setOnParentClickListener(new AreaAdapter.OnParentClickListener() {
            @Override
            public void onParentClick(int position) {
                provinceList.get(position).isCheck = !provinceList.get(position).isCheck;
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setOnClickListener(new AreaAdapter.OnClickListener() {
            @Override
            public void onClick(int parent, int position) {
                provinceList.get(parent).cityList.get(position).isCheck = !provinceList.get(parent).cityList.get(position).isCheck;
                adapter.notifyDataSetChanged();
            }
        });
    }
}
