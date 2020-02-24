package com.huajie.fileexport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huajie.fileexplore.R;
import com.huajie.fileexport.adapter.FileAdapter;
import com.huajie.fileexport.entity.FileInfo;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilesActivity extends AppCompatActivity {

    private ListView listView;
    private FileAdapter adapter;
    private String currentPath;
    private LinearLayout sub_layout;
    private int clickPosition = 0;
    List<FileInfo> tabList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        listView = (ListView) findViewById(R.id.listView);
        sub_layout = (LinearLayout) findViewById(R.id.sub_layout);
        findViewById(R.id.tv_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPath = Environment.getExternalStorageDirectory().getPath();
                lstFile.clear();
                getFiles(currentPath);
                adapter.setData(lstFile);
                adapter.notifyDataSetChanged();
                tabList.clear();
                addTab();

            }
        });
        //确定选择
        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //统计所有选择了的文件
                ArrayList<FileInfo> chooseFile = new ArrayList<FileInfo>();
                for (int i = 0; i < lstFile.size(); i++) {
                    if (lstFile.get(i).isSelect) {
                        chooseFile.add(lstFile.get(i));
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("files", chooseFile);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
        findViewById(R.id.tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(FilesActivity.this, FileSortActivity.class), 200);
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getFiles(Environment.getExternalStorageDirectory().getPath());
        currentPath = Environment.getExternalStorageDirectory().getPath();
        adapter = new FileAdapter(this, lstFile);
        adapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onFileClick(int position) {
                clickPosition = position;
                boolean isSelect = lstFile.get(position).isSelect;
                lstFile.get(position).isSelect = !isSelect;
                adapter.setData(lstFile);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onDirClick(int position) {
                //目录
                String path = lstFile.get(position).filePath;
                currentPath = path;
                tabList.add(lstFile.get(position));
                addTab();

                lstFile.clear();
                getFiles(path);
                adapter.setData(lstFile);
                adapter.notifyDataSetChanged();
            }
        });

        listView.setAdapter(adapter);

    }

    public void addTab() {
        sub_layout.removeAllViews();
        for (int i = 0; i < tabList.size(); i++) {
            TextView filename = (TextView) getLayoutInflater().inflate(R.layout.view_path, null);
            filename.setText(tabList.get(i).fileName);
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
                    currentPath = tabList.get(tabList.size() - 1).filePath;
                    lstFile.clear();
                    getFiles(currentPath);
                    adapter.setData(lstFile);
                    adapter.notifyDataSetChanged();

                }
            });
        }
    }

    private List<FileInfo> lstFile = new ArrayList<FileInfo>(); //结果 List
    DecimalFormat df = new DecimalFormat("#.00");

    public void getFiles(String Path) //搜索目录，扩展名，是否进入子文件夹
    {
        File[] files = new File(Path + "/").listFiles();
        if (null != files) {
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                FileInfo info = new FileInfo();
                if (f.isFile()) {
                    info.fileType = 0;

                    long fileS = f.length();
                    if (fileS < 1024) {
                        info.fileSize = df.format((double) fileS) + "B";
                    } else if (fileS < 1048576) {
                        info.fileSize = df.format((double) fileS / 1024) + "KB";
                    } else if (fileS < 1073741824) {
                        info.fileSize = df.format((double) fileS / 1048576) + "MB";
                    } else {
                        info.fileSize = df.format((double) fileS / 1073741824) + "GB";
                    }

                    // info.fileSize = ((double) f.length() / 1024) + "KB";
                } else if (f.isDirectory()) {
                    info.fileType = 1;
                    info.hasChilds = f.listFiles().length > 0;
                }
                info.fileName = f.getName();
                info.filePath = f.getPath();
                if (!info.fileName.startsWith(".") && ((info.fileType == 1 && info.hasChilds) || (info.fileType == 0 && info.fileName.contains(".")))) {
                    //过滤所有以“.”开头的文件和没有扩展名的文件和空目录
                    lstFile.add(info);

                }
            }
            sortDir(lstFile);
        } else {
            Toast.makeText(this, "获取文件列表失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void sortDir(List<FileInfo> fileInfos) {
        Collections.sort(fileInfos, new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                if (o1.fileType == 1 && o2.fileType == 0)
                    return -1;
                if (o1.fileType == 0 && o2.fileType == 1)
                    return 1;
                return o1.fileName.compareToIgnoreCase(o2.fileName);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if (currentPath.equals(Environment.getExternalStorageDirectory().getPath())) {
            finish();
        } else {
            currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
            lstFile.clear();
            getFiles(currentPath);
            adapter.setData(lstFile);
            listView.smoothScrollToPosition(clickPosition);
            adapter.notifyDataSetChanged();
            tabList.remove(tabList.size() - 1);
            addTab();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
