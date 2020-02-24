package com.huejie.osmdroid.map;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.AssociationWorkpointActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.dialog.AddInterchangeDialog;
import com.huejie.osmdroid.dialog.AddLargeMediumBridgeDialog;
import com.huejie.osmdroid.dialog.AddOtherEngineeringsDialog;
import com.huejie.osmdroid.dialog.AddRetainingWallDialog;
import com.huejie.osmdroid.dialog.AddSeparateTypeBridgeDialog;
import com.huejie.osmdroid.dialog.AddTakeOrDiscardSoilDialog;
import com.huejie.osmdroid.listener.SimpleStyler;
import com.huejie.osmdroid.manager.MapViewManager;
import com.huejie.osmdroid.model.CoreProject;
import com.huejie.osmdroid.model.SimpleProject;
import com.huejie.osmdroid.model.basic.RecordBookType;
import com.huejie.osmdroid.model.books.AlongTheLineConditionsOfMaterialRecordBook;
import com.huejie.osmdroid.model.books.AlongTheLineDrainageSystemRecordBook;
import com.huejie.osmdroid.model.books.AlongTheLineOverallSituationRecordBook;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.model.books.CuttingAndSubgradeRecordBook;
import com.huejie.osmdroid.model.books.ExistingBridgeCulvertRecordBook;
import com.huejie.osmdroid.model.books.InterchangeRecordBook;
import com.huejie.osmdroid.model.books.LargeMediumBridgeRecordBook;
import com.huejie.osmdroid.model.books.OtherEngineeringRecordBook;
import com.huejie.osmdroid.model.books.OtherEngineeringSubInfo;
import com.huejie.osmdroid.model.books.RetainingWallRecordBook;
import com.huejie.osmdroid.model.books.SeparateTypeBridgeRecordBook;
import com.huejie.osmdroid.model.books.SmallBridgeAndCulvertRecordBook;
import com.huejie.osmdroid.model.books.TakeOrDiscardSoilRecordBook;
import com.huejie.osmdroid.model.books.TunnelEngineeringRecordBook;
import com.huejie.osmdroid.more.activity.ChooseMapActivity;
import com.huejie.osmdroid.overlay.CustomIconOverlay;
import com.huejie.osmdroid.project.recordbook.activity.BookDetailActivity;
import com.huejie.osmdroid.project.recordbook.activity.ChooseWorkPointActivity;
import com.huejie.osmdroid.project.recordbook.activity.LocalProjectActivity;
import com.huejie.osmdroid.util.BookUtils;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.DictUtil;
import com.huejie.osmdroid.util.Util;

import org.bonuspack.kml.KmlDocument;
import org.litepal.LitePal;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static android.app.Activity.RESULT_OK;
import static com.huejie.osmdroid.project.recordbook.activity.BookDetailActivity.REQ_OPEN_CAMERA;

/**
 * 底图切换类
 *
 * @author guc
 */

public class MapFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_scale)
    TextView tv_scale;
    @BindView(R.id.rl_title)
    View rl_title;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.iv_overlay)
    ImageView iv_overlay;

    @BindView(R.id.iv_logo)
    ImageView iv_map_logo;

    //    private FolderOverlay kmlOverlay;
    private CustomIconOverlay iconOverlay;

    @OnClick(R.id.location)
    public void getLocation() {
        MapViewManager.getInstance().toLocationPosition();
    }

    @OnClick(R.id.iv_camera)
    public void camera() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        GalleryFinal.openCamera(REQ_OPEN_CAMERA, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }


    public MapFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.iv_clean)
    public void clean() {
        mapView.getOverlays().remove(iconOverlay);
        iconOverlay = null;
        mapView.invalidate();
    }

    private int editModel;

    @OnClick(R.id.tv_right)
    public void menu() {
        View contentView = getLayoutInflater().inflate(R.layout.pop_project_menu, null);
        //设置popwindow为布局大小
        final PopupWindow pop_grade = new PopupWindow(contentView, SizeUtils.dp2px(100), LinearLayout.LayoutParams.WRAP_CONTENT);
        // 点击PopupWindow以外的区域，PopupWindow是否会消失。
        pop_grade.setBackgroundDrawable(new BitmapDrawable());
        pop_grade.setOutsideTouchable(true);
        contentView.findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), LocalProjectActivity.class).putExtra("projectId", project.projectId), 106);
                pop_grade.dismiss();
            }
        });
        contentView.findViewById(R.id.tv_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editModel = 1;
                pop_grade.dismiss();
            }
        });
        contentView.findViewById(R.id.tv_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关联工点
                editModel = 2;
                pop_grade.dismiss();
            }
        });
        pop_grade.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        pop_grade.showAsDropDown(tv_right, 20, 10);
