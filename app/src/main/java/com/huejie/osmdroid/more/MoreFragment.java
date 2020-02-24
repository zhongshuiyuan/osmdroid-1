package com.huejie.osmdroid.more;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.LoginActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.more.activity.ChooseMapActivity;
import com.huejie.osmdroid.more.activity.FavoritesActivity;
import com.huejie.osmdroid.more.activity.MapSettingActivity;
import com.huejie.osmdroid.more.mapdownload.ChooseDownloadTypeActivity;
import com.huejie.osmdroid.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 底图切换类
 *
 * @author guc
 */

public class MoreFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    @BindView(R.id.btn_logout)
    Button btn_logout;

    @OnClick(R.id.tv_download)
    public void toMapDownload() {
        startActivity(new Intent(getActivity(), ChooseDownloadTypeActivity.class));

    }

    @OnClick(R.id.tv_favorites)
    public void toFavorites() {
//        if (AppContext.sp.getBoolean(Config.SP.ISLOGIN)) {
        startActivity(new Intent(getActivity(), FavoritesActivity.class));
//        } else {
//            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 101);
//        }


    }

    @OnClick(R.id.tv_map_type)
    public void chooseMap() {
        //选择地图
        startActivity(new Intent(getActivity(), ChooseMapActivity.class));
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        if (AppContext.sp.getBoolean(Config.SP.IS_LOGIN)) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("确认退出登录吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppContext.sp.putBoolean(Config.SP.IS_LOGIN, false);
                    AppContext.sp.putString(Config.SP.TOKEN, Config.DEFAULT_TOKEN);
                    startActivity(new Intent(getActivity(), LoginActivity.class).putExtra(LoginActivity.FROM, LoginActivity.FROM_LAUNCH));
                    getActivity().finish();
                }
            }).create();
            dialog.show();

        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 102);
        }


    }

    @OnClick(R.id.tv_map_setting)
    public void toMapSetting() {
        startActivity(new Intent(getActivity(), MapSettingActivity.class));

    }


    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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
        return inflater.inflate(R.layout.fragment_more, container, false);
    }


    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (AppContext.sp.getBoolean(Config.SP.IS_LOGIN)) {
            btn_logout.setText("退出登录");
        } else {
            btn_logout.setText("登录");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 102) {
                btn_logout.setText("退出登录");
            } else if (requestCode == 101) {
                btn_logout.setText("退出登录");
                startActivity(new Intent(getActivity(), FavoritesActivity.class));
            }

        }
    }
}
