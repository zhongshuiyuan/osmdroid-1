package com.huejie.osmdroid.project.recordbook.activity;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.LocationUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.dialog.DownloadSettingDialog;
import com.huejie.osmdroid.listener.SimpleOnLocationChangeListener;
import com.huejie.osmdroid.listener.SimpleStyler;
import com.huejie.osmdroid.manager.MapViewManager;
import com.huejie.osmdroid.model.CoreProject;
import com.huejie.osmdroid.model.SimpleProject;
import com.huejie.osmdroid.model.basic.RecordBookType;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.more.mapdownload.MapDownloader;
import com.huejie.osmdroid.more.mapdownload.MyCacheManager;
import com.huejie.osmdroid.overlay.CustomIconOverlay;
import com.huejie.osmdroid.tileprovider.MySqlTileWriter;
import com.huejie.osmdroid.tileprovider.MyTileProvider;
import com.huejie.osmdroid.tilesource.TianDiTuTileSource;
import com.huejie.osmdroid.util.BookUtils;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.Util;
import com.huejie.osmdroid.view.rect.model.DrawBaseModel;
import com.huejie.osmdroid.view.rect.view.DrawPicView;

import org.bonuspack.kml.KmlDocument;
import org.litepal.LitePal;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.modules.OfflineTileProvider;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.CompressDirectedLocationOverlay;
import org.osmdroid.views.util.GISUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RectMapDownloadActivity extends BaseActivity {

    public static final int ACTION_DOWNLOAD_MAP = 1;
    public static final int ACTION_SHOW_LINE = 2;
    private Marker marker;
    private TilesOverlay tianCvaTile;


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.drawPicView)
    DrawPicView drawPicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_edit);
        ButterKnife.bind(this);
        initView();
    }

    private PointF startPoint;
    private boolean status;
    int actionType;

    private void initView() {
        actionType = getIntent().getIntExtra("actionType", -1);
        if (actionType == ACTION_DOWNLOAD_MAP) {
            tv_title.setText("离线地图下载");
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText("开始");
            initMap();
            tv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status = !status;
                    if (status) {
                        drawPicView.setVisibility(View.VISIBLE);
                        tv_right.setText("结束");
                    } else {
                        drawPicView.setVisibility(View.GONE);
                        tv_right.setText("开始");
                    }

                }
            });
            drawPicView.setType(DrawBaseModel.TPEY_RECT);
            drawPicView.setOnStartAndCompletionListener(new DrawPicView.OnStartAndCompletionListener() {
                @Override
                public void onComplete(PointF pointF) {
                    drawPicView.clean();
                    IGeoPoint sPoint = mapView.getProjection().fromPixels((int) startPoint.x, (int) startPoint.y);
                    IGeoPoint ePoint = mapView.getProjection().fromPixels((int) pointF.x, (int) pointF.y);
                    double n = sPoint.getLatitude();
                    double e = sPoint.getLongitude();
                    double s = ePoint.getLatitude();
                    double w = ePoint.getLongitude();

                    //北东南西
                    final BoundingBox bb = new BoundingBox(n > s ? n : s, e > w ? e : w, n < s ? n : s, e < w ? e : w);//北东南西
                    int total = MyCacheManager.getTilesCoverage(bb, 1, getIntent().getIntExtra("zoomLevel", 0)).size();

                    final DownloadSettingDialog dialog = DownloadSettingDialog.getInstance(RectMapDownloadActivity.this);
                    dialog.setSeekBarProgress(total + "", ((15 * total) / 1024) + "MB");
                    dialog.setOnclick(new DownloadSettingDialog.OnClick() {
                        @Override
                        public void onCancel() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onConfirm() {
                            MapDownloader mapDownloader = new MapDownloader(RectMapDownloadActivity.this, bb, getIntent().getIntExtra("zoomLevel", 0), getIntent().getStringExtra("mapType"));
                            mapDownloader.showNotificationAndDownload();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }

                @Override
                public void onStart(PointF pointF) {
                    startPoint = pointF;
                }
            });
        } else if (actionType == ACTION_SHOW_LINE) {
            MapViewManager.getInstance().init(this, mapView);
            MapViewManager.getInstance().location();
            tv_title.setText("查看线路");
            DBUtil.useBaseDatabases(this);
            List<RecordBookType> records = LitePal.findAll(RecordBookType.class);
            SimpleProject simpleProject = (SimpleProject) getIntent().getSerializableExtra("project");
            DBUtil.useExtraDbByProjectName(this, simpleProject.projectName);
            CoreProject project = LitePal.where("project_id = ?", simpleProject.projectId + "").findFirst(CoreProject.class);
            if (!TextUtils.isEmpty(project.projectLineInfo)) {
                loadKml(project.projectLineInfo);
            }
            addWorks(project, records);
        }


    }

    private FolderOverlay folderOverlay = new FolderOverlay();

    private void addWorks(CoreProject project, List<RecordBookType> records) {
        //加载所有工点
        folderOverlay.getItems().clear();
        for (int i = 0; i < records.size(); i++) {
            long type = records.get(i).id;
            List<BookSimple> works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(type));
            for (int j = 0; j < works.size(); j++) {
                final BookSimple book = works.get(j);
                if (!TextUtils.isEmpty(book.positionY) && !TextUtils.isEmpty(book.positionX)) {
                    CustomIconOverlay iconOverlay = new CustomIconOverlay(null);
                    iconOverlay.set(new GeoPoint(Double.valueOf(book.positionY), Double.valueOf(book.positionX)), BookUtils.getDrawableByRecordBookTypeId(this, type));
                    iconOverlay.setOnMarkClickListener(new CustomIconOverlay.MarkClickListener() {
                        @Override
                        public boolean onMarkerClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
                            if (book.recordBookTypeId > 2) {
                                ToastUtils.showShortToast("功能待完善");
                                return false;
                            }
                            startActivity(new Intent(RectMapDownloadActivity.this, BookDetailActivity.class).putExtra("book", book));
                            return false;
                        }
                    });
                    folderOverlay.add(iconOverlay);

                }

            }

        }
        mapView.getOverlays().add(folderOverlay);
        mapView.invalidate();

    }


    private void initMap() {
        mapView.setDrawingCacheEnabled(true);
        mapView.setMultiTouchControls(true);// 触控放大缩小
        mapView.setMaxZoomLevel(Double.valueOf(AppContext.sp.getInt(Config.ZDJB)));
        mapView.setMinZoomLevel(MapViewManager.MinZoomLevel);
        mapView.getController().setZoom(12.0);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapView.setUseDataConnection(true);
        //是否显示地图数据源
        mapView.getOverlayManager().getTilesOverlay().setEnabled(true);
        //加载谷歌地图，设置地图数据源的形式
        String mapType = getIntent().getStringExtra("mapType");
        final ITileSource tileSource = Util.getTileSourceByType(mapType);
        mapView.setTileProvider(new MyTileProvider(this, tileSource, new MySqlTileWriter()));
        if (mapType.equals(Config.TileSourceName.TIANDITUROAD) || mapType.equals(Config.TileSourceName.TIANDITUIMAGE) || mapType.equals(Config.TileSourceName.TIANDITUTERRAIN)) {
            tianCvaTile = new TilesOverlay(new MyTileProvider(this, new TianDiTuTileSource(Config.TileSourceName.TIANDITUCVA), new MySqlTileWriter()), this);
            mapView.getOverlays().add(0, tianCvaTile);
        }
        //定位
        LocationUtils.register(-1, 10, new SimpleOnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {
                GeoPoint mLocation;
                if (com.huejie.osmdroid.util.GISUtils.judgeMapNeedCorrection(tileSource)) {
                    double[] res = com.huejie.osmdroid.util.GISUtils.transform(location.getLatitude(), location.getLongitude());
                    mLocation = new GeoPoint(res[0], res[1]);
                } else {
                    mLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                }
                //设置导航图标
                CompressDirectedLocationOverlay mLocationOverlay = new CompressDirectedLocationOverlay(RectMapDownloadActivity.this, mapView);
                mLocationOverlay.setAccuracy((int) location.getAccuracy());
                mLocationOverlay.setDirectionArrow(((BitmapDrawable) getResources().getDrawable(R.mipmap.position)).getBitmap());
                mapView.getOverlays().add(mLocationOverlay);
                mLocationOverlay.setLocation(mLocation);
                mapView.getController().setCenter(mLocation);
            }
        });
    }


    private void loadKml(String str) {
        KmlDocument kmlDocument = new KmlDocument();
        kmlDocument.parseGeoJSON(str);
        FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, null, new SimpleStyler(mapView.getTileProvider().getTileSource()), kmlDocument);
        mapView.getOverlays().add(kmlOverlay);
        try {
            BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
            mapView.zoomToBoundingBox(bb, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    public void showMbtile(File file) {
        try {
            OfflineTileProvider tileProvider = new OfflineTileProvider((IRegisterReceiver) new SimpleRegisterReceiver(this), new File[]{file});
            TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, this);
            mapView.getOverlays().add(tilesOverlay);
            mapView.zoomToBoundingBox(GISUtils.getMbtilesBoundingBox(file), false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
