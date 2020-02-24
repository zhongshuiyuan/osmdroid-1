package com.huejie.osmdroid.more.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.huajie.fileexport.activity.FilesActivity;
import com.huajie.fileexport.entity.FileInfo;
import com.huejie.osmdroid.BuildConfig;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.AnnexLibraryActivity;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.dialog.ChooseFileDialog;
import com.huejie.osmdroid.model.favorites.Favorites;
import com.huejie.osmdroid.model.favorites.FavoritesAccessories;
import com.huejie.osmdroid.model.favorites.FavoritesLatLng;
import com.huejie.osmdroid.more.adapter.LocalFilesAdapter;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.Util;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.ColorPicker;

public class MarkSettingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_color_show)
    TextView tv_color_show;
    @BindView(R.id.tv_filename)
    TextView tv_filename;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_remark)
    EditText et_remark;
    @BindView(R.id.ll_choose_file)
    View ll_choose_file;
    @BindView(R.id.ll_fill_choose_color)
    View ll_fill_choose_color;
    @BindView(R.id.tv_fill_color_show)
    TextView tv_fill_color_show;
    @BindView(R.id.recycleView)
    LRecyclerView recycleView;

    @BindView(R.id.view_show_width)
    LinearLayout view_show_width;
    @BindView(R.id.ll_choose_icon)
    LinearLayout ll_choose_icon;
    @BindView(R.id.ll_line)
    LinearLayout ll_line;
    @BindView(R.id.seek_bar)
    SeekBar seek_bar;
    @BindView(R.id.seek_bar_alpha)
    SeekBar seek_bar_alpha;
    @BindView(R.id.tv_show_alpha)
    TextView tv_show_alpha;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.tv_progress_alpha)
    TextView tv_progress_alpha;
    @BindView(R.id.tv_folder)
    TextView tv_folder;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;

    private LocalFilesAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.ll_choose_folder)
    public void chooseFolder() {
        //选择文件夹
        startActivityForResult(new Intent(this, ChooseFolderActivity.class), 101);

    }

    @OnClick(R.id.ll_choose_icon)
    public void setLl_choose_icon() {
        //选择图标
        startActivityForResult(new Intent(this, IconChooseActivity.class), 102);

    }

    public static final String ACTION_ADD = "action_add";
    public static final String ACTION_UPDATE = "action_update";
    public static final String FROM_ACTION = "fromAction";

    @OnClick(R.id.tv_right)
    public void confirm() {
        DBUtil.useLocal();
        String name = et_name.getText().toString().trim();
        String fName = tv_folder.getText().toString().trim();
        String parentName = TextUtils.isEmpty(fName) ? Config.Favorites.LOCAL_FAVOTIRES : fName;
        if (action.equals(ACTION_UPDATE)) {
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShortToast("请输入名称");
                return;
            }
            if (LitePal.where("name = ? and id != ?", name, favorites.id + "").count(Favorites.class) > 0) {
                ToastUtils.showShortToast("名称重复，请重新输入");
                return;
            }
            List<FavoritesLatLng> latlngList = LitePal.where("favoritesName = ?", favorites.name).find(FavoritesLatLng.class);
            //更新坐标集
            for (int i = 0; i < latlngList.size(); i++) {
                FavoritesLatLng latLng = latlngList.get(i);
                latLng.favoritesName = name;
                latLng.parentName = parentName;
                latLng.saveOrUpdate("id = ?", latLng.id + "");
            }
            for (int i = 0; i < favorites.accessoriesList.size(); i++) {
                FavoritesAccessories file = favorites.accessoriesList.get(i);
                file.favoritesName = name;
                file.parentName = parentName;
                file.saveOrUpdate("id = ?", file.id + "");
            }
            favorites.name = name;
            favorites.parentName = parentName;
            favorites.remark = et_remark.getText().toString().trim();
            favorites.saveOrUpdate("id = ?", favorites.id + "");
        } else if (action.equals(ACTION_ADD)) {
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShortToast("请输入名称");
                return;
            }
            if (LitePal.where("name = ? and parentName = ?", name, parentName).count(Favorites.class) > 0) {
                ToastUtils.showShortToast("名称重复，请重新输入");
                return;
            }
            favorites.name = name;
            favorites.remark = et_remark.getText().toString().trim();
            favorites.parentName = parentName;
            favorites.save();

            FavoritesLatLng latLng = favorites.points;
            latLng.favoritesName = name;
            latLng.parentName = parentName;
            latLng.save();

            if (favorites.accessoriesList != null) {
                for (int i = 0; i < favorites.accessoriesList.size(); i++) {
                    FavoritesAccessories accessories = favorites.accessoriesList.get(i);
                    accessories.favoritesName = name;
                    accessories.parentName = parentName;
                    accessories.save();
                }
            }
        }
        setResult(RESULT_OK);
        finish();

    }

    @OnClick(R.id.ll_choose_file)
    public void chooseFile() {
        ChooseFileDialog.init(this);
        final ChooseFileDialog dialog = ChooseFileDialog.getInstance();
        dialog.setOnclick(new ChooseFileDialog.OnClick() {
            @Override
            public void fromFile() {
                startActivityForResult(new Intent(MarkSettingActivity.this, FilesActivity.class), 100);
                dialog.dismiss();
            }

            @Override
            public void fromFjLib() {
                startActivityForResult(new Intent(MarkSettingActivity.this, AnnexLibraryActivity.class), 103);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @OnClick(R.id.ll_choose_color)
    public void chooseColor() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
                tv_color_show.setBackgroundColor(pickedColor);
                tv_show_alpha.setBackgroundColor(ColorUtils.setAlphaComponent(pickedColor, favorites.alpha));
                favorites.color = pickedColor;
            }
        });
        colorPicker.show();
    }

    @OnClick(R.id.ll_fill_choose_color)
    public void fillChooseColor() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
                tv_fill_color_show.setBackgroundColor(pickedColor);
                favorites.fillColor = pickedColor;
            }
        });
        colorPicker.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_setting);
        ButterKnife.bind(this);
        initView();
    }

    private Favorites favorites;
    private String action;


    private void initView() {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("确定");
        tv_title.setText("属性设置");
        favorites = (Favorites) getIntent().getSerializableExtra("data");
        et_name.setText(favorites.name);
        et_remark.setText(favorites.remark);
        tv_folder.setText(favorites.parentName);
        if (favorites.type == 0) {
            //点，就显示更改图标功能
            ll_choose_icon.setVisibility(View.VISIBLE);
            ll_line.setVisibility(View.GONE);
            iv_icon.setImageResource(favorites.icon == -1 ? R.mipmap.marker_icon_1 : favorites.icon);

        } else if (favorites.type == 1) {
            //线，显示线宽，颜色
            ll_choose_icon.setVisibility(View.GONE);
            ll_line.setVisibility(View.VISIBLE);
            ll_fill_choose_color.setVisibility(View.GONE);
            tv_color_show.setBackgroundColor(favorites.color);
            view_show_width.setBackgroundColor(favorites.color);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeUtils.dp2px(20), favorites.width);
            view_show_width.setLayoutParams(params);
            seek_bar.setProgress(favorites.width);
            tv_progress.setText(favorites.width + "");
            tv_show_alpha.setBackgroundColor(ColorUtils.setAlphaComponent(favorites.color, favorites.alpha));
            seek_bar_alpha.setProgress(favorites.alpha);
            tv_progress_alpha.setText(favorites.alpha + "");

        } else if (favorites.type == 2 || favorites.type == 3) {
            ll_choose_icon.setVisibility(View.GONE);
            ll_line.setVisibility(View.VISIBLE);
            ll_fill_choose_color.setVisibility(View.VISIBLE);

            tv_color_show.setBackgroundColor(favorites.color);
            view_show_width.setBackgroundColor(favorites.color);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeUtils.dp2px(20), favorites.width);
            view_show_width.setLayoutParams(params);
            seek_bar.setProgress(favorites.width);
            tv_progress.setText(favorites.width + "");
            tv_show_alpha.setBackgroundColor(ColorUtils.setAlphaComponent(favorites.fillColor, favorites.alpha));
            seek_bar_alpha.setProgress(favorites.alpha);
            tv_progress_alpha.setText(favorites.alpha + "");
        }

        action = getIntent().getStringExtra(FROM_ACTION);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeUtils.dp2px(20), progress);
                view_show_width.setLayoutParams(params);
                tv_progress.setText(progress + "");
                favorites.width = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seek_bar_alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (favorites.type == 1) {
                    tv_show_alpha.setBackgroundColor(ColorUtils.setAlphaComponent(favorites.color, progress));
                } else if (favorites.type == 2 || favorites.type == 3) {
                    tv_show_alpha.setBackgroundColor(ColorUtils.setAlphaComponent(favorites.fillColor, progress));
                }
                tv_progress_alpha.setText(progress + "");
                favorites.alpha = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        adapter = new LocalFilesAdapter(this, favorites.accessoriesList, true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnClickListener(new LocalFilesAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                //打开文件
                openFile(new File(favorites.accessoriesList.get(position).path));
            }

            @Override
            public void onDel(int position) {
                if (action.equals(ACTION_UPDATE)) {
                    LitePal.delete(FavoritesAccessories.class, favorites.accessoriesList.get(position).id);
                }
                favorites.accessoriesList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(0, favorites.accessoriesList.size());

            }
        });

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recycleView.setAdapter(mLRecyclerViewAdapter);
        //设置底部刷新样式及颜色
        recycleView.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.white);
        recycleView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //设置不能下拉刷新
        recycleView.setPullRefreshEnabled(false);
        recycleView.setLoadMoreEnabled(false);
        recycleView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

    }

    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        /* 调用getMIMEType()来取得MimeType */
        String type = getMIMEType(f);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(MarkSettingActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", f);
            intent.setDataAndType(contentUri, type);
        } else {
            intent.setDataAndType(Uri.fromFile(f), type);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            startActivity(intent);
            /* 设置intent的file与MimeType */

        } catch (Exception e) {
            if (e instanceof ActivityNotFoundException) {
                ToastUtils.showShortToast("没有找到与之相关的应用");
            }
        }
    }

    /* 判断文件MimeType的method */
    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".")
                + 1, fName.length()).toLowerCase();
        return Util.getMIMEType(this, end);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                ArrayList<FileInfo> temList = (ArrayList<FileInfo>) data.getSerializableExtra("files");
                for (int i = 0; i < temList.size(); i++) {
                    FileInfo temFile = temList.get(i);
                    boolean has = false;
                    for (int j = 0; j < favorites.accessoriesList.size(); j++) {
                        if (temFile.filePath.equals(favorites.accessoriesList.get(j).path)) {
                            has = true;
                        }
                    }
                    if (!has) {
                        FavoritesAccessories accessories = new FavoritesAccessories();
                        accessories.path = temFile.filePath;
                        accessories.size = temFile.fileSize;
                        accessories.accessoriesName = FileUtils.getFileName(temFile.filePath);
                        long time = System.currentTimeMillis();
                        accessories.createTime = time;
                        accessories.updateTime = time;
                        favorites.accessoriesList.add(accessories);
                    }
                }
                adapter.notifyDataSetChanged();
                tv_filename.setText("共" + favorites.accessoriesList.size() + "个文件");
            } else if (requestCode == 101) {
                if (null != data) {
                    tv_folder.setText(data.getStringExtra("result"));
                }
            } else if (requestCode == 102) {
                if (null != data) {
                    int icon = data.getIntExtra("result", -1);
                    iv_icon.setImageResource(icon);
                    favorites.icon = icon;
                }
            } else if (requestCode == 103) {
                FavoritesAccessories fav = (FavoritesAccessories) data.getSerializableExtra("result");
                for (int i = 0; i < favorites.accessoriesList.size(); i++) {
                    if (favorites.accessoriesList.get(i).path.equals(fav.path)) {
                        return;
                    }

                }
                favorites.accessoriesList.add(fav);
                adapter.notifyDataSetChanged();


            }
        }
    }

}
