package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.books.OtherEngineeringSubInfo;
import com.huejie.osmdroid.util.Util;

import java.util.List;

/**
 * @author 谷超
 * 专业分类适配器
 */
public class OtherEngineeringSubInfoAdapter extends BaseAdapter {
    private List<OtherEngineeringSubInfo> data;
    private LayoutInflater inflater;

    public OtherEngineeringSubInfoAdapter(Context context, List<OtherEngineeringSubInfo> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;

    }
    public void setData(List<OtherEngineeringSubInfo> data){
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
            convertView = inflater.inflate(R.layout.item_other_engineering_sub_info, null);
            holder.tv_jczh = (TextView) convertView.findViewById(R.id.tv_jczh);
            holder.tv_gygcmc = (TextView) convertView.findViewById(R.id.tv_gygcmc);
            holder.tv_jcjd = (TextView) convertView.findViewById(R.id.tv_jcjd);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OtherEngineeringSubInfo subInfo = data.get(position);
        holder.tv_jczh.setText("交叉桩号：" + subInfo.crossStake);
        holder.tv_gygcmc.setText("改移工程名称：" + subInfo.relocationName);
        holder.tv_jcjd.setText("交叉角度（度）：" + Util.valueString(subInfo.crossAngle));
        return convertView;
    }

    class ViewHolder {
        TextView tv_jczh;
        TextView tv_gygcmc;
        TextView tv_jcjd;


    }

}
