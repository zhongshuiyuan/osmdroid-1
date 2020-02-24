package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * 挡土墙外业调查记录簿
 */
@Table("large_medium_bridge_record_book")
public class LargeMediumBridgeRecordBook extends BookSimple {
    public String name;
    @Column(columeName = "place_name")
    public String placeName;
    @Column(columeName = "river_center_stake")
    public String riverCenterStake;
    @Column(columeName = "bridge_center_stake")
    public String bridgeCenterStake;
    @Column(columeName = "intersect_center_stake")
    public String intersectCenterStake;
    @Column(columeName = "control_factors_desc")
    public String controlFactorsDesc;
    @Column(columeName = "cross_line_desc")
    public String crossLineDesc;
    @Column(columeName = "change_river_desc")
    public String changeRiverDesc;
    @Column(columeName = "intersect_name")
    public String intersectName;
    @Column(columeName = "flood_survey_desc")
    public String floodSurveyDesc;
    @Column(columeName = "metrical_data")
    public String metricalData;
    @Column(columeName = "river_cross_angle")
    public long riverCrossAngle;
    @Column(columeName = "flow_direction")
    public long flowDirection;
    @Column(columeName = "navigation_level")
    public long navigationLevel;
    @Column(columeName = "intersect_cross_angle")
    public long intersectCrossAngle;
    @Column(columeName = "holes_number")
    public long holesNumber;
    public long structure;
    @Column(columeName = "cross_angle")
    public long crossAngle;
    @Column(columeName = "catchment_area")
    public double catchmentArea;
    @Column(columeName = "river_depth")
    public double riverDepth;
    @Column(columeName = "desgin_flood_level")
    public double desginFloodLevel;
    @Column(columeName = "highest_navigation_level")
    public double highestNavigationLevel;
    @Column(columeName = "lowest_navigation_level")
    public double lowestNavigationLevel;
    @Column(columeName = "holes_aperture")
    public double holesAperture;

}
