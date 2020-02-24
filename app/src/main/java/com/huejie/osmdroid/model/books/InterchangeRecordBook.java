package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * 互通式立交外业调查记录簿
 */
@Table("interchange_record_book")
public class InterchangeRecordBook extends BookSimple {
    @Column(columeName = "start_stake")
    public String startStake;
    @Column(columeName = "end_stake")
    public String endStake;
    @Column(columeName = "cross_stake")
    public String crossStake;
    @Column(columeName = "intersect_name")
    public String intersectName;
    @Column(columeName = "cross_angle")
    public int crossAngle;
    @Column(columeName = "intersect_now_level")
    public int intersectNowLevel;
    @Column(columeName = "intersect_plan_level")
    public int intersectPlanLevel;
    @Column(columeName = "intersect_now_width")
    public double intersectNowWidth;
    @Column(columeName = "intersect_plan_width")
    public double intersectPlanWidth;

    public double headroom;
    @Column(columeName = "design_high")
    public double designHigh;
    @Column(columeName = "center_elevation")
    public double centerElevation;
    @Column(columeName = "initial_form")
    public String initialForm;
    @Column(columeName = "now_describe")
    public String nowDescribe;
    @Column(columeName = "traffic_situation")
    public String trafficSituation;
    @Column(columeName = "topography_factor")
    public String topographyFactor;
    @Column(columeName = "project_description")
    public String projectDescription;

    public String name;//互通式立交名称

}
