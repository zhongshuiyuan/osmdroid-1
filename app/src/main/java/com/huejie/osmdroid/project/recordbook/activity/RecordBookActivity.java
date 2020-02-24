package com.huejie.osmdroid.project.recordbook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.blankj.utilcode.utils.ZipUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.dialog.AddExistingBridgeAndCulvertDialog;
import com.huejie.osmdroid.dialog.AddInterchangeDialog;
import com.huejie.osmdroid.dialog.AddLargeMediumBridgeDialog;
import com.huejie.osmdroid.dialog.AddOtherEngineeringsDialog;
import com.huejie.osmdroid.dialog.AddRetainingWallDialog;
import com.huejie.osmdroid.dialog.AddSeparateTypeBridgeDialog;
import com.huejie.osmdroid.dialog.AddSmallBridgeAndCulvertDialog;
import com.huejie.osmdroid.dialog.AddTakeOrDiscardSoilDialog;
import com.huejie.osmdroid.dialog.LodingDialog;
import com.huejie.osmdroid.http.FileCallBack;
import com.huejie.osmdroid.http.HttpProvider;
import com.huejie.osmdroid.http.HttpUtil;
import com.huejie.osmdroid.http.JsonHelper;
import com.huejie.osmdroid.http.ParamsMap;
import com.huejie.osmdroid.http.ResponseCallBack;
import com.huejie.osmdroid.model.CommonRecordBookMedia;
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
import com.huejie.osmdroid.project.recordbook.adapter.BookAdapter;
import com.huejie.osmdroid.project.recordbook.adapter.BookNewAdapter;
import com.huejie.osmdroid.util.BookUtils;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.DictUtil;
import com.huejie.osmdroid.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class RecordBookActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.cb_sl)
    CheckBox cb_sl;
    @BindView(R.id.cb_dt)
    CheckBox cb_dt;
    @BindView(R.id.cb_qt)
    CheckBox cb_qt;

    @BindView(R.id.listViewDownload)
    ListView listViewDownload;
    @BindView(R.id.ll_download)
    View ll_download;
    private BookNewAdapter adapter;

    private List<RecordBookType> list = new ArrayList<>();
    private SimpleProject project;
    //项目类型，0是本地项目，1是在线项目
    private int projectType;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void download() {
        //下载所选的记录簿
        LodingDialog.getInstance().show();
        final String filePath = Util.getProjectsPath(this, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR));
        String fileName = project.projectName + ".zip";
        JSONObject params = new JSONObject();
        JSONArray recordBookDownLoadDtoList = new JSONArray();
        try {
            params.put("downLoadMaps", cb_dt.isChecked());
            params.put("downLoadOther", cb_qt.isChecked());
            params.put("downLoadVectors", cb_sl.isChecked());
            params.put("projectId", project.projectId);
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                RecordBookType books = list.get(i);
                jsonObject.put("recordBookTypeCode", books.recordBookTypeCode);
                JSONArray recordBookIds = new JSONArray();
                List<BookSimple> sList = books.list;
                for (int j = 0; j < sList.size(); j++) {
                    BookSimple bs = sList.get(j);
                    if (bs.isCheck) {
                        recordBookIds.put(bs.id);
                    }
                }
                jsonObject.put("recordBookIds", recordBookIds);
                recordBookDownLoadDtoList.put(jsonObject);

            }
            params.put("recordBookDownLoadDtoList", recordBookDownLoadDtoList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpProvider.postString(this, AppContext.sp.getString(Config.SP.CURRENT_URL) + HttpUtil.downloadProject, params.toString(), new FileCallBack(filePath, fileName) {
            @Override
            public void downLoadError(Exception e) {

            }

            @Override
            public void inProgress(float progress, long total, int id) {

            }

            @Override
            public void onSuccess(File file, int id) {
                try {
                    //开始解压文件
                    if (!ZipUtils.unzipFile(file.getPath(), filePath)) {
                        ToastUtils.showShortToast("解压文件失败，文件错误");
                    } else {
                        DBUtil.useProjectDatabases(RecordBookActivity.this);
                        project.saveOrUpdate("projectId = ?", project.projectId + "");
                    }
                    file.delete();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {
                LodingDialog.getInstance().dismiss();
                RecordBookActivity.this.finish();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShortToast("下载出错");
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_book);
        ButterKnife.bind(this);
        LodingDialog.init(this);
//        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        project = (SimpleProject) getIntent().getSerializableExtra("project");
        tv_title.setText(project.projectName);
        projectType = getIntent().getIntExtra("projectType", 0);
        if (projectType == 0) {
            //本地项目
            tv_right.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            ll_download.setVisibility(View.GONE);
            DBUtil.useBaseDatabases(this);
            list = LitePal.findAll(RecordBookType.class);
            for (int i = 0; i < list.size(); i++) {
                DBUtil.useExtraDbByProjectName(this, project.projectName);
                list.get(i).list = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(list.get(i).id));
                String recordBookTypeCode = list.get(i).recordBookTypeCode;
                for (int j = 0; j < list.get(i).list.size(); j++) {

                    switch (recordBookTypeCode) {
                        case "RetainingWallRecordBook":
                            RetainingWallRecordBook retainingWallRecordBook = (RetainingWallRecordBook) list.get(i).list.get(j);
                            retainingWallRecordBook.uniqueTag = retainingWallRecordBook.startStake + " - " + retainingWallRecordBook.endStake + " - " + DictUtil.getDictLabelByCode(RecordBookActivity.this, DictUtil.WALL_TYPE, retainingWallRecordBook.wallType);
                            break;
                        case "InterchangeRecordBook":
                            InterchangeRecordBook interchangeRecordBook = (InterchangeRecordBook) list.get(i).list.get(j);
                            interchangeRecordBook.uniqueTag = interchangeRecordBook.crossStake + " - " + interchangeRecordBook.name;
                            break;
                        case "TakeOrDiscardSoilRecordBook":
                            TakeOrDiscardSoilRecordBook takeOrDiscardSoilRecordBook = (TakeOrDiscardSoilRecordBook) list.get(i).list.get(j);
                            takeOrDiscardSoilRecordBook.uniqueTag = takeOrDiscardSoilRecordBook.roadStake + " - " + takeOrDiscardSoilRecordBook.name;
                            break;
                        case "OtherEngineeringRecordBook":
                            OtherEngineeringRecordBook otherEngineeringRecordBook = (OtherEngineeringRecordBook) list.get(i).list.get(j);
                            DBUtil.useExtraDbByProjectName(this, project.projectName);
                            OtherEngineeringSubInfo subInfo = LitePal.where("record_book_id = ?", otherEngineeringRecordBook.id + "").findFirst(OtherEngineeringSubInfo.class);
                            if (null != subInfo) {
                                String crossStack = LitePal.where("record_book_id = ?", otherEngineeringRecordBook.id + "").findFirst(OtherEngineeringSubInfo.class).crossStake;
                                otherEngineeringRecordBook.uniqueTag = crossStack + " - " + DictUtil.getDictLabelByCode(this, DictUtil.RELOCATION_TYPE_GROUP, otherEngineeringRecordBook.relocationType);
                            } else {
                                otherEngineeringRecordBook.uniqueTag = "工点" + j + " - " + DictUtil.getDictLabelByCode(this, DictUtil.RELOCATION_TYPE_GROUP, otherEngineeringRecordBook.relocationType);
                            }

                            break;
                        case "LargeMediumBridgeRecordBook":
                            LargeMediumBridgeRecordBook largeMediumBridgeRecordBook = (LargeMediumBridgeRecordBook) list.get(i).list.get(j);
                            largeMediumBridgeRecordBook.uniqueTag = largeMediumBridgeRecordBook.bridgeCenterStake + " - " + largeMediumBridgeRecordBook.name;
                            break;
                        case "SeparateTypeBridgeRecordBook":
                            SeparateTypeBridgeRecordBook separateTypeBridgeRecordBook = (SeparateTypeBridgeRecordBook) list.get(i).list.get(j);
                            separateTypeBridgeRecordBook.uniqueTag = separateTypeBridgeRecordBook.crossStake + " - " + DictUtil.getDictLabelByCode(this, DictUtil.STRUCTURAL_TYPES, separateTypeBridgeRecordBook.structure);
                            break;

                        case "SmallBridgeAndCulvertRecordBook":
                            SmallBridgeAndCulvertRecordBook smallBridgeAndCulvertRecordBook = (SmallBridgeAndCulvertRecordBook) list.get(i).list.get(j);
                            String structure = DictUtil.getDictLabelByCode(RecordBookActivity.this, DictUtil.SMALL_BRIDGE_CULVERT_STRUCTURE, smallBridgeAndCulvertRecordBook.structure);
                            smallBridgeAndCulvertRecordBook.uniqueTag = smallBridgeAndCulvertRecordBook.centerStake + " - " + structure;
                            break;
                        case "ExistingBridgeAndCulvertRecordBook":
                            ExistingBridgeCulvertRecordBook existingBridgeAndCulvertRecordBook = (ExistingBridgeCulvertRecordBook) list.get(i).list.get(j);
                            existingBridgeAndCulvertRecordBook.uniqueTag = existingBridgeAndCulvertRecordBook.nowCenterStake + " - " +existingBridgeAndCulvertRecordBook.structureName;
                            break;

                    }


                }

            }
            adapter = new BookNewAdapter(this, list, projectType, false);
            listView.setAdapter(adapter);
            adapter.setOnParentClickListener(new BookNewAdapter.OnParentClickListener() {
                @Override
                public void onParentClick(int position) {
                    list.get(position).isCheck = !list.get(position).isCheck;
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onAddClick(final int position) {
                    String type = list.get(position).recordBookTypeCode;
                    switch (type) {
                        case "RetainingWallRecordBook":
                            //新增工点
                            final AddRetainingWallDialog retainingWalldialog = AddRetainingWallDialog.getInstance(RecordBookActivity.this);
                            retainingWalldialog.setTitle("新建工点");
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
                                    retainingWallRecordBook.wallType = DictUtil.getDictCodeByLabel(RecordBookActivity.this, DictUtil.WALL_TYPE, wallType);
                                    retainingWallRecordBook.recordBookTypeId = list.get(position).id;
                                    DBUtil.useExtraDbByProjectName(RecordBookActivity.this, project.projectName);
                                    retainingWallRecordBook.save();
                                    list.get(position).list.clear();
                                    List works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(list.get(position).id));
                                    for (int i = 0; i < works.size(); i++) {
                                        RetainingWallRecordBook book = (RetainingWallRecordBook) works.get(i);
                                        book.uniqueTag = book.startStake + " - " + book.endStake + " - " + DictUtil.getDictLabelByCode(RecordBookActivity.this, DictUtil.WALL_TYPE, book.wallType);
                                    }
                                    list.get(position).list.addAll(works);
                                    adapter.notifyDataSetChanged();
                                    retainingWalldialog.dismiss();
                                }

                                @Override
                                public void onChoose() {

                                }
                            });
                            retainingWalldialog.show();
                            break;
                        case "InterchangeRecordBook":
                            //新增工点
                            final AddInterchangeDialog interchangeDialog = AddInterchangeDialog.getInstance(RecordBookActivity.this);
                            interchangeDialog.setTitle("新建工点");
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
                                    interchangeRecordBook.recordBookTypeId = list.get(position).id;
                                    DBUtil.useExtraDbByProjectName(RecordBookActivity.this, project.projectName);
                                    interchangeRecordBook.save();
                                    list.get(position).list.clear();
                                    List works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(list.get(position).id));
                                    for (int i = 0; i < works.size(); i++) {
                                        InterchangeRecordBook book = (InterchangeRecordBook) works.get(i);
                                        book.uniqueTag = book.crossStake + " - " + book.name;
                                    }
                                    list.get(position).list.addAll(works);
                                    adapter.notifyDataSetChanged();
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
                            cuttingAndSubgradeRecordBook.recordBookTypeId = list.get(position).id;
//                                    cuttingAndSubgradeRecordBook.projectName = project.projectName;
                            cuttingAndSubgradeRecordBook.save();
                            break;
                        case "AlongTheLineDrainageSystemRecordBook":
                            AlongTheLineDrainageSystemRecordBook along_The_Line_Drainage_System_Record_Book = new AlongTheLineDrainageSystemRecordBook();
                            along_The_Line_Drainage_System_Record_Book.projectId = project.projectId;
                            along_The_Line_Drainage_System_Record_Book.createTime = System.currentTimeMillis();
                            along_The_Line_Drainage_System_Record_Book.updateTime = along_The_Line_Drainage_System_Record_Book.createTime;
//                                    along_The_Line_Drainage_System_Record_Book.uniqueTag = uniqueTag;
                            along_The_Line_Drainage_System_Record_Book.recordBookTypeId = list.get(position).id;
//                                    along_The_Line_Drainage_System_Record_Book.projectName = project.projectName;
                            along_The_Line_Drainage_System_Record_Book.save();
                            break;
                        case "AlongTheLineConditionsOfMaterialRecordBook":
                            AlongTheLineConditionsOfMaterialRecordBook alongTheLineConditionsOfMaterialRecordBook = new AlongTheLineConditionsOfMaterialRecordBook();
                            alongTheLineConditionsOfMaterialRecordBook.projectId = project.projectId;
                            alongTheLineConditionsOfMaterialRecordBook.createTime = System.currentTimeMillis();
                            alongTheLineConditionsOfMaterialRecordBook.updateTime = alongTheLineConditionsOfMaterialRecordBook.createTime;
//                                    alongTheLineConditionsOfMaterialRecordBook.uniqueTag = uniqueTag;
                            alongTheLineConditionsOfMaterialRecordBook.recordBookTypeId = list.get(position).id;
//                                    alongTheLineConditionsOfMaterialRecordBook.projectName = project.projectName;
                            alongTheLineConditionsOfMaterialRecordBook.save();
                            break;
                        case "AlongTheLineOverallSituationRecordBook":
                            AlongTheLineOverallSituationRecordBook along_The_Line_Overall_Situation_Record_Book = new AlongTheLineOverallSituationRecordBook();
                            along_The_Line_Overall_Situation_Record_Book.projectId = project.projectId;
                            along_The_Line_Overall_Situation_Record_Book.createTime = System.currentTimeMillis();
                            along_The_Line_Overall_Situation_Record_Book.updateTime = along_The_Line_Overall_Situation_Record_Book.createTime;
//                                    along_The_Line_Overall_Situation_Record_Book.uniqueTag = uniqueTag;
                            along_The_Line_Overall_Situation_Record_Book.recordBookTypeId = list.get(position).id;
//                                    along_The_Line_Overall_Situation_Record_Book.projectName = project.projectName;
                            along_The_Line_Overall_Situation_Record_Book.save();
                            break;
                        case "OtherEngineeringRecordBook":
                            //新增工点
                            final AddOtherEngineeringsDialog addOtherEngineeringsDialog = AddOtherEngineeringsDialog.getInstance(RecordBookActivity.this);
                            addOtherEngineeringsDialog.setTitle("新建工点");
                            addOtherEngineeringsDialog.setOnclick(new AddOtherEngineeringsDialog.OnClick() {
                                @Override
                                public void onCancel() {
                                    addOtherEngineeringsDialog.dismiss();
                                }

                                @Override
                                public void onConfirm(String crossStake, long relocationType) {
                                    //新建目录
                                    OtherEngineeringRecordBook otherEngineeringRecordBook = new OtherEngineeringRecordBook();
                                    otherEngineeringRecordBook.projectId = project.projectId;
                                    otherEngineeringRecordBook.createTime = System.currentTimeMillis();
                                    otherEngineeringRecordBook.updateTime = otherEngineeringRecordBook.createTime;
                                    otherEngineeringRecordBook.relocationType = relocationType;
                                    otherEngineeringRecordBook.recordBookTypeId = list.get(position).id;
                                    DBUtil.useExtraDbByProjectName(RecordBookActivity.this, project.projectName);
                                    otherEngineeringRecordBook.save();
                                    long id = LitePal.where("record_book_type_id = ? and create_time = ?", otherEngineeringRecordBook.recordBookTypeId + "", otherEngineeringRecordBook.createTime + "").findFirst(OtherEngineeringRecordBook.class).id;
                                    OtherEngineeringSubInfo subInfo = new OtherEngineeringSubInfo();
                                    subInfo.recordBookId = id;
                                    subInfo.crossStake = crossStake;
                                    subInfo.updateTime = Util.getFromatDate(otherEngineeringRecordBook.createTime, Util.Y_M_D_H_M_S);
                                    subInfo.save();
                                    list.get(position).list.clear();
                                    List works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(list.get(position).id));
                                    for (int i = 0; i < works.size(); i++) {
                                        OtherEngineeringRecordBook book = (OtherEngineeringRecordBook) works.get(i);
                                        DBUtil.useExtraDbByProjectName(RecordBookActivity.this, project.projectName);
                                        OtherEngineeringSubInfo info = LitePal.where("record_book_id = ?", book.id + "").findFirst(OtherEngineeringSubInfo.class);
                                        if (null == info) {
                                            book.uniqueTag = "工点" + i;
                                        } else {
                                            String stake = LitePal.where("record_book_id = ?", book.id + "").findFirst(OtherEngineeringSubInfo.class).crossStake;
                                            book.uniqueTag = stake + " - " + DictUtil.getDictLabelByCode(RecordBookActivity.this, DictUtil.RELOCATION_TYPE_GROUP, book.relocationType);
                                        }
                                    }
                                    list.get(position).list.addAll(works);
                                    adapter.notifyDataSetChanged();
                                    addOtherEngineeringsDialog.dismiss();
                                }

                                @Override
                                public void onChoose() {

                                }
                            });
                            addOtherEngineeringsDialog.show();
                            break;
                        case "TakeOrDiscardSoilRecordBook":
                            //新增工点
                            final AddTakeOrDiscardSoilDialog addTakeOrDiscardSoilDialog = AddTakeOrDiscardSoilDialog.getInstance(RecordBookActivity.this);
                            addTakeOrDiscardSoilDialog.setTitle("新建工点");
                            addTakeOrDiscardSoilDialog.setOnclick(new AddTakeOrDiscardSoilDialog.OnClick() {
                                @Override
                                public void onCancel() {
                                    addTakeOrDiscardSoilDialog.dismiss();
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
                                    takeOrDiscardSoilRecordBook.recordBookTypeId = list.get(position).id;
                                    DBUtil.useExtraDbByProjectName(RecordBookActivity.this, project.projectName);
                                    takeOrDiscardSoilRecordBook.save();
                                    list.get(position).list.clear();
                                    List works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(list.get(position).id));
                                    for (int i = 0; i < works.size(); i++) {
                                        TakeOrDiscardSoilRecordBook book = (TakeOrDiscardSoilRecordBook) works.get(i);
                                        book.uniqueTag = book.roadStake + " - " + book.name;
                                    }
                                    list.get(position).list.addAll(works);
                                    adapter.notifyDataSetChanged();
                                    addTakeOrDiscardSoilDialog.dismiss();
                                }

                                @Override
                                public void onChoose() {

                                }
                            });
                            addTakeOrDiscardSoilDialog.show();

                            break;
                        case "SeparateTypeBridgeRecordBook":
                            //新增工点
                            final AddSeparateTypeBridgeDialog separateTypeBridgeDialog = AddSeparateTypeBridgeDialog.getInstance(RecordBookActivity.this);
                            separateTypeBridgeDialog.setTitle("新建工点");
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
                                    separateTypeBridgeRecordBook.structure = DictUtil.getDictCodeByLabel(RecordBookActivity.this, DictUtil.STRUCTURAL_TYPES, wallType);
                                    separateTypeBridgeRecordBook.recordBookTypeId = list.get(position).id;
                                    DBUtil.useExtraDbByProjectName(RecordBookActivity.this, project.projectName);
                                    separateTypeBridgeRecordBook.save();
                                    list.get(position).list.clear();
                                    List works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(list.get(position).id));
                                    for (int i = 0; i < works.size(); i++) {
                                        SeparateTypeBridgeRecordBook book = (SeparateTypeBridgeRecordBook) works.get(i);
                                        book.uniqueTag = book.crossStake + " - " + DictUtil.getDictLabelByCode(RecordBookActivity.this, DictUtil.STRUCTURAL_TYPES, book.structure);
                                    }
                                    list.get(position).list.addAll(works);
                                    adapter.notifyDataSetChanged();
                                    separateTypeBridgeDialog.dismiss();
                                }

                                @Override
                                public void onChoose() {

                                }
                            });
                            separateTypeBridgeDialog.show();
                            break;
                        case "LargeMediumBridgeRecordBook":
                            final AddLargeMediumBridgeDialog largeMediumBridgeDialog = AddLargeMediumBridgeDialog.getInstance(RecordBookActivity.this);
                            largeMediumBridgeDialog.setTitle("新建工点");
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
                                    largeMediumBridgeRecordBook.recordBookTypeId = list.get(position).id;
                                    DBUtil.useExtraDbByProjectName(RecordBookActivity.this, project.projectName);
                                    largeMediumBridgeRecordBook.save();
                                    list.get(position).list.clear();
                                    List works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(list.get(position).id));
                                    for (int i = 0; i < works.size(); i++) {
                                        LargeMediumBridgeRecordBook book = (LargeMediumBridgeRecordBook) works.get(i);
                                        book.uniqueTag = book.bridgeCenterStake + " - " + book.name;
                                    }
                                    list.get(position).list.addAll(works);
                                    adapter.notifyDataSetChanged();
                                    largeMediumBridgeDialog.dismiss();
                                }

                                @Override
                                public void onChoose() {

                                }
                            });
                            largeMediumBridgeDialog.show();

                            break;
                        case "SmallBridgeAndCulvertRecordBook":
                            final AddSmallBridgeAndCulvertDialog smallBridgeAndCulvertDialog = AddSmallBridgeAndCulvertDialog.getInstance(RecordBookActivity.this);
                            smallBridgeAndCulvertDialog.setTitle("新建工点");
                            smallBridgeAndCulvertDialog.setOnclick(new AddSmallBridgeAndCulvertDialog.OnClick() {
                                @Override
                                public void onCancel() {
                                    smallBridgeAndCulvertDialog.dismiss();
                                }

                                @Override
                                public void onConfirm(String crossStake, String structure) {
                                    //新建目录
                                    SmallBridgeAndCulvertRecordBook smallBridgeAndCulvertRecordBook = new SmallBridgeAndCulvertRecordBook();
                                    smallBridgeAndCulvertRecordBook.projectId = project.projectId;
                                    smallBridgeAndCulvertRecordBook.createTime = System.currentTimeMillis();
                                    smallBridgeAndCulvertRecordBook.updateTime = smallBridgeAndCulvertRecordBook.createTime;
                                    smallBridgeAndCulvertRecordBook.structure = DictUtil.getDictCodeByLabel(RecordBookActivity.this, DictUtil.SMALL_BRIDGE_CULVERT_STRUCTURE, structure);
                                    smallBridgeAndCulvertRecordBook.centerStake = crossStake;
                                    smallBridgeAndCulvertRecordBook.recordBookTypeId = list.get(position).id;
                                    DBUtil.useExtraDbByProjectName(RecordBookActivity.this, project.projectName);
                                    smallBridgeAndCulvertRecordBook.save();

                                    list.get(position).list.clear();
                                    List works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(list.get(position).id));
                                    for (int i = 0; i < works.size(); i++) {
                                        SmallBridgeAndCulvertRecordBook book = (SmallBridgeAndCulvertRecordBook) works.get(i);
                                        book.uniqueTag = book.centerStake + " - " + DictUtil.getDictLabelByCode(RecordBookActivity.this, DictUtil.SMALL_BRIDGE_CULVERT_STRUCTURE, book.structure);
                                    }
                                    list.get(position).list.addAll(works);
                                    adapter.notifyDataSetChanged();
                                    smallBridgeAndCulvertDialog.dismiss();
                                }

                                @Override
                                public void onChoose() {

                                }
                            });
                            smallBridgeAndCulvertDialog.show();
                            break;
                        case "ExistingBridgeAndCulvertRecordBook":
                            final AddExistingBridgeAndCulvertDialog existingBridgeAndCulvertDialog = AddExistingBridgeAndCulvertDialog.getInstance(RecordBookActivity.this);
                            existingBridgeAndCulvertDialog.setTitle("新建工点");
                            existingBridgeAndCulvertDialog.setOnclick(new AddExistingBridgeAndCulvertDialog.OnClick() {
                                @Override
                                public void onCancel() {
                                    existingBridgeAndCulvertDialog.dismiss();
                                }

                                @Override
                                public void onConfirm(String nowCenterStake, String structureName) {
                                    //新建目录
                                    ExistingBridgeCulvertRecordBook existingBridgeAndCulvertRecordBook = new ExistingBridgeCulvertRecordBook();
                                    existingBridgeAndCulvertRecordBook.projectId = project.projectId;
                                    existingBridgeAndCulvertRecordBook.createTime = System.currentTimeMillis();
                                    existingBridgeAndCulvertRecordBook.updateTime = existingBridgeAndCulvertRecordBook.createTime;
                                    existingBridgeAndCulvertRecordBook.recordBookTypeId = list.get(position).id;
                                    existingBridgeAndCulvertRecordBook.nowCenterStake=nowCenterStake;
                                    existingBridgeAndCulvertRecordBook.structureName=structureName;
                                    existingBridgeAndCulvertRecordBook.save();

                                    list.get(position).list.clear();
                                    List works = LitePal.where("project_id = ?", project.projectId + "").find(BookUtils.getClassByType(list.get(position).id));
                                    for (int i = 0; i < works.size(); i++) {
                                        ExistingBridgeCulvertRecordBook book = (ExistingBridgeCulvertRecordBook) works.get(i);
                                        book.uniqueTag = book.nowCenterStake + " - " +book.structureName ;
                                    }
                                    list.get(position).list.addAll(works);
                                    adapter.notifyDataSetChanged();
                                    existingBridgeAndCulvertDialog.dismiss();
                                }

                                @Override
                                public void onChoose() {

                                }
                            });
                            existingBridgeAndCulvertDialog.show();
                            break;
                        case "TunnelEngineeringRecordBook":
                            TunnelEngineeringRecordBook tunnelEngineeringRecordBook = new TunnelEngineeringRecordBook();
                            tunnelEngineeringRecordBook.projectId = project.projectId;
                            tunnelEngineeringRecordBook.createTime = System.currentTimeMillis();
                            tunnelEngineeringRecordBook.updateTime = tunnelEngineeringRecordBook.createTime;
