package com.huejie.osmdroid.http;

/**
 * Created by guc on 2017/3/4.
 * 所有http请求都呈现于此
 */

public class HttpUtil {

    public static final String TianDiTuSearchUrl = "http://api.tianditu.gov.cn/search";//正式地址

    public static final String IP = "http://192.168.1.79:8092";

    /**
     * 获取项目列表
     */
    public static final String getProjectList = "/internal/coreproject/list";
    /**
     * 获取项目列表
     */
    public static final String selectWorksiteOverView = "/internal/commonrecordbook/selectWorksiteOverView";
    /**
     * 登陆
     */
    public static final String login = "/auth/oauth/token";
    /**
     * 下载
     */
    public static final String download = "/fastdfs/file/core/download";
    /**
     * 下载项目
     */
    public static final String downloadProject = "/fastdfs/file/download/downLoadProjectInformation";
    /**
     * 下载基础数据库信息【项目基础数据+后台服务基础数据】
     */
    public static final String downLoadBaseInformation = "/fastdfs/file/download/downLoadBaseInformation";


}
