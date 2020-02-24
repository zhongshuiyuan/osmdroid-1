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
import com.huejie.osmdroid.model.books.TakeOrDiscardSoilRecordBook;
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


public class TakeOrDiscardSoilRecordBookFragment extends BookBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BookSimple bookSimple;
    private String projectName;
    //挡墙类型
    @BindView(R.id.tv_lsgx)
    CheckedTextView tv_lsgx;
    //结构形式
    @BindView(R.id.tv_zdlx)
    CheckedTextView tv_zdlx;

    @OnClick(R.id.tv_lsgx)
    public void lsgx() {
        Util.showPopwindow(getActivity(), tv_lsgx, DictUtil.getDictLabels(getActivity(), DictUtil.SUBORDINATE_RELATIONS), "");
    }

    @OnClick(R.id.tv_zdlx)
    public void zdlx() {
        Util.showPopwindow(getActivity(), tv_zdlx, DictUtil.getDictLabels(getActivity(), DictUtil.COVERS_AREA_TYPE), "");
    }

    @BindView(R.id.et_tcmc)
    EditText et_tcmc;
    @BindView(R.id.et_tcbh)
    EditText et_tcbh;
    @BindView(R.id.et_slzh)
    EditText et_slzh;
    @BindView(R.id.et_zdqg)
    EditText et_zdqg;
    @BindView(R.id.et_tcwz)
    EditText et_tcwz;
    @BindView(R.id.et_bd)
    EditText et_bd;
    @BindView(R.id.et_cqjzw)
    EditText et_cqjzw;
    @BindView(R.id.et_cqdl)
    EditText et_cqdl;
    @BindView(R.id.et_kfsm)
    EditText et_kfsm;
    @BindView(R.id.et_tllx)
    EditText et_tllx;
    @BindView(R.id.et_djtj)
    EditText et_djtj;
    @BindView(R.id.et_swqk)
    EditText et_swqk;

    @BindView(R.id.tv_dmsyt)
    TextView tv_dmsyt;
    @BindView(R.id.tv_pmsyt)
    TextView tv_pmsyt;


    @BindView(R.id.iv_dmsyt)
    ImageView iv_dmsyt;
    @BindView(R.id.iv_pmsyt)
    ImageView iv_pmsyt;


    //选择示意图片
    @OnClick(R.id.iv_dt_load_file)
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
    @OnClick(R.id.iv_dt_add_shiyitu)
    public void addFile() {
        startActivityForResult(new Intent(getActivity(), PaintActivity.class).putExtra("projectId", bookSimple.projectId + ""), 100);
    }

    //选择示意图片
    @OnClick(R.id.iv_pm_load_file)
    public void pmLoadFile() {
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
    @OnClick(R.id.iv_pm_add_shiyitu)
    public void pmAddFile() {
        Intent intent_paint = new Intent(getActivity(), PaintActivity.class);
        intent_paint.putExtra("projectId", bookSimple.projectId + "");
        startActivityForResult(intent_paint, 101);
    }

    public TakeOrDiscardSoilRecordBookFragment() {
        // Required empty public constructor
    }

    public static TakeOrDiscardSoilRecordBookFragment newInstance(BookSimple param1, String param2) {
        TakeOrDiscardSoilRecordBookFragment fragment = new TakeOrDiscardSoilRecordBookFragment();
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
        return inflater.inflate(R.layout.fragment_take_or_discard_soil_recordbook, container, false);
    }

    private TakeOrDiscardSoilRecordBook book;
    private CommonRecordBookMedia mediaCrossSectionDiagram;
    private CommonRecordBookMedia mediaPlanePositionDiagram;
    private int cType;
    private int pType;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        book = LitePal.find(TakeOrDiscardSoilRecordBook.class, bookSimple.id);
        et_tcmc.setText(book.name);
        et_tcbh.setText(Util.valueString(book.code));
        et_slzh.setText(book.roadStake);
        tv_lsgx.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.SUBORDINATE_RELATIONS, book.subordinateRelations));
        et_zdqg.setText(Util.valueString(book.coversArea));
        tv_zdlx.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.COVERS_AREA_TYPE, book.coversAreaType));
        et_tcwz.setText(book.position);
        et_bd.setText(book.newAccessRoad);
        et_cqjzw.setText(book.demolitionBuilding);
        et_cqdl.setText(book.demolitionPowerConduit);
        et_kfsm.setText(book.cuttingDownTrees);
        et_tllx.setText(book.soilType);
        et_djtj.setText(book.foundationCondition);
        et_swqk.setText(book.hydrologicalSituation);
        cType = (int) DictUtil.getBookMediaCode(getActivity(), DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_CROSS_SECTION_DIAGRAM);
        pType = (int) DictUtil.getBookMediaCode(getActivity(), DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_PLANE_POSITION_DIAGRAM);
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
            iv_pmsyt.setVisibility(View.VISIBLE);
            tv_pmsyt.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage("file://" + Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaPlanePositionDiagram.mediaPath, iv_pmsyt);

        } else {
            iv_pmsyt.setVisibility(View.GONE);
            tv_pmsyt.setVisibility(View.VISIBLE);
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
        iv_pmsyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PaintActivity.class).putExtra("projectId", bookSimple.projectId + "").putExtra("mediaPath", Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaPlanePositionDiagram.mediaPath), 101);

            }
        });

        iv_pmsyt.setOnLongClickListener(new View.OnLongClickListener() {
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
                        tv_pmsyt.setVisibility(View.VISIBLE);
                        iv_pmsyt.setVisibility(View.GONE);
                    }
                }).create();
                dialog.show();
                return false;
            }
        });

    }


    @Override
    public BookSimple getBookContent() {
        book.name = et_tcmc.getText().toString().trim();
        book.code = Util.valueLong(et_tcbh.getText().toString().trim());
        book.roadStake = et_slzh.getText().toString().trim();
        book.coversArea = Util.valueDouble(et_zdqg.getText().toString().trim());
        book.position = et_tcwz.getText().toString().trim();
        book.newAccessRoad = et_bd.getText().toString().trim();
        book.demolitionBuilding = et_cqjzw.getText().toString().trim();
        book.demolitionPowerConduit = et_cqdl.getText().toString().trim();
        book.cuttingDownTrees = et_kfsm.getText().toString().trim();
        book.soilType = et_tllx.getText().toString().trim();
        book.foundationCondition = et_djtj.getText().toString().trim();
        book.hydrologicalSituation = et_swqk.getText().toString().trim();
        if (tv_lsgx.getTag() != null) {
            book.subordinateRelations = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.SUBORDINATE_RELATIONS, tv_lsgx.getTag().toString());
        }
        if (tv_zdlx.getTag() != null) {
            book.coversAreaType = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.COVERS_AREA_TYPE, tv_zdlx.getTag().toString());
        }
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
                iv_pmsyt.setVisibility(View.VISIBLE);
                tv_pmsyt.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage("file://" + picPath, iv_pmsyt);
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
