package com.huejie.osmdroid.model.favorites;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 收藏夹附件
 */
public class FavoritesAccessories extends LitePalSupport implements Serializable {
    public long id;
    @Column(nullable = false)
    public String favoritesName;
    @Column(nullable = false)
    public String parentName;
    public String path;
    public String accessoriesName;
    public String size;
    public long createTime;
    public long updateTime;
}
