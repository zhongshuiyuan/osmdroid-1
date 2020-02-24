package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
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
public class PictureAdapter extends BaseAdapter {
    private List<CommonRecordBookMedia> data;
    private LayoutInflater inflater;
    private Context context;

    public PictureAdapter(Context context, List<CommonRecordBookMedia> data) {
        this.context = context;
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
            convertView = inflater.inflate(R.layout.item_photo, null);
            holder.tv_pic_num = (TextView) convertView.findViewById(R.id.tv_pic_num);
            holder.tv_pic_time = (TextView) convertView.findViewById(R.id.tv_pic_time);
            holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommonRecordBookMedia photo = data.get(position);
        holder.tv_pic_num.setText(photo.mediaName);
        ImageLoader.getInstance().displayImage("file://" + Util.getProjectsPath(context, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + photo.mediaPath, holder.iv_pic);
        return convertView;
    }

    class ViewHolder {
        TextView tv_pic_num;
        TextView tv_pic_time;
        ImageView iv_pic;
    }

}
