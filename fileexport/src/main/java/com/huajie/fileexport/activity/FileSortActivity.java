package com.huajie.fileexport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;

import com.huajie.fileexplore.R;
import com.huajie.fileexport.entity.FileInfo;
import com.huajie.fileexport.utils.Util;
import com.huajie.fileexport.view.LazyViewPager;
import com.huajie.fileexport.view.PagerSlidingTabStrip;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSortActivity extends AppCompatActivity {


    private PagerSlidingTabStrip tabs;
    private LazyViewPager viewPager;
    private int currentPage = 0;
    private String[] titles = new String[]{"图片", "文档", "音视频", "其他"};
    private List<FileListFragment> frgList = new ArrayList<>();
    private static final int TYPE_PIC = 0;
    private static final int TYPE_DOC = 1;
    private static final int TYPE_DEDIA = 2;
    private static final int TYPE_OTHER = 3;
    private View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_sort);
        EventBus.getDefault().register(this);
        initView();
    }

    public Map<String, FileInfo> chooseFile = new HashMap<>();


    private void initView() {
        //开始扫描所有文件
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        viewPager = (LazyViewPager) findViewById(R.id.viewPager);
        progress = findViewById(R.id.progress);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFiles(Environment.getExternalStorageDirectory().getPath());
                EventBus.getDefault().post(new MessageEvent());
            }
        }).start();
        findViewById(R.id.tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<FileInfo> chooseList = new ArrayList<FileInfo>();
                chooseList.addAll(chooseFile.values());
                Intent intent = new Intent();
                intent.putExtra("files", chooseList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void startWork() {
        progress.setVisibility(View.GONE);
        frgList.add(FileListFragment.newInstance(TYPE_PIC, picList));
        frgList.add(FileListFragment.newInstance(TYPE_DOC, docList));
        frgList.add(FileListFragment.newInstance(TYPE_DEDIA, mediaList));
        frgList.add(FileListFragment.newInstance(TYPE_OTHER, otherList));
        FragmentPagerAdapter adapter = new NewsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        tabs.setViewPager(viewPager);
        tabs.setOnPageChangeListener(new LazyViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;

            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FileSortActivity.MessageEvent event) {
        System.out.println("收到信息");
        startWork();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private ArrayList<FileInfo> picList = new ArrayList<>();
    private ArrayList<FileInfo> docList = new ArrayList<>();
    private ArrayList<FileInfo> mediaList = new ArrayList<>();
    private ArrayList<FileInfo> otherList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.00");

    public void getFiles(String path) //搜索目录，扩展名，是否进入子文件夹
    {
        File file = new File(path);
        if (file.isFile()) {
            FileInfo info = new FileInfo();
            info.fileType = 0;
            long fileS = file.length();
            if (fileS < 1024) {
                info.fileSize = df.format((double) fileS) + "B";
            } else if (fileS < 1048576) {
                info.fileSize = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                info.fileSize = df.format((double) fileS / 1048576) + "MB";
            } else {
                info.fileSize = df.format((double) fileS / 1073741824) + "GB";
            }
            info.fileName = file.getName();
            info.filePath = file.getPath();
            if (Util.endsWith(file.getName(), ".jpg") || Util.endsWith(file.getName(), ".png") || Util.endsWith(file.getName(), ".jpeg") || Util.endsWith(file.getName(), ".psd") || Util.endsWith(file.getName(), ".gif") || Util.endsWith(file.getName(), ".webp")) {
                picList.add(info);
            } else if (Util.endsWith(file.getName(), ".doc") || Util.endsWith(file.getName(), ".docx") || Util.endsWith(file.getName(), ".xls") || Util.endsWith(file.getName(), ".xlsx") || Util.endsWith(file.getName(), ".pdf") || Util.endsWith(file.getName(), ".txt")) {
                docList.add(info);
            } else if (Util.endsWith(file.getName(), ".rmvb") || Util.endsWith(file.getName(), ".avi") || Util.endsWith(file.getName(), ".mp3") || Util.endsWith(file.getName(), ".mp4") || Util.endsWith(file.getName(), ".mov") || Util.endsWith(file.getName(), ".wmv") || Util.endsWith(file.getName(), ".mpg") || Util.endsWith(file.getName(), ".wav")) {
                mediaList.add(info);
            } else {
                otherList.add(info);
            }


        } else if (file.isDirectory()) {
            File[] files = new File(path).listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getName();
                    if (fileName.startsWith(".")) {
                        //过滤所有以“.”开头的文件和没有扩展名的文件和空目录
                        continue;
                    }
                    getFiles(files[i].getPath());
                }

            }
        }

    }

    class NewsAdapter extends FragmentPagerAdapter {
        public NewsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return frgList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position % titles.length];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

    public static class MessageEvent {

    }
}
