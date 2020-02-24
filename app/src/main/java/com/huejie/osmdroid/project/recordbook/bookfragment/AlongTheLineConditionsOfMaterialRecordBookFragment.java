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
import com.huejie.osmdroid.model.books.RetainingWallRecordBook;
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


public class AlongTheLineConditionsOfMaterialRecordBookFragment extends BookBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BookSimple bookSimple;
    private String mParam2;
    //挡墙类型
    @BindView(R.id.tv_dqlx)
    CheckedTextView tv_dqlx;
    //结构形式
    @BindView(R.id.tv_jgxs)
    CheckedTextView tv_jgxs;

    @OnClick(R.id.tv_dqlx)
    public void dqlx() {
        Util.showPopwindow(getActivity(), tv_dqlx, DictUtil.getDictLabels(getActivity(), DictUtil.WALL_TYPE), "");
    }

    @OnClick(R.id.tv_jgxs)
    public void jgxs() {
        Util.showPopwindow(getActivity(), tv_jgxs, DictUtil.getDictLabels(getActivity(), DictUtil.RETAIN_WALL_STRUCTURE), "");
    }

    @BindView(R.id.et_bianhao)
    EditText et_bianhao;
    @BindView(R.id.et_zcpj)
    EditText et_zcpj;
    @BindView(R.id.et_ycpj)
    EditText et_ycpj;
    @BindView(R.id.et_start_qz)
    EditText et_start_qz;
    @BindView(R.id.et_start_zh)
    EditText et_start_zh;
    @BindView(R.id.et_end_qz)
    EditText et_end_qz;
    @BindView(R.id.et_end_zh)
    EditText et_end_zh;
    @BindView(R.id.et_ldcd)
    EditText et_ldcd;
    @BindView(R.id.et_zdqg)
    EditText et_zdqg;
    @BindView(R.id.et_reason)
    EditText et_reason;
    @BindView(R.id.et_tjcz)
    EditText et_tjcz;

    @BindView(R.id.tv_dt_syt)
    TextView tv_dt_syt;
    @BindView(R.id.tv_pm_syt)
    TextView tv_pm_syt;

    @BindView(R.id.et_qdzh)
    EditText et_qdzh;
    @BindView(R.id.et_zdzh)
    EditText et_zdzh;
    @BindView(R.id.iv_hdm)
    ImageView iv_hdm;
    @BindView(R.id.iv_pm)
    ImageView iv_pm;


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

    public AlongTheLineConditionsOfMaterialRecordBookFragment() {
        // Required empty public constructor
    }

    public static AlongTheLineConditionsOfMaterialRecordBookFragment newInstance(BookSimple param1, String param2) {
        AlongTheLineConditionsOfMaterialRecordBookFragment fragment = new AlongTheLineConditionsOfMaterialRecordBookFragment();
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_retainingwall_recordbook, container, false);
    }

    private RetainingWallRecordBook book;
    private String projectName;
    private CommonRecordBookMedia mediaCrossSectionDiagram;
    private CommonRecordBookMedia mediaPlanePositionDiagram;
    private int cType;
    private int pType;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        book = LitePal.find(RetainingWallRecordBook.class, bookSimple.id);
        et_bianhao.setText(Util.valueString(book.code));
        et_zcpj.setText(Util.valueString(book.centerOffsetLeft));
        et_ycpj.setText(Util.valueString(book.centerOffsetRight));
        et_qdzh.setText(book.startStake);
        et_zdzh.setText(book.endStake);
        et_ldcd.setText(Util.valueString(book.roadLength));
        et_bianhao.setText(Util.valueString(book.code));
        et_zdqg.setText(Util.valueString(book.highestWall));
        tv_dqlx.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.WALL_TYPE, book.wallType));
        tv_jgxs.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.RETAIN_WALL_STRUCTURE, book.structure));
        et_reason.setText(book.setReason);
        et_tjcz.setText(book.condition);
        cType = (int) DictUtil.getBookMediaCode(getActivity(), DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_CROSS_SECTION_DIAGRAM);
        pType = (int) DictUtil.getBookMediaCode(getActivity(), DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_PLANE_POSITION_DIAGRAM);
        projectName = DBUtil.getProjectNameById(getActivity(), book.projectId + "");
        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
        //获取横断面示意图
        mediaCrossSectionDiagram = LitePal.where("record_book_id = ? and record_book_type_id = ? and media_type = ?", book.id + "", book.recordBookTypeId + "", cType + "").findFirst(CommonRecordBookMedia.class);

        if (null != mediaCrossSectionDiagram && !TextUtils.isEmpty(mediaCrossSectionDiagram.mediaPath)) {
            iv_hdm.setVisibility(View.VISIBLE);
            tv_dt_syt.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage("file://" + Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaCrossSectionDiagram.mediaPath, iv_hdm);

        } else {
            iv_hdm.setVisibility(View.GONE);
            tv_dt_syt.setVisibility(View.VISIBLE);
        }
        mediaPlanePositionDiagram = LitePal.where("record_book_id = ? and record_book_type_id = ? and media_type = ?", book.id + "", book.recordBookTypeId + "", pType + "").findFirst(CommonRecordBookMedia.class);

        if (null != mediaPlanePositionDiagram && !TextUtils.isEmpty(mediaPlanePositionDiagram.mediaPath)) {
            iv_pm.setVisibility(View.VISIBLE);
            tv_pm_syt.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage("file://" + Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaPlanePositionDiagram.mediaPath, iv_pm);

        } else {
            iv_pm.setVisibility(View.GONE);
            tv_pm_syt.setVisibility(View.VISIBLE);
        }
        iv_hdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PaintActivity.class).putExtra("projectId", bookSimple.projectId + "").putExtra("mediaPath", Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaCrossSectionDiagram.mediaPath), 100);
            }
        });

        iv_hdm.setOnLongClickListener(new View.OnLongClickListener() {
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
                        tv_dt_syt.setVisibility(View.VISIBLE);
                        iv_hdm.setVisibility(View.INVISIBLE);
                    }
                }).create();
                dialog.show();
                return false;
            }
        });
        iv_pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PaintActivity.class).putExtra("projectId", bookSimple.projectId + "").putExtra("mediaPath", Util.getProjectsPath(getActivity(), AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR)) + mediaPlanePositionDiagram.mediaPath), 101);

            }
        });

        iv_pm.setOnLongClickListener(new View.OnLongClickListener() {
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
                        tv_pm_syt.setVisibility(View.VISIBLE);
                        iv_pm.setVisibility(View.GONE);
                    }
                }).create();
                dialog.show();
                return false;
            }
        });

    }


    @Override
    public BookSimple getBookContent() {
        book.code = Util.valueLong(et_bianhao.getText().toString().trim());
        book.centerOffsetLeft = Util.valueDouble(et_zcpj.getText().toString().trim());
        book.centerOffsetRight = Util.valueDouble(et_ycpj.getText().toString().trim());
        book.startStake = et_qdzh.getText().toString().trim();
        book.endStake = et_zdzh.getText().toString().trim();
        book.roadLength = Util.valueLong(et_ldcd.getText().toString().trim());
        book.highestWall = Util.valueDouble(et_zdqg.getText().toString().trim());
        book.wallType = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.WALL_TYPE, tv_dqlx.getText().toString().trim());

        if (tv_jgxs.getTag() != null) {
            book.structure = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.RETAIN_WALL_STRUCTURE, tv_jgxs.getTag().toString());
        }
        book.setReason = et_reason.getText().toString().trim();
        book.condition = et_tjcz.getText().toString().trim();
        return book;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            String picPath = data.getStringExtra("mediaPath");
            if (requestCode == 100) {
                //挡土墙
                iv_hdm.setVisibility(View.VISIBLE);
                tv_dt_syt.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage("file://" + picPath, iv_hdm);
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
                iv_pm.setVisibility(View.VISIBLE);
                tv_pm_syt.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage("file://" + picPath, iv_pm);
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
