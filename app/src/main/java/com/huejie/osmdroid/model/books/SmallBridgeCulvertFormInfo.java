package com.huejie.osmdroid.model.books;

import org.litepal.annotation.Column;
import org.litepal.annotation.Table;

/**
 * @author: TongWeiJie
 * @date: 2019/6/21 17:18
 * @description: 小桥涵--初拟
 */
@Table("small_bridge_culvert_form_info")
public class SmallBridgeCulvertFormInfo extends SmallBridgeAndCulvertSimple {

    @Column(columeName = "init_import_export_form")
    public long initImportExportForm;
    @Column(columeName = "init_substructure_infrastructure_form")
    public String initSubstructureInfrastructureForm;

    //初拟进出口对应的文字
    @Column(ignore = true)
    public String initImportExportFormStr;

}
