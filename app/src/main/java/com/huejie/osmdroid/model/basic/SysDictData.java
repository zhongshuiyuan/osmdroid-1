package com.huejie.osmdroid.model.basic;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

@Table("system_dict_data")
public class SysDictData extends LitePalSupport implements Serializable {
    public long id;
    @Column(columeName = "dict_sort")
    public long dictSort;
    @Column(columeName = "dict_label")
    public String dictLabel;
    @Column(columeName = "dict_type")
    public String dictType;
    @Column(columeName = "css_class")
    public String cssClass;
    @Column(columeName = "list_class")
    public String listClass;
    @Column(columeName = "is_default")
    public int isDefault;
    public int status;
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
