package com.huejie.osmdroid.project.recordbook.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.event.OverlayChangeEvent;
import com.huejie.osmdroid.listener.SimpleOnLocationChangeListener;
import com.huejie.osmdroid.listener.SimpleStyler;
import com.huejie.osmdroid.manager.MapViewManager;
import com.huejie.osmdroid.model.CommonProjectMedia;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
import com.huejie.osmdroid.model.CoreProject;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.overlay.CustomIconOverlay;
import com.huejie.osmdroid.project.recordbook.adapter.AudioAdapter;
import com.huejie.osmdroid.project.recordbook.adapter.PictureAdapter;
import com.huejie.osmdroid.project.recordbook.adapter.VideoAdapter;
import com.huejie.osmdroid.project.recordbook.bookfragment.BookBaseFragment;
import com.huejie.osmdroid.util.BookUtils;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.DictUtil;
import com.huejie.osmdroid.util.GISUtils;
import com.huejie.osmdroid.util.Util;
import com.huejie.osmdroid.view.AllShowGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.bonuspack.kml.KmlDocument;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.LocationUtils;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.toolsfinal.BitmapUtils;


public class BookDetailActivity extends BaseActivity implements GalleryFinal.OnHanlderResultCallback {
    /**
     * 打开相机
     */
    public static final int REQ_OPEN_CAMERA = 100;
    /**
     * 选择照片
     */
    public static final int REQ_OPEN_PIC_CHOOSE = 101;
    /**
     * 选择录音
     */
    public static final int REQUEST_ADD_SOUND = 103;
    /**
     * 录音
     */
    public static final int REQUEST_LOAD_SOUND = 104;
    /**
     * 选择视频
     */
    public static final int REQUEST_LOAD_FILM = 105;
    /**
     * 选择视频
     */
    public static final int REQUEST_ADD_FILM = 106;
    /**
     * 选择示意图
     */
    public static final int REQUEST_ADD_SHIYITU = 107;
    /**
     * 通过图片选择工点位置信息
     */
    public static final int REQUEST_START_POSITON_BY_PIC = 108;
    /**
     * 图层控制
     */
    public static final int REQUEST_OVERLAY_CONTROL = 109;
    /**
     * 图片展示
     */
    @BindView(R.id.photo_gridview)
    AllShowGridView photo_gridview;
    private PictureAdapter picAdapter;
    /**
     * 音频展示
     */
    @BindView(R.id.lv_recording)
    AllShowGridView lv_recording;
    private AudioAdapter audioAdapter;
    /**
     * 视频展示
     */
    @BindView(R.id.rl_film)
    AllShowGridView rl_film;
    @BindView(R.id.tv_project_name)
    TextView tv_project_name;
    @BindView(R.id.tv_book_name)
    TextView tv_book_name;
    @BindView(R.id.tv_project_code)
    TextView tv_project_code;
    @BindView(R.id.tv_project_createtime)
    TextView tv_project_createtime;
    @BindView(R.id.tv_project_updatetime)
    TextView tv_project_updatetime;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_weather)
    CheckedTextView tv_weather;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_position)
    TextView tv_position;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.ll_map)
    View ll_map;

    private BookSimple book;
    private BookBaseFragment currentFragment;
    private VideoAdapter videoAdapter;
    private long mediaDiagram;
    private long mediaLivePhotos;
    private long mediaRecord;
    private long mediaVideo;

    /**
     * 图片列表
     */
    private ArrayList<CommonRecordBookMedia> photoList = new ArrayList<>();
    /**
     * 音频列表
     */
    private ArrayList<CommonRecordBookMedia> selectAudioList = new ArrayList<>();
    /**
     * 视频列表
     */
    private ArrayList<CommonRecordBookMedia> selectVideoList = new ArrayList<>();
    private String projectName;
    private String currentWorkDir;
    private String mediaPath;
    private CoreProject project;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    private boolean mapShow = false;

    @OnClick(R.id.tv_show_map)
    public void showMap() {
        ll_map.setVisibility(View.VISIBLE);
        if (!mapShow) {
            //获取路线信息
            if (!TextUtils.isEmpty(project.projectLineInfo)) {
                loadKml(project.projectLineInfo);
            }
            //在地图上显示所有多媒体
            loadMediaOverlay();
            mapShow = true;
        }

    }

    @OnClick(R.id.tv_hide_map)
    public void hideMap() {
        ll_map.setVisibility(View.GONE);
    }

    //通过图片选择工点位置
    @OnClick(R.id.iv_pic_location)
    public void picLocation() {
        startActivityForResult(new Intent(this, ChoosePositionByPictureActivity.class).putExtra("book", book).putExtra("projectName", projectName).putExtra("mediaType", mediaDiagram + ""), REQUEST_START_POSITON_BY_PIC);
    }

    //添加覆盖物
    @OnClick(R.id.iv_overlay_location)
    public void overlayLocation() {
        startActivityForResult(new Intent(this, OverlayControlActivity.class).putExtra("book", book).putExtra("projectName", projectName).putExtra("data", multList), REQUEST_OVERLAY_CONTROL);
    }

    @Override
    public void onBackPressed() {
        if (ll_map.getVisibility() == View.VISIBLE) {
            ll_map.setVisibility(View.GONE);
        } else {
            finish();
        }

    }

    //定位获取位置
    @OnClick(R.id.iv_location)
    public void location() {
        MapViewManager.getInstance().location();
        AlertDialog dialog = new AlertDialog.Builder(BookDetailActivity.this).setMessage("是否选取当前位置作为工点位置？").setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                com.blankj.utilcode.utils.LocationUtils.register(-1, 0, new SimpleOnLocationChangeListener() {
                    @Override
                    public void getLastKnownLocation(Location location) {
                        book.positionY = location.getLatitude() + "";
                        book.positionX = location.getLongitude() + "";
                        if (book.saveOrUpdate("project_id = ? and id = ?", book.projectId + "", book.id + "")) {
                            GeoPoint point;
                            if (GISUtils.judgeMapNeedCorrection(mapView.getTileProvider().getTileSource())) {
                                double[] d = GISUtils.transform(location.getLatitude(), location.getLongitude());
                                point = new GeoPoint(d[0], d[1]);
                            } else {
                                point = new GeoPoint(location.getLatitude(), location.getLongitude());
                            }
                            if (marker != null) {
                                marker.moveTo(point, mapView);
                            } else {
                                marker = new CustomIconOverlay(mapView);
                                marker.set(point, getResources().getDrawable(R.mipmap.marker_icon_1));
                                mapView.getOverlays().add(marker);
                            }
                            ToastUtils.showShortToast("success");
                        }
                    }
                });

            }
        }).create();
        dialog.show();

    }

    private CustomIconOverlay marker;

    //十字位置获取位置
    @OnClick(R.id.iv_shizi_location)
    public void shiziLocation() {

        AlertDialog dialog = new AlertDialog.Builder(BookDetailActivity.this).setMessage("是否选取十字位置作为工点位置？").setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GeoPoint location = mapView.getProjection().getCurrentCenter();
                book.positionY = location.getLatitude() + "";
                book.positionX = location.getLongitude() + "";
                DBUtil.useExtraDbByProjectName(BookDetailActivity.this, projectName);
                if (book.saveOrUpdate("project_id = ? and id = ?", book.projectId + "", book.id + "")) {
                /*    GeoPoint point;
                    if (GISUtils.judgeMapNeedCorrection(mapView.getTileProvider().getTileSource())) {
                        double[] d = GISUtils.transform(location.getLatitude(), location.getLongitude());
                        point = new GeoPoint(d[0], d[1]);
                    } else {
                        point = new GeoPoint(location.getLatitude(), location.getLongitude());
                    }*/
                    if (marker != null) {
//                        marker.setInfoWindow(new MarkerInfoWindow());
                        marker.moveTo(location, mapView);
                    } else {
                        marker = new CustomIconOverlay(mapView);
                        marker.set(location, getResources().getDrawable(R.mipmap.marker_icon_1));
                        mapView.getOverlays().add(marker);
                    }
                    ToastUtils.showShortToast("success");
                }
            }
        }).create();
        dialog.show();
    }

    @OnTouch(R.id.ll_map)
    public boolean shadeTouch() {
        ll_map.setVisibility(View.GONE);
        return true;
    }

    @OnClick(R.id.tv_weather)
    public void chooseWeather() {
        Util.showPopwindow(this, tv_weather, DictUtil.getDictLabels(this, DictUtil.WEATHER), "天气：");
    }

    @OnClick(R.id.tv_right)
    public void submit() {
        BookSimple editBook = currentFragment.getBookContent();
        editBook.projectId = book.projectId;
        editBook.updateTime = System.currentTimeMillis();
        if (null != tv_weather.getTag()) {
            editBook.weather = (int) DictUtil.getDictCodeByLabel(this, DictUtil.WEATHER, tv_weather.getTag().toString());
        }
            DBUtil.useExtraDbByProjectName(this, projectName);
        BookUtils.saveBook(editBook);
        ToastUtils.showShortToast("保存成功");
        finish();

    }

    @OnClick(R.id.iv_load_pic)
    public void choosePicture() {
        DBUtil.useExtraDbByProjectName(this, projectName);
        if (photoList.size() < 20) {
            GalleryFinal.openGalleryMuti(REQ_OPEN_PIC_CHOOSE, 20 - photoList.size(), this);
        } else {
            ToastUtils.showShortToast("照片数量不能超过20张");
        }
    }

    @OnClick(R.id.iv_add_pic)
    public void camera() {
        DBUtil.useExtraDbByProjectName(this, projectName);
        if (photoList.size() < 20) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                builder.detectFileUriExposure();
            }
            GalleryFinal.openCamera(REQ_OPEN_CAMERA, this);
        } else {
            ToastUtils.showShortToast("照片数量不能超过20张");
        }
    }


    @OnClick(R.id.iv_load_soundrecord)
    public void selectAudio() {
        DBUtil.useExtraDbByProjectName(this, projectName);
        Intent intent_sound = new Intent(this, AudioSelectActivity.class);
        intent_sound.putExtra("book", book);
        intent_sound.putExtra("mediaType", mediaRecord);
        intent_sound.putExtra("mediaPath", mediaPath);
        intent_sound.putExtra("projectName", projectName);
        startActivityForResult(intent_sound, REQUEST_LOAD_SOUND);
    }

    @OnClick(R.id.iv_add_soundrecord)
    public void addAudio() {
        DBUtil.useExtraDbByProjectName(this, projectName);
        if (selectAudioList.size() >= 10) {
            ToastUtils.showShortToast("音频数量不能超过十个");
            return;
        }
        Intent mi = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        try {
            startActivityForResult(mi, REQUEST_ADD_SOUND);
        } catch (Exception e) {
            ToastUtils.showShortToast("未找到关联应用");
        }

    }

    @OnClick(R.id.iv_load_film)
    public void chooseVideo() {
        DBUtil.useExtraDbByProjectName(this, projectName);
        Intent intent_sound = new Intent(this, VideoSelectActivity.class);
        intent_sound.putExtra("book", book);
        intent_sound.putExtra("mediaType", mediaVideo);
        intent_sound.putExtra("mediaPath", mediaPath);
        intent_sound.putExtra("projectName", projectName);
        startActivityForResult(intent_sound, REQUEST_LOAD_FILM);

    }

    /**
     * 拍视频
     */
    @OnClick(R.id.iv_add_film)
    public void filmRecording() {
        DBUtil.useExtraDbByProjectName(this, projectName);
        if (selectVideoList.size() >= 10) {
            ToastUtils.showShortToast("视频数量不能超过十个");
            return;
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri fileUri = Uri.fromFile(new File(Util.getMediaPathByProjectName(this, currentWorkDir, projectName), Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S) + ".mp4"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        try {
            startActivityForResult(intent, REQUEST_ADD_FILM);
        } catch (Exception e) {
            ToastUtils.showShortToast("未找到关联应用");
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        initView();
    }

    private int mediaProjectLine;
    private int mediaImageMap;
    private int mediaVectorFile;
    private int mediaOther;

    private void initView() {
        tv_right.setText("提交");
        tv_right.setVisibility(View.VISIBLE);
        tv_date.setText(Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D));
        tv_title.setText("记录簿编辑");
        MapViewManager.getInstance().init(this, mapView);
        MapViewManager.getInstance().location().toLocationPosition();
        EventBus.getDefault().register(this);
        mapView.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                GeoPoint location = mapView.getProjection().getCurrentCenter();
                tv_position.setText("经度：" + location.getLongitude() + "  纬度：" + location.getLatitude());
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                return false;
            }
        });

        BookSimple eBook = (BookSimple) getIntent().getSerializableExtra("book");

        tv_book_name.setText(DictUtil.getRecordBookTypeById(this, eBook.recordBookTypeId).recordBookTypeName);
        projectName = DBUtil.getProjectNameById(BookDetailActivity.this, eBook.projectId + "");
        mediaDiagram = DictUtil.getBookMediaCode(BookDetailActivity.this, DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_DIAGRAM);
        mediaLivePhotos = DictUtil.getBookMediaCode(BookDetailActivity.this, DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_LIVE_PHOTOS);
        mediaRecord = DictUtil.getBookMediaCode(BookDetailActivity.this, DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_RECORD);
        mediaVideo = DictUtil.getBookMediaCode(BookDetailActivity.this, DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_VIDEO);

        mediaProjectLine = (int) DictUtil.getBookMediaCode(BookDetailActivity.this, DictUtil.PROJECT_MEDIA_TYPE, DictUtil.LABEL_PROJECT_MEDIA_TYPE_LINE_INFO);
        mediaImageMap = (int) DictUtil.getBookMediaCode(BookDetailActivity.this, DictUtil.PROJECT_MEDIA_TYPE, DictUtil.LABEL_PROJECT_MEDIA_TYPE_IMAGE_MAP);
        mediaVectorFile = (int) DictUtil.getBookMediaCode(BookDetailActivity.this, DictUtil.PROJECT_MEDIA_TYPE, DictUtil.LABEL_PROJECT_MEDIA_TYPE_VECTOR_FILE);
        mediaOther = (int) DictUtil.getBookMediaCode(BookDetailActivity.this, DictUtil.PROJECT_MEDIA_TYPE, DictUtil.LABEL_PROJECT_MEDIA_TYPE_OTHER);


        currentWorkDir = AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR);

        mediaPath = Util.getMediaPathByProjectName(BookDetailActivity.this, currentWorkDir, projectName);

        DBUtil.useExtraDbByProjectName(this, projectName);
        book = (BookSimple) LitePal.find(BookUtils.getClassByType(eBook.recordBookTypeId), eBook.id);

        project = LitePal.where("project_id = ?", book.projectId + "").findFirst(CoreProject.class);
        tv_project_name.setText("项目名称：" + project.projectName);
        tv_project_code.setText("项目编号：" + project.projectCode);
        tv_project_createtime.setText("创建时间：" + Util.getFromatDate(project.projectCreateTime, Util.Y_M_D_H_M_S));
        tv_project_updatetime.setText("更新时间：" + Util.getFromatDate(project.projectUpdateTime, Util.Y_M_D_H_M_S));
        String weather = DictUtil.getDictLabelByCode(this, DictUtil.WEATHER, book.weather);
        tv_weather.setText("天气：" + weather);
        tv_weather.setTag(weather);
        currentFragment = BookUtils.getFragment(this, project.projectName, book);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_flayout, currentFragment).commit();

        DBUtil.useExtraDbByProjectName(this, projectName);
        List<CommonRecordBookMedia> qList = LitePal.where("record_book_id = ? and record_book_type_id = ?", book.id + "", book.recordBookTypeId + "").find(CommonRecordBookMedia.class);
        for (int i = 0; i < qList.size(); i++) {
            CommonRecordBookMedia res = qList.get(i);
            if (res.mediaType == mediaDiagram || res.mediaType == mediaLivePhotos) {
                photoList.add(res);
            } else if (res.mediaType == mediaRecord) {
                selectAudioList.add(res);
            } else if (res.mediaType == mediaVideo) {
                selectVideoList.add(res);
            }
        }

        //查询简图或现场照片
        picAdapter = new PictureAdapter(this, photoList);
        photo_gridview.setAdapter(picAdapter);
        photo_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookDetailActivity.this, PhotoViewActivity.class);
                intent.putExtra("mediaPath", photoList);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        photo_gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //是否删除照片
                AlertDialog dialog = new AlertDialog.Builder(BookDetailActivity.this).setMessage("确定删除该图片吗？").setTitle("警告").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBUtil.useExtraDbByProjectName(BookDetailActivity.this, projectName);
                        if (LitePal.where("media_path = ?", photoList.get(position).mediaPath).count(CommonRecordBookMedia.class) == 1) {
                            FileUtils.deleteFile(Util.getProjectsPath(BookDetailActivity.this, currentWorkDir) + photoList.get(position).mediaPath);
                        }
                        photoList.get(position).delete();
                        photoList.remove(position);
                        picAdapter.notifyDataSetChanged();
                    }
                }).create();
                dialog.show();
                return true;
            }
        });
        //取音频
        audioAdapter = new AudioAdapter(this, selectAudioList);
        lv_recording.setAdapter(audioAdapter);
        audioAdapter.setOnLongClick(new AudioAdapter.OnLongClicked() {
            @Override
            public boolean onLongClick(final int position) {
                //长按选择是否要删除该记录
                AlertDialog dialog = new AlertDialog.Builder(BookDetailActivity.this).setMessage("确定删除该视频吗？").setTitle("警告").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBUtil.useExtraDbByProjectName(BookDetailActivity.this, projectName);
                        if (LitePal.where("media_path = ?", selectAudioList.get(position).mediaPath).count(CommonRecordBookMedia.class) == 1) {
                            FileUtils.deleteFile(Util.getProjectsPath(BookDetailActivity.this, currentWorkDir) + selectAudioList.get(position).mediaPath);
                        }
                        selectAudioList.get(position).delete();
                        selectAudioList.remove(position);
                        audioAdapter.notifyDataSetChanged();
                    }
                }).create();
                dialog.show();
                return true;
            }
        });
        audioAdapter.setOnClick(new AudioAdapter.OnClick() {
            @Override
            public void onPlay(int position) {
                startActivity(new Intent(BookDetailActivity.this, MediaPlayerActivity.class).putExtra("mediaPath", Util.getProjectsPath(BookDetailActivity.this, currentWorkDir) + selectAudioList.get(position).mediaPath));
            }

        });
        //取视频
        videoAdapter = new VideoAdapter(this, selectVideoList);
        videoAdapter.setOnClicked(new VideoAdapter.OnClicked() {
            @Override
            public void onDelete(final int position) {
                AlertDialog dialog = new AlertDialog.Builder(BookDetailActivity.this).setMessage("确定删除该视频吗？").setTitle("警告").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBUtil.useExtraDbByProjectName(BookDetailActivity.this, projectName);
                        if (LitePal.where("media_path = ?", selectVideoList.get(position).mediaPath).count(CommonRecordBookMedia.class) == 1) {
                            //删除视频
                            FileUtils.deleteFile(Util.getProjectsPath(BookDetailActivity.this, currentWorkDir) + selectVideoList.get(position).mediaPath);
                        }
                        if (LitePal.where("thumbnail = ?", selectVideoList.get(position).thumbnail).count(CommonRecordBookMedia.class) == 1) {
                            //删除缩略图
                            FileUtils.deleteFile(Util.getProjectsPath(BookDetailActivity.this, currentWorkDir) + selectVideoList.get(position).thumbnail);
                        }
                        selectVideoList.get(position).delete();
                        selectVideoList.remove(position);
                        videoAdapter.notifyDataSetChanged();
                    }
                }).create();
                dialog.show();
            }

            @Override
            public void onPlay(int position) {
                startActivity(new Intent(BookDetailActivity.this, MediaPlayerActivity.class).putExtra("mediaPath", Util.getProjectsPath(BookDetailActivity.this, currentWorkDir) + selectVideoList.get(position).mediaPath));
            }
        });
        rl_film.setAdapter(videoAdapter);
        //显示工点位置
        if (!TextUtils.isEmpty(book.positionY) && !TextUtils.isEmpty(book.positionX)) {
            marker = new CustomIconOverlay(mapView);
            if (GISUtils.judgeMapNeedCorrection(mapView.getTileProvider().getTileSource())) {
                double[] d = GISUtils.transform(Double.valueOf(book.positionY), Double.valueOf(book.positionX));
                marker.set(new GeoPoint(d[0], d[1]), BookUtils.getDrawableByRecordBookTypeId(this, book.recordBookTypeId));
            } else {
                marker.set(new GeoPoint(Double.valueOf(book.positionY), Double.valueOf(book.positionX)), getResources().getDrawable(R.mipmap.marker_icon_1));

            }

            mapView.getOverlays().add(marker);
        }

    }

    private void loadMediaOverlay() {
        //添加图片到地图上面
        for (int i = 0; i < photoList.size(); i++) {
            CommonRecordBookMedia photo = photoList.get(i);
            if (!TextUtils.isEmpty(photo.gpsLatitude) && !TextUtils.isEmpty(photo.gpsLongitude)) {
                GeoPoint point = new GeoPoint(Double.valueOf(photo.gpsLatitude), Double.valueOf(photo.gpsLongitude));
                CustomIconOverlay iconOverlay = new CustomIconOverlay(photo);
//                iconOverlay.set(point, ConvertUtils.bitmap2Drawable(getResources(), ImageLoader.getInstance().loadImageSync("file://" + curPath + photo.mediaPath, new ImageSize(80, 80))));
                iconOverlay.set(point, getResources().getDrawable(R.mipmap.picture));
                iconOverlay.setOnMarkClickListener(new CustomIconOverlay.MarkClickListener() {
                    @Override
                    public boolean onMarkerClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
                        Intent intent = new Intent(BookDetailActivity.this, PhotoViewActivity.class);
                        ArrayList<CommonRecordBookMedia> list = new ArrayList<>();
                        list.add((CommonRecordBookMedia) markerData);
                        intent.putExtra("mediaPath", list);
                        intent.putExtra("position", 0);
                        startActivity(intent);
                        return false;
                    }
                });
                meidaOverlay.add(iconOverlay);
            }

        }

        for (int i = 0; i < selectAudioList.size(); i++) {
            CommonRecordBookMedia audio = selectAudioList.get(i);
            if (!TextUtils.isEmpty(audio.gpsLatitude) && !TextUtils.isEmpty(audio.gpsLongitude)) {
                GeoPoint point = new GeoPoint(Double.valueOf(audio.gpsLatitude), Double.valueOf(audio.gpsLongitude));
                CustomIconOverlay iconOverlay = new CustomIconOverlay(audio);
                iconOverlay.set(point, getResources().getDrawable(R.mipmap.audio));
                iconOverlay.setOnMarkClickListener(new CustomIconOverlay.MarkClickListener() {
                    @Override
                    public boolean onMarkerClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
                        startActivity(new Intent(BookDetailActivity.this, MediaPlayerActivity.class).putExtra("mediaPath", ((CommonRecordBookMedia) markerData).mediaPath));
                        return false;
                    }
                });
                meidaOverlay.add(iconOverlay);
            }

        }

        for (int i = 0; i < selectVideoList.size(); i++) {
            CommonRecordBookMedia video = selectVideoList.get(i);
            if (!TextUtils.isEmpty(video.gpsLatitude) && !TextUtils.isEmpty(video.gpsLongitude)) {
                GeoPoint point = new GeoPoint(Double.valueOf(video.gpsLatitude), Double.valueOf(video.gpsLongitude));
                CustomIconOverlay iconOverlay = new CustomIconOverlay(video);
                iconOverlay.set(point, getResources().getDrawable(R.mipmap.video));
                iconOverlay.setOnMarkClickListener(new CustomIconOverlay.MarkClickListener() {
                    @Override
                    public boolean onMarkerClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
                        startActivity(new Intent(BookDetailActivity.this, MediaPlayerActivity.class).putExtra("mediaPath", ((CommonRecordBookMedia) markerData).mediaPath));
                        return false;
                    }
                });
                meidaOverlay.add(iconOverlay);
            }

        }
        mapView.getOverlays().add(meidaOverlay);
    }

    FolderOverlay meidaOverlay = new FolderOverlay();

    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if ((reqeustCode == REQ_OPEN_PIC_CHOOSE || reqeustCode == REQ_OPEN_CAMERA) && resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                String pic_path = resultList.get(i).getPhotoPath();
                CommonRecordBookMedia imgInfo = new CommonRecordBookMedia();
                String newPath = mediaPath + File.separator + FileUtils.getFileName(pic_path);
                if (!FileUtils.isFileExists(newPath)) {
                    FileUtils.copyFile(pic_path, newPath);
                }
                imgInfo.mediaPath = Util.getStoreUrl(BookDetailActivity.this, newPath);
                imgInfo.mediaName = FileUtils.getFileName(newPath);
                imgInfo.mediaType = (int) mediaLivePhotos;
                imgInfo.recordBookId = book.id;
                imgInfo.recordBookTypeId = book.recordBookTypeId;
                imgInfo.extension = FileUtils.getFileExtension(newPath);
                imgInfo.size = FileUtils.getFileLength(mediaPath);
                Location latLnt = LocationUtils.getLastKnownLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE));
                if (null != latLnt) {
                    imgInfo.gpsLatitude = latLnt.getLatitude() + "";
                    imgInfo.gpsLongitude = latLnt.getLongitude() + "";
                    imgInfo.gpsAltitude = latLnt.getAltitude() + "";
                }

                imgInfo.save();
                photoList.add(imgInfo);

                //添加到地图上面
                GeoPoint point = new GeoPoint(latLnt.getLatitude(), latLnt.getLongitude());
                CustomIconOverlay iconOverlay = new CustomIconOverlay(imgInfo);
                iconOverlay.set(point, ConvertUtils.bitmap2Drawable(getResources(), ImageLoader.getInstance().loadImageSync("file://" + imgInfo.mediaPath, new ImageSize(80, 80))));
                iconOverlay.setOnMarkClickListener(new CustomIconOverlay.MarkClickListener() {
                    @Override
                    public boolean onMarkerClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {

                        return false;
                    }
                });
                meidaOverlay.add(iconOverlay);
                mapView.invalidate();


            }
            picAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_SOUND) {
                Uri uri = data.getData();
                CommonRecordBookMedia result = getAudioFilePathFromUriAndSave(uri);
                selectAudioList.add(result);
                audioAdapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_LOAD_SOUND) {
                //选择录音文件或者录音
                selectAudioList.clear();
                DBUtil.useExtraDbByProjectName(this, projectName);
                List<CommonRecordBookMedia> list = LitePal.where("record_book_id = ? and record_book_type_id = ? and media_type = ?", book.id + "", book.recordBookTypeId + "", mediaRecord + "").find(CommonRecordBookMedia.class);
                selectAudioList.addAll(list);
                audioAdapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_LOAD_FILM) {
                selectVideoList.clear();
                List<CommonRecordBookMedia> list = LitePal.where("record_book_id = ? and record_book_type_id = ? and media_type = ?", book.id + "", book.recordBookTypeId + "", mediaVideo + "").find(CommonRecordBookMedia.class);
                selectVideoList.addAll(list);
                videoAdapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_ADD_FILM) {
                String path = data.getData().getPath();
                CommonRecordBookMedia video = new CommonRecordBookMedia();
                video.mediaName = FileUtils.getFileName(path);
                video.mediaType = (int) mediaVideo;
                video.mediaPath = Util.getStoreUrl(BookDetailActivity.this, path);
                video.extension = FileUtils.getFileExtension(path);
                video.size = FileUtils.getFileLength(path);
                File thuPic = new File(mediaPath, FileUtils.getFileNameNoExtension(path) + ".jpg");
                if (!thuPic.exists()) {
                    BitmapUtils.saveBitmap(Util.getVideoThumb(path), thuPic);
                }
                video.thumbnail = Util.getStoreUrl(this, thuPic.getPath());
                video.recordBookId = book.id;
                video.recordBookTypeId = book.recordBookTypeId;
                video.dateTimeOriginal = Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S);
                Location latLnt = LocationUtils.getLastKnownLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE));
                if (null != latLnt) {
                    video.gpsLatitude = latLnt.getLatitude() + "";
                    video.gpsLongitude = latLnt.getLongitude() + "";
                    video.gpsAltitude = latLnt.getAltitude() + "";
                }
                video.save();
                selectVideoList.add(video);
                videoAdapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_START_POSITON_BY_PIC) {
                GeoPoint point = (GeoPoint) data.getSerializableExtra("result");
                if (marker != null) {
                    marker.moveTo(point, mapView);
                } else {
                    marker = new CustomIconOverlay(mapView);
                    marker.set(point, getResources().getDrawable(R.mipmap.marker_icon_1));
                    mapView.getOverlays().add(marker);
                }

            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private ArrayList<CommonProjectMedia> multList;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOverlayChangeEvent(OverlayChangeEvent event) {
        multList = event.chooseList;
        MapViewManager.getInstance().clean();
        //开始加载图层
        for (int i = 0; i < multList.size(); i++) {
            loadOverlay(multList.get(i));
        }
        //加载工点

    }

    private void loadOverlay(CommonProjectMedia multi) {

        if (multi.mediaType == mediaImageMap) {
            //影像数据

        } else if (multi.mediaType == mediaVectorFile) {
            //矢量文件

        } else if (multi.mediaType == mediaOther) {
            //其他文件

        }

    }

    private void loadKml(String str) {
        KmlDocument kmlDocument = new KmlDocument();
        kmlDocument.parseGeoJSON(str);
        FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, null, new SimpleStyler(mapView.getTileProvider().getTileSource()), kmlDocument);
        mapView.getOverlays().add(kmlOverlay);
        try {
            BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
            if (null != bb) {
                mapView.zoomToBoundingBox(bb, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private CommonRecordBookMedia getAudioFilePathFromUriAndSave(Uri uri) {
        String[] projection = new String[]{MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        String path;
        if (null == cursor) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            path = cursor.getString(0);
            cursor.close();
        }
        //添加到数据库
        CommonRecordBookMedia audio = new CommonRecordBookMedia();
        String mediaPath = Util.getMediaPathByProjectName(BookDetailActivity.this, currentWorkDir, projectName);
        String newPath = mediaPath + File.separator + FileUtils.getFileName(path);
        if (!FileUtils.isFileExists(newPath)) {
            FileUtils.copyFile(path, newPath);
        }
        audio.mediaName = FileUtils.getFileName(path);
        audio.mediaType = (int) mediaRecord;
        audio.dateTimeOriginal = Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S);
        audio.mediaPath = Util.getStoreUrl(BookDetailActivity.this, newPath);
        audio.extension = FileUtils.getFileExtension(mediaPath);
        audio.size = FileUtils.getFileLength(mediaPath);
        audio.recordBookId = book.id;
        audio.recordBookTypeId = book.recordBookTypeId;
        Location latLnt = LocationUtils.getLastKnownLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        if (null != latLnt) {
            audio.gpsLatitude = latLnt.getLatitude() + "";
            audio.gpsLongitude = latLnt.getLongitude() + "";
            audio.gpsAltitude = latLnt.getAltitude() + "";
        }
        audio.save();
        return audio;

    }
}
