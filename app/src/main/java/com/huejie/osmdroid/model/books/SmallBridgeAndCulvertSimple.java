package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author: TongWeiJie
 * @date: 2019/6/24 17:28
 * @description:
 */
public class SmallBridgeAndCulvertSimple extends LitePalSupport implements Serializable {
    public long id;
    @Column(columeName = "record_book_id")
    public long recordBookId;

    @Column(columeName = "update_time")
    public long updateTime;
    @Column(columeName = "update_user")
    public long updateUser;
    @Column(columeName = "version_id")
    public long versionId;

}
