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
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.model.books.InterchangeRecordBook;
import com.huejie.osmdroid.project.recordbook.activity.PaintActivity;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.DictUtil;
import com.huejie.osmdroid.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.litepal.LitePal;
import org.osmdroid.util.LocationUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static android.app.Activity.RESULT_OK;

/**
 * 互通式立交外业调查记录簿
 */
public class InterchangeRecordBookFragment extends BookBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BookSimple bookSimple;
    private String projectName;


    @OnClick(R.id.tv_currentSituation)
    public void currentSituation() {

        Util.showPopwindow(getActivity(), tv_currentSituation, DictUtil.getDictLabels(getActivity(), DictUtil.CROSS_ROAD_LEVEL), "");
    }

    @OnClick(R.id.tv_planning)
    public void planning() {
        Util.showPopwindow(getActivity(), tv_planning, DictUtil.getDictLabels(getActivity(), DictUtil.CROSS_ROAD_LEVEL), "");
    }

    @BindView(R.id.et_ljmc)
    EditText et_ljmc;

    @BindView(R.id.et_jczh)
    EditText et_jczh;


    @BindView(R.id.et_qdzh)
    EditText et_qdzh;
    @BindView(R.id.et_zdzh)
    EditText et_zdzh;

    @BindView(R.id.et_jcmc)
    EditText et_jcmc;
    @BindView(R.id.et_crossAngle)
    EditText et_crossAngle;

    //被交叉道路等级  现状
    @BindView(R.id.tv_currentSituation)
    CheckedTextView tv_currentSituation;
    //被交叉道路等级  规划
    @BindView(R.id.tv_planning)
    CheckedTextView tv_planning;

    @BindView(R.id.et_widthSituation)
    EditText et_widthSituation;
    @BindView(R.id.et_widthPlanning)
    EditText et_widthPlanning;
    @BindView(R.id.et_headroom)
    EditText et_headroom;
    @BindView(R.id.et_designHeight)
    EditText et_designHeight;
    @BindView(R.id.et_centralCoordinate)
    EditText et_centralCoordinate;
    @BindView(R.id.et_clht)
    EditText et_clht;
    @BindView(R.id.et_now_describe)
    EditText et_now_describe;

    @BindView(R.id.iv_dmsyt)
    ImageView iv_dmsyt;
    @BindView(R.id.tv_dmsyt)
    TextView tv_dmsyt;

    @BindView(R.id.et_situationDescription)
    EditText et_situationDescription;
    @BindView(R.id.et_kzys)
    EditText et_kzys;
    @BindView(R.id.et_sggc)
    EditText et_sggc;

    @BindView(R.id.iv_fajt)
    ImageView iv_fajt;
    @BindView(R.id.tv_fajt)
    TextView tv_fajt;


    //断面示意图片选择
    @OnClick(R.id.iv_dmsyt_load)
    public void dmLoadFile() {
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

    //断面示意图片新建
    @OnClick(R.id.iv_dmsyt_add)
    public void dmAddFile() {
        startActivityForResult(new Intent(getActivity(), PaintActivity.class).putExtra("projectId", bookSimple.projectId + ""), 100);
    }

    //选择示意图片
    @OnClick(R.id.iv_fajt_load)
    public void faLoadFile() {
        GalleryFinal.openGalleryMuti(100, 1, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                String photoPath = resultList.get(0).getPhotoPath();
                Intent intent_paint = new Intent(getActivity(), PaintActivity.class);
                intent_paint.putExtra("projectId", bookSimple.projectId + "");
                intent_paint.putExtra("mediaPath", photoPath);
                startActivityForResult(intent_paint, 101);
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    //创建示意图片
    @OnClick(R.id.iv_fajt_add)
    public void faAddFile() {
        Intent intent_paint = new Intent(getActivity(), PaintActivity.class);
        intent_paint.putExtra("projectId", bookSimple.projectId + "");
        startActivityForResult(intent_paint, 101);
    }

    public InterchangeRecordBookFragment() {
        // Required empty public constructor
    }

    public static InterchangeRecordBookFragment newInstance(BookSimple param1, String param2) {
        InterchangeRecordBookFragment fragment = new InterchangeRecordBookFragment();
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
        return inflater.inflate(R.layout.fragment_interchange_recordbook, container, false);
    }

    private InterchangeRecordBook book;

    private CommonRecordBookMedia mediaCrossSectionDiagram;
    private CommonRecordBookMedia mediaPlanePositionDiagram;
    int cType;
    int pType;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        book = LitePal.find(InterchangeRecordBook.class, bookSimple.id);
        et_ljmc.setText(book.name);
        et_jczh.setText(book.crossStake);
        et_qdzh.setText(book.startStake);
        et_zdzh.setText(book.endStake);
        et_jcmc.setText(book.intersectName);
        et_crossAngle.setText(Util.valueString(book.crossAngle));
        tv_currentSituation.setText("现状：" + DictUtil.getDictLabelByCode(getActivity(), DictUtil.CROSS_ROAD_LEVEL, book.intersectNowLevel));
        tv_planning.setText("规划：" + DictUtil.getDictLabelByCode(getActivity(), DictUtil.CROSS_ROAD_LEVEL, book.intersectPlanLevel));
        et_widthSituation.setText(Util.valueString(book.intersectNowWidth) );
        et_widthPlanning.setText(Util.valueString(book.intersectPlanWidth ));
        et_headroom.setText(Util.valueString(book.headroom) );
        et_designHeight.setText(Util.valueString(book.designHigh) );
        et_centralCoordinate.setText(Util.valueString(book.centerElevation) );
        et_clht.setText(book.initialForm);
        et_now_describe.setText(book.nowDescribe);
        et_situationDescription.setText(book.trafficSituation);
        et_kzys.setText(book.topographyFactor);
        et_sggc.setText(book.projectDescription);
        cType = (int) DictUtil.getBookMediaCode(getActivity(), DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_DMSYT);
        pType = (int) DictUtil.getBookMediaCode(getActivity(), DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_FAJT);
        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
        //获取横断面示意图
        mediaCrossSectionDiagram = LitePal.where("record_book_id = ? and record_book_type_id = ? and media_type = ?", book.id + "", book.recordBookTypeId + "", cType + "").findFirst(CommonRecordBookMedia.class);

        if (null != mediaCrossSectionDiagram && !TextUtils.isEmpty(mediaCrossSectionDiagram.mediaPath)) {
            iv_dmsyt.setVisibility(View.VISIBLE);
            tv_dmsyt.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage("file://" + Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaCrossSectionDiagram.mediaPath, iv_dmsyt);

        } else {
            iv_dmsyt.setVisibility(View.GONE);
            tv_dmsyt.setVisibility(View.VISIBLE);
        }
        mediaPlanePositionDiagram = LitePal.where("record_book_id = ? and record_book_type_id = ? and media_type = ?", book.id + "", book.recordBookTypeId + "", pType + "").findFirst(CommonRecordBookMedia.class);

        if (null != mediaPlanePositionDiagram && !TextUtils.isEmpty(mediaPlanePositionDiagram.mediaPath)) {
            iv_fajt.setVisibility(View.VISIBLE);
            tv_fajt.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage("file://" + Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaPlanePositionDiagram.mediaPath, iv_fajt);

        } else {
            iv_fajt.setVisibility(View.GONE);
            tv_fajt.setVisibility(View.VISIBLE);
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
                        tv_dmsyt.setVisibility(View.VISIBLE);
                        iv_dmsyt.setVisibility(View.INVISIBLE);
                    }
                }).create();
                dialog.show();
                return false;
            }
        });
        iv_fajt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PaintActivity.class).putExtra("projectId", bookSimple.projectId + "").putExtra("mediaPath", Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaPlanePositionDiagram.mediaPath), 101);

            }
        });

        iv_fajt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("确定删除该图片吗？").setTitle("警告").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FileUtils.deleteFile(Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaPlanePositionDiagram.mediaPath);
                        mediaPlanePositionDiagram.delete();
                        tv_fajt.setVisibility(View.VISIBLE);
                        iv_fajt.setVisibility(View.GONE);
                    }
                }).create();
                dialog.show();
                return false;
            }
        });

    }


    @Override
    public BookSimple getBookContent() {
        book.name = et_ljmc.getText().toString().trim();
        book.crossStake = et_jczh.getText().toString().trim();
        book.startStake = et_qdzh.getText().toString().trim();
        book.endStake = et_zdzh.getText().toString().trim();
        book.intersectName = et_jcmc.getText().toString().trim();
        book.crossAngle = Util.valueInteger(et_crossAngle.getText().toString().trim());
        if (tv_currentSituation.getTag() != null) {
            book.intersectNowLevel = (int) DictUtil.getDictCodeByLabel(getActivity(), DictUtil.CROSS_ROAD_LEVEL, tv_currentSituation.getTag().toString());
        }
        if (tv_planning.getTag() != null) {
            book.intersectPlanLevel = (int) DictUtil.getDictCodeByLabel(getActivity(), DictUtil.CROSS_ROAD_LEVEL, tv_planning.getTag().toString());
        }
        book.intersectNowWidth = Util.valueDouble(et_widthSituation.getText().toString().trim());
        book.intersectPlanWidth = Util.valueDouble(et_widthPlanning.getText().toString().trim());
        book.headroom = Util.valueDouble(et_headroom.getText().toString().trim());
        book.designHigh = Util.valueDouble(et_designHeight.getText().toString().trim());
        book.centerElevation = Util.valueDouble(et_centralCoordinate.getText().toString().trim());
        book.initialForm = et_clht.getText().toString().trim();
        book.nowDescribe = et_now_describe.getText().toString().trim();
        book.trafficSituation = et_situationDescription.getText().toString().trim();
        book.topographyFactor = et_kzys.getText().toString().trim();
        book.projectDescription = et_sggc.getText().toString().trim();


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
                tv_dmsyt.setVisibility(View.GONE);
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

            } else if (requestCode == 101) {
                iv_fajt.setVisibility(View.VISIBLE);
                tv_fajt.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage("file://" + picPath, iv_fajt);
                if (null == mediaPlanePositionDiagram) {
                    mediaPlanePositionDiagram = new CommonRecordBookMedia();
                    mediaPlanePositionDiagram.recordBookId = book.id;
                    mediaPlanePositionDiagram.recordBookTypeId = book.recordBookTypeId;
                    mediaPlanePositionDiagram.mediaType = pType;
                }
                mediaPlanePositionDiagram.mediaPath = Util.getStoreUrl(getActivity(), picPath);
                mediaPlanePositionDiagram.mediaName = FileUtils.getFileName(mediaPlanePositionDiagram.mediaPath);
                mediaPlanePositionDiagram.extension = FileUtils.getFileExtension(mediaPlanePositionDiagram.mediaPath);
                mediaPlanePositionDiagram.dateTimeOriginal = Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S);
                Location location = LocationUtils.getLastKnownLocation((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE));
                if (null != location) {
                    mediaPlanePositionDiagram.gpsLatitude = location.getLatitude() + "";
                    mediaPlanePositionDiagram.gpsLongitude = location.getLongitude() + "";
                    mediaPlanePositionDiagram.gpsAltitude = location.getAltitude() + "";
                }
                mediaPlanePositionDiagram.size = FileUtils.getFileLength(picPath);
                DBUtil.useExtraDbByProjectName(getActivity(), projectName);
                mediaPlanePositionDiagram.saveOrUpdate("record_book_id = ? and record_book_type_id = ? and media_type = ?", mediaPlanePositionDiagram.recordBookId + "", mediaPlanePositionDiagram.recordBookTypeId + "", mediaPlanePositionDiagram.mediaType + "");

            }
        }
    }
}
