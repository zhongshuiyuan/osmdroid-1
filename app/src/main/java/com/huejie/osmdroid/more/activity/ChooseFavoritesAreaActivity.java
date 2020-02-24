package com.huejie.osmdroid.more.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.favorites.Favorites;
import com.huejie.osmdroid.model.favorites.FavoritesLatLng;
import com.huejie.osmdroid.more.adapter.FavoritesChooseAdapter;
import com.huejie.osmdroid.util.DBUtil;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseFavoritesAreaActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_favorites_area);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        DBUtil.useLocal();
        tv_title.setText("区域选择");
        final List<Favorites> favorites = LitePal.where("type > ? and fileType = ?", "1", "0").find(Favorites.class);
        for (int i = 0; i < favorites.size(); i++) {
            Favorites favorite = favorites.get(i);
//            favorite.latLngList = LitePal.where("parentName = ? and favoritesName = ?", favorite.parentName, favorite.name).find(FavoritesLatLng.class);
            favorite.points = LitePal.where("parentName = ? and favoritesName = ?", favorite.parentName, favorite.name).findFirst(FavoritesLatLng.class);

        }
        FavoritesChooseAdapter adapter = new FavoritesChooseAdapter(this, favorites);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        adapter.setOnClickListener(new FavoritesChooseAdapter.OnClickListener() {
            @Override
            public void onFileClick(int position) {
                setResult(RESULT_OK, new Intent().putExtra("result", favorites.get(position)));
                finish();
            }
        });


    }
}
