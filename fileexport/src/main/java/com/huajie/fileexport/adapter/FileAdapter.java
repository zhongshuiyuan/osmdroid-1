package com.huajie.fileexport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huajie.fileexplore.R;
import com.huajie.fileexport.entity.FileInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * @author 谷超
 * 适配器
 */
public class FileAdapter extends BaseAdapter {
    private List<FileInfo> data;
    private LayoutInflater inflater;

    public FileAdapter(Context context, List<FileInfo> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    public void setData(List<FileInfo> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return null == data ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_files, null);
            holder.tv_filename = (TextView) convertView.findViewById(R.id.tv_filename);
            holder.iv_file = (ImageView) convertView.findViewById(R.id.iv_file);
            holder.rb_tag = (RadioButton) convertView.findViewById(R.id.rb_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FileInfo file = data.get(position);
        if (file.fileType == 0) {

            holder.rb_tag.setVisibility(View.VISIBLE);
            if (file.fileName.endsWith(".txt")) {
                holder.iv_file.setImageResource(R.mipmap.txt);
            } else if (file.fileName.endsWith(".apk")) {
                holder.iv_file.setImageResource(R.mipmap.apk);
            } else if (file.fileName.endsWith(".zip") || file.fileName.endsWith(".rar")) {
                holder.iv_file.setImageResource(R.mipmap.rar);
            } else if (file.fileName.endsWith(".doc") || file.fileName.endsWith(".docx")) {
                holder.iv_file.setImageResource(R.mipmap.doc);
            } else if (file.fileName.endsWith(".ppt")) {
                holder.iv_file.setImageResource(R.mipmap.ppt);
            } else if (file.fileName.endsWith(".xml")) {
                holder.iv_file.setImageResource(R.mipmap.xml);
            } else if (file.fileName.endsWith(".pdf")) {
                holder.iv_file.setImageResource(R.mipmap.pdf);
            } else if (file.fileName.endsWith(".mp4") || file.fileName.endsWith(".rmvb") || file.fileName.endsWith(".avi")) {
                holder.iv_file.setImageResource(R.mipmap.video);
            } else if (file.fileName.endsWith(".mp3") || file.fileName.endsWith(".wmv") || file.fileName.endsWith(".amr")) {
                holder.iv_file.setImageResource(R.mipmap.music);
            } else if (file.fileName.endsWith(".xls") || file.fileName.endsWith(".xlsx")) {
                holder.iv_file.setImageResource(R.mipmap.xls);
            } else if (file.fileName.endsWith(".jpg") || file.fileName.endsWith(".png") || file.fileName.endsWith(".jpeg")) {
//                holder.iv_file.setImageResource(R.mipmap.picture);
                ImageLoader.getInstance().displayImage("file://" + file.filePath, holder.iv_file);
            } else {
                holder.iv_file.setImageResource(R.mipmap.unknown);
            }
            holder.rb_tag.setChecked(file.isSelect);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onFileClick(position);
                    }

                }
            });

        } else {
            holder.iv_file.setImageResource(R.mipmap.mulu);
            holder.rb_tag.setVisibility(View.GONE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onDirClick(position);
                    }

                }
            });
        }
        holder.tv_filename.setText(file.fileName);
        return convertView;
    }

    class ViewHolder {
        TextView tv_filename;
        RadioButton rb_tag;
        ImageView iv_file;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        void onFileClick(int position);

        void onDirClick(int position);
    }

}
