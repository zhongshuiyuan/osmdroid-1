package com.huejie.osmdroid.model;


import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 保存所有下载的key值
 */

public class MapDownloadKey extends LitePalSupport implements Serializable {
    public long id;
    public long historyId;
    public long key;

}
