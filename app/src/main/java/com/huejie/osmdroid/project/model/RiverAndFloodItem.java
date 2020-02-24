package com.huejie.osmdroid.project.model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class RiverAndFloodItem extends LitePalSupport implements Serializable {
    public long id;
    @Column(columeName = "record_book_id")
    public long recordBookId;
    @Column(columeName = "record_book_type_id")
    public long recordBookTypeId;
    @Column(columeName = "version_id")
    public long versionId;
    @Column(columeName = "update_user")
    public long updateUser;
    @Column(columeName = "update_time")
    public String updateTime;
    public String name;
}
