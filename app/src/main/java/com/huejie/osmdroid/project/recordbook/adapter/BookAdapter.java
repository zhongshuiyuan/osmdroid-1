package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.util.Util;

import java.util.List;

/**
 * @author 谷超
 * 专业分类适配器
 */
public class BookAdapter extends BaseAdapter {
    private List<BookSimple> data;
    private LayoutInflater inflater;
    private int projectType;

    public BookAdapter(Context context, List<BookSimple> data, int projectType) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.projectType = projectType;

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
            convertView = inflater.inflate(R.layout.item_book, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_createtime = (TextView) convertView.findViewById(R.id.tv_createtime);
            holder.rb_tag = convertView.findViewById(R.id.rb_tag);
            holder.rl_top = convertView.findViewById(R.id.rl_top);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BookSimple book = data.get(position);
        holder.tv_name.setText(book.uniqueTag);

        if (projectType == 0) {
            holder.tv_createtime.setVisibility(View.VISIBLE);
            holder.rb_tag.setVisibility(View.GONE);
            holder.tv_createtime.setText(Util.getDateTimeString(book.createTime));
        } else {
            holder.tv_createtime.setVisibility(View.GONE);
            holder.rb_tag.setVisibility(View.VISIBLE);
            holder.rb_tag.setChecked(book.isCheck);
        }
        holder.rl_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    onClickListener.onClick(position);
                }
            }
        });
        holder.rl_top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onLongClickListener) {
                    onLongClickListener.onLongClick(position);
                }
                return true;
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_createtime;
        CheckedTextView rb_tag;
        View rl_top;

    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {
        void onClick(int position);
    }

    private OnLongClickListener onLongClickListener;

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public static interface OnLongClickListener {
        void onLongClick(int position);
    }

}
