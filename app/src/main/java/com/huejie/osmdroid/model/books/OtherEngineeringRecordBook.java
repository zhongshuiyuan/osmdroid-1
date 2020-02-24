package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * 挡土墙外业调查记录簿
 */
@Table("other_engineering_record_book")
public class OtherEngineeringRecordBook extends BookSimple {
    public String presentation;
    @Column(columeName = "relocation_type")
    public long relocationType;


}
