package com.huejie.osmdroid.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.SimpleProject;

import java.util.List;


/**
 * Created by Administrator on 2016/10/11.
 */

public class ChooseProjectAdapter extends RecyclerView.Adapter<ChooseProjectAdapter.Holder> {
    private Context context;
    private List<SimpleProject> list;
    private boolean swipeEnable;

    private boolean tag;

    /**
     * @param swipeEnable 是否能侧滑编辑
     */
    public ChooseProjectAdapter(Context context, List<SimpleProject> list, boolean swipeEnable) {
        this.context = context;
        this.list = list;

        this.swipeEnable = swipeEnable;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }


    public void setList(List<SimpleProject> list) {
        this.list = list;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_project, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (holder instanceof Holder) {
            Holder mHolder = (Holder) holder;
            final SimpleProject bean = list.get(position);
            mHolder.tv_name.setText("项目名称：" + bean.projectName);
            mHolder.tv_number.setText("项目编号：" + bean.projectCode);
            mHolder.tv_xmfzr.setText("项目负责人：" + bean.projectLeaders);
            mHolder.tv_fxfzr.setText("分项负责人：" + bean.projectSubLeader);
            mHolder.rb_check.setChecked(bean.isCheck);
            mHolder.swipe_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_number;
        private TextView tv_xmfzr;
        private TextView tv_fxfzr;
        private CheckedTextView rb_check;
        private View swipe_content;

        public Holder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_xmfzr = (TextView) itemView.findViewById(R.id.tv_xmfzr);
            tv_fxfzr = (TextView) itemView.findViewById(R.id.tv_fxfzr);
            rb_check = itemView.findViewById(R.id.rb_check);
            swipe_content = itemView;
        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {

        void onClick(int position);
    }
}
