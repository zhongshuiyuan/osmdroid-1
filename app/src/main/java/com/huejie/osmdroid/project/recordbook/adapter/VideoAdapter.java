package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * @author 谷超
 * 专业分类适配器
 */
public class VideoAdapter extends BaseAdapter {
    private List<CommonRecordBookMedia> data;
    private LayoutInflater inflater;
    private String workDir;

    public VideoAdapter(Context context, List<CommonRecordBookMedia> data) {
        workDir = Util.getProjectsPath(context, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR));
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_video, null);
            holder.tv_pic_num = (TextView) convertView.findViewById(R.id.tv_pic_num);
            holder.tv_pic_time = (TextView) convertView.findViewById(R.id.tv_pic_time);
            holder.iv_play = (ImageView) convertView.findViewById(R.id.iv_play);
            holder.iv_thumbnail = (ImageView) convertView.findViewById(R.id.iv_thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommonRecordBookMedia bean = data.get(position);
        holder.tv_pic_num.setText("视频" + (position + 1));
        holder.tv_pic_time.setText("拍摄于" + bean.dateTimeOriginal);
        if (!TextUtils.isEmpty(bean.thumbnail)) {
            ImageLoader.getInstance().displayImage("file://" + workDir + bean.thumbnail, holder.iv_thumbnail);
        } else {
            ImageLoader.getInstance().displayImage("drawable://" + R.mipmap.moren, holder.iv_thumbnail);
        }

        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClicked) {
                    onClicked.onPlay(position);
                }
            }
        });
        holder.iv_thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onClicked) {
                    onClicked.onDelete(position);
                }
                return true;
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_pic_num;
        TextView tv_pic_time;
        ImageView iv_play;
        ImageView iv_thumbnail;
    }

    private OnClicked onClicked;

    public void setOnClicked(OnClicked onClicked) {
        this.onClicked = onClicked;
    }


    public static interface OnClicked {
        void onDelete(int position);

        void onPlay(int position);

    }
}
