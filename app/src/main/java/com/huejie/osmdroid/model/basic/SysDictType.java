package com.huejie.osmdroid.model.basic;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

@Table("system_dict_type")
public class SysDictType extends LitePalSupport implements Serializable {
    public long id;
    @Column(columeName = "dict_name")
    public String dictName;
    @Column(columeName = "dict_type")
    public String dictType;
    public long status;
    @Column(columeName = "is_sys")
    public long isSys;
    @Column(columeName = "create_user")
    public long createUser;
    @Column(columeName = "create_time")
    public String createTime;
    @Column(columeName = "update_user")
    public long updateUser;
    @Column(columeName = "update_time")
    public String updateTime;
    public String remark;

}
