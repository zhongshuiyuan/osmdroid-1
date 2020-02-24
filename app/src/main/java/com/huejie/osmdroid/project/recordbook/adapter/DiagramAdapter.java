package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huejie.osmdroid.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;



/**
 * @author 谷超
 *         示意图适配器
 */
public class DiagramAdapter extends BaseAdapter {
    private List<String> data;
    private LayoutInflater inflater;

    public DiagramAdapter(Context context, List<String> data) {
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
            convertView = inflater.inflate(R.layout.item_diagram, null);
            holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String photo = data.get(position);

        ImageLoader.getInstance().displayImage("file://" + photo, holder.iv_pic);
        return convertView;
    }

    class ViewHolder {

        ImageView iv_pic;
    }

}
