package com.huejie.osmdroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.adapter.PopIpAdapter;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.dialog.LodingDialog;
import com.huejie.osmdroid.http.FileCallBack;
import com.huejie.osmdroid.http.HttpProvider;
import com.huejie.osmdroid.http.HttpUtil;
import com.huejie.osmdroid.http.JsonHelper;
import com.huejie.osmdroid.http.ParamsMap;
import com.huejie.osmdroid.http.ResponseCallBack;
import com.huejie.osmdroid.model.HostTable;
import com.huejie.osmdroid.model.basic.SysUser;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.PasswordUtils;
import com.huejie.osmdroid.util.Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_ip)
    EditText et_ip;
    @BindView(R.id.ll_ip)
    View ll_ip;
    @BindView(R.id.iv_ip)
    ImageView iv_ip;
    @BindView(R.id.et_port)
    EditText et_port;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.ll_online)
    View ll_online;
    @BindView(R.id.cb_auto_login)
    CheckBox cb_auto_login;

    @BindView(R.id.rb_online)
    RadioButton rb_online;
    @BindView(R.id.rb_outline)
    RadioButton rb_outline;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }


    public static final String FROM_LAUNCH = "launch";
    public static final String FROM = "from";


    @OnClick(R.id.iv_ip)
    public void showIpList() {
        DBUtil.useLocal();
        final List<HostTable> list = LitePal.findAll(HostTable.class);
        if (null == list) {
            return;
        }
        ll_ip.post(new Runnable() {
            @Override
            public void run() {
                int mWidth = ll_ip.getWidth();
                View contentView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.pop_lv, null);
                final PopupWindow pop_grade = new PopupWindow(contentView, mWidth, LinearLayout.LayoutParams.WRAP_CONTENT);  //设置popwindow为布局大小
                // 点击PopupWindow以外的区域，PopupWindow是否会消失。
                pop_grade.setBackgroundDrawable(new BitmapDrawable());
                pop_grade.setOutsideTouchable(true);
                ListView listView = (ListView) contentView.findViewById(R.id.listView);
                PopIpAdapter adapter = new PopIpAdapter(LoginActivity.this, list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_ip.setText(list.get(position).ip);
                        et_port.setText(list.get(position).port);
                        pop_grade.dismiss();
                    }
                });
                pop_grade.showAsDropDown(ll_ip);    //popwindow显示在控件下方

            }
        });
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (checkTag == 0) {
            //在线登录
            if (NetworkUtils.isConnected()) {
                //有网则在线登录
                final String ip = et_ip.getText().toString().trim();
                if (TextUtils.isEmpty(ip)) {
                    ToastUtils.showShortToast("ip地址不能为空");
                    return;
                }
                final String port = et_port.getText().toString().trim();
                if (TextUtils.isEmpty(port)) {
                    ToastUtils.showShortToast("端口号不能为空");
                    return;
                }
                final String username = et_username.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    ToastUtils.showShortToast("用户名不能为空");
                    return;
                }
                final String pwd = et_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShortToast("密码不能为空");
                    return;
                }
                final String url = "http://" + ip + ":" + port;
                LodingDialog.init(this);
                final LodingDialog dialog = LodingDialog.getInstance();
                dialog.show();
                radioGroup.check(R.id.rb_online);
                HttpProvider.postString(this, url + HttpUtil.login, new ParamsMap().with("grant_type", "password").with("scope", "all").with("username", username).with("password", pwd).with("isAutoLogin", "0"), new ResponseCallBack() {
                    @Override
                    public void onSuccess(String json) {
                        try {
                            JSONObject object = new JSONObject(json);
                            String token = "Bearer " + JsonHelper.getJsonString(JsonHelper.getJsonObject(object, "data"), "access_token");
                            final String workDir = Util.ipToName(url);
                            AppContext.sp.putString(Config.SP.LOGIN_TYPE, Config.ONLINE);
                            AppContext.sp.putBoolean(Config.SP.IS_LOGIN, true);
                            AppContext.sp.putString(Config.SP.CURRENT_USER, username);
                            AppContext.sp.putString(Config.CURRENT_PROJECT, null);
                            AppContext.sp.putLong(Config.SP.CURRENT_USER_ID, JsonHelper.getJsonLong(JsonHelper.getJsonObject(object, "data"), "userId"));
                            AppContext.sp.putString(Config.SP.CURRENT_WORK_DIR, workDir);
                            AppContext.sp.putBoolean(Config.SP.AUTO_LOGIN, cb_auto_login.isChecked());
                            AppContext.sp.putString(Config.SP.TOKEN, token);
                            AppContext.sp.putString(Config.SP.CURRENT_URL, url);
//                            copyAssetAndWrite("base.db");
//                            //复制基础表到对应的目录中，目前这个功能仅用于演示用，后期直接改成从服务端同步
//                            FileUtils.copyFile(new File(getCacheDir(), "base.db"), new File(Util.getBaseDbPath(LoginActivity.this, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)), "base.db"));
                            HttpProvider.postString(this, AppContext.sp.getString(Config.SP.CURRENT_URL) + HttpUtil.downLoadBaseInformation, new ParamsMap(), new FileCallBack(Util.getBaseDbPath(LoginActivity.this, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)), "base.db") {
                                @Override
                                public void downLoadError(Exception e) {

                                }

                                @Override
                                public void inProgress(float progress, long total, int id) {

                                }

                                @Override
                                public void onSuccess(File file, int id) {
                                    DBUtil.useLocal();
                                    boolean exist = LitePal.where("workDir = ?", workDir).count(HostTable.class) > 0;
                                    if (!exist) {
                                        HostTable newIp = new HostTable();
                                        newIp.url = url;
                                        newIp.ip = ip;
                                        newIp.port = port;
                                        newIp.workDir = workDir;
                                        newIp.save();
                                        FileUtils.createOrExistsDir(Util.getMyRootPath(LoginActivity.this) + workDir);
                                        FileUtils.createOrExistsDir(Util.getBaseDbPath(LoginActivity.this, workDir));
                                        //创建本地项目信息数据库文件夹
                                        FileUtils.createOrExistsDir(Util.getProjectPath(LoginActivity.this, workDir));
                                        FileUtils.createOrExistsDir(Util.getBackUpPath(LoginActivity.this, workDir));
                                        FileUtils.createOrExistsDir(Util.getProjectsPath(LoginActivity.this, workDir));
                                    }

                                    if (FROM_LAUNCH.equals(getIntent().getStringExtra(FROM))) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    }
                                    setResult(RESULT_OK);
                                    finish();

                                }

                                @Override
                                public void onComplete() {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    ToastUtils.showShortToast("下载出错");
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        //没网的情况下，弹出对话框，让用户选择是否进行离线登录
                        AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).setMessage("服务器异常，是否切换离线登录？").setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                outlineLogin();
                                dialog.dismiss();
                            }
                        }).create();
                        dialog.show();
                    }

                    @Override
                    public void onFail(String message) {

                    }

                    @Override
                    public void onComplete() {
//                        dialog.dismiss();
                    }
                });
            } else {
                //没网的情况下，弹出对话框，让用户选择是否进行离线登录
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).setMessage("当前无网络状态，是否切换离线登录？").setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        outlineLogin();
                        dialog.dismiss();
                    }
                }).create();
                dialog.show();
            }
        } else {
            //离线登录
            outlineLogin();
        }
    }

    /**
     * 将asset文件写入缓存
     */
    private boolean copyAssetAndWrite(String fileName) {
        try {
            File cacheDir = getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return false;
                }
            } else {
                if (outFile.length() > 10) {//表示已经写入一次
                    return true;
                }
            }
            InputStream is = getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void downloadBaseDb() {


    }


    /**
     * 离线登录
     */
    private void outlineLogin() {
        //首选判断本地数据库是否存在
        radioGroup.check(R.id.rb_outline);
        String ip = et_ip.getText().toString().trim();
        if (TextUtils.isEmpty(ip)) {
            ToastUtils.showShortToast("ip地址不能为空");
            return;
        }
        String port = et_port.getText().toString().trim();
        if (TextUtils.isEmpty(port)) {
            ToastUtils.showShortToast("端口号不能为空");
            return;
        }
        final String url = "http://" + ip + ":" + port;
        final String username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showShortToast("用户名不能为空");
            return;
        }
        final String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShortToast("密码不能为空");
            return;
        }

        if (!FileUtils.isFileExists(new File(Util.getBaseDbPath(this, Util.ipToName(url)), "base.db"))) {
            ToastUtils.showShortToast("用户不存在");
            return;
        }
        AppContext.sp.putString(Config.SP.CURRENT_WORK_DIR, Util.ipToName(url));
        DBUtil.useBaseDatabases(this);
        SysUser user = LitePal.where("login_name = ?", username).findFirst(SysUser.class);
        if (null == user) {
            ToastUtils.showShortToast("用户不存在");
            return;
        }
        if (!PasswordUtils.getSaltMD5(pwd, user.salt).equals(user.password)) {
            ToastUtils.showShortToast("密码错误");
            et_pwd.setText("");
            return;
        }
        AppContext.sp.putString(Config.SP.CURRENT_USER, username);
        AppContext.sp.putLong(Config.SP.CURRENT_USER_ID, user.id);
        AppContext.sp.putString(Config.CURRENT_PROJECT, null);
        AppContext.sp.putString(Config.SP.CURRENT_URL, url);
        AppContext.sp.putBoolean(Config.SP.AUTO_LOGIN, cb_auto_login.isChecked());
        AppContext.sp.putString(Config.SP.LOGIN_TYPE, Config.OUTLINE);
        AppContext.sp.putBoolean(Config.SP.IS_LOGIN, true);
        if (FROM_LAUNCH.equals(getIntent().getStringExtra(FROM))) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        setResult(RESULT_OK);
        finish();
    }


    private int checkTag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tv_title.setText("登录");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_online:
                        checkTag = 0;
                        break;
                    case R.id.rb_outline:
                        checkTag = 1;
                        break;
                }

            }
        });
    }

}
