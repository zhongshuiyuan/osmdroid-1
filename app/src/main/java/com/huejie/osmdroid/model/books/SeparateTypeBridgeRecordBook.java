package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * 分离式立交天桥外业调查记录簿
 */
@Table("separate_type_bridge_record_book")
public class SeparateTypeBridgeRecordBook extends BookSimple {
    @Column(columeName = "cross_stake")
    public String crossStake;
    @Column(columeName = "crossed_road_name")
    public String crossedRoadName;
    @Column(columeName = "upper_lower_structure_form")
    public String upperLowerStructureForm;
    @Column(columeName = "nearby_villages_distribution")
    public String nearbyVillagesDistribution;
    @Column(columeName = "pavement_structure")
    public String pavementStructure;
    @Column(columeName = "drainage_engineering")
    public String drainageEngineering;
    @Column(columeName = "protection_engineering")
    public String protectionEngineering;
    @Column(columeName = "lateral_pipeline_distribution")
    public String lateralPipelineDistribution;
    @Column(columeName = "vehicles_crossing_traffic_desc")
    public String vehiclesCrossingTrafficDesc;
    @Column(columeName = "bridge_topography")
    public String bridgeTopography;
    @Column(columeName = "bridge_landform")
    public String bridgeLandform;
    @Column(columeName = "bridge_factors")
    public String bridgeFactors;
    @Column(columeName = "pavement_form")
    public String pavementForm;


    @Column(columeName = "structure")
    public long structure;
    @Column(columeName = "crossed_angle")
    public long crossedAngle;
    @Column(columeName = "intersect_now_level")
    public long intersectNowLevel;
    @Column(columeName = "intersect_plan_level")
    public long intersectPlanLevel;
    @Column(columeName = "line_cross_form")
    public long lineCrossForm;
    @Column(columeName = "holes_number")
    public long holesNumber;
    @Column(columeName = "oblique_angle")
    public long obliqueAngle;

    @Column(columeName = "intersect_now_width")
    public double intersectNowWidth;
    @Column(columeName = "intersect_plan_width")
    public double intersectPlanWidth;
    @Column(columeName = "headroom")
    public double headroom;
    @Column(columeName = "design_intersection_high")
    public double designIntersectionHigh;
    @Column(columeName = "crossing_center_elevation")
    public double crossingCenterElevation;
    @Column(columeName = "holes_aperture")
    public double holesAperture;
    @Column(columeName = "modification_width")
    public double modificationWidth;
    @Column(columeName = "modification_length")
    public double modificationLength;
    @Column(columeName = "cross_stake_mileage")
    public double crossStakeMileage;


}
