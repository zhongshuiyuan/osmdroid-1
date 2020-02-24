package com.huejie.osmdroid.more.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.favorites.Favorites;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Favorites> list;

    public FavoritesAdapter(Context context, List<Favorites> list) {
        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_favorites, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof Holder) {
            final Holder mHolder = (Holder) viewHolder;
            final Favorites favorites = list.get(i);
            if (favorites.fileType == 0) {
                switch (favorites.type) {
                    case 0:
                        mHolder.iv_img.setImageResource(R.mipmap.dian);
                        break;
                    case 1:
                        mHolder.iv_img.setImageResource(R.mipmap.xian);
                        break;
                    case 2:
                    case 3:
                        mHolder.iv_img.setImageResource(R.mipmap.mian);
                        break;
                }
            } else {
                mHolder.iv_img.setImageResource(R.mipmap.mulu);
            }
            mHolder.iv_status.setImageResource(favorites.fileStatus == 0 ? R.mipmap.show : R.mipmap.hide);
            mHolder.tv_file.setText(favorites.name);

            mHolder.iv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onFileStatusClick(i);
                    }
                }
            });

            mHolder.tv_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        if (favorites.fileType == 0) {
                            onClickListener.onFileClick(i);
                        } else {
                            onClickListener.onFloderClick(i);
                        }
                    }

                }
            });
            mHolder.tv_file.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onLongClickListener != null) {
                        onLongClickListener.onLongClick(i);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        public TextView tv_file;
        public ImageView iv_img;
        public ImageView iv_status;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_file = itemView.findViewById(R.id.tv_file);
            iv_img = itemView.findViewById(R.id.iv_img);
            iv_status = itemView.findViewById(R.id.iv_status);
        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {
        void onFileClick(int position);

        void onFloderClick(int position);

        void onFileStatusClick(int position);

    }

    private OnLongClickListener onLongClickListener;

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public static interface OnLongClickListener {
        void onLongClick(int position);
    }


}
