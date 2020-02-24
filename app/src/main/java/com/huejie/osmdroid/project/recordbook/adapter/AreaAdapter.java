package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.project.recordbook.model.Province;

import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Province> provinceList;

    public AreaAdapter(Context context, List<Province> provinceList) {
        this.context = context;
        this.provinceList = provinceList;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_province, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof Holder) {
            final Holder mHolder = (Holder) viewHolder;
            Province province = provinceList.get(i);
            mHolder.tv_name.setText(province.provinceName);
            mHolder.listView.setVisibility(province.isCheck ? View.VISIBLE : View.GONE);
            mHolder.tv_name.setChecked(province.isCheck);
            CityAdapter adapter = new CityAdapter(context, province.cityList);
            mHolder.listView.setAdapter(adapter);
            mHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != onClickListener) {
                        onClickListener.onClick(i, position);
                    }
                }
            });
            mHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onParentClickListener) {
                        onParentClickListener.onParentClick(i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        public CheckedTextView tv_name;
        public ListView listView;


        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            listView = itemView.findViewById(R.id.listView);
        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {
        void onClick(int parent, int position);
    }

    private OnParentClickListener onParentClickListener;

    public void setOnParentClickListener(OnParentClickListener onParentClickListener) {
        this.onParentClickListener = onParentClickListener;
    }

    public static interface OnParentClickListener {
        void onParentClick(int position);
    }


}
