package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * @author: TongWeiJie
 * @date: 2019/6/24 17:26
 * @description:
 */
@Table("small_bridge_culvert_flood_survey")
public class SmallBridgeCulvertFloodSurvey extends SmallBridgeAndCulvertSimple {

    @Column(columeName = "historical_flood_year")
    public long historicalFloodYear;
    @Column(columeName = "historical_flood_elevation")
    public double historicalFloodElevation;
    @Column(columeName = "historical_flood_position")
    public double historicalFloodPosition;
    @Column(columeName = "historical_flood_period")
    public double historicalFloodPeriod;

}
