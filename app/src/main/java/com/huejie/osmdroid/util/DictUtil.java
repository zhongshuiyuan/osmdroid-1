package com.huejie.osmdroid.util;

import android.content.Context;

import com.huejie.osmdroid.model.CommonProjectMedia;
import com.huejie.osmdroid.model.basic.RecordBookType;
import com.huejie.osmdroid.model.basic.SysDictData;

import org.litepal.LitePal;

import java.util.List;

public class DictUtil {

    public static final String WALL_TYPE = "wall_type";
    public static final String WEATHER = "weather_type";
    public static final String RETAIN_WALL_STRUCTURE = "retain_wall_structure";
    public static final String CROSS_ROAD_LEVEL = "cross_road_level";//道路等级
    public static final String SUBORDINATE_RELATIONS = "subordinate_relations";
    public static final String COVERS_AREA_TYPE = "covers_area_type";
    public static final String SMALL_BRIDGE_CULVERT_STRUCTURE="small_bridge_culvert_structure";//小桥-结构形式
    public static final String INIT_IMPORT_EXPORT_FORM="init_import_export_form";//初拟进出口形式-涵洞
    public static final String SYS_IS_SYSTEM="sys_is_system";

    //既有桥涵
    public static final String BRIDGE_FLOOR_DISEASE="bridge_floor_disease";//桥面病害
    public static final String EXPANSION_JOINTS_DISEASE="expansion_joints_disease";//伸缩缝病害
    public static final String DRAIN_PIPE_DISEASE="drain_pipe_disease";//泄水管病害
    public static final String BEARING_DISEASE="bearing_disease";//支座病害
    public static final String SUPERSTRUCTURE_DISEASE="superstructure_disease";//上部结构病害
    public static final String SUBSTRUCTURE_DISEASE="substructure_disease";//下部结构病害
    public static final String BRIDGE_FUNCTIONAL_DEPLETION="bridge_functional_depletion";//使用功能减损
    public static final String HOLE_DISEASE="hole_disease";//既有涵洞洞口病害
    public static final String CULVERT_BODY_DISEASE="culvert_body_disease";//既有涵洞涵身病害
    public static final String SETTLEMENT_JOINT_DISEASE="settlement_joint_disease";//既有涵洞沉降缝病害
    public static final String CULVERT_FUNCTIONAL_DEPLETION="culvert_functional_depletion";//既有涵洞功能使用减损
    public static final String PRELIMINARY_CONSTRUCTION_PLAN="preliminary_construction_plan";//初拟构造物方案


    //大、中桥外业调查记录薄
    public static final String FLOW_DIRECTION = "flow_direction";//水流方向
    public static final String NAVIGATION_LEVEL = "navigation_level";//通航等级及净空
    public static final String STRUCTURE = "structure";//结构形式
    public static final String REFER_RELATIVE_POSITION = "refer_relative_position";//与新桥相对位置
    public static final String REFER_CONDITION = "refer_condition";//使用情况
    public static final String REFER_GEOLOGY = "refer_geology";//地质情况
    public static final String REFER_SEDIMENT = "refer_sediment";//冲淤情况
    public static final String PERSON_RELIABILITY = "person_reliability";//可靠度

    //其他工程项目
    public static final String RELOCATION_TYPE_GROUP = "relocation-type-group";//改路、改沟（渠）、其他改移



    //分离式立交、通道、天桥外业调查记录簿
    public static final String STRUCTURAL_TYPES = "structural_types";//改路、改沟（渠）、其他改移
    public static final String  LABEL_RECORD_BOOK_MEDIA_CROSS_ROAD = "被交叉道路断面示意图";//改路、改沟（渠）、其他改移
    public static final String  MAIN_INTERSECT_WAY = "main_intersect_way";//主线交叉方式