//                                    tunnelEngineeringRecordBook.uniqueTag = uniqueTag;
                            tunnelEngineeringRecordBook.recordBookTypeId = list.get(position).id;
//                                    tunnelEngineeringRecordBook.projectName = project.projectName;
                            tunnelEngineeringRecordBook.save();
                            break;
                        default:
                            break;
                    }
                }
            });
            adapter.setOnClickListener(new BookNewAdapter.OnClickListener() {
                @Override
                public void onClick(final int parent, final int position, BookAdapter adapter) {
                    RecordBookType recordBook = list.get(parent);
                    if (recordBook.recordBookTypeCode.equals("RetainingWallRecordBook")
                            || recordBook.recordBookTypeCode.equals("TakeOrDiscardSoilRecordBook")
                            || recordBook.recordBookTypeCode.equals("OtherEngineeringRecordBook")
                            || recordBook.recordBookTypeCode.equals("InterchangeRecordBook")
                            || recordBook.recordBookTypeCode.equals("LargeMediumBridgeRecordBook")
                            || recordBook.recordBookTypeCode.equals("SeparateTypeBridgeRecordBook")
                            || recordBook.recordBookTypeCode.equals("SmallBridgeAndCulvertRecordBook")
                            ||recordBook.recordBookTypeCode.equals("ExistingBridgeAndCulvertRecordBook")
                    ) {
                        startActivity(new Intent(RecordBookActivity.this, BookDetailActivity.class).putExtra("book", list.get(parent).list.get(position)));
                    }
                }
            });
            adapter.setOnChildItemLongClickListener(new BookNewAdapter.OnChildItemLongClickListener() {
                @Override
                public void onItemLongClick(final int parent, final int position) {
                    //删除该工点
                    AlertDialog dialog = new AlertDialog.Builder(RecordBookActivity.this).setMessage("删除后数据不可恢复，确定删除该工点吗？").setTitle("删除").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBUtil.useExtraDbByProjectName(RecordBookActivity.this, project.projectName);
                            BookSimple work = list.get(parent).list.get(position);
                            //先删除工点关联的附件，图片
                            LitePal.deleteAll(CommonRecordBookMedia.class, " record_book_id = ? and record_book_type_id = ?", work.id + "", list.get(parent).id + "");
                            work.delete();
                            list.get(parent).list.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }).create();
                    dialog.show();


                }
            });

        } else if (projectType == 1) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText("下载");
            listView.setVisibility(View.GONE);
            ll_download.setVisibility(View.VISIBLE);
            adapter = new BookNewAdapter(this, list, projectType);
            listViewDownload.setAdapter(adapter);
            //在线项目，只能拷贝项目到本地
            adapter.setOnParentClickListener(new BookNewAdapter.OnParentClickListener() {
                @Override
                public void onParentClick(int position) {
                    list.get(position).isCheck = !list.get(position).isCheck;
                    adapter.notifyDataSetChanged();
                }


                @Override
                public void onAddClick(int position) {

                }
            });
            adapter.setOnAllChooseListener(new BookNewAdapter.OnAllChooseListener() {
                @Override
                public void onAllChoose(int position) {
                    List<BookSimple> childs = list.get(position).list;
                    list.get(position).isAllCheck = !list.get(position).isAllCheck;
                    for (int i = 0; i < childs.size(); i++) {
                        childs.get(i).isCheck = list.get(position).isAllCheck;
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            adapter.setOnClickListener(new BookNewAdapter.OnClickListener() {
                @Override
                public void onClick(int parent, int position, BookAdapter adapter) {
                    list.get(parent).list.get(position).isCheck = !list.get(parent).list.get(position).isCheck;
                    adapter.notifyDataSetChanged();
                }
            });
            getProjectBookList();
        }
    }

    private void getProjectBookList() {
        HttpProvider.get(this, AppContext.sp.getString(Config.SP.CURRENT_URL) + HttpUtil.selectWorksiteOverView, new ParamsMap().with("projectId", project.projectId + ""), new ResponseCallBack() {
            @Override
            public void onSuccess(String jsonObject) {
                try {
                    JSONObject object = new JSONObject(jsonObject);
                    JSONArray array = JsonHelper.getJsonArray(object, "data");
                    for (int i = 0; i < array.length(); i++) {
                        RecordBookType books = new RecordBookType();
                        JSONObject jObject = JsonHelper.getJsonObject(array, i);
                        JSONObject recordBookType = JsonHelper.getJsonObject(jObject, "recordBookType");
                        books.recordBookTypeName = JsonHelper.getJsonString(recordBookType, "recordBookTypeName");
                        books.recordBookTypeCode = JsonHelper.getJsonString(recordBookType, "recordBookTypeCode");
                        books.updateUser = JsonHelper.getJsonLong(recordBookType, "updateUser");
                        books.updateTime = JsonHelper.getJsonLong(recordBookType, "updateTime");
                        books.status = JsonHelper.getJsonString(recordBookType, "status");
                        books.id = JsonHelper.getJsonInt(recordBookType, "id");
                        books.list = new ArrayList<>();
                        if (!jObject.isNull("workStations")) {
                            JSONArray workArrray = JsonHelper.getJsonArray(jObject, "workStations");
                            for (int j = 0; j < workArrray.length(); j++) {
                                JSONObject wObject = JsonHelper.getJsonObject(workArrray, j);
                                BookSimple simple = BookUtils.newBookByType(books.recordBookTypeCode);
                                simple.positionX = JsonHelper.getJsonString(wObject, "positionX");
                                simple.positionY = JsonHelper.getJsonString(wObject, "positionY");

                                JSONObject record = JsonHelper.getJsonObject(wObject, "recordBook");
                                simple.id = JsonHelper.getJsonLong(record, "id");
                                simple.recordBookTypeId = JsonHelper.getJsonLong(record, "recordBookTypeId");
                                simple.createTime = JsonHelper.getJsonLong(record, "createTime");
                                simple.updateTime = JsonHelper.getJsonLong(record, "updateTime");
                                simple.createUser = JsonHelper.getJsonLong(record, "createUser");
                                simple.updateUser = JsonHelper.getJsonLong(record, "updateUser");
                                simple.projectId = JsonHelper.getJsonLong(record, "projectId");
                                if ("RetainingWallRecordBook".equals(books.recordBookTypeCode)) {
                                    RetainingWallRecordBook recordBook = (RetainingWallRecordBook) simple;
                                    recordBook.startStake = JsonHelper.getJsonString(record, "startStake");
                                    recordBook.endStake = JsonHelper.getJsonString(record, "endStake");
                                    recordBook.wallType = JsonHelper.getJsonLong(record, "wallType");
                                    recordBook.uniqueTag = recordBook.startStake + " - " + recordBook.endStake + " - " + DictUtil.getDictLabelByCode(RecordBookActivity.this, DictUtil.WALL_TYPE, recordBook.wallType);

                                } else if ("InterchangeRecordBook".equals(books.recordBookTypeCode)) {
                                    InterchangeRecordBook recordBook = (InterchangeRecordBook) simple;
                                    recordBook.crossStake = JsonHelper.getJsonString(record, "crossStake");
                                    recordBook.name = JsonHelper.getJsonString(record, "name");
                                    recordBook.uniqueTag = recordBook.crossStake + " - " + recordBook.name;

                                } else if ("TakeOrDiscardSoilRecordBook".equals(books.recordBookTypeCode)) {
                                    TakeOrDiscardSoilRecordBook recordBook = (TakeOrDiscardSoilRecordBook) simple;
                                    recordBook.roadStake = JsonHelper.getJsonString(record, "roadStake");
                                    recordBook.name = JsonHelper.getJsonString(record, "name");
                                    recordBook.uniqueTag = recordBook.roadStake + " - " + recordBook.name;
                                } else if ("SmallBridgeAndCulvertRecordBook".equals(books.recordBookTypeCode)) {
                                    SmallBridgeAndCulvertRecordBook recordBook = (SmallBridgeAndCulvertRecordBook) simple;
                                    recordBook.centerStake = JsonHelper.getJsonString(record, "centerStake");
                                    recordBook.structure = JsonHelper.getJsonLong(record, "structure");
                                    recordBook.uniqueTag = recordBook.centerStake + " - " + DictUtil.getDictLabelByCode(RecordBookActivity.this, DictUtil.STRUCTURE, recordBook.structure);
                                }else if ("ExistingBridgeAndCulvertRecordBook".equals(books.recordBookTypeCode)) {
                                    ExistingBridgeCulvertRecordBook recordBook = (ExistingBridgeCulvertRecordBook) simple;
                                    recordBook.nowCenterStake = JsonHelper.getJsonString(record, "nowCenterStake");
                                    recordBook.structureName = JsonHelper.getJsonString(record, "structureName");
                                    recordBook.uniqueTag = recordBook.nowCenterStake + " - "  +recordBook.structureName;
                                }
                                books.list.add(simple);
                            }
                        }
                        list.add(books);

                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String message) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
