package com.huejie.osmdroid.more.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.model.favorites.Favorites;
import com.huejie.osmdroid.more.adapter.ChooseFolderAdapter;
import com.huejie.osmdroid.util.Config;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseFolderActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.sub_layout)
    LinearLayout sub_layout;
    @BindView(R.id.tv_top)
    TextView tv_top;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    public List<Favorites> list = new ArrayList<>();
    private String currentParentName;
    private ChooseFolderAdapter adapter;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_top)
    public void root() {
        sub_layout.removeAllViews();
        tabList.clear();
        currentParentName = Config.Favorites.LOCAL_FAVOTIRES;
        list.clear();
        list.addAll(LitePal.where("parentName = ? and fileType = ?", currentParentName, "1").find(Favorites.class));
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_right)
    public void confirm() {
        setResult(RESULT_OK, new Intent().putExtra("result", chooseFolder));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        initView();
    }

    private String chooseFolder = Config.Favorites.LOCAL_FAVOTIRES;

    private void initView() {
        tv_title.setText(Config.Favorites.LOCAL_FAVOTIRES);
        tv_right.setText("确定");
        tv_top.setText(Config.Favorites.LOCAL_FAVOTIRES);
        tv_right.setVisibility(View.VISIBLE);
        currentParentName = Config.Favorites.LOCAL_FAVOTIRES;
        list.addAll(LitePal.where("parentName = ? and fileType = ?", currentParentName, "1").find(Favorites.class));
        adapter = new ChooseFolderAdapter(this, list);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        adapter.setOnClickListener(new ChooseFolderAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                //文件夹点击
                String name = list.get(position).name;
                currentParentName = currentParentName + "/" + name;
                tabList.add(currentParentName);
                addTab();
                list.clear();
                list.addAll(LitePal.where("parentName = ? and fileType = ?", currentParentName, "1").find(Favorites.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCheck(int position) {
                for (int i = 0; i < list.size(); i++) {
                    Favorites fav = list.get(i);
                    fav.isChoose = position == i;
                }
                chooseFolder = list.get(position).parentName + "/" + list.get(position).name;
                adapter.notifyDataSetChanged();
            }
        });
    }

    private List<String> tabList = new ArrayList<>();

    public void addTab() {
        sub_layout.removeAllViews();
        for (int i = 0; i < tabList.size(); i++) {
            TextView filename = (TextView) getLayoutInflater().inflate(com.huajie.fileexplore.R.layout.view_path, null);
            String name = tabList.get(i);
            filename.setText(name.substring(name.lastIndexOf("/") + 1, name.length()));
            filename.setTag(i);
            sub_layout.addView(filename);
            filename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tabSize = tabList.size();
                    int tag = (int) v.getTag();
                    for (int j = tabSize - 1; j > tag; j--) {
                        tabList.remove(j);
                    }
                    addTab();
                    //重绘文件列表
                    currentParentName = tabList.get(tabList.size() - 1);
                    //重新加载数据
                    list.clear();
                    list.addAll(LitePal.where("parentName = ? and fileType = ?", currentParentName, "1").find(Favorites.class));
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

}
