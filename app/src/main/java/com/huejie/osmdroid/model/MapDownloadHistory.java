package com.huejie.osmdroid.model;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class MapDownloadHistory extends LitePalSupport implements Serializable {
    @Column(unique = true, nullable = false)
    public long id;
    public String mapType;
}
