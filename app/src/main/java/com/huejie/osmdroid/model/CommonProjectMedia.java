package com.huejie.osmdroid.model;


import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 记录簿相关文件存储路径
 */
@Table("common_project_media")
public class CommonProjectMedia extends LitePalSupport implements Serializable {
    public long id;                     //数据库id
    @Column(columeName = "project_id")
    public long projectId;       //项目id
    @Column(columeName = "media_type")
    public int mediaType;                //数据类型（音频、视频、图片） 0：图片1：音频 2：视频
    @Column(columeName = "media_path")
    public String mediaPath;                //数据存储路径
    @Column(columeName = "media_name")
    public String mediaName;//文件名
    public String extension;
    public double size;
    @Column(columeName = "version_id")
    public long versionId;
    @Column(ignore = true)
    public boolean isChoose;

}
