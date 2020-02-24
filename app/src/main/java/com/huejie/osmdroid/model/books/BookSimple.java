package com.huejie.osmdroid.model.books;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class BookSimple extends LitePalSupport implements Serializable {
    public long id;
    @Column(columeName = "project_id")
    public long projectId;
    @Column(columeName = "create_time")
    public long createTime;
    @Column(columeName = "create_user")
    public long createUser;
    @Column(columeName = "update_time")
    public long updateTime;
    @Column(columeName = "update_user")
    public long updateUser;
    @Column(columeName = "record_book_type_id")
    public long recordBookTypeId;
    @Column(ignore = true)
    public boolean isCheck;
    @Column(defaultValue = "-1")
    public int weather;//从基础表里面查
    /**
     * 纬度
     */
    @Column(columeName = "position_y")
    public String positionY;
    /**
     * 经度
     */
    @Column(columeName = "position_x")
    public String positionX;

    public String status;

    @Column(columeName = "version_id")
    public long versionId;

    @Column(ignore = true)
    public String uniqueTag;
}
