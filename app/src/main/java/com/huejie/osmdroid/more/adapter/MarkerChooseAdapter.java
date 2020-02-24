package com.huejie.osmdroid.more.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huejie.osmdroid.R;


/**
 * @author 谷超
 * 音频适配器
 */
public class MarkerChooseAdapter extends BaseAdapter {
    private int[] data;
    private LayoutInflater inflater;
    private Context context;

    public MarkerChooseAdapter(Context context, int[] data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
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
            convertView = inflater.inflate(R.layout.item_icon_choose, null);
            holder.image = convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int bean = data[position];
        holder.image.setImageResource(bean);

        return convertView;
    }

    class ViewHolder {
        ImageView image;


    }

    private OnClick onClick;

    public void setOnClickListener(OnClick onClick) {
        this.onClick = onClick;
    }

    public static interface OnClick {
        void onClick(int position);

    }

}
