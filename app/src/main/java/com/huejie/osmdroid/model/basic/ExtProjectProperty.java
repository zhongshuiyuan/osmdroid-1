package com.huejie.osmdroid.model.basic;


import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

@Table("ext_project_property")
public class ExtProjectProperty extends LitePalSupport implements Serializable {
    public long id;
    @Column(columeName = "projectId")
    public long projectId;//项目主键id
    @Column(columeName = "property_id")
    public long propertyId;//拓展属性主键id
    @Column(columeName = "versionId")
    public long versionId;//备份版本主键id
    @Column(columeName = "property_value")
    public String propertyValue;//拓展属性对应的值
}
