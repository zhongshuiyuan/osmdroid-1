package com.huejie.osmdroid.model.books;

import com.huejie.osmdroid.project.model.RiverAndFloodItem;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * 既有涉河工程调查
 */
@Table("existing_related_river_engineering_survey_info")
public class ExistingRelatedRiverEngineeringSurveyInfo extends RiverAndFloodItem {

    @Column(columeName = "build_year")
    public long buildYear;
    @Column(columeName = "relative_position")
    public long relativePosition;
    @Column(columeName = "used_condition")
    public long usedCondition;
    @Column(columeName = "sediment")
    public long sediment;
    @Column(columeName = "geology")
    public long geology;
    @Column(columeName = "cross_river_angle")
    public long crossRiverAngle;
    @Column(columeName = "flood_time")
    public long floodTime;
    @Column(columeName = "place_name")
    public String placeName;
    @Column(columeName = "level_width")
    public String levelWidth;
    @Column(columeName = "superstructure")
    public String superstructure;
    @Column(columeName = "substructure")
    public String substructure;
    @Column(columeName = "span")
    public double span;
    @Column(columeName = "aperture")
    public double aperture;
    @Column(columeName = "bridge_length")
    public double bridgeLength;
    @Column(columeName = "flood_elevation")
    public double floodElevation;

}
