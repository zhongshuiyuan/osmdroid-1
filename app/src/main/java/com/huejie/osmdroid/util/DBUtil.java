package com.huejie.osmdroid.util;

import android.content.Context;

import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.model.SimpleProject;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

public class DBUtil {

    public static final String DB_NAME = "hsdfas";
    public static final String DB_PROJECT = "project";
    public static final String DB_BASE = "base";
    public static final int DB_VERSION = 1;

    /**
     * 使用databases/local.db数据库
     */
    public static void useLocal() {
        LitePal.useDefault();
    }

    /**
     * 使用projects目录下面对应的项目的数据库
     */
    public static void useExtraDbByProjectName(Context context, String projectName) {
        LitePalDB litePalDB = new LitePalDB(DB_NAME, DB_VERSION);
        litePalDB.setStorage(Util.getProjectsPathByName(context, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR), projectName));
        LitePal.use(litePalDB);
    }

    /**
     * 使用project/project.db数据库
     */
    public static void useProjectDatabases(Context context) {
        LitePalDB litePalDB = new LitePalDB(DB_PROJECT, DB_VERSION);
        litePalDB.setStorage(Util.getProjectPath(context, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)));
        litePalDB.addClassName(SimpleProject.class.getName());
        LitePal.use(litePalDB);
    }

    /**
     * 使用base/base.db基础数据库，该数据库从服务器下载
     */
    public static void useBaseDatabases(Context context) {
        LitePalDB litePalDB = new LitePalDB(DB_BASE, DB_VERSION);
        litePalDB.setStorage(Util.getBaseDbPath(context, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)));
        LitePal.use(litePalDB);

    }


    public static String getProjectNameById(Context context, String id) {
        useProjectDatabases(context);
        return LitePal.where("projectId = ?", id).findFirst(SimpleProject.class).projectName;

    }

}
