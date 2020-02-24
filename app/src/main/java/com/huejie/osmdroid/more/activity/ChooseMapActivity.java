package com.huejie.osmdroid.more.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.manager.MapViewManager;
import com.huejie.osmdroid.model.SimpleChooseModel;
import com.huejie.osmdroid.more.adapter.MapChooseAdapter;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseMapActivity extends AppCompatActivity {
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


    private String[] mapType = new String[]{"Google街道图", "Google卫星图", "Google地形图", "天地图街道图", "天地图卫星图", "天地图地形图", "腾讯地图", "Bing地图"};
    private List<SimpleChooseModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_map);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("选择地图");
        tv_right.setVisibility(View.GONE);
        String cMap = Util.mapTranslateOver(AppContext.sp.getString(Config.CURRENT_MAP));
        for (int i = 0; i < mapType.length; i++) {
            list.add(new SimpleChooseModel(mapType[i], mapType[i].equals(cMap)));
        }
        final MapChooseAdapter adapter = new MapChooseAdapter(this, list);
        listView.setAdapter(adapter);
        adapter.setOnClickListener(new MapChooseAdapter.OnClick() {
            @Override
            public void onClick(int position) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).isChoose = position == i;
                }
                AppContext.sp.putString(Config.CURRENT_MAP, Util.mapTranslate(list.get(position).name));
                MapViewManager.getInstance().setCurrentMap(Util.mapTranslate(list.get(position).name));
                adapter.notifyDataSetChanged();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
