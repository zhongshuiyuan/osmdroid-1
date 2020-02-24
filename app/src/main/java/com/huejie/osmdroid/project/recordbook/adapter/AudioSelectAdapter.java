
package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.CommonRecordBookMedia;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class AudioSelectAdapter extends BaseAdapter {

    // 填充数据的list
    private List<CommonRecordBookMedia> list;

    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public AudioSelectAdapter(Context context, List<CommonRecordBookMedia> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.item_selectvideo, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            holder.iv_video_icon = (ImageView) convertView.findViewById(R.id.iv_video_icon);
            holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置list中TextView的显示
        CommonRecordBookMedia video = list.get(position);
        holder.tv_title.setText(video.mediaName);
        holder.tv_size.setText(FormetFileSize(video.size));
//        holder.iv_video_icon.setImageBitmap(video.getIcon());
        // 根据isSelected来设置checkbox的选中状况
        holder.cb.setChecked(video.isSelect);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != click) {
                    click.onClick(position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {

        public CheckBox cb;
        public TextView tv_title;
        public TextView tv_size;
        public ImageView iv_video_icon;
    }

    /**
     * 时间格式化
     *
     * @param timestamp
     * @return
     */
    public String getStandardTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(timestamp);
//        sdf.format(date);
        return sdf.format(date);
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    private Click click;

    public void setClick(Click click) {
        this.click = click;
    }

    public static interface Click {
        void onClick(int position);

    }

}


