package com.huejie.osmdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huejie.osmdroid.R;


/**
 * @author 谷超
 *         专业分类适配器
 */
public class PopLvAdapter extends BaseAdapter {
    private String[] data;
    private LayoutInflater inflater;

    public PopLvAdapter(Context context, String[] data) {
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return null == data ? 0 : data.length;
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
            convertView = inflater.inflate(R.layout.pop_lv_item, null);
            holder.tv_item = (TextView) convertView.findViewById(R.id.tv_item);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_item.setText(data[position]);
        holder.line.setVisibility(position >= data.length - 1 ? View.GONE : View.VISIBLE);

        return convertView;
    }

    class ViewHolder {
        TextView tv_item;
        View line;
    }

}
