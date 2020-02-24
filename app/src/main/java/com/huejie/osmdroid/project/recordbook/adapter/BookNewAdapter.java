package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.basic.RecordBookType;

import java.util.List;

/**
 * @author 谷超
 * 专业分类适配器
 */
public class BookNewAdapter extends BaseAdapter {
    private Context context;
    private List<RecordBookType> booksList;
    private int projectType;
    private LayoutInflater inflater;
    private boolean showCheck = true;

    public BookNewAdapter(Context context, List<RecordBookType> booksList, int projectType) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.booksList = booksList;
        this.projectType = projectType;
    }

    public BookNewAdapter(Context context, List<RecordBookType> booksList, int projectType, boolean showCheck) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.booksList = booksList;
        this.projectType = projectType;
        this.showCheck = showCheck;
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
    public View getView(final int i, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_books, null);
            holder.tv_file = convertView.findViewById(R.id.tv_file);
            holder.checkBox = convertView.findViewById(R.id.checkTextView);
            holder.iv_add_work = convertView.findViewById(R.id.iv_add_work);
            holder.rl_top = convertView.findViewById(R.id.rl_top);
            holder.listView = convertView.findViewById(R.id.listView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecordBookType book = booksList.get(i);
        holder.tv_file.setText(book.recordBookTypeName + "（" + (book.list == null ? 0 : book.list.size()) + "）");

        holder.rl_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onParentClickListener) {
                    onParentClickListener.onParentClick(i);
                }
            }
        });
        if (projectType == 0) {
            //本地项目
            holder.iv_add_work.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
            holder.iv_add_work.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onParentClickListener) {
                        onParentClickListener.onAddClick(i);
                    }
                }
            });
        } else {
            //在线项目
            holder.iv_add_work.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(book.isAllCheck);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onAllChooseListener) {
                        onAllChooseListener.onAllChoose(i);
                    }
                }
            });
        }
        holder.checkBox.setVisibility(showCheck ? View.VISIBLE : View.GONE);
        holder.listView.setVisibility(book.isCheck ? View.VISIBLE : View.GONE);
        final BookAdapter adapter = new BookAdapter(context, book.list, projectType);
        holder.listView.setAdapter(adapter);
        adapter.setOnClickListener(new BookAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                if (null != onClickListener) {
                    onClickListener.onClick(i, position, adapter);
                }
            }
        });

        adapter.setOnLongClickListener(new BookAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                if (null != onChildItemLongClickListener) {
                    onChildItemLongClickListener.onItemLongClick(i, position);
                }
            }
        });


        return convertView;
    }

    class ViewHolder {
        public TextView tv_file;
        public CheckedTextView checkBox;
        public ImageView iv_add_work;
        public ListView listView;
        public View rl_top;

    }

    private OnParentClickListener onParentClickListener;

    public void setOnParentClickListener(OnParentClickListener onParentClickListener) {
        this.onParentClickListener = onParentClickListener;
    }

    public static interface OnParentClickListener {
        void onParentClick(int position);

        void onAddClick(int position);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {
        void onClick(int parent, int position, BookAdapter adapter);

    }

    private OnAllChooseListener onAllChooseListener;

    public void setOnAllChooseListener(OnAllChooseListener onAllChooseListener) {
        this.onAllChooseListener = onAllChooseListener;
    }

    public static interface OnAllChooseListener {
        void onAllChoose(int position);

    }

    private OnChildItemLongClickListener onChildItemLongClickListener;

    public void setOnChildItemLongClickListener(OnChildItemLongClickListener onChildItemLongClickListener) {
        this.onChildItemLongClickListener = onChildItemLongClickListener;
    }

    public static interface OnChildItemLongClickListener {
        void onItemLongClick(int parent, int position);
    }

}
