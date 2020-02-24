package com.huejie.osmdroid.model.basic;


import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

@Table("core_project_role")
public class CoreProjectRole extends LitePalSupport implements Serializable {

    public long id;
    @Column(columeName = "project_role_name")
    public String projectRoleName;//项目负责人  分项负责人
    @Column(columeName = "project_role_code")
    public String projectRoleCode;// projectleader  projectsubleader
    @Column(columeName = "createTime")
    public String createTime;
    @Column(columeName = "create_user")
    public long createUser;
    @Column(columeName = "updateTime")
    public String updateTime;
    @Column(columeName = "updateUser")
    public long updateUser;
}
