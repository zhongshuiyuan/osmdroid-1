package com.huejie.osmdroid.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.SimpleProject;

import java.util.List;


/**
 * Created by Administrator on 2016/10/11.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.Holder> {
    private Context context;
    private List<SimpleProject> list;
    private boolean swipeEnable;
    private int projectType;
    private boolean tag;

    /**
     * @param swipeEnable 是否能侧滑编辑
     */
    public ProjectAdapter(Context context, List<SimpleProject> list, int projectType, boolean swipeEnable) {
        this.context = context;
        this.list = list;
        this.projectType = projectType;
        this.swipeEnable = swipeEnable;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }

    public void setList(List<SimpleProject> list) {
        this.list = list;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false));
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

            if (projectType == 0) {
                mHolder.btn_jlb.setText("查看记录簿");
                mHolder.btn_xl.setVisibility(View.VISIBLE);
                mHolder.btn_xl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != onClickListener) {
                            onClickListener.onLineClick(position);
                        }
                    }
                });
            } else {
                mHolder.btn_jlb.setText("下载项目");
                mHolder.btn_xl.setVisibility(View.GONE);
            }

            mHolder.btn_jlb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onBookClick(position);
                    }
                }
            });
            mHolder.swipe_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onLongClick(position, bean.projectId);
                    }
                    return true;
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
        private Button btn_xl;
        private Button btn_jlb;

        private View swipe_content;

        public Holder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_xmfzr = (TextView) itemView.findViewById(R.id.tv_xmfzr);
            tv_fxfzr = (TextView) itemView.findViewById(R.id.tv_fxfzr);
            btn_xl = (Button) itemView.findViewById(R.id.btn_xl);
            btn_jlb = (Button) itemView.findViewById(R.id.btn_jlb);
            swipe_content = itemView;
        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static interface OnClickListener {
        void onLongClick(int position, long id);

        void onLineClick(int position);

        void onBookClick(int position);
    }
}
