package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * 取、弃土场外业调查记录簿
 */
@Table("take_discard_soil_record_book")
public class TakeOrDiscardSoilRecordBook extends BookSimple {
    public String name;//名称
    public long code;//编号
    @Column(columeName = "road_stake")
    public String roadStake;//上路桩号
    public String position;
    @Column(columeName = "new_access_road")
    public String newAccessRoad;
    @Column(columeName = "demolition_building")
    public String demolitionBuilding;//拆除建筑物
    @Column(columeName = "demolition_power_conduit")
    public String demolitionPowerConduit;//拆除电力
    @Column(columeName = "cutting_down_trees")
    public String cuttingDownTrees;//砍伐树木
    @Column(columeName = "soil_type")
    public String soilType;//岩层类型
    @Column(columeName = "foundation_condition")
    public String foundationCondition;//地基条件
    @Column(columeName = "hydrological_situation")
    public String hydrologicalSituation;//水文情况
    @Column(columeName = "subordinate_relations")
    public long subordinateRelations;//录属关系
    @Column(columeName = "covers_area_type")
    public long coversAreaType;//占地类型
    @Column(columeName = "covers_area")
    public double coversArea;//占地
}
