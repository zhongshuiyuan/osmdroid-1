package com.huejie.osmdroid.project.recordbook.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.project.recordbook.adapter.AudioSelectAdapter;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.BitmapUtils;


/**
 * 选择手机中的视频文件
 */
public class VideoSelectActivity extends BaseActivity {
    @BindView(R.id.lv_video)
    ListView lv_video;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    private BookSimple book;


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void complete() {
        //点击完成，则插入新的数据到数据库里面
        String mediaPath = getIntent().getStringExtra("mediaPath");
        for (int i = 0; i < selectVideoList.size(); i++) {
            CommonRecordBookMedia bean = selectVideoList.get(i);
            if (bean.isSelect) {
                bean.recordBookId = book.id;
                bean.recordBookTypeId = book.recordBookTypeId;
                String newPath = mediaPath + File.separator + FileUtils.getFileName(bean.mediaPath);
                if (!FileUtils.isFileExists(newPath)) {
                    FileUtils.copyFile(bean.mediaPath, newPath);
                }
                File thuPic = new File(mediaPath, FileUtils.getFileNameNoExtension(newPath) + ".jpg");
                if (!thuPic.exists()) {
                    BitmapUtils.saveBitmap(Util.getVideoThumb(newPath), thuPic);
                }
                bean.thumbnail = Util.getStoreUrl(this, thuPic.getPath());
                bean.mediaPath = Util.getStoreUrl(this, newPath);
                DBUtil.useExtraDbByProjectName(this,getIntent().getStringExtra("projectName"));
                bean.save();
            }
        }
        setResult(RESULT_OK);
        finish();

    }

    private List<CommonRecordBookMedia> selectVideoList = new ArrayList<>();

    private AudioSelectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectvideo);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        tv_title.setText("选择视频");
        tv_right.setText("完成");
        tv_right.setVisibility(View.VISIBLE);

        book = (BookSimple) getIntent().getSerializableExtra("book");
        selectVideoList = getVideoFiles();

        adapter = new AudioSelectAdapter(this, selectVideoList);
        lv_video.setAdapter(adapter);
        adapter.setClick(new AudioSelectAdapter.Click() {
            @Override
            public void onClick(int position) {
                int count = 0;
                for (int i = 0; i < selectVideoList.size(); i++) {
                    if (selectVideoList.get(i).isSelect) {
                        count++;
                    }
                }
                if (count > 10) {
                    ToastUtils.showShortToast("视频数量不能超过十个");
                    return;
                }
                selectVideoList.get(position).isSelect = !selectVideoList.get(position).isSelect;
                adapter.notifyDataSetChanged();

            }
        });

    }


    /**
     * 从数据库中获取视频频相关数据
     *
     * @return
     */
    private List<CommonRecordBookMedia> getVideoFiles() {
        List<CommonRecordBookMedia> list = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{MediaStore.Video.Media.DISPLAY_NAME, // 名称加后缀名
                MediaStore.Video.Media.MIME_TYPE,  //文件后缀名
                MediaStore.Video.Media.SIZE, // 大小
                MediaStore.Video.Media.DURATION, // 时长
                MediaStore.Video.Media.DATA, // 路径
                MediaStore.Audio.Media.DATE_ADDED,   // 添加到数据库的时间
        };

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        long mediaType = getIntent().getLongExtra("mediaType", 0);
        while (cursor.moveToNext()) {
            String title = cursor.getString(0);
            long size = cursor.getLong(2);
//            String mime_type = cursor.getString(1);
//            long duration = cursor.getLong(3);
            String path = cursor.getString(4);
            long date_added = cursor.getLong(5);

            CommonRecordBookMedia selectVideo = new CommonRecordBookMedia();
            selectVideo.mediaName = title;
            selectVideo.mediaType = (int) mediaType;
            selectVideo.size = size;
            selectVideo.mediaPath = path;
            selectVideo.extension = FileUtils.getFileExtension(path);
            selectVideo.dateTimeOriginal = Util.getFromatDate(date_added * 1000, Util.Y_M_D_H_M_S);
            list.add(selectVideo);

        }
        Collections.sort(list, new Comparator<CommonRecordBookMedia>() {       //按时间进行倒叙排序
            @Override
            public int compare(CommonRecordBookMedia arg0, CommonRecordBookMedia arg1) {
                return (arg1.dateTimeOriginal + "").compareTo(arg0.dateTimeOriginal + "");
            }
        });
        return list;
    }


}
