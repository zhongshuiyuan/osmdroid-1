package com.huejie.osmdroid.model;


import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 记录簿相关文件存储路径
 */
@Table("common_record_book_media")
public class CommonRecordBookMedia extends LitePalSupport implements Serializable {
    public long id;                     //数据库id
    @Column(columeName = "record_book_id")
    public long recordBookId;       //记录簿id
    @Column(columeName = "record_book_type_id")
    public long recordBookTypeId;       //记录簿类型
    @Column(columeName = "media_type")
    public int mediaType;                //数据类型（音频、视频、图片） 0：图片1：音频 2：视频
    @Column(columeName = "media_path")
    public String mediaPath;                //数据存储路径
    public String extension;                //扩展名
    @Column(columeName = "media_name")
    public String mediaName;//文件名
    @Column(columeName = "date_time_original")
    public String dateTimeOriginal;         //数据创建时间
    public long size;                 //文件大小
//    public long duration;            //如果是音视频，则音视频时长
    @Column(ignore = true)
    public boolean isSelect;
    public long make; //设备品牌
    public double model; //设备型号
    @Column(columeName = "gps_latitude")
    public String gpsLatitude;
    @Column(columeName = "gps_longitude")
    public String gpsLongitude;
    @Column(columeName = "gps_altitude")
    public String gpsAltitude;
    public String thumbnail;//缩略图


}
