package com.huejie.osmdroid.more.mapdownload;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.dialog.DownloadSettingDialog;
import com.huejie.osmdroid.model.favorites.Favorites;
import com.huejie.osmdroid.more.activity.ChooseFavoritesAreaActivity;
import com.huejie.osmdroid.project.recordbook.activity.RectMapDownloadActivity;
import com.huejie.osmdroid.project.recordbook.model.City;
import com.huejie.osmdroid.util.Util;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseDownloadTypeActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.tv_area_choose)
    TextView tv_area_choose;
    @BindView(R.id.ll_area)
    View ll_area;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.tv_map_type)
    CheckedTextView tv_map_type;
    @BindView(R.id.tv_jq)
    CheckedTextView tv_jq;
    @BindView(R.id.seek_bar)
    SeekBar seek_bar;
    private String[] mapType = new String[]{"Google街道图", "Google卫星图", "Google地形图", "天地图街道图", "天地图卫星图", "天地图地形图", "腾讯地图", "Bing地图"};
    private String[] suburb = new String[]{"郊区只下载15级", "郊区同市区一样处理"};

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.ll_area_choose)
    public void areaChoose() {
        //这段代码不要删，以后有区域数据了再开始做
        //  startActivityForResult(new Intent(this, AreaChooseActivity.class), 100);
        startActivityForResult(new Intent(this, ChooseFavoritesAreaActivity.class), 101);


    }

    @OnClick(R.id.tv_map_type)
    public void mapTypeChoose() {
        Util.showPopwindow(this, tv_map_type, mapType, "");

    }

    @OnClick(R.id.tv_jq)
    public void suburbChoose() {
        Util.showPopwindow(this, tv_jq, suburb, "");
    }

    @OnClick(R.id.tv_right)
    public void download() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_choose:
                //北东南西
                if (favotite != null && favotite.points != null) {
                    List<GeoPoint> geoPoints = Util.string2geoPoints(favotite.points.coordinates);
                    final BoundingBox bb = BoundingBox.fromGeoPoints(geoPoints);
                    int total = MyCacheManager.getTilesCoverage(bb, 1, seek_bar.getProgress()).size();
                    final DownloadSettingDialog dialog = DownloadSettingDialog.getInstance(this);
                    dialog.setSeekBarProgress(total + "", ((15 * total) / 1024) + "MB");
                    dialog.setOnclick(new DownloadSettingDialog.OnClick() {
                        @Override
                        public void onCancel() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirm() {
                            MapDownloader mapDownloader = new MapDownloader(ChooseDownloadTypeActivity.this, bb, seek_bar.getProgress(), Util.mapTranslate(tv_map_type.getText().toString()));
                            mapDownloader.showNotificationAndDownload();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    ToastUtils.showShortToast("请选择要下载的区域");
                }
                break;
            case R.id.rb_ract:
                startActivity(new Intent(this, RectMapDownloadActivity.class).putExtra("actionType", RectMapDownloadActivity.ACTION_DOWNLOAD_MAP).putExtra("mapType", Util.mapTranslate(tv_map_type.getText().toString())).putExtra("zoomLevel", seek_bar.getProgress()));
                break;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_download_type);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("地图下载");
        tv_right.setText("下载");
        tv_right.setVisibility(View.VISIBLE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_ract:
//                        seek_bar.setMax(18);
                        ll_area.setVisibility(View.GONE);
                        break;
                    case R.id.rb_choose:
//                        seek_bar.setMax(15);
                        ll_area.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_progress.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    Favorites favotite;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                //区域选好了
                List<City> result = (List<City>) data.getSerializableExtra("data");
            } else if (requestCode == 101) {
                favotite = (Favorites) data.getSerializableExtra("result");
                tv_area_choose.setText(favotite.name);
            }
        }
    }
}
