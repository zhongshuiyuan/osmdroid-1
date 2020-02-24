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


/**
 * 选择手机中的音频文件
 * Created by guc on 2017/6/20.
 */
public class AudioSelectActivity extends BaseActivity {


    private List<CommonRecordBookMedia> selectAudioList = new ArrayList<>();

    private AudioSelectAdapter adapter;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.lv_video)
    ListView lv_audio;
    private BookSimple book;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void complete() {
        //点击完成，则插入新的数据到数据库里面

        String mediaPath = getIntent().getStringExtra("mediaPath");
        for (int i = 0; i < selectAudioList.size(); i++) {
            CommonRecordBookMedia bean = selectAudioList.get(i);
            if (bean.isSelect) {
                bean.recordBookId = book.id;
                bean.recordBookTypeId = book.recordBookTypeId;
                String newPath = mediaPath + File.separator + FileUtils.getFileName(bean.mediaPath);
                if (!FileUtils.isFileExists(newPath)) {
                    FileUtils.copyFile(bean.mediaPath, newPath);
                }
                bean.mediaPath = Util.getStoreUrl(this, newPath);
                DBUtil.useExtraDbByProjectName(this,getIntent().getStringExtra("projectName"));
                bean.save();
            }
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectvideo);
        ButterKnife.bind(this);
        initData();
    }

    public void initData() {
        tv_title.setText("选择音频");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("完成");
        book = (BookSimple) getIntent().getSerializableExtra("book");
        getAudioFiles();
        Collections.sort(selectAudioList, new Comparator<CommonRecordBookMedia>() {       //按时间进行倒叙排序
            @Override
            public int compare(CommonRecordBookMedia arg0, CommonRecordBookMedia arg1) {
                return (arg1.dateTimeOriginal + "").compareTo(arg0.dateTimeOriginal + "");
            }
        });

        adapter = new AudioSelectAdapter(this, selectAudioList);
        lv_audio.setAdapter(adapter);
        adapter.setClick(new AudioSelectAdapter.Click() {
            @Override
            public void onClick(int position) {
                selectAudioList.get(position).isSelect = !selectAudioList.get(position).isSelect;
                adapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 从数据库中获取音频相关数据
     *
     * @return
     */
    private void getAudioFiles() {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{MediaStore.Audio.Media.DISPLAY_NAME, // 名称加后缀名   DISPLAY_NAME
                MediaStore.Audio.Media.MIME_TYPE,  //文件后缀名
                MediaStore.Audio.Media.SIZE, // 大小
                MediaStore.Audio.Media.DURATION, // 时长
                MediaStore.Audio.Media.DATA,   // 路径
                MediaStore.Audio.Media.DATE_ADDED,   // 添加到数据库的时间
        };
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        long mediaType = getIntent().getLongExtra("mediaType", 0);
        while (cursor.moveToNext()) {
            String title = cursor.getString(0);
            String mimeType = cursor.getString(1);
            if (null != title) {
                long size = cursor.getLong(2);
                long duration = cursor.getLong(3);
                String path = cursor.getString(4);
                long createTime = cursor.getLong(5);
                CommonRecordBookMedia selectVideo = new CommonRecordBookMedia();
                selectVideo.mediaName = title;
                selectVideo.mediaType = (int) mediaType;
                selectVideo.size = size;
                selectVideo.mediaPath = path;
                selectVideo.extension = mimeType;
                selectVideo.dateTimeOriginal = Util.getFromatDate(createTime, Util.Y_M_D_H_M_S);
                selectAudioList.add(selectVideo);
            }
        }

    }

}
