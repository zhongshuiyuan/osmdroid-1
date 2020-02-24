package com.huejie.osmdroid.project.recordbook.bookfragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
import com.huejie.osmdroid.model.basic.SysDictData;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.model.books.SeparateTypeBridgeRecordBook;
import com.huejie.osmdroid.project.recordbook.activity.PaintActivity;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.DictUtil;
import com.huejie.osmdroid.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.litepal.LitePal;
import org.osmdroid.util.LocationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static android.app.Activity.RESULT_OK;


public class SeparateTypeBridgeRecordBookFragment extends BookBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BookSimple bookSimple;
    private String projectName;

    @BindView(R.id.tv_currentSituation)
    CheckedTextView tv_currentSituation;

    @BindView(R.id.tv_planning)
    CheckedTextView tv_planning;

    @BindView(R.id.tv_mainlineCrossover)
    CheckedTextView tv_mainlineCrossover;
    private long chooseIndex;

    @OnClick(R.id.tv_currentSituation)
    public void currentSituation() {
        Util.showPopwindow(getActivity(), tv_currentSituation, DictUtil.getDictLabels(getActivity(), DictUtil.CROSS_ROAD_LEVEL), "现状：");
    }

    @OnClick(R.id.tv_planning)
    public void planning() {
        Util.showPopwindow(getActivity(), tv_planning, DictUtil.getDictLabels(getActivity(), DictUtil.CROSS_ROAD_LEVEL), "规划：");
    }

    @OnClick(R.id.tv_mainlineCrossover)
    public void mainlineCrossover() {
        Util.showPopwindow(getActivity(), tv_mainlineCrossover, DictUtil.getDictLabels(getActivity(), DictUtil.MAIN_INTERSECT_WAY), "");
    }


    @BindView(R.id.layout_type)
    LinearLayout layout_type;

    @BindView(R.id.et_jczh)
    EditText et_jczh;
    @BindView(R.id.et_crossRoadType)
    EditText et_crossRoadType;
    @BindView(R.id.et_crossAngle)
    EditText et_crossAngle;
    @BindView(R.id.et_headroom)
    EditText et_headroom;
    @BindView(R.id.et_widthSituation)
    EditText et_widthSituation;
    @BindView(R.id.et_widthPlanning)
    EditText et_widthPlanning;
    @BindView(R.id.et_holeNumber)
    EditText et_holeNumber;
    @BindView(R.id.et_holeSize)
    EditText et_holeSize;
    @BindView(R.id.et_designHeight)
    EditText et_designHeight;
    @BindView(R.id.et_centralCoordinate)
    EditText et_centralCoordinate;
    @BindView(R.id.et_skewAngle)
    EditText et_skewAngle;
    @BindView(R.id.et_upperAndLowerStructure)
    EditText et_upperAndLowerStructure;
    @BindView(R.id.et_villageDistribution)
    EditText et_villageDistribution;
    @BindView(R.id.et_pavementStructure)
    EditText et_pavementStructure;
    @BindView(R.id.et_drainageWorks)
    EditText et_drainageWorks;
    @BindView(R.id.et_protectionWorks)
    EditText et_protectionWorks;
    @BindView(R.id.et_roadsidePipelineDistribution)
    EditText et_roadsidePipelineDistribution;
    @BindView(R.id.et_situationDescription)
    EditText et_situationDescription;
    @BindView(R.id.et_terrain)
    EditText et_terrain;
    @BindView(R.id.et_landforms)
    EditText et_landforms;
    @BindView(R.id.et_programmeControl)
    EditText et_programmeControl;
    @BindView(R.id.et_width)
    EditText et_width;
    @BindView(R.id.et_lengh)
    EditText et_lengh;
    @BindView(R.id.et_pavementForm)
    EditText et_pavementForm;

    @BindView(R.id.tv_syt)
    TextView tv_syt;


    @BindView(R.id.iv_dmsyt)
    ImageView iv_dmsyt;


    //选择示意图片
    @OnClick(R.id.iv_load_file)
    public void loadFile() {
        GalleryFinal.openGalleryMuti(100, 1, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                String photoPath = resultList.get(0).getPhotoPath();
                Intent intent_paint = new Intent(getActivity(), PaintActivity.class);
                intent_paint.putExtra("mediaPath", photoPath);
                intent_paint.putExtra("projectId", bookSimple.projectId + "");
                startActivityForResult(intent_paint, 100);
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    //创建示意图片
    @OnClick(R.id.iv_add_shiyitu)
    public void addFile() {
        startActivityForResult(new Intent(getActivity(), PaintActivity.class).putExtra("projectId", bookSimple.projectId + ""), 100);
    }


    public SeparateTypeBridgeRecordBookFragment() {
        // Required empty public constructor
    }

    public static SeparateTypeBridgeRecordBookFragment newInstance(BookSimple param1, String param2) {
        SeparateTypeBridgeRecordBookFragment fragment = new SeparateTypeBridgeRecordBookFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookSimple = (BookSimple) getArguments().getSerializable(ARG_PARAM1);
            projectName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_separate_type_bridge, container, false);
    }

    private SeparateTypeBridgeRecordBook book;
    private CommonRecordBookMedia mediaCrossSectionDiagram;
    private int cType;

    private List<SysDictData> dicList;
    private List<CheckedTextView> radioButtonList = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        dicList = DictUtil.getDictDatas(getActivity(), DictUtil.STRUCTURAL_TYPES);
        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
        book = LitePal.find(SeparateTypeBridgeRecordBook.class, bookSimple.id);
        chooseIndex = book.structure;
        for (int i = 0; i < dicList.size(); i++) {
            CheckedTextView radioButton = (CheckedTextView) getLayoutInflater().inflate(R.layout.radio_button, null);
            SysDictData dict = dicList.get(i);
            radioButton.setText(dict.dictLabel);
            radioButton.setTag(dict.id);
            radioButton.setChecked(dict.id == book.structure);
            radioButtonList.add(radioButton);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < radioButtonList.size(); j++) {
                        if (radioButtonList.get(j).getTag() == v.getTag()) {
                            radioButtonList.get(j).setChecked(true);
                            chooseIndex = (Long) v.getTag();
                        } else {
                            radioButtonList.get(j).setChecked(false);
                        }
                    }
                }
            });
            layout_type.addView(radioButton);
        }

        et_jczh.setText(Util.valueString(book.crossStake));
        et_crossRoadType.setText(book.crossedRoadName);
        et_crossAngle.setText(Util.valueString(book.crossedAngle));
        et_headroom.setText(Util.valueString(book.headroom));
        et_widthSituation.setText(Util.valueString(book.intersectNowWidth));
        et_widthPlanning.setText(Util.valueString(book.intersectPlanWidth));
        et_holeNumber.setText(Util.valueString(book.holesNumber));
        et_holeSize.setText(Util.valueString(book.holesAperture));
        et_designHeight.setText(Util.valueString(book.designIntersectionHigh));
        et_centralCoordinate.setText(Util.valueString(book.crossingCenterElevation));
        et_skewAngle.setText(Util.valueString(book.obliqueAngle));
        et_upperAndLowerStructure.setText(Util.valueString(book.upperLowerStructureForm));
        et_villageDistribution.setText(Util.valueString(book.nearbyVillagesDistribution));
        et_pavementStructure.setText(Util.valueString(book.pavementStructure));
        et_drainageWorks.setText(Util.valueString(book.drainageEngineering));
        et_protectionWorks.setText(Util.valueString(book.protectionEngineering));
        et_roadsidePipelineDistribution.setText(Util.valueString(book.lateralPipelineDistribution));
        et_situationDescription.setText(Util.valueString(book.vehiclesCrossingTrafficDesc));
        et_terrain.setText(Util.valueString(book.bridgeTopography));
        et_landforms.setText(Util.valueString(book.bridgeLandform));
        et_programmeControl.setText(Util.valueString(book.bridgeFactors));
        et_width.setText(Util.valueString(book.modificationWidth));
        et_lengh.setText(Util.valueString(book.modificationLength));
        et_pavementForm.setText(Util.valueString(book.pavementForm));
        tv_currentSituation.setText("现状："+DictUtil.getDictLabelByCode(getActivity(), DictUtil.CROSS_ROAD_LEVEL, book.intersectNowLevel));
        tv_planning.setText("规划："+DictUtil.getDictLabelByCode(getActivity(), DictUtil.CROSS_ROAD_LEVEL, book.intersectPlanLevel));
        tv_mainlineCrossover.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.MAIN_INTERSECT_WAY, book.lineCrossForm));

        cType = (int) DictUtil.getBookMediaCode(getActivity(), DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_CROSS_ROAD);
        projectName = DBUtil.getProjectNameById(getActivity(), book.projectId + "");
        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
        //获取横断面示意图
        mediaCrossSectionDiagram = LitePal.where("record_book_id = ? and record_book_type_id = ? and media_type = ?", book.id + "", book.recordBookTypeId + "", cType + "").findFirst(CommonRecordBookMedia.class);

        if (null != mediaCrossSectionDiagram && !TextUtils.isEmpty(mediaCrossSectionDiagram.mediaPath)) {
            iv_dmsyt.setVisibility(View.VISIBLE);
            tv_syt.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage("file://" + Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaCrossSectionDiagram.mediaPath, iv_dmsyt);

        } else {
            iv_dmsyt.setVisibility(View.GONE);
            tv_syt.setVisibility(View.VISIBLE);
        }

        iv_dmsyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PaintActivity.class).putExtra("projectId", bookSimple.projectId + "").putExtra("mediaPath", Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaCrossSectionDiagram.mediaPath), 100);
            }
        });

        iv_dmsyt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("确定删除该图片吗？").setTitle("警告").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FileUtils.deleteFile(Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaCrossSectionDiagram.mediaPath);
                        mediaCrossSectionDiagram.delete();
                        tv_syt.setVisibility(View.VISIBLE);
                        iv_dmsyt.setVisibility(View.INVISIBLE);
                    }
                }).create();
                dialog.show();
                return false;
            }
        });

    }


    @Override
    public BookSimple getBookContent() {
        book.crossStake = et_jczh.getText().toString().trim();
        book.crossedRoadName = et_crossRoadType.getText().toString().trim();
        book.crossedAngle = Util.valueLong(et_crossAngle.getText().toString().trim());
        book.headroom = Util.valueDouble(et_headroom.getText().toString().trim());
        book.intersectNowWidth = Util.valueDouble(et_widthSituation.getText().toString().trim());
        book.intersectPlanWidth = Util.valueDouble(et_widthPlanning.getText().toString().trim());
        book.holesNumber = Util.valueLong(et_holeNumber.getText().toString().trim());
        book.holesAperture = Util.valueDouble(et_holeSize.getText().toString().trim());
        book.designIntersectionHigh = Util.valueDouble(et_designHeight.getText().toString().trim());
        book.crossingCenterElevation = Util.valueDouble(et_centralCoordinate.getText().toString().trim());
        book.obliqueAngle = Util.valueLong(et_skewAngle.getText().toString().trim());
        book.upperLowerStructureForm = et_upperAndLowerStructure.getText().toString().trim();
        book.nearbyVillagesDistribution = et_villageDistribution.getText().toString().trim();
        book.pavementStructure = et_pavementStructure.getText().toString().trim();
        book.drainageEngineering = et_drainageWorks.getText().toString().trim();
        book.protectionEngineering = et_protectionWorks.getText().toString().trim();
        book.lateralPipelineDistribution = et_roadsidePipelineDistribution.getText().toString().trim();
        book.vehiclesCrossingTrafficDesc = et_situationDescription.getText().toString().trim();
        book.bridgeTopography = et_terrain.getText().toString().trim();
        book.bridgeLandform = et_landforms.getText().toString().trim();
        book.bridgeFactors = et_programmeControl.getText().toString().trim();
        book.modificationWidth = Util.valueDouble(et_width.getText().toString().trim());
        book.modificationLength = Util.valueDouble(et_lengh.getText().toString().trim());
        book.pavementForm = et_pavementForm.getText().toString().trim();
        if (tv_currentSituation.getTag() != null) {
            book.intersectNowLevel = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.CROSS_ROAD_LEVEL, tv_currentSituation.getTag().toString().trim());
        }
        if (tv_planning.getTag() != null) {
            book.intersectPlanLevel = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.CROSS_ROAD_LEVEL, tv_planning.getTag().toString().trim());
        }
        if (tv_mainlineCrossover.getTag() != null) {
            book.lineCrossForm = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.MAIN_INTERSECT_WAY, tv_mainlineCrossover.getTag().toString().trim());
        }
        book.structure = chooseIndex;

        return book;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            String picPath = data.getStringExtra("mediaPath");
            if (requestCode == 100) {
                //挡土墙
                iv_dmsyt.setVisibility(View.VISIBLE);
                tv_syt.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage("file://" + picPath, iv_dmsyt);
                //保存示意图
                if (null == mediaCrossSectionDiagram) {
                    mediaCrossSectionDiagram = new CommonRecordBookMedia();
                    mediaCrossSectionDiagram.recordBookId = book.id;
                    mediaCrossSectionDiagram.recordBookTypeId = book.recordBookTypeId;
                    mediaCrossSectionDiagram.mediaType = cType;
                }
                mediaCrossSectionDiagram.mediaPath = Util.getStoreUrl(getActivity(), picPath);
                mediaCrossSectionDiagram.mediaName = FileUtils.getFileName(mediaCrossSectionDiagram.mediaPath);
                mediaCrossSectionDiagram.extension = FileUtils.getFileExtension(mediaCrossSectionDiagram.mediaPath);
                mediaCrossSectionDiagram.dateTimeOriginal = Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S);
                Location location = LocationUtils.getLastKnownLocation((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE));
                if (null != location) {
                    mediaCrossSectionDiagram.gpsLatitude = location.getLatitude() + "";
                    mediaCrossSectionDiagram.gpsLongitude = location.getLongitude() + "";
                    mediaCrossSectionDiagram.gpsAltitude = location.getAltitude() + "";
                }
                mediaCrossSectionDiagram.size = FileUtils.getFileLength(picPath);
                DBUtil.useExtraDbByProjectName(getActivity(), projectName);
                mediaCrossSectionDiagram.saveOrUpdate("record_book_id = ? and record_book_type_id = ? and media_type = ?", mediaCrossSectionDiagram.recordBookId + "", mediaCrossSectionDiagram.recordBookTypeId + "", mediaCrossSectionDiagram.mediaType + "");

            }
        }
    }
}
