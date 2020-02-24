package com.huejie.osmdroid.more.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.favorites.FavoritesAccessories;
import com.huejie.osmdroid.util.Util;
import com.huejie.osmdroid.view.SwipeMenuView;

import java.util.List;


/**
 * Created by Administrator on 2016/10/11.
 */

public class LocalFilesAdapter extends RecyclerView.Adapter<LocalFilesAdapter.Holder> {
    private Context context;
    private List<FavoritesAccessories> list;
    private boolean swipeEnable;

    /**
     * @param swipeEnable 是否能侧滑编辑
     */
    public LocalFilesAdapter(Context context, List<FavoritesAccessories> list, boolean swipeEnable) {
        this.context = context;
        this.list = list;
        this.swipeEnable = swipeEnable;
    }

    public void setList(List<FavoritesAccessories> list) {
        this.list = list;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_localfile_swipe, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (holder instanceof Holder) {
            Holder mHolder = (Holder) holder;
            ((SwipeMenuView) holder.itemView).setIos(false).setLeftSwipe(swipeEnable).setSwipeEnable(true);

            mHolder.tv_fileName.setText(list.get(position).accessoriesName);
            mHolder.tv_size.setText(list.get(position).size + "");
            mHolder.tv_createDate.setText(Util.getFromatDate(list.get(position).createTime, Util.Y_M_D_H_M_S));
            mHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onDel(position);
                    }
                }
            });
            mHolder.swipe_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView tv_fileName;
        private TextView tv_createDate;
        private TextView tv_size;
        private TextView btnDelete;
        private View swipe_content;

        public Holder(View itemView) {
            super(itemView);
            tv_fileName = (TextView) itemView.findViewById(R.id.tv_fileName);
            swipe_content = itemView.findViewById(R.id.swipe_content);
            tv_createDate = (TextView) itemView.findViewById(R.id.tv_createDate);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            btnDelete = (TextView) itemView.findViewById(R.id.btnDelete);
        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {
        void onClick(int position);

        void onDel(int position);
    }
}
