package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * 小桥涵外业调查记录簿
 */
@Table("small_bridge_culvert_record_book")
public class SmallBridgeAndCulvertRecordBook extends BookSimple {
    /**
     * 中心桩号
     */
    @Column(columeName = "center_stake")
    public String centerStake="";
    /**
     * 地名或河名
     */
    @Column(columeName = "place_or_river_name")
    public String placeOrRiverName="";
    /**
     * 河床土壤及植被
     */
    @Column(columeName = "bed_soil_vegetation")
    public String bedSoilVegetation="";
    /**
     * 河道（沟、渠）、被交路、交叉管线描述
     */
    @Column(columeName = "channels_intersected_pipelines_desc")
    public String channelsIntersectedPipelinesDesc="";
    /**
     * 汇水面积、地形、地貌、地质概况描述
     */
    @Column(columeName = "catchment_landform_geology_desc")
    public String catchmentLandformGeologyDesc="";
    /**
     * 改河（沟、渠）和改路方案描述
     */
    @Column(columeName = "river_road_modification_desc")
    public String riverRoadModificationDesc="";


    /**
     * 结构形式(枚举类型)
     */
    @Column(columeName = "structure")
    public long structure=-1;
    /**
     * 角度
     */
    @Column(columeName = "angle")
    public long angle=-1;
    /**
     * 结构物主要用途：枚举类型
     */
    @Column(columeName = "main_purpose")
    public long mainPurpose=-1;
    /**
     * 河床坡度
     */
    @Column(columeName = "river_bed_slope")
    public long riverBedSlope=-1;

    /**
     * 初拟孔径
     */
    @Column(columeName = "init_aperture")
    public double initAperture=-1;
    /**
     * 设计标高
     */
    @Column(columeName = "design_elevation")
    public double designElevation=-1;
    /**
     * 沟底标高
     */
    @Column(columeName = "bottom_elevation")
    public double bottomElevation=-1;
    /**
     * 涵长（或桥长)
     */
    @Column(columeName = "culvert_length")
    public double culvertLength=-1;
    /**
     * 涵顶填土高度
     */
    @Column(columeName = "culvert_roof_height")
    public double culvertRoofHeight=-1;
    /**
     * 测时水深
     */
    @Column(columeName = "measuring_depth")
    public double measuringDepth=-1;

}
