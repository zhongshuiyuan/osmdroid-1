package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.project.recordbook.model.City;

import java.util.List;

/**
 * @author 谷超
 * 专业分类适配器
 */
public class CityAdapter extends BaseAdapter {
    private List<City> data;
    private LayoutInflater inflater;

    public CityAdapter(Context context, List<City> data) {
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
            convertView = inflater.inflate(R.layout.item_city, null);
            holder.tv_city = (CheckedTextView) convertView.findViewById(R.id.tv_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        City city = data.get(position);
        holder.tv_city.setText(city.cityName);
        holder.tv_city.setChecked(city.isCheck);
        return convertView;
    }

    class ViewHolder {
        CheckedTextView tv_city;


    }

}
