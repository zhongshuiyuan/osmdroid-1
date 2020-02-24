package com.huejie.osmdroid.model;


import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
@Table("core_project")
public class CoreProject extends LitePalSupport implements Serializable {
    public long id;
    /**
     * 项目名称
     */
    @Column(columeName = "project_name")
    public String projectName;
    /**
     * 项目编号
     */
    @Column(columeName = "project_code")
    public String projectCode;
    /**
     * 桩号前缀
     */
    @Column(columeName = "stake_prefix")
    public String stakePrefix;
    /**
     * 项目主键ID
     */
    @Column(unique = true, nullable = false,columeName = "project_id")
    public long projectId;
    @Column(columeName = "project_leader_id")
    public long projectLeaderId;

    /**
     * 项目状态：正常normal ，异常abnormal，回收recycle，删除delete
     */
    @Column(columeName = "project_status")
    public int projectStatus;

    /**
     * 创建人主键ID
     */
    @Column(columeName = "project_create_user")
    public long projectCreateUser;
    /**
     * 创建时间
     */
    @Column(columeName = "project_create_time")
    public long projectCreateTime;
    /**
     * 更新人主键ID
     */
    @Column(columeName = "project_update_user")
    public long projectUpdateUser;
    /**
     * 更新时间
     */
    @Column(columeName = "project_update_time")
    public long projectUpdateTime;
    /**
     * 项目备注
     */
    @Column(columeName = "project_remark")
    public String projectRemark;
    /**
     * 业主单位
     */
    @Column(columeName = "project_owner")
    public String projectOwner;
    /**
     * 项目地域(行政区域主键ID)
     */
    @Column(columeName = "project_regional")
    public long projectRegional;
    /**
     * 项目等级
     */
    @Column(columeName = "project_level")
    public int projectLevel;
    /**
     * 项目阶段
     */
    @Column(columeName = "project_phase")
    public int projectPhase;
    /**
     * 项目线路信息
     */
    @Column(columeName = "project_line_info")
    public String projectLineInfo;
    @Column(columeName = "projectleadername")
    public String projectLeaderName;
    @Column(columeName = "projectcreateusername")
    public String projectCreateUserName;
    @Column(columeName = "project_update_user_name")
    public String projectUpdateUserName;
    @Column(columeName = "project_regional_name")
    public String projectRegionalName;
    /**
     * 备份版本主键ID
     */
    @Column(columeName = "version_id")
    public long versionId;
    @Column(ignore = true)
    public boolean isCheck;

    /**
     * 项目负责人
     */
    @Column(columeName = "project_leaders")
    public String projectLeaders;
    /**
     * 分项负责人
     */
    @Column(columeName = "project_sub_leader")
    public String projectSubLeader;

}
