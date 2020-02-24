package com.huejie.osmdroid.model.basic;

import com.huejie.osmdroid.model.books.BookSimple;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

@Table("record_book_type")
public class RecordBookType extends LitePalSupport implements Serializable {
    public long id;
    @Column(columeName = "record_book_type_name")
    public String recordBookTypeName;
    @Column(columeName = "record_book_type_code")
    public String recordBookTypeCode;
    public String status;
    @Column(columeName = "update_user")
    public long updateUser;
    @Column(columeName = "update_time")
    public long updateTime;
    @Column(ignore = true)
    public boolean isCheck;
    @Column(ignore = true)
    public boolean isAllCheck;
    @Column(ignore = true)
    public List<BookSimple> list;
}
