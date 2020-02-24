package com.huejie.osmdroid.more.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.favorites.Favorites;

import java.util.List;

public class ChooseFolderAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Favorites> list;

    public ChooseFolderAdapter(Context context, List<Favorites> list) {
        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_choose_folder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof Holder) {
            final Holder mHolder = (Holder) viewHolder;
            Favorites favorites = list.get(i);
            mHolder.tv_filename.setText(favorites.name);
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(i);
                    }
                }
            });
            mHolder.cb_tag.setChecked(favorites.isChoose);
            mHolder.cb_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onCheck(i);
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

        public TextView tv_filename;

        public CheckedTextView cb_tag;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_filename = itemView.findViewById(R.id.tv_filename);
            cb_tag = itemView.findViewById(R.id.cb_tag);
        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {
        void onClick(int position);

        void onCheck(int position);


    }


}
