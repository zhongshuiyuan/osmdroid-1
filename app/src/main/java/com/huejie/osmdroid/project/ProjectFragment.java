package com.huejie.osmdroid.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.blankj.utilcode.utils.FileUtils;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.huajie.videoplayer.utils.NetworkUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.LoginActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.http.HttpProvider;
import com.huejie.osmdroid.http.HttpUtil;
import com.huejie.osmdroid.http.JsonHelper;
import com.huejie.osmdroid.http.ParamsMap;
import com.huejie.osmdroid.http.ResponseCallBack;
import com.huejie.osmdroid.model.SimpleProject;
import com.huejie.osmdroid.model.basic.SysUser;
import com.huejie.osmdroid.project.adapter.ProjectAdapter;
import com.huejie.osmdroid.project.recordbook.activity.RecordBookActivity;
import com.huejie.osmdroid.project.recordbook.activity.RectMapDownloadActivity;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 工程项目
 *
 * @author guc
 */

public class ProjectFragment extends Fragment {

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.iv_tb)
    ImageView iv_tb;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recycleView)
    LRecyclerView recycleView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private ProjectAdapter adapter;
    private List<SimpleProject> list = new ArrayList<>();
    private int projectType;


    public ProjectFragment() {
        // Required empty public constructor
    }

    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project, container, false);
    }


    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        //先获取本地数据库的数据
        adapter = new ProjectAdapter(getActivity(), list, projectType, false);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recycleView.setAdapter(mLRecyclerViewAdapter);
