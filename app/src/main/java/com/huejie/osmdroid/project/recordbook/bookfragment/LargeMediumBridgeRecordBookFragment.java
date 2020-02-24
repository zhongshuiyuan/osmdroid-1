package com.huejie.osmdroid.project.recordbook.bookfragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.model.books.ExistingRelatedRiverEngineeringSurveyInfo;
import com.huejie.osmdroid.model.books.FloodLevelRecordSurveyInfo;
import com.huejie.osmdroid.model.books.LargeMediumBridgeRecordBook;
import com.huejie.osmdroid.project.recordbook.activity.ExistingRiverProjectActivity;
import com.huejie.osmdroid.project.recordbook.activity.FloodLevelSurveyRecordsActivity;
import com.huejie.osmdroid.project.recordbook.adapter.LargeMediumBridgeFloodAdapter;
import com.huejie.osmdroid.project.recordbook.adapter.LargeMediumBridgeRiverAdapter;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.DictUtil;
import com.huejie.osmdroid.util.Util;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class LargeMediumBridgeRecordBookFragment extends BookBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BookSimple bookSimple;
    private String projectName;
    //挡墙类型
    @BindView(R.id.tv_slfx)
    CheckedTextView tv_slfx;
    private LargeMediumBridgeRiverAdapter riverAdapter;
    private LargeMediumBridgeFloodAdapter floodAdapter;

    //添加既有涉河工程调查记录
    @OnClick(R.id.iv_add_project)
    public void addPrject() {
        ExistingRelatedRiverEngineeringSurveyInfo info = new ExistingRelatedRiverEngineeringSurveyInfo();
        info.recordBookId = book.id;
        info.recordBookTypeId = book.recordBookTypeId;
        info.updateUser = AppContext.sp.getLong(Config.SP.CURRENT_USER_ID);
        startActivityForResult(new Intent(getActivity(), ExistingRiverProjectActivity.class).putExtra(FROM_TAG, TAG_ADD).putExtra(INTENT_DATA, info).putExtra("projectName", projectName), 100);

    }

    //添加洪水位调查记录
    @OnClick(R.id.iv_add_record)
    public void addRecord() {
        FloodLevelRecordSurveyInfo info = new FloodLevelRecordSurveyInfo();
        info.recordBookId = book.id;
        info.recordBookTypeId = book.recordBookTypeId;
        info.updateUser = AppContext.sp.getLong(Config.SP.CURRENT_USER_ID);
        startActivityForResult(new Intent(getActivity(), FloodLevelSurveyRecordsActivity.class).putExtra(FROM_TAG, TAG_ADD).putExtra(INTENT_DATA, info).putExtra("projectName", projectName), 101);

    }

    @OnClick(R.id.tv_slfx)
    public void slfx() {
        Util.showPopwindow(getActivity(), tv_slfx, DictUtil.getDictLabels(getActivity(), DictUtil.FLOW_DIRECTION), "");
    }

    @BindView(R.id.tv_thdjjk)
    CheckedTextView tv_thdjjk;

    @OnClick(R.id.tv_thdjjk)
    public void thdjjk() {
        Util.showPopwindow(getActivity(), tv_thdjjk, DictUtil.getDictLabels(getActivity(), DictUtil.NAVIGATION_LEVEL), "");
    }

    //结构形式
    @BindView(R.id.tv_jgxs)
    CheckedTextView tv_jgxs;

    @OnClick(R.id.tv_jgxs)
    public void jgxs() {
        Util.showPopwindow(getActivity(), tv_jgxs, DictUtil.getDictLabels(getActivity(), DictUtil.STRUCTURE), "");
    }


    @BindView(R.id.riverListView)
    ListView riverListView;
    @BindView(R.id.floodListView)
    ListView floodListView;

    @BindView(R.id.et_qlmc)
    EditText et_qlmc;
    @BindView(R.id.et_dmhm)
    EditText et_dmhm;
    @BindView(R.id.et_qlzxzh)
    EditText et_qlzxzh;
    @BindView(R.id.et_hlzxzh)
    EditText et_hlzxzh;
    @BindView(R.id.et_lxhljj)
    EditText et_lxhljj;
    @BindView(R.id.et_hsmj)
    EditText et_hsmj;
    @BindView(R.id.et_cssw)
    EditText et_cssw;
    @BindView(R.id.et_sjhsw)
    EditText et_sjhsw;
    @BindView(R.id.et_zgthsw)
    EditText et_zgthsw;
    @BindView(R.id.et_zdthsw)
    EditText et_zdthsw;
    @BindView(R.id.et_bjlmc)
    EditText et_bjlmc;
    @BindView(R.id.et_bjlzxzh)
    EditText et_bjlzxzh;
    @BindView(R.id.et_lxbjljj)
    EditText et_lxbjljj;
    @BindView(R.id.et_holeNumber)
    EditText et_holeNumber;
    @BindView(R.id.et_holeSize)
    EditText et_holeSize;
    @BindView(R.id.et_jxjd)
    EditText et_jxjd;
    @BindView(R.id.et_hdjlms)
    EditText et_hdjlms;
    @BindView(R.id.et_dxdmkyms)
    EditText et_dxdmkyms;
    @BindView(R.id.et_ghglfa)
    EditText et_ghglfa;
    @BindView(R.id.et_fwdcqk)
    EditText et_fwdcqk;
    @BindView(R.id.et_clsj)
    EditText et_clsj;

    public static final String FROM_TAG = "from_tag";
    public static final String INTENT_DATA = "intent_data";
    public static final String TAG_ADD = "tag_add";
    public static final String TAG_MODIFY = "tag_modify";


    public LargeMediumBridgeRecordBookFragment() {
        // Required empty public constructor
    }

    public static LargeMediumBridgeRecordBookFragment newInstance(BookSimple param1, String param2) {
        LargeMediumBridgeRecordBookFragment fragment = new LargeMediumBridgeRecordBookFragment();
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
        return inflater.inflate(R.layout.fragment_large_medium_bridge_recordbook, container, false);
    }

    private LargeMediumBridgeRecordBook book;
    private List<ExistingRelatedRiverEngineeringSurveyInfo> riverList;
    private List<FloodLevelRecordSurveyInfo> floodList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        book = LitePal.find(LargeMediumBridgeRecordBook.class, bookSimple.id);
        riverList = LitePal.where("record_book_id = ? and record_book_type_id = ?", book.id + "", book.recordBookTypeId + "").find(ExistingRelatedRiverEngineeringSurveyInfo.class);
        floodList = LitePal.where("record_book_id = ? and record_book_type_id = ?", book.id + "", book.recordBookTypeId + "").find(FloodLevelRecordSurveyInfo.class);
        et_qlmc.setText(book.name);
        et_dmhm.setText(book.placeName);
        et_qlzxzh.setText(book.bridgeCenterStake);
        et_hlzxzh.setText(book.riverCenterStake);
        et_lxhljj.setText(Util.valueString(book.riverCrossAngle));
        et_hsmj.setText(Util.valueString(book.catchmentArea));
        et_cssw.setText(Util.valueString(book.riverDepth));
        et_sjhsw.setText(Util.valueString(book.desginFloodLevel));
        et_zgthsw.setText(Util.valueString(book.highestNavigationLevel));
        et_zdthsw.setText(Util.valueString(book.lowestNavigationLevel));
        et_bjlmc.setText(book.intersectName);
        et_bjlzxzh.setText(book.intersectCenterStake);
        et_lxbjljj.setText(Util.valueString(book.intersectCrossAngle));
        et_holeNumber.setText(Util.valueString(book.holesNumber));
        et_holeSize.setText(Util.valueString(book.holesAperture));
        et_jxjd.setText(Util.valueString(book.crossAngle));
        et_hdjlms.setText(book.crossLineDesc);
        et_dxdmkyms.setText(book.controlFactorsDesc);
        et_ghglfa.setText(book.changeRiverDesc);
        et_fwdcqk.setText(book.floodSurveyDesc);
        et_clsj.setText(book.metricalData);
        tv_slfx.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.FLOW_DIRECTION, book.flowDirection));
        tv_thdjjk.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.NAVIGATION_LEVEL, book.navigationLevel));
        tv_jgxs.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.STRUCTURE, book.structure));

        riverAdapter = new LargeMediumBridgeRiverAdapter(getActivity(), riverList);
        riverListView.setAdapter(riverAdapter);
        riverAdapter.setOnClickListener(new LargeMediumBridgeRiverAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                startActivityForResult(new Intent(getActivity(), ExistingRiverProjectActivity.class).putExtra(FROM_TAG, TAG_MODIFY).putExtra(INTENT_DATA, riverList.get(position)).putExtra("projectName", projectName), 100);
            }
        });
        riverAdapter.setOnItemLongClickListener(new LargeMediumBridgeRiverAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final int position) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("警告").setMessage("确认删除该记录吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
                        riverList.get(position).delete();
                        dialog.dismiss();
                        riverList.remove(position);
                        riverAdapter.notifyDataSetChanged();
                    }
                }).create();
                dialog.show();
            }
        });

        floodAdapter = new LargeMediumBridgeFloodAdapter(getActivity(), floodList);
        floodListView.setAdapter(floodAdapter);
        floodAdapter.setOnClickListener(new LargeMediumBridgeFloodAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                startActivityForResult(new Intent(getActivity(), FloodLevelSurveyRecordsActivity.class).putExtra(FROM_TAG, TAG_MODIFY).putExtra(INTENT_DATA, floodList.get(position)).putExtra("projectName", projectName), 101);
            }
        });
        floodAdapter.setOnItemLongClickListener(new LargeMediumBridgeFloodAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final int position) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("警告").setMessage("确认删除该记录吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
                        floodList.get(position).delete();
                        dialog.dismiss();
                        floodList.remove(position);
                        floodAdapter.notifyDataSetChanged();
                    }
                }).create();
                dialog.show();
            }
        });

    }


    @Override
    public BookSimple getBookContent() {
        book.name = et_qlmc.getText().toString().trim();
        book.placeName = et_dmhm.getText().toString().trim();
        book.bridgeCenterStake = et_qlzxzh.getText().toString().trim();
        book.riverCenterStake = et_hlzxzh.getText().toString().trim();
        book.intersectName = et_bjlmc.getText().toString().trim();
        book.intersectCenterStake = et_bjlzxzh.getText().toString().trim();
        book.crossLineDesc = et_hdjlms.getText().toString().trim();
        book.controlFactorsDesc = et_dxdmkyms.getText().toString().trim();
        book.changeRiverDesc = et_ghglfa.getText().toString().trim();
        book.riverCrossAngle = Util.valueLong(et_lxhljj.getText().toString().trim());
        book.intersectCrossAngle = Util.valueLong(et_lxbjljj.getText().toString().trim());
        book.holesNumber = Util.valueLong(et_holeNumber.getText().toString().trim());
        book.crossAngle = Util.valueLong(et_jxjd.getText().toString().trim());
        book.catchmentArea = Util.valueDouble(et_hsmj.getText().toString().trim());
        book.riverDepth = Util.valueDouble(et_cssw.getText().toString().trim());
        book.desginFloodLevel = Util.valueDouble(et_sjhsw.getText().toString().trim());
        book.highestNavigationLevel = Util.valueDouble(et_zgthsw.getText().toString().trim());
        book.lowestNavigationLevel = Util.valueDouble(et_zdthsw.getText().toString().trim());
        book.holesAperture = Util.valueDouble(et_holeSize.getText().toString().trim());
        book.floodSurveyDesc = et_fwdcqk.getText().toString().trim();
        book.metricalData = et_clsj.getText().toString().trim();
        if (tv_slfx.getTag() != null) {
            book.flowDirection = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.FLOW_DIRECTION, tv_slfx.getTag().toString());
        }
        if (tv_thdjjk.getTag() != null) {
            book.navigationLevel = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.NAVIGATION_LEVEL, tv_thdjjk.getTag().toString());
        }
        if (tv_jgxs.getTag() != null) {
            book.structure = DictUtil.getDictCodeByLabel(getActivity(), DictUtil.STRUCTURE, tv_jgxs.getTag().toString());
        }
        return book;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                riverList.clear();
                DBUtil.useExtraDbByProjectName(getActivity(), projectName);
                riverList = LitePal.where("record_book_id = ? and record_book_type_id = ?", book.id + "", book.recordBookTypeId + "").find(ExistingRelatedRiverEngineeringSurveyInfo.class);
                riverAdapter.setList(riverList);
                riverAdapter.notifyDataSetChanged();

            } else if (requestCode == 101) {
                floodList.clear();
                DBUtil.useExtraDbByProjectName(getActivity(), projectName);
                floodList = LitePal.where("record_book_id = ? and record_book_type_id = ?", book.id + "", book.recordBookTypeId + "").find(FloodLevelRecordSurveyInfo.class);
                floodAdapter.setList(floodList);
                floodAdapter.notifyDataSetChanged();
            }

        }

    }
}
