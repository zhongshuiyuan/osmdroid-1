package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

@Table("other_engineering_sub_info")
public class OtherEngineeringSubInfo extends LitePalSupport implements Serializable {
    public long id;
    @Column(columeName = "record_book_id")
    public long recordBookId;
    @Column(columeName = "cross_angle")
    public long crossAngle;
    @Column(columeName = "update_user")
    public long updateUser;
    @Column(columeName = "version_id")
    public long versionId;
    @Column(columeName = "relocation_name")
    public String relocationName = "";
    @Column(columeName = "cross_stake")
    public String crossStake = "";
    @Column(columeName = "update_time")
    public String updateTime = "";
}
