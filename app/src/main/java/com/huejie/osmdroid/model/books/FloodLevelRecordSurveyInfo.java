package com.huejie.osmdroid.model.books;

import com.huejie.osmdroid.project.model.RiverAndFloodItem;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

@Table("flood_level_record_survey_info")
public class FloodLevelRecordSurveyInfo extends RiverAndFloodItem {
    @Column(columeName = "person_name")
    public String personName;
    @Column(columeName = "person_address")
    public String personAddress;
    @Column(columeName = "detail_record")
    public String detailRecord;
    @Column(columeName = "person_age")
    public long personAge;
    @Column(columeName = "person_reliability")
    public long personReliability;
    @Column(columeName = "flood_year")
    public long floodYear;
    @Column(columeName = "flood_elevation")
    public double floodElevation;


}