//        pop_grade.showAsDropDown(tv_right);    //popwindow显示在控件下方
    }

    @OnClick(R.id.iv_overlay)
    public void overlay() {
        startActivity(new Intent(getActivity(), ChooseMapActivity.class));

    }

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    private List<RecordBookType> records;

    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        MapViewManager.getInstance().init(getActivity(), mapView);
        MapViewManager.getInstance().addScaleBar().location().toLocationPosition();
        iv_map_logo.setImageResource(Util.getLogoByMapType(AppContext.sp.getString(Config.CURRENT_MAP)));
        initListener();
        tv_scale.setText(Util.m1(mapView.getZoomLevel()));
        mapView.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                tv_scale.setText(Util.m1(event.getZoomLevel()));
                return false;
            }
        });
        if (AppContext.sp.getBoolean(Config.SP.IS_LOGIN)) {
            DBUtil.useBaseDatabases(getActivity());
            records = LitePal.findAll(RecordBookType.class);
            String projectName = AppContext.sp.getString(Config.CURRENT_PROJECT);
            //加载最新的项目
            if (TextUtils.isEmpty(projectName)) {
                DBUtil.useProjectDatabases(getActivity());
                SimpleProject pro = LitePal.findLast(SimpleProject.class);
                if (null == pro) {
                    tv_title.setText("地图");
                    tv_right.setVisibility(View.GONE);
                    return;
                } else {
                    projectName = pro.projectName;
                    AppContext.sp.putString(Config.CURRENT_PROJECT, projectName);
                }
            }
            DBUtil.useExtraDbByProjectName(getActivity(), projectName);
            project = LitePal.where("project_name = ?", projectName).findFirst(CoreProject.class);
            tv_right.setVisibility(View.VISIBLE);
            tv_title.setText(project.projectName);
            loadOverlay();
        } else {
            tv_title.setText("地图");
            tv_right.setVisibility(View.GONE);
        }
    }

    private void loadOverlay() {
        //获取路线信息
        if (!TextUtils.isEmpty(project.projectLineInfo)) {
            loadKml(project.projectLineInfo);
        }
        addWorks();
    }

    private void addWorks() {
        //加载所有工点
        FolderOverlay folderOverlay = new FolderOverlay();
        DBUtil.useExtraDbByProjectName(getActivity(), project.projectName);
        for (int i = 0; i < records.size(); i++) {
            long type = records.get(i).id;
            List<BookSimple> works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(type));
            for (int j = 0; j < works.size(); j++) {
                final BookSimple book = works.get(j);
                if (!TextUtils.isEmpty(book.positionY) && !TextUtils.isEmpty(book.positionX)) {
                    CustomIconOverlay iconOverlay = new CustomIconOverlay(null);
                    int id = j;
                    iconOverlay.setText("工点" + (id++));
                    iconOverlay.set(new GeoPoint(Double.valueOf(book.positionY), Double.valueOf(book.positionX)), BookUtils.getDrawableByRecordBookTypeId(getActivity(), type));
                    iconOverlay.setOnMarkClickListener(new CustomIconOverlay.MarkClickListener() {
                        @Override
                        public boolean onMarkerClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
                            startActivity(new Intent(getActivity(), BookDetailActivity.class).putExtra("book", book));
                            return true;
                        }
                    });
                    folderOverlay.add(iconOverlay);

                }

            }

        }
        mapView.getOverlays().add(folderOverlay);
        mapView.invalidate();

    }


    private CoreProject project;


    private void initListener() {
        MapViewManager.getInstance().setMapEventListener(new MapViewManager.MapEventListener() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if (editModel > 0) {
                    if (iconOverlay == null) {
                        iconOverlay = new CustomIconOverlay(mapView);
                        iconOverlay.set(p, getResources().getDrawable(R.mipmap.marker_icon_1));
                        mapView.getOverlays().add(iconOverlay);
                    } else {
                        iconOverlay.moveTo(p, mapView);
                    }
                    iconOverlay.setMarkLongClickListener(new CustomIconOverlay.MarkLongClickListener() {
                        @Override
                        public boolean onMarkerLongClicked(final MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
                            final GeoPoint point = new GeoPoint(makerPosition.getLatitude(), makerPosition.getLongitude());
                            if (editModel == 1) {
                                startActivityForResult(new Intent(getActivity(), ChooseWorkPointActivity.class).putExtra("point", (Serializable) point), 105);
                            } else if (editModel == 2) {
                                startActivityForResult(new Intent(getActivity(), AssociationWorkpointActivity.class).putExtra("projectId", project.projectId + "").putExtra("point", (Serializable) point), 110);
                            }

                            return false;
                        }
                    });
                    mapView.invalidate();
                }
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });
    }

    private HashMap<Long, KmlDocument> LineOverlay = new HashMap<>();

    private void loadKml(String geoJson) {
        if (!LineOverlay.containsKey(project.projectId)) {
            KmlDocument kmlDocument = new KmlDocument();
            kmlDocument.parseGeoJSON(geoJson);
            LineOverlay.put(project.projectId, kmlDocument);
        }
        FolderOverlay kmlOverlay = (FolderOverlay) LineOverlay.get(project.projectId).mKmlRoot.buildOverlay(mapView, null, new SimpleStyler(mapView.getTileProvider().getTileSource()), LineOverlay.get(project.projectId));
        mapView.getOverlays().add(kmlOverlay);
        BoundingBox bb = LineOverlay.get(project.projectId).mKmlRoot.getBoundingBox();
        if (null != bb) {
            mapView.zoomToBoundingBox(bb, true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            if (requestCode == 105) {
                showAddPointDialog((RecordBookType) data.getSerializableExtra("result"), (GeoPoint) data.getSerializableExtra("point"));
            } else if (requestCode == 106) {
                //切换本地项目
                SimpleProject pro = (SimpleProject) data.getSerializableExtra("result");
                DBUtil.useExtraDbByProjectName(getActivity(), pro.projectName);
                project = LitePal.where("project_id = ?", pro.projectId + "").findFirst(CoreProject.class);
                tv_title.setText(pro.projectName);
                AppContext.sp.putString(Config.CURRENT_PROJECT, pro.projectName);
                MapViewManager.getInstance().clean();
                iconOverlay = null;
                initListener();
                editModel = 0;
                loadOverlay();
            } else if (requestCode == 110) {
                //关联工点
                DBUtil.useExtraDbByProjectName(getActivity(), project.projectName);
                BookSimple result = (BookSimple) data.getSerializableExtra("result");
                result.update(result.id);
                MapViewManager.getInstance().clean();
                iconOverlay = null;
                initListener();
                editModel = 0;
                loadOverlay();
            }
        }
    }

    private void showAddPointDialog(final RecordBookType recordBookType, final GeoPoint point) {
        String type = recordBookType.recordBookTypeCode;
        switch (type) {
            case "RetainingWallRecordBook":
                //新增工点
                final AddRetainingWallDialog retainingWalldialog = AddRetainingWallDialog.getInstance(getActivity());
                retainingWalldialog.setTitle("新建工点");
                retainingWalldialog.showChooseBookType(true);
                retainingWalldialog.setChoose(recordBookType.recordBookTypeName);
                retainingWalldialog.setOnclick(new AddRetainingWallDialog.OnClick() {
                    @Override
                    public void onCancel() {
                        retainingWalldialog.dismiss();
                    }

                    @Override
                    public void onConfirm(String startStake, String endStake, String wallType) {
                        //新建目录
                        RetainingWallRecordBook retainingWallRecordBook = new RetainingWallRecordBook();
                        retainingWallRecordBook.projectId = project.projectId;
                        retainingWallRecordBook.createTime = System.currentTimeMillis();
                        retainingWallRecordBook.updateTime = retainingWallRecordBook.createTime;
                        retainingWallRecordBook.startStake = startStake;
                        retainingWallRecordBook.endStake = endStake;
                        retainingWallRecordBook.wallType = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.WALL_TYPE, wallType);
                        retainingWallRecordBook.recordBookTypeId = recordBookType.id;
                        DBUtil.useExtraDbByProjectName(getActivity(), project.projectName);
                        retainingWallRecordBook.positionY = point.getLatitude() + "";
                        retainingWallRecordBook.positionX = point.getLongitude() + "";
                        retainingWallRecordBook.save();
                        MapViewManager.getInstance().clean();
                        iconOverlay = null;
                        initListener();
                        editModel = 0;
                        loadOverlay();
                        retainingWalldialog.dismiss();
                    }

                    @Override
                    public void onChoose() {

                    }
                });
                retainingWalldialog.show();
                break;
            case "InterchangeRecordBook":
                final AddInterchangeDialog interchangeDialog = AddInterchangeDialog.getInstance(getActivity());
                interchangeDialog.setTitle("新建工点");
                interchangeDialog.showChooseBookType(true);
                interchangeDialog.setChoose(recordBookType.recordBookTypeName);
                interchangeDialog.setOnclick(new AddInterchangeDialog.OnClick() {
                    @Override
                    public void onCancel() {
                        interchangeDialog.dismiss();
                    }

                    @Override
                    public void onConfirm(String crossStake, String name) {
                        //新建目录
                        InterchangeRecordBook interchangeRecordBook = new InterchangeRecordBook();
                        interchangeRecordBook.projectId = project.projectId;
                        interchangeRecordBook.createTime = System.currentTimeMillis();
                        interchangeRecordBook.updateTime = interchangeRecordBook.createTime;
                        interchangeRecordBook.name = name;
                        interchangeRecordBook.crossStake = crossStake;
                        interchangeRecordBook.recordBookTypeId = recordBookType.id;
                        DBUtil.useExtraDbByProjectName(getActivity(), project.projectName);
                        interchangeRecordBook.positionY = point.getLatitude() + "";
                        interchangeRecordBook.positionX = point.getLongitude() + "";
                        interchangeRecordBook.save();
                        MapViewManager.getInstance().clean();
                        iconOverlay = null;
                        initListener();
                        editModel = 0;
                        loadOverlay();
                        interchangeDialog.dismiss();
                    }

                    @Override
                    public void onChoose() {

                    }
                });
                interchangeDialog.show();
                break;
            case "CuttingAndSubgradeRecordBook":
                CuttingAndSubgradeRecordBook cuttingAndSubgradeRecordBook = new CuttingAndSubgradeRecordBook();
                cuttingAndSubgradeRecordBook.projectId = project.projectId;
                cuttingAndSubgradeRecordBook.createTime = System.currentTimeMillis();
                cuttingAndSubgradeRecordBook.updateTime = cuttingAndSubgradeRecordBook.createTime;
//                                    cuttingAndSubgradeRecordBook.uniqueTag = uniqueTag;
                cuttingAndSubgradeRecordBook.recordBookTypeId = recordBookType.id;
//                                    cuttingAndSubgradeRecordBook.projectName = project.projectName;
                cuttingAndSubgradeRecordBook.save();
                break;
            case "AlongTheLineDrainageSystemRecordBook":
                AlongTheLineDrainageSystemRecordBook along_The_Line_Drainage_System_Record_Book = new AlongTheLineDrainageSystemRecordBook();
                along_The_Line_Drainage_System_Record_Book.projectId = project.projectId;
                along_The_Line_Drainage_System_Record_Book.createTime = System.currentTimeMillis();
                along_The_Line_Drainage_System_Record_Book.updateTime = along_The_Line_Drainage_System_Record_Book.createTime;
//                                    along_The_Line_Drainage_System_Record_Book.uniqueTag = uniqueTag;
                along_The_Line_Drainage_System_Record_Book.recordBookTypeId = recordBookType.id;
//                                    along_The_Line_Drainage_System_Record_Book.projectName = project.projectName;
                along_The_Line_Drainage_System_Record_Book.save();
                break;
            case "AlongTheLineConditionsOfMaterialRecordBook":
                AlongTheLineConditionsOfMaterialRecordBook alongTheLineConditionsOfMaterialRecordBook = new AlongTheLineConditionsOfMaterialRecordBook();
                alongTheLineConditionsOfMaterialRecordBook.projectId = project.projectId;
                alongTheLineConditionsOfMaterialRecordBook.createTime = System.currentTimeMillis();
                alongTheLineConditionsOfMaterialRecordBook.updateTime = alongTheLineConditionsOfMaterialRecordBook.createTime;
//                                    alongTheLineConditionsOfMaterialRecordBook.uniqueTag = uniqueTag;
                alongTheLineConditionsOfMaterialRecordBook.recordBookTypeId = recordBookType.id;
//                                    alongTheLineConditionsOfMaterialRecordBook.projectName = project.projectName;
                alongTheLineConditionsOfMaterialRecordBook.save();
                break;
            case "AlongTheLineOverallSituationRecordBook":
                AlongTheLineOverallSituationRecordBook along_The_Line_Overall_Situation_Record_Book = new AlongTheLineOverallSituationRecordBook();
                along_The_Line_Overall_Situation_Record_Book.projectId = project.projectId;
                along_The_Line_Overall_Situation_Record_Book.createTime = System.currentTimeMillis();
                along_The_Line_Overall_Situation_Record_Book.updateTime = along_The_Line_Overall_Situation_Record_Book.createTime;
//                                    along_The_Line_Overall_Situation_Record_Book.uniqueTag = uniqueTag;
                along_The_Line_Overall_Situation_Record_Book.recordBookTypeId = recordBookType.id;
//                                    along_The_Line_Overall_Situation_Record_Book.projectName = project.projectName;
                along_The_Line_Overall_Situation_Record_Book.save();
                break;
            case "OtherEngineeringRecordBook":
                OtherEngineeringRecordBook other_Engineering_Record_Book = new OtherEngineeringRecordBook();
                other_Engineering_Record_Book.projectId = project.projectId;
                other_Engineering_Record_Book.createTime = System.currentTimeMillis();
                other_Engineering_Record_Book.updateTime = other_Engineering_Record_Book.createTime;
//                                    other_Engineering_Record_Book.uniqueTag = uniqueTag;
                other_Engineering_Record_Book.recordBookTypeId = recordBookType.id;
//                                    other_Engineering_Record_Book.projectName = project.projectName;
                other_Engineering_Record_Book.save();

                final AddOtherEngineeringsDialog otherEngineeringsDialog = AddOtherEngineeringsDialog.getInstance(getActivity());
                otherEngineeringsDialog.setTitle("新建工点");
                otherEngineeringsDialog.showChooseBookType(true);
                otherEngineeringsDialog.setChoose(recordBookType.recordBookTypeName);
                otherEngineeringsDialog.setOnclick(new AddOtherEngineeringsDialog.OnClick() {
                    @Override
                    public void onCancel() {
                        otherEngineeringsDialog.dismiss();
                    }

                    @Override
                    public void onConfirm(String startStake, long relocationType) {
                        //新建目录
                        OtherEngineeringRecordBook otherEngineeringRecordBook = new OtherEngineeringRecordBook();
                        otherEngineeringRecordBook.projectId = project.projectId;
                        otherEngineeringRecordBook.createTime = System.currentTimeMillis();
                        otherEngineeringRecordBook.updateTime = otherEngineeringRecordBook.createTime;
                        otherEngineeringRecordBook.relocationType = relocationType;
                        otherEngineeringRecordBook.recordBookTypeId = recordBookType.id;
                        DBUtil.useExtraDbByProjectName(getActivity(), project.projectName);
                        otherEngineeringRecordBook.positionY = point.getLatitude() + "";
                        otherEngineeringRecordBook.positionX = point.getLongitude() + "";
                        otherEngineeringRecordBook.save();
                        long id = LitePal.where("record_book_type_id = ? and create_time = ?", otherEngineeringRecordBook.recordBookTypeId + "", otherEngineeringRecordBook.createTime + "").findFirst(OtherEngineeringRecordBook.class).id;
                        OtherEngineeringSubInfo subInfo = new OtherEngineeringSubInfo();
                        subInfo.recordBookId = id;
                        subInfo.crossStake = startStake;
                        subInfo.updateTime = Util.getFromatDate(otherEngineeringRecordBook.createTime, Util.Y_M_D_H_M_S);
                        subInfo.save();
                        MapViewManager.getInstance().clean();
                        iconOverlay = null;
                        initListener();
                        editModel = 0;
                        loadOverlay();
                        otherEngineeringsDialog.dismiss();
                    }

                    @Override
                    public void onChoose() {

                    }
                });
                otherEngineeringsDialog.show();

                break;
            case "TakeOrDiscardSoilRecordBook":
                final AddTakeOrDiscardSoilDialog takeOrDiscardSoilDialog = AddTakeOrDiscardSoilDialog.getInstance(getActivity());
                takeOrDiscardSoilDialog.setTitle("新建工点");
                takeOrDiscardSoilDialog.showChooseBookType(true);
                takeOrDiscardSoilDialog.setChoose(recordBookType.recordBookTypeName);
                takeOrDiscardSoilDialog.setOnclick(new AddTakeOrDiscardSoilDialog.OnClick() {
                    @Override
                    public void onCancel() {
                        takeOrDiscardSoilDialog.dismiss();
                    }

                    @Override
                    public void onConfirm(String crossStake, String name) {
                        //新建目录
                        TakeOrDiscardSoilRecordBook takeOrDiscardSoilRecordBook = new TakeOrDiscardSoilRecordBook();
                        takeOrDiscardSoilRecordBook.projectId = project.projectId;
                        takeOrDiscardSoilRecordBook.createTime = System.currentTimeMillis();
                        takeOrDiscardSoilRecordBook.updateTime = takeOrDiscardSoilRecordBook.createTime;
                        takeOrDiscardSoilRecordBook.name = name;
                        takeOrDiscardSoilRecordBook.roadStake = crossStake;
                        takeOrDiscardSoilRecordBook.recordBookTypeId = recordBookType.id;
                        DBUtil.useExtraDbByProjectName(getActivity(), project.projectName);
                        takeOrDiscardSoilRecordBook.positionY = point.getLatitude() + "";
                        takeOrDiscardSoilRecordBook.positionX = point.getLongitude() + "";
                        takeOrDiscardSoilRecordBook.save();
                        MapViewManager.getInstance().clean();
                        iconOverlay = null;
                        initListener();
                        editModel = 0;
                        loadOverlay();
                        takeOrDiscardSoilDialog.dismiss();
                    }

                    @Override
                    public void onChoose() {

                    }
                });
                takeOrDiscardSoilDialog.show();
                break;
            case "SeparateTypeBridgeRecordBook":
                //新增工点
                final AddSeparateTypeBridgeDialog separateTypeBridgeDialog = AddSeparateTypeBridgeDialog.getInstance(getActivity());
                separateTypeBridgeDialog.setTitle("新建工点");
                separateTypeBridgeDialog.showChooseBookType(true);
                separateTypeBridgeDialog.setChoose(recordBookType.recordBookTypeName);
                separateTypeBridgeDialog.setOnclick(new AddSeparateTypeBridgeDialog.OnClick() {
                    @Override
                    public void onCancel() {
                        separateTypeBridgeDialog.dismiss();
                    }

                    @Override
                    public void onConfirm(String crossStake, String wallType) {
                        //新建目录
                        SeparateTypeBridgeRecordBook separateTypeBridgeRecordBook = new SeparateTypeBridgeRecordBook();
                        separateTypeBridgeRecordBook.projectId = project.projectId;
                        separateTypeBridgeRecordBook.createTime = System.currentTimeMillis();
                        separateTypeBridgeRecordBook.updateTime = separateTypeBridgeRecordBook.createTime;
                        separateTypeBridgeRecordBook.crossStake = crossStake;
                        separateTypeBridgeRecordBook.structure = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.STRUCTURAL_TYPES, wallType);
                        separateTypeBridgeRecordBook.recordBookTypeId = recordBookType.id;
                        DBUtil.useExtraDbByProjectName(getActivity(), project.projectName);
                        separateTypeBridgeRecordBook.positionY = point.getLatitude() + "";
                        separateTypeBridgeRecordBook.positionX = point.getLongitude() + "";
                        separateTypeBridgeRecordBook.save();
                        MapViewManager.getInstance().clean();
                        iconOverlay = null;
                        initListener();
                        editModel = 0;
                        loadOverlay();
                        separateTypeBridgeDialog.dismiss();
                    }

                    @Override
                    public void onChoose() {

                    }
                });
                separateTypeBridgeDialog.show();
                break;
            case "LargeMediumBridgeRecordBook":
                final AddLargeMediumBridgeDialog largeMediumBridgeDialog = AddLargeMediumBridgeDialog.getInstance(getActivity());
                largeMediumBridgeDialog.setTitle("新建工点");
                largeMediumBridgeDialog.showChooseBookType(true);
                largeMediumBridgeDialog.setChoose(recordBookType.recordBookTypeName);
                largeMediumBridgeDialog.setOnclick(new AddLargeMediumBridgeDialog.OnClick() {
                    @Override
                    public void onCancel() {
                        largeMediumBridgeDialog.dismiss();
                    }

                    @Override
                    public void onConfirm(String crossStake, String name) {
                        //新建目录
                        LargeMediumBridgeRecordBook largeMediumBridgeRecordBook = new LargeMediumBridgeRecordBook();
                        largeMediumBridgeRecordBook.projectId = project.projectId;
                        largeMediumBridgeRecordBook.createTime = System.currentTimeMillis();
                        largeMediumBridgeRecordBook.updateTime = largeMediumBridgeRecordBook.createTime;
                        largeMediumBridgeRecordBook.name = name;
                        largeMediumBridgeRecordBook.bridgeCenterStake = crossStake;
                        largeMediumBridgeRecordBook.recordBookTypeId = recordBookType.id;
                        DBUtil.useExtraDbByProjectName(getActivity(), project.projectName);
                        largeMediumBridgeRecordBook.positionY = point.getLatitude() + "";
                        largeMediumBridgeRecordBook.positionX = point.getLongitude() + "";
                        largeMediumBridgeRecordBook.save();
                        MapViewManager.getInstance().clean();
                        iconOverlay = null;
                        initListener();
                        editModel = 0;
                        loadOverlay();
                        largeMediumBridgeDialog.dismiss();
                    }

                    @Override
                    public void onChoose() {

                    }
                });
                largeMediumBridgeDialog.show();


                break;
            case "SmallBridgeAndCulvertRecordBook":
                SmallBridgeAndCulvertRecordBook smallBridgeAndCulvertRecordBook = new SmallBridgeAndCulvertRecordBook();
                smallBridgeAndCulvertRecordBook.projectId = project.projectId;
                smallBridgeAndCulvertRecordBook.createTime = System.currentTimeMillis();
                smallBridgeAndCulvertRecordBook.updateTime = smallBridgeAndCulvertRecordBook.createTime;
//                                    smallBridgeAndCulvertRecordBook.uniqueTag = uniqueTag;
                smallBridgeAndCulvertRecordBook.recordBookTypeId = recordBookType.id;
//                                    smallBridgeAndCulvertRecordBook.projectName = project.projectName;
                smallBridgeAndCulvertRecordBook.save();
                break;
            case "ExistingBridgeCulvertRecordBook":
                ExistingBridgeCulvertRecordBook existingBridgeCulvertRecordBook = new ExistingBridgeCulvertRecordBook();
                existingBridgeCulvertRecordBook.projectId = project.projectId;
                existingBridgeCulvertRecordBook.createTime = System.currentTimeMillis();
                existingBridgeCulvertRecordBook.updateTime = existingBridgeCulvertRecordBook.createTime;
//                                    existingBridgeCulvertRecordBook.uniqueTag = uniqueTag;
                existingBridgeCulvertRecordBook.recordBookTypeId = recordBookType.id;
//                                    existingBridgeCulvertRecordBook.projectName = project.projectName;
                existingBridgeCulvertRecordBook.save();
                break;
            case "TunnelEngineeringRecordBook":
                TunnelEngineeringRecordBook tunnelEngineeringRecordBook = new TunnelEngineeringRecordBook();
                tunnelEngineeringRecordBook.projectId = project.projectId;
                tunnelEngineeringRecordBook.createTime = System.currentTimeMillis();
                tunnelEngineeringRecordBook.updateTime = tunnelEngineeringRecordBook.createTime;
//                                    tunnelEngineeringRecordBook.uniqueTag = uniqueTag;
                tunnelEngineeringRecordBook.recordBookTypeId = recordBookType.id;
//                                    tunnelEngineeringRecordBook.projectName = project.projectName;
                tunnelEngineeringRecordBook.save();
                break;
            default:
                break;
        }
    }
}
