package com.huejie.osmdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.SimpleChooseModel;

import java.util.List;


/**
 * @author 谷超
 * 专业分类适配器
 */
public class PopListAdapter extends BaseAdapter {
    private List<SimpleChooseModel> data;
    private LayoutInflater inflater;

    public PopListAdapter(Context context, List<SimpleChooseModel> data) {
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
            convertView = inflater.inflate(R.layout.pop_list_item, null);
            holder.tv_item = (CheckedTextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SimpleChooseModel model = data.get(position);
        holder.tv_item.setText(model.name);
        holder.tv_item.setChecked(model.isChoose);

        return convertView;
    }

    class ViewHolder {
        CheckedTextView tv_item;
    }

}
