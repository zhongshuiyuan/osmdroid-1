package com.huejie.osmdroid.more.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.dialog.InputFileNameDialog;
import com.huejie.osmdroid.model.favorites.Favorites;
import com.huejie.osmdroid.model.favorites.FavoritesAccessories;
import com.huejie.osmdroid.model.favorites.FavoritesLatLng;
import com.huejie.osmdroid.more.adapter.FavoritesAdapter;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huejie.osmdroid.more.activity.MarkSettingActivity.ACTION_UPDATE;
import static com.huejie.osmdroid.more.activity.MarkSettingActivity.FROM_ACTION;

public class FavoritesActivity extends BaseActivity {
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
    private FavoritesAdapter adapter;

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
        list.addAll(LitePal.where("parentName = ?", currentParentName).find(Favorites.class));
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_right)
    public void add() {
        final InputFileNameDialog dialog = InputFileNameDialog.getInstance(this);
        dialog.setTitle("新建文件夹");
        dialog.setOnclick(new InputFileNameDialog.OnClick() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onConfirm(String name) {
                //新建目录
                if (LitePal.where("name = ? and parentName = ? and fileType = ?", name, currentParentName, "1").count(Favorites.class) > 0) {
                    ToastUtils.showShortToast("名称重复，请重新输入");
                    return;
                }
                Favorites favorites = new Favorites();
                favorites.fileType = 1;
                favorites.name = name;
                favorites.parentName = currentParentName;
                if (favorites.save()) {
                    list.add(0, favorites);
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public List<Favorites> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        initView();
    }

    private String currentParentName;

    private void initView() {
        DBUtil.useLocal();
        tv_title.setText(Config.Favorites.LOCAL_FAVOTIRES);
        tv_right.setText("新建");
        tv_top.setText(Config.Favorites.LOCAL_FAVOTIRES + "[" + LitePal.where("fileType = ?", "0").count(Favorites.class) + "]");
        tv_right.setVisibility(View.VISIBLE);
        currentParentName = Config.Favorites.LOCAL_FAVOTIRES;
        list.addAll(LitePal.where("parentName = ?", currentParentName).find(Favorites.class));
        adapter = new FavoritesAdapter(this, list);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        adapter.setOnClickListener(new FavoritesAdapter.OnClickListener() {
            @Override
            public void onFileClick(int position) {
                //文件点击
                startActivityForResult(new Intent(FavoritesActivity.this, MapFavotiresActivity.class).putExtra("data", list.get(position)).putExtra(FROM_ACTION, ACTION_UPDATE), 100);

            }

            @Override
            public void onFloderClick(int position) {
                //目录点击
                String name = list.get(position).name;
                currentParentName = currentParentName + "/" + name;
                tabList.add(currentParentName);
                addTab();
                list.clear();
                list.addAll(LitePal.where("parentName = ?", currentParentName).find(Favorites.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFileStatusClick(int position) {
                //显示隐藏文件夹
                Favorites favorites = list.get(position);
                favorites.fileStatus = favorites.fileStatus == 0 ? 1 : 0;
                if (favorites.saveOrUpdate("id = ?", favorites.id + "")) {
                    adapter.notifyDataSetChanged();
                }
            }

        });
        adapter.setOnLongClickListener(new FavoritesAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(final int position) {

                AlertDialog dialog = new AlertDialog.Builder(FavoritesActivity.this).setTitle("提示").setMessage("确认删除该标签吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Favorites fav = list.get(position);
                        //删除标签或者删除目录
                        deleteFolder(fav);
                        list.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, list.size());
                        dialog.dismiss();
                    }
                }).create();
                dialog.show();

            }
        });

    }

    public void deleteFolder(Favorites favorites) {
        if (favorites.fileType == 0) {
            LitePal.deleteAll(FavoritesLatLng.class, "favoritesName = ? and parentName = ?", favorites.name, favorites.parentName);
            LitePal.deleteAll(FavoritesAccessories.class, "favoritesName = ? and parentName = ?", favorites.name, favorites.parentName);
            LitePal.delete(Favorites.class, favorites.id);
        } else {
            List<Favorites> fList = LitePal.where("name = ? and parentName = ?", favorites.name, favorites.parentName + "/" + favorites.name).find(Favorites.class);
            if (null != fList && fList.size() > 0) {
                for (int i = 0; i < fList.size(); i++) {
                    deleteFolder(fList.get(i));
                }
            }
            LitePal.delete(Favorites.class, favorites.id);

        }
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
                    list.addAll(LitePal.where("parentName = ?", currentParentName).find(Favorites.class));
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            list.clear();
            list.addAll(LitePal.where("parentName = ?", currentParentName).find(Favorites.class));
            adapter.notifyDataSetChanged();
        }
    }
}
