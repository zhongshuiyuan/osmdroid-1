package com.huajie.fileexport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huajie.fileexplore.R;
import com.huajie.fileexport.adapter.FileAdapter;
import com.huajie.fileexport.entity.FileInfo;

import java.util.ArrayList;


public class FileListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private int mParam1;
    private ArrayList<FileInfo> fileList;
    private int clickPosition = 0;

    private ListView listView;
    private FileAdapter adapter;
    private FileSortActivity activity;

    public FileListFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static FileListFragment newInstance(int param1, ArrayList<FileInfo> fileList) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, fileList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FileSortActivity) getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            fileList = (ArrayList<FileInfo>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new FileAdapter(getActivity(), fileList);
        listView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onFileClick(int position) {
                clickPosition = position;
                boolean isSelect = fileList.get(position).isSelect;
                fileList.get(position).isSelect = !isSelect;
                String abs = fileList.get(position).filePath + "/" + fileList.get(position).fileName;
                if (isSelect) {
                    activity.chooseFile.remove(abs);
                } else {
                    activity.chooseFile.put(abs, fileList.get(position));
                }

                adapter.setData(fileList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onDirClick(int position) {
                //目录

            }
        });
    }


}
