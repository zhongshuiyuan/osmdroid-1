package com.huejie.osmdroid.more.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
 * 音频适配器
 */
public class MapChooseAdapter extends BaseAdapter {
    private List<SimpleChooseModel> data;
    private LayoutInflater inflater;
    private Context context;

    public MapChooseAdapter(Context context, List<SimpleChooseModel> data) {
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
            convertView = inflater.inflate(R.layout.item_overlay_choose, null);
            holder.ctv_tag = convertView.findViewById(R.id.ctv_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SimpleChooseModel bean = data.get(position);
        holder.ctv_tag.setText(bean.name);
        holder.ctv_tag.setChecked(bean.isChoose);
        if (bean.isChoose) {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.gouxuan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.ctv_tag.setCompoundDrawables(null, null, drawable, null);
        } else {
            holder.ctv_tag.setCompoundDrawables(null, null, null, null);
        }
        holder.ctv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClick) {
                    onClick.onClick(position);
                }

            }
        });
        return convertView;
    }

    class ViewHolder {
        CheckedTextView ctv_tag;


    }

    private OnClick onClick;

    public void setOnClickListener(OnClick onClick) {
        this.onClick = onClick;
    }

    public static interface OnClick {
        void onClick(int position);

    }

}
