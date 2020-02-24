package com.huejie.osmdroid.app;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.Utils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.HostTable;
import com.huejie.osmdroid.project.recordbook.activity.PicassoImageLoader;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import org.litepal.LitePal;
import org.osmdroid.config.Configuration;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

public class AppContext extends TinkerApplication {

    public static Application app;
    /**
     * 偏好设置类
     */
    public static SPUtils sp;

    public AppContext() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.huejie.osmdroid.app.CustomerApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Utils.init(this);
        initImageLoader();
        LitePal.initialize(this);
        MultiDex.install(this);
        //初始化sp工具类
        initSp();
        initConfig();
        //初始化图片选择
        initImage();

    }

    private void initConfig() {
        DBUtil.useLocal();
        if (!LitePal.isExist(HostTable.class, "url = ?", "http://192.168.1.79:8092")) {
            HostTable ip1 = new HostTable();
            ip1.url = "http://192.168.1.79:8092";
            ip1.workDir = Util.ipToName(ip1.url);
            ip1.ip = "192.168.1.79";
            ip1.port = "8092";
            ip1.save();
        }
        if (!LitePal.isExist(HostTable.class, "url = ?", "http://27.17.20.242:8085")) {
            HostTable ip2 = new HostTable();
            ip2.url = "http://27.17.20.242:8085";
            ip2.ip = "27.17.20.242";
            ip2.port = "8085";
            ip2.workDir = Util.ipToName(ip2.url);
            ip2.save();
        }
        //创建目录

        List<HostTable> ips = LitePal.findAll(HostTable.class);
        for (int i = 0; i < ips.size(); i++) {
            HostTable ip = ips.get(i);
            //创建目录
            FileUtils.createOrExistsDir(Util.getMyRootPath(this) + ip.workDir);
            FileUtils.createOrExistsDir(Util.getBaseDbPath(this, ip.workDir));
            //创建本地项目信息数据库文件夹
            FileUtils.createOrExistsDir(Util.getProjectPath(this, ip.workDir));
            FileUtils.createOrExistsDir(Util.getBackUpPath(this, ip.workDir));
            FileUtils.createOrExistsDir(Util.getProjectsPath(this, ip.workDir));
        }
        //生成地图目录
        Configuration.getInstance().setOsmdroidTileCache(new File(Util.getTilesPath(this)));

    }

    /**
     * 初始化sp
     */
    private void initSp() {
        if (null == sp) {
            sp = new SPUtils(getPackageName());
            if (!sp.contains(Config.JWDGS)) {
                sp.putString(Config.JWDGS, "度");
            }
            if (!sp.contains(Config.CDDWGS)) {
                sp.putString(Config.CDDWGS, "公里、米");
            }
            if (!sp.contains(Config.ZDJB)) {
                sp.putInt(Config.ZDJB, 20);
            }
            if (!sp.contains(Config.HCDX)) {
                sp.putInt(Config.HCDX, 1024);
            }
            if (!sp.contains(Config.CURRENT_MAP)) {
                sp.putString(Config.CURRENT_MAP, Config.TileSourceName.GOOGLEROAD);
            }
        }
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initImage() {
        //设置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getResources().getColor(R.color.colorPrimary))
                .setEditPhotoBgTexture(ContextCompat.getDrawable(this, android.R.color.black))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(false)
                .setCropSquare(false)
                .setMutiSelectMaxSize(9)
                .setForceCrop(false)
                .setEnablePreview(false)
                .setCropHeight(320)
                .setCropWidth(480)
                .build();

        //配置imageloader
        PicassoImageLoader imageloader = new PicassoImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setNoAnimcation(true)
                .setFunctionConfig(functionConfig)
                .setEditPhotoCacheFolder(new File(Util.getMyRootPath(this) + File.separator + "GalleryFinal" + File.separator + "EditCache"))
                .setTakePhotoFolder(new File(Util.getMyRootPath(this) + File.separator + "GalleryFinal" + File.separator + "photo"))
                .build();
        GalleryFinal.init(coreConfig);
    }

}
