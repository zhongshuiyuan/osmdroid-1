package com.huejie.osmdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.favorites.FavoritesAccessories;
import com.huejie.osmdroid.more.adapter.LocalFilesAdapter;
import com.huejie.osmdroid.util.DBUtil;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 标签附件库
 */
public class AnnexLibraryActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annex_library);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("标签附件库");
        DBUtil.useLocal();
        final List<FavoritesAccessories> list = LitePal.findAll(FavoritesAccessories.class);
        LocalFilesAdapter adapter = new LocalFilesAdapter(this, list, false);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        adapter.setOnClickListener(new LocalFilesAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                setResult(RESULT_OK, new Intent().putExtra("result", list.get(position)));
                finish();

            }

            @Override
            public void onDel(int position) {

            }
        });
    }
}
