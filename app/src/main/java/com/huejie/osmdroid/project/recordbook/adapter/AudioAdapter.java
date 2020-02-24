package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.CommonRecordBookMedia;

import java.util.List;


/**
 * @author 谷超
 * 音频适配器
 */
public class AudioAdapter extends BaseAdapter {
    private List<CommonRecordBookMedia> data;
    private LayoutInflater inflater;

    public AudioAdapter(Context context, List<CommonRecordBookMedia> data) {
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
            convertView = inflater.inflate(R.layout.item_media, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_play = (ImageView) convertView.findViewById(R.id.iv_play);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommonRecordBookMedia bean = data.get(position);
        holder.tv_name.setText(bean.mediaName);
        //当前播放进度时间

        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClick) {
                    onClick.onPlay(position);
                }
            }
        });


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onLongClicked) {
                    return onLongClicked.onLongClick(position);
                }
                return false;
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;

        ImageView iv_play;

    }

    private OnLongClicked onLongClicked;

    public void setOnLongClick(OnLongClicked onLongClicked) {
        this.onLongClicked = onLongClicked;
    }


    public static interface OnLongClicked {
        boolean onLongClick(int position);
    }

    private OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public static interface OnClick {
        void onPlay(int position);

    }

}
