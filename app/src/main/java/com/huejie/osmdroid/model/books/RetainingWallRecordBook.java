package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * 挡土墙外业调查记录簿
 */
@Table("retaining_wall_record_book")
public class RetainingWallRecordBook extends BookSimple {
    /**
     * 起点桩号
     */
    @Column(columeName = "start_stake")
    public String startStake = "";
    /**
     * 终点桩号
     */
    @Column(columeName = "end_stake")
    public String endStake = "";
    /**
     * 编号
     */
    public long code = -1;
    /**
     * 路段长度
     */
    @Column(columeName = "road_length")
    public long roadLength = -1;
    /**
     * 最大墙高 小数点后一位
     */
    @Column(columeName = "highest_wall")
    public double highestWall = -1;
    /**
     * 档墙类型
     */
    @Column(columeName = "wall_type")
    public long wallType = -1;
    /**
     * 距中心线左侧偏距 小数点后3位
     */
    @Column(columeName = "center_offset_left")
    public double centerOffsetLeft = -1;
    /**
     * 距中心线右侧偏距 小数点后3位
     */
    @Column(columeName = "center_offset_right")
    public double centerOffsetRight = -1;
    /**
     * 结构形式
     */
    public long structure = -1;
    /**
     * 设置原因
     */
    @Column(columeName = "set_reason")
    public String setReason = "";
    /**
     * 基础条件、岩层产状
     */
    public String condition = "";
}
