package com.huejie.osmdroid.model;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class SimpleProject extends LitePalSupport implements Serializable {
    /**
     * 项目名称
     */
    public String projectName;
    /**
     * 项目编号
     */

    public String projectCode;

    /**
     * 项目主键ID
     */
    @Column(unique = true, nullable = false)
    public long projectId;



    @Column(ignore = true)
    public boolean isCheck;

    /**
     * 项目负责人
     */

    public String projectLeaders;
    /**
     * 分项负责人
     */

    public String projectSubLeader;
}