//        recycleView.setRefreshHeader(null);
        //设置底部刷新样式及颜色
        recycleView.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.white);
        recycleView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //设置不能下拉刷新
        recycleView.setPullRefreshEnabled(false);
        // recycleView.setLoadMoreEnabled(false);
        recycleView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        recycleView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_local) {
                    getLocalData();
                } else {
                    getInlineData();
                }

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                list.clear();
                adapter.notifyDataSetChanged();
                page = 1;
                switch (checkedId) {
                    case R.id.rb_local:
                        projectType = 0;
                        radioGroup.check(R.id.rb_local);
                        adapter.setProjectType(projectType);
                        getLocalData();
                        break;
                    case R.id.rb_inline:
                        if (Config.ONLINE.equals(AppContext.sp.getString(Config.SP.LOGIN_TYPE))) {
                            projectType = 1;
                            radioGroup.check(R.id.rb_inline);
                            adapter.setProjectType(projectType);
                            getInlineData();
                        } else {
                            //进行离线登录
                            if (NetworkUtils.isNetworkConnected(getActivity())) {
                                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("当前为离线状态，是否切换在线登录？").setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String cUrl = AppContext.sp.getString(Config.SP.CURRENT_URL);
                                        String cUser = AppContext.sp.getString(Config.SP.CURRENT_USER);
                                        DBUtil.useBaseDatabases(getActivity());
                                        final SysUser user = LitePal.where("login_name = ?", cUser).findFirst(SysUser.class);

                                        HttpProvider.postString(this, cUrl + HttpUtil.login, new ParamsMap().with("grant_type", "password").with("scope", "all").with("username", user.loginName).with("password", user.password).with("isAutoLogin", "1"), new ResponseCallBack() {
                                            @Override
                                            public void onSuccess(String json) {
                                                try {
                                                    JSONObject object = new JSONObject(json);
                                                    String token = "Bearer " + JsonHelper.getJsonString(JsonHelper.getJsonObject(object, "data"), "access_token");
                                                    AppContext.sp.putString(Config.SP.LOGIN_TYPE, Config.ONLINE);
                                                    AppContext.sp.putBoolean(Config.SP.IS_LOGIN, true);
                                                    AppContext.sp.putString(Config.SP.TOKEN, token);
                                                    getInlineData();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onFail(String message) {
                                                //用户名或密码错误，用户不存在，直接跳到登录界面
                                                AppContext.sp.putBoolean(Config.SP.IS_LOGIN, false);
                                                startActivity(new Intent(getActivity(), LoginActivity.class).putExtra(LoginActivity.FROM, LoginActivity.FROM_LAUNCH));
                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });
                                    }


                                }).create();
                                dialog.show();
                            }
                        }
                        break;

                }
            }
        });
        adapter.setOnClickListener(new ProjectAdapter.OnClickListener() {
            @Override
            public void onLongClick(final int position, final long id) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_local) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("警告").setMessage("确认删除该项目吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除附件
                            DBUtil.useProjectDatabases(getActivity());
                            //删除项目目录
                            FileUtils.deleteDir(Util.getProjectsPathByName(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR), list.get(position).projectName));
                            if (list.get(position).projectName.equals(AppContext.sp.getString(Config.CURRENT_PROJECT))) {
                                AppContext.sp.remove(Config.CURRENT_PROJECT);
                            }
                            list.get(position).delete();
                            dialog.dismiss();
                            list.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(0, list.size());
                        }
                    }).create();
                    dialog.show();
                }

            }

            @Override
            public void onLineClick(int position) {
                //查看线路
                startActivity(new Intent(getActivity(), RectMapDownloadActivity.class).putExtra("actionType", RectMapDownloadActivity.ACTION_SHOW_LINE).putExtra("project", list.get(position)));
            }

            @Override
            public void onBookClick(int position) {
                startActivity(new Intent(getActivity(), RecordBookActivity.class).putExtra("project", list.get(position)).putExtra("projectType", projectType));
            }
        });
        getLocalData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getInlineData();
    }

    private void getLocalData() {
        progressBar.setVisibility(View.VISIBLE);
        DBUtil.useProjectDatabases(getActivity());
        int total = LitePal.count(SimpleProject.class);
        list.addAll(LitePal.limit(15).offset(15 * (page - 1)).find(SimpleProject.class));
        adapter.notifyDataSetChanged();
        page++;
        recycleView.setNoMore(list.size() >= total);
        recycleView.setLoadMoreEnabled(list.size() < total);
        progressBar.setVisibility(View.GONE);
    }

    private int page = 1;

    private void getInlineData() {
        progressBar.setVisibility(View.VISIBLE);
        HttpProvider.postString(this, AppContext.sp.getString(Config.SP.CURRENT_URL) + HttpUtil.getProjectList, new ParamsMap().with("pageNum", page).with("pageSize", 15), new ResponseCallBack() {
            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    JSONObject data = JsonHelper.getJsonObject(object, "data");
                    int total = JsonHelper.getJsonInt(data, "total");
                    JSONArray array = JsonHelper.getJsonArray(data, "rows");
                    for (int i = 0; i < array.length(); i++) {
                        SimpleProject project = new SimpleProject();
                        JSONObject pObject = JsonHelper.getJsonObject(array, i);
                        project.projectId = JsonHelper.getJsonLong(pObject, "projectId");
                        project.projectName = JsonHelper.getJsonString(pObject, "projectName");
                        project.projectCode = JsonHelper.getJsonString(pObject, "projectCode");
                        JSONArray rObj = JsonHelper.getJsonArray(pObject, "coreProjectRolePeoples");

                        String projecLeader = "";
                        String projecSubLeader = "";
                        for (int j = 0; j < rObj.length(); j++) {
                            JSONObject leader = JsonHelper.getJsonObject(rObj, j);
                            if ("ProjectLeader".equals(JsonHelper.getJsonString(leader, "projectRoleCode"))) {
                                projecLeader += (JsonHelper.getJsonString(leader, "userName") + ",");
                            } else if ("ProjectSubLeader".equals(JsonHelper.getJsonString(leader, "projectRoleCode"))) {
                                projecSubLeader += (JsonHelper.getJsonString(leader, "userName") + ",");
                            }

                        }
                        project.projectLeaders = projecLeader.contains(",") ? projecLeader.substring(0, projecLeader.lastIndexOf(",")) : projecLeader;
                        project.projectSubLeader = projecSubLeader.contains(",") ? projecSubLeader.substring(0, projecSubLeader.lastIndexOf(",")) : projecSubLeader;
                        list.add(project);
                    }


                    adapter.notifyDataSetChanged();
                    page++;
                    recycleView.setNoMore(list.size() >= total);
                    recycleView.setLoadMoreEnabled(list.size() < total);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String message) {

            }

            @Override
            public void onComplete() {
                progressBar.setVisibility(View.GONE);
            }
        });

    }


}
