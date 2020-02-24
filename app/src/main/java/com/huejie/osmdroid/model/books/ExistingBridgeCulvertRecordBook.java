package com.huejie.osmdroid.model.books;

/**
 * 挡土墙外业调查记录簿
 */

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

@Table("existing_bridge_culvert_record_book")
public class ExistingBridgeCulvertRecordBook extends BookSimple {

    @Column(columeName = "structure_name")
    public String structureName;
    @Column(columeName = "place_name")
    public String placeName;
    @Column(columeName = "metrical_data")
    public String metricalData;
    @Column(columeName = "original_center_stake")
    public String originalCenterStake;
    @Column(columeName = "now_center_stake")
    public String nowCenterStake;
    @Column(columeName = "river_center_stake")
    public String riverCenterStake;
    @Column(columeName = "intersect_name")
    public String intersectName;
    @Column(columeName = "construction_structural_form")
    public String constructionStructuralForm;
    @Column(columeName = "adopting_reasons")
    public String adoptingReasons;
    @Column(columeName = "control_factors_desc")
    public String controlFactorsDesc;
    @Column(columeName = "cross_line_desc")
    public String crossLineDesc;
    @Column(columeName = "change_river_desc")
    public String changeRiverDesc;
    @Column(columeName = "flood_survey_desc")
    public String floodSurveyDesc;

    @Column(columeName = "bridge_additional_desc")
    public String bridgeAdditionalDesc;


    @Column(columeName = "road_river_angle")
    public long roadRiverAngle;
    @Column(columeName = "flow_direction")
    public long flowDirection;
    @Column(columeName = "navigation_level_headroom")
    public long navigationLevelHeadroom;
    @Column(columeName = "meets_standards")
    public long meetsStandards;
    @Column(columeName = "intersect_cross_angle")
    public long intersectCrossAngle;
    @Column(columeName = "intersect_now_level")
    public long intersectNowLevel;
    @Column(columeName = "intersect_plan_level")
    public long intersectPlanLevel;

    @Column(columeName = "main_intersect_way")
    public long mainIntersectWay;
    @Column(columeName = "bridge_floor_disease")
    public long bridgeFloorDisease;
    @Column(columeName = "expansion_joints_disease")
    public long expansionJointsDisease;
    @Column(columeName = "drain_pipe_disease")
    public long drainPipeDisease;
    @Column(columeName = "bearing_disease")
    public long bearingDisease;
    @Column(columeName = "superstructure_disease")
    public long superstructureDisease;
    @Column(columeName = "substructure_disease")
    public long substructureDisease;

    @Column(columeName = "bridge_functional_depletion")
    public long bridgeFunctionalDepletion;
    @Column(columeName = "hole_disease")
    public long holeDisease;
    @Column(columeName = "culvert_body_disease")
    public long culvertBodyDisease;
    @Column(columeName = "settlement_joint_disease")
    public long settlementJointDisease;
    @Column(columeName = "culvert_functional_depletion")
    public long culvertFunctionalDepletion;
    @Column(columeName = "culvert_additional_desc")
    public String culvertAdditionalDesc;
    @Column(columeName = "preliminary_construction_plan")
    public long preliminaryConstructionPlan;
    @Column(columeName = "structure_hole_number")
    public long structureHoleNumber;

    @Column(columeName = "construction_cross_angle")
    public long constructionCrossAngle;

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
    @Column(columeName = "intersect_now_width")
    public double intersectNowWidth;
    @Column(columeName = "intersect_plan_width")
    public double intersectPlanWidth;
    @Column(columeName = "headroom_now")
    public double headroomNow;
    @Column(columeName = "headroom_plan")
    public double headroomPlan;
    @Column(columeName = "structure_hole_aperture")
    public double structureHoleAperture;


}
