package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * @author 谷超
 * 音频适配器
 */
public class PositionAdapter extends BaseAdapter {
    private List<CommonRecordBookMedia> data;
    private LayoutInflater inflater;
    private String workDir;

    public PositionAdapter(Context context, List<CommonRecordBookMedia> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        workDir = Util.getProjectsPath(context, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR));

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
            convertView = inflater.inflate(R.layout.item_position_choose, null);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            holder.iv_pic = convertView.findViewById(R.id.iv_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommonRecordBookMedia bean = data.get(position);
        ImageLoader.getInstance().displayImage("file://" + workDir + bean.mediaPath, holder.iv_pic);
        holder.checkBox.setChecked(bean.isSelect);
        convertView.setOnClickListener(new View.OnClickListener() {
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
        CheckBox checkBox;

        ImageView iv_pic;

    }

    private OnClick onClick;

    public void setOnClickListener(OnClick onClick) {
        this.onClick = onClick;
    }

    public static interface OnClick {
        void onClick(int position);

    }

}
