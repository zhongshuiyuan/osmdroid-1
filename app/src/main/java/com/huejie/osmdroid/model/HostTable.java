package com.huejie.osmdroid.model;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * ip地址映射表
 */
public class HostTable extends LitePalSupport implements Serializable {
    public long id;
    @Column(unique = true)
    public String url;
    @Column(unique = true)
    public String workDir;//工作目录
    public String ip;
    public String port;

}
