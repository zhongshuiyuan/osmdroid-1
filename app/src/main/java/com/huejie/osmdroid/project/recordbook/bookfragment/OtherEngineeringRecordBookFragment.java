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
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.dialog.AddOtherEngineeringsSubInfoDialog;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
import com.huejie.osmdroid.model.basic.SysDictData;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.model.books.OtherEngineeringRecordBook;
import com.huejie.osmdroid.model.books.OtherEngineeringSubInfo;
import com.huejie.osmdroid.project.recordbook.activity.PaintActivity;
import com.huejie.osmdroid.project.recordbook.adapter.OtherEngineeringSubInfoAdapter;
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

/**
 * 其他工程外业调查记录簿
 */
public class OtherEngineeringRecordBookFragment extends BookBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BookSimple bookSimple;
    private String projectName;

    @BindView(R.id.radioGroup)
    LinearLayout radioGroup;

    @BindView(R.id.listView)
    ListView listView;
    private List<SysDictData> dicList;
    private List<OtherEngineeringSubInfo> subInfos;
    private OtherEngineeringSubInfoAdapter adapter;

    @OnClick(R.id.btn_add)
    public void add() {
        final AddOtherEngineeringsSubInfoDialog dialog = AddOtherEngineeringsSubInfoDialog.getInstance(getActivity());
        dialog.setTitle("新建记录");
        dialog.setOnclick(new AddOtherEngineeringsSubInfoDialog.OnClick() {
            @Override
            public void onCancel() {
                dialog.dismiss();
            }

            @Override
            public void onConfirm(String startStake, String relocationName, long crossAngle) {
                DBUtil.useExtraDbByProjectName(getActivity(), projectName);
                OtherEngineeringSubInfo subInfo = new OtherEngineeringSubInfo();
                subInfo.crossStake = startStake;
                subInfo.relocationName = relocationName;
                subInfo.recordBookId = book.id;
                subInfo.crossAngle = crossAngle;
                subInfo.updateTime = Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S);
                subInfo.updateUser = AppContext.sp.getLong(Config.SP.CURRENT_USER_ID);
                subInfo.save();
                subInfos.add(subInfo);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @BindView(R.id.et_situationDescription)
    EditText et_situationDescription;


    @BindView(R.id.tv_dt_syt)
    TextView tv_dt_syt;
    @BindView(R.id.tv_pm_syt)
    TextView tv_pm_syt;

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

    public OtherEngineeringRecordBookFragment() {
        // Required empty public constructor
    }

    public static OtherEngineeringRecordBookFragment newInstance(BookSimple param1, String param2) {
        OtherEngineeringRecordBookFragment fragment = new OtherEngineeringRecordBookFragment();
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
        return inflater.inflate(R.layout.fragment_other_engineering, container, false);
    }

    private OtherEngineeringRecordBook book;

    private CommonRecordBookMedia mediaCrossSectionDiagram;
    private CommonRecordBookMedia mediaPlanePositionDiagram;
    private int cType;
    private int pType;

    private List<CheckedTextView> radioButtonList = new ArrayList<>();
    private long chooseIndex;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        dicList = DictUtil.getDictDatas(getActivity(), DictUtil.RELOCATION_TYPE_GROUP);
        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
        book = LitePal.find(OtherEngineeringRecordBook.class, bookSimple.id);
        chooseIndex = book.relocationType;
        for (int i = 0; i < dicList.size(); i++) {
            CheckedTextView radioButton = (CheckedTextView) getLayoutInflater().inflate(R.layout.radio_button, null);
            SysDictData dict = dicList.get(i);
            radioButton.setText(dict.dictLabel);
            radioButton.setTag(dict.id);
            radioButton.setChecked(dict.id == book.relocationType);
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
            radioGroup.addView(radioButton);
        }

        et_situationDescription.setText(book.presentation);
        subInfos = LitePal.where("record_book_id = ?", book.id + "").find(OtherEngineeringSubInfo.class);
        adapter = new OtherEngineeringSubInfoAdapter(getActivity(), subInfos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final OtherEngineeringSubInfo subInfo = subInfos.get(position);
                final AddOtherEngineeringsSubInfoDialog dialog = AddOtherEngineeringsSubInfoDialog.getInstance(getActivity());
                dialog.setTitle("修改记录");
                dialog.setData(subInfo);
                dialog.setOnclick(new AddOtherEngineeringsSubInfoDialog.OnClick() {
                    @Override
                    public void onCancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onConfirm(String startStake, String relocationName, long crossAngle) {
                        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
                        subInfo.crossStake = startStake;
                        subInfo.relocationName = relocationName;
                        subInfo.crossAngle = crossAngle;
                        subInfo.updateTime = Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S);
                        subInfo.updateUser = AppContext.sp.getLong(Config.SP.CURRENT_USER_ID);
                        subInfo.update(subInfo.id);
                        subInfos.clear();
                        subInfos = LitePal.where("record_book_id = ?", book.id + "").find(OtherEngineeringSubInfo.class);
                        adapter.setData(subInfos);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                //删除该工点
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("确定删除该记录吗？").setTitle("删除").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
                        final OtherEngineeringSubInfo subInfo = subInfos.get(position);
                        //先删除工点关联的附件，图片
                        subInfo.delete();
                        subInfos.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }).create();
                dialog.show();
                return true;
            }
        });

        cType = (int) DictUtil.getBookMediaCode(getActivity(), DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_CROSS_SECTION_DIAGRAM);
        pType = (int) DictUtil.getBookMediaCode(getActivity(), DictUtil.MEDIA_TYPE, DictUtil.LABEL_RECORD_BOOK_MEDIA_PLANE_POSITION_DIAGRAM);

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

        book.presentation = et_situationDescription.getText().toString().trim();
        book.relocationType = chooseIndex;

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
