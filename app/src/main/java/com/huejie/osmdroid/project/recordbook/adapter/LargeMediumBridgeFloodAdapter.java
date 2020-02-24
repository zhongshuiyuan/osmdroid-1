package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.books.FloodLevelRecordSurveyInfo;

import java.util.List;

/**
 * @author 谷超
 * 专业分类适配器
 */
public class LargeMediumBridgeFloodAdapter extends BaseAdapter {
    private Context context;
    private List<FloodLevelRecordSurveyInfo> booksList;

    private LayoutInflater inflater;


    public LargeMediumBridgeFloodAdapter(Context context, List<FloodLevelRecordSurveyInfo> booksList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.booksList = booksList;

    }

    public void setList(List<FloodLevelRecordSurveyInfo> booksList) {
        this.booksList = booksList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return null == booksList ? 0 : booksList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return booksList.get(position);
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
            convertView = inflater.inflate(R.layout.item_river_flood, null);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_updatetime = convertView.findViewById(R.id.tv_updatetime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FloodLevelRecordSurveyInfo book = booksList.get(position);
        holder.tv_name.setText("记录" + (position + 1));
        holder.tv_updatetime.setText(book.updateTime);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    onClickListener.onClick(position);
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onItemLongClickListener) {
                    onItemLongClickListener.onItemLongClick(position);
                }
                return false;
            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView tv_name;
        public TextView tv_updatetime;

    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {
        void onClick(int position);

    }

    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public static interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

}
