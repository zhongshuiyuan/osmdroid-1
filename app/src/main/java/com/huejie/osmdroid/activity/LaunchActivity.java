package com.huejie.osmdroid.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.huajie.videoplayer.utils.NetworkUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.http.HttpProvider;
import com.huejie.osmdroid.http.HttpUtil;
import com.huejie.osmdroid.http.JsonHelper;
import com.huejie.osmdroid.http.ParamsMap;
import com.huejie.osmdroid.http.ResponseCallBack;
import com.huejie.osmdroid.model.basic.SysUser;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.List;

import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class LaunchActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int TO_MAIN = 1;
    private static final int RC_CAMERA_AND_LOCATION = 1;
    private String[] permission = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        requiresPermission();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void requiresPermission() {
        if (EasyPermissions.hasPermissions(this, permission)) {
            handler.sendEmptyMessageDelayed(TO_MAIN, 3000);

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_hint),
                    RC_CAMERA_AND_LOCATION, permission);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_MAIN:
                    if (AppContext.sp.getBoolean(Config.SP.IS_LOGIN) && AppContext.sp.getBoolean(Config.SP.AUTO_LOGIN)) {
                        AppContext.sp.putBoolean(Config.SP.IS_LOGIN, false);
                        String cUrl = AppContext.sp.getString(Config.SP.CURRENT_URL);
                        String cUser = AppContext.sp.getString(Config.SP.CURRENT_USER);
                        DBUtil.useBaseDatabases(LaunchActivity.this);
                        final SysUser user = LitePal.where("login_name = ?", cUser).findFirst(SysUser.class);
                        if (NetworkUtils.isNetworkConnected(LaunchActivity.this)) {
                            HttpProvider.postString(this, cUrl + HttpUtil.login, new ParamsMap().with("grant_type", "password").with("scope", "all").with("username", user.loginName).with("password", user.password).with("isAutoLogin", "1"), new ResponseCallBack() {
                                @Override
                                public void onSuccess(String json) {
                                    try {
                                        JSONObject object = new JSONObject(json);
                                        String token = "Bearer " + JsonHelper.getJsonString(JsonHelper.getJsonObject(object, "data"), "access_token");
                                        AppContext.sp.putString(Config.SP.LOGIN_TYPE, Config.ONLINE);
                                        AppContext.sp.putBoolean(Config.SP.IS_LOGIN, true);
                                        AppContext.sp.putString(Config.SP.TOKEN, token);
                                        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFail(String message) {
                                    //用户名或密码错误，用户不存在，直接跳到登录界面
                                    AppContext.sp.putBoolean(Config.SP.IS_LOGIN, false);
                                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class).putExtra(LoginActivity.FROM, LoginActivity.FROM_LAUNCH));
                                    finish();

                                }

                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    super.onError(call, e, id);
                                    showOutLineLoginDialog();
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                        } else {
                            showOutLineLoginDialog();
                        }
                    } else {
                        AppContext.sp.putBoolean(Config.SP.IS_LOGIN, false);
                        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                        finish();
                    }
                    break;
            }
        }
    };

    private void showOutLineLoginDialog() {
        //进行离线登录
        AlertDialog dialog = new AlertDialog.Builder(LaunchActivity.this).setMessage("当前无网络状态，是否切换离线登录？").setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppContext.sp.putBoolean(Config.SP.IS_LOGIN, false);
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                finish();
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppContext.sp.putString(Config.SP.LOGIN_TYPE, Config.OUTLINE);
                AppContext.sp.putBoolean(Config.SP.IS_LOGIN, true);
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                finish();
                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }


}