    //记录簿相关多媒体文件类型
    public static final String MEDIA_TYPE = "media_type";
    public static final String LABEL_RECORD_BOOK_MEDIA_CROSS_SECTION_DIAGRAM = "横断面示意图";
    public static final String LABEL_RECORD_BOOK_MEDIA_PLANE_POSITION_DIAGRAM = "平面位置示意图";
    public static final String LABEL_RECORD_BOOK_MEDIA_DIAGRAM = "简图";
    public static final String LABEL_RECORD_BOOK_MEDIA_LIVE_PHOTOS = "现场照片";
    public static final String LABEL_RECORD_BOOK_MEDIA_RECORD = "录音";
    public static final String LABEL_RECORD_BOOK_MEDIA_VIDEO = "录像";
    public static final String LABEL_RECORD_BOOK_MEDIA_DMSYT = "被交叉道路断面示意图";
    public static final String LABEL_RECORD_BOOK_MEDIA_FAJT = "初拟互通式立交方案简图";


    //项目相关多媒体文件类型
    public static final String PROJECT_MEDIA_TYPE = "project_media_type";
    public static final String LABEL_PROJECT_MEDIA_TYPE_LINE_INFO = "项目线路";
    public static final String LABEL_PROJECT_MEDIA_TYPE_IMAGE_MAP = "影像地图";
    public static final String LABEL_PROJECT_MEDIA_TYPE_VECTOR_FILE = "矢量文件";
    public static final String LABEL_PROJECT_MEDIA_TYPE_OTHER = "其它";

    //挡墙类型
    public static String getDictLabelByCode(Context context, String dictType, long code) {
        DBUtil.useBaseDatabases(context);
        SysDictData data = LitePal.where("dict_type = ? and id = ?", dictType, code + "").findFirst(SysDictData.class);
        if (null == data) {
            return "";
        }
        return LitePal.where("dict_type = ? and id = ?", dictType, code + "").findFirst(SysDictData.class).dictLabel;
    }

    public static long getDictCodeByLabel(Context context, String dictType, String label) {
        DBUtil.useBaseDatabases(context);
        SysDictData data = LitePal.where("dict_type = ? and dict_label = ?", dictType, label).findFirst(SysDictData.class);
        if (null == data) {
            return -1;
        }
        return LitePal.where("dict_type = ? and dict_label = ?", dictType, label).findFirst(SysDictData.class).id;
    }

    public static String[] getDictLabels(Context context, String dictType) {
        DBUtil.useBaseDatabases(context);
        List<SysDictData> datas = LitePal.where("dict_type = ?", dictType).find(SysDictData.class);
        String[] array = new String[datas.size()];
        for (int i = 0; i < datas.size(); i++) {
            array[i] = datas.get(i).dictLabel;
        }
        return array;
    }

    public static List<SysDictData> getDictDatas(Context context, String dictType) {
        DBUtil.useBaseDatabases(context);
        return LitePal.where("dict_type = ?", dictType).find(SysDictData.class);
    }

    public static String getProjectMediaPath(Context context, String dictType, String dictLable, String projectId) {
        DBUtil.useBaseDatabases(context);
        long dictCode = LitePal.where("dict_type = ? and dict_label = ?", dictType, dictLable).findFirst(SysDictData.class).id;
        DBUtil.useExtraDbByProjectName(context, DBUtil.getProjectNameById(context, projectId));
        CommonProjectMedia media = LitePal.where("project_id = ? and media_type = ?", projectId, dictCode + "").findFirst(CommonProjectMedia.class);
        if (null == media) {
            return null;
        } else {
            return media.mediaPath;
        }

    }

    public static RecordBookType getRecordBookTypeById(Context context, long recordBookId) {
        DBUtil.useBaseDatabases(context);
        return LitePal.where("id = ?", recordBookId + "").findFirst(RecordBookType.class);


    }

    public static long getBookMediaCode(Context context, String dictType, String dictLable) {
        DBUtil.useBaseDatabases(context);
        return LitePal.where("dict_type = ? and dict_label = ?", dictType, dictLable).findFirst(SysDictData.class).id;
    }

}
