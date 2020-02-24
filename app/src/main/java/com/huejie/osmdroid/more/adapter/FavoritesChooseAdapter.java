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

public class FavoritesChooseAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Favorites> list;

    public FavoritesChooseAdapter(Context context, List<Favorites> list) {
        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_favorites, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof Holder) {
            final Holder mHolder = (Holder) viewHolder;
            final Favorites favorites = list.get(i);
            mHolder.iv_img.setImageResource(R.mipmap.mian);
            mHolder.iv_status.setVisibility(View.GONE);
            mHolder.tv_file.setText(favorites.name);
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onFileClick(i);

                    }

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


    }


}
