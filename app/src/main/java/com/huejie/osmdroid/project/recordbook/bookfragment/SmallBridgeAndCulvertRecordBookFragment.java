package com.huejie.osmdroid.project.recordbook.bookfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.books.BookSimple;
import com.huejie.osmdroid.model.books.SmallBridgeAndCulvertRecordBook;
import com.huejie.osmdroid.model.books.SmallBridgeCulvertFloodSurvey;
import com.huejie.osmdroid.model.books.SmallBridgeCulvertFormInfo;
import com.huejie.osmdroid.project.recordbook.activity.SmallBridgeCulvertFloodSurveyActivity;
import com.huejie.osmdroid.project.recordbook.activity.SmallBridgeCulvertInitialActivity;
import com.huejie.osmdroid.project.recordbook.adapter.FloodSurveyAdapter;
import com.huejie.osmdroid.project.recordbook.adapter.InitialAdapter;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.DictUtil;
import com.huejie.osmdroid.util.Util;

import org.litepal.LitePal;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SmallBridgeAndCulvertRecordBookFragment extends BookBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;

    private BookSimple bookSimple;
    //结构形式
    @BindView(R.id.tv_structureType)
    CheckedTextView tv_structureType;
    //结构物用途
    @BindView(R.id.tv_jgwyt)
    CheckedTextView tv_jgwyt;

    public static final String SMALL_BRIDGE_CULVERT_FORM_INFO = "smallBridgeCulvertFormInfo";
    public static final String SMALL_BRIDGE_CULVERT_FLOOD_SURVEY = "smallBridgeCulvertFloodSurvey";
    public static final int REQUEST_CODE_CREATE = 101;
    public static final int REQUEST_CODE_EDIT = 102;
    private InitialAdapter mInitialAdapter;
    private ArrayList<SmallBridgeCulvertFormInfo> mListFormInfo;
    private ArrayList<SmallBridgeCulvertFormInfo> mListFormInfoDel=new ArrayList<>();
    private int formInfoPosition;

    private FloodSurveyAdapter mFloodSurveyAdapter;
    private ArrayList<SmallBridgeCulvertFloodSurvey> mListFloodSurvey;
    private ArrayList<SmallBridgeCulvertFloodSurvey> mListFloodSurveyDel=new ArrayList<>();
    private int floodSurveyPosition;

    @OnClick(R.id.tv_structureType)
    public void structureType() {
        Util.showPopwindow(getActivity(), tv_structureType, DictUtil.getDictLabels(getActivity(), DictUtil.SMALL_BRIDGE_CULVERT_STRUCTURE), "");
    }

    @OnClick(R.id.tv_jgwyt)
    public void jgwyt() {
        Util.showPopwindow(getActivity(), tv_jgwyt, DictUtil.getDictLabels(getActivity(), DictUtil.SMALL_BRIDGE_CULVERT_STRUCTURE), "");
    }

    @BindView(R.id.et_zh)
    EditText et_zh;
    @BindView(R.id.et_dmhm)
    EditText et_dmhm;
    @BindView(R.id.et_jiaodu)
    EditText et_jiaodu;
    @BindView(R.id.et_cnkj)
    EditText et_cnkj;
    @BindView(R.id.et_sjbg)
    EditText et_sjbg;
    @BindView(R.id.et_gdbg)
    EditText et_gdbg;
    @BindView(R.id.et_hcqc)
    EditText et_hcqc;
    @BindView(R.id.et_hdttgd)
    EditText et_hdttgd;
    @BindView(R.id.et_hcpd)
    EditText et_hcpd;
    @BindView(R.id.et_hctrzb)
    EditText et_hctrzb;
    @BindView(R.id.et_csss)
    EditText et_csss;
    @BindView(R.id.et_hdjlms)
    EditText et_hdjlms;
    @BindView(R.id.et_hsdxdmms)
    EditText et_hsdxdmms;
    @BindView(R.id.et_ghglfa)
    EditText et_ghglfa;

    @BindView(R.id.lv_cn)
    ListView lv_cn;
    @BindView(R.id.iv_add_cn)
    ImageView iv_add_cn;
    @BindView(R.id.lv_lshsw)
    ListView lvLshsw;
    @BindView(R.id.iv_add_lshsw)
    ImageView ivAddLshsw;

    public SmallBridgeAndCulvertRecordBookFragment() {
        // Required empty public constructor
    }

    public static SmallBridgeAndCulvertRecordBookFragment newInstance(BookSimple param1, String param2) {
        SmallBridgeAndCulvertRecordBookFragment fragment = new SmallBridgeAndCulvertRecordBookFragment();
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
        View view = inflater.inflate(R.layout.fragment_small_bridge_culvert_recordbook, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private SmallBridgeAndCulvertRecordBook book;
    private String projectName;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        book = LitePal.find(SmallBridgeAndCulvertRecordBook.class, bookSimple.id);

        et_zh.setText(book.centerStake);
        et_dmhm.setText(book.placeOrRiverName);
        et_jiaodu.setText(Util.valueString(book.angle));
        et_cnkj.setText(Util.valueString(book.initAperture));
        et_sjbg.setText(Util.valueString(book.designElevation));
        et_gdbg.setText(Util.valueString(book.bottomElevation));
        et_hcqc.setText(Util.valueString(book.culvertLength));
        et_hdttgd.setText(Util.valueString(book.culvertRoofHeight));
        et_hcpd.setText(Util.valueString(book.riverBedSlope));
        et_hctrzb.setText(book.bedSoilVegetation);
        et_csss.setText(Util.valueString(book.measuringDepth));
        et_hdjlms.setText(book.channelsIntersectedPipelinesDesc);
        et_hsdxdmms.setText(book.catchmentLandformGeologyDesc);
        et_ghglfa.setText(book.riverRoadModificationDesc);

        if(book.structure!=-1){
            tv_structureType.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.SMALL_BRIDGE_CULVERT_STRUCTURE,book.structure));
            tv_jgwyt.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.SMALL_BRIDGE_CULVERT_STRUCTURE,book.structure));
        }

        initRvInitial();
        initRvFloodSurvey();
    }

    private void initRvFloodSurvey() {
        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
        mListFloodSurvey = (ArrayList<SmallBridgeCulvertFloodSurvey>) LitePal.where("record_book_id = ?",book.id+"").find(SmallBridgeCulvertFloodSurvey.class);
        mFloodSurveyAdapter = new FloodSurveyAdapter(getActivity(), mListFloodSurvey);
        lvLshsw.setAdapter(mFloodSurveyAdapter);
        lvLshsw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SmallBridgeAndCulvertRecordBookFragment.this.floodSurveyPosition = position;
                Intent intent = new Intent(getActivity(), SmallBridgeCulvertFloodSurveyActivity.class);
                intent.putExtra(SMALL_BRIDGE_CULVERT_FLOOD_SURVEY, mListFloodSurvey.get(position));
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });
        lvLshsw.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("确定删除该记录吗？").setTitle("警告").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListFloodSurveyDel.add(mListFloodSurvey.get(position));
                        mListFloodSurvey.remove(position);
                        mFloodSurveyAdapter.notifyDataSetChanged();
                    }
                }).create();
                dialog.show();
                return false;
            }
        });
    }

    private void initRvInitial() {
        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
        mListFormInfo = (ArrayList<SmallBridgeCulvertFormInfo>) LitePal.where("record_book_id = ?",book.id+"").find(SmallBridgeCulvertFormInfo.class);
        if(mListFormInfo!=null){
            for(int i=0;i<mListFormInfo.size();i++){
                String cnjck = DictUtil.getDictLabelByCode(getActivity(), DictUtil.INIT_IMPORT_EXPORT_FORM, mListFormInfo.get(i).initImportExportForm);
                mListFormInfo.get(i).initImportExportFormStr=cnjck;
            }
        }

        mInitialAdapter = new InitialAdapter(getActivity(), mListFormInfo);
        lv_cn.setAdapter(mInitialAdapter);
        lv_cn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SmallBridgeAndCulvertRecordBookFragment.this.formInfoPosition = position;
                Intent intent = new Intent(getActivity(), SmallBridgeCulvertInitialActivity.class);
                intent.putExtra(SMALL_BRIDGE_CULVERT_FORM_INFO, mListFormInfo.get(position));
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });
        lv_cn.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("确定删除该记录吗？").setTitle("警告").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListFormInfoDel.add(mListFormInfo.get(position));
                        mListFormInfo.remove(position);
                        mInitialAdapter.notifyDataSetChanged();
                    }
                }).create();
                dialog.show();
                return false;
            }
        });
    }


    @Override
    public BookSimple getBookContent() {

        book.structure=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.SMALL_BRIDGE_CULVERT_STRUCTURE, tv_structureType.getText().toString().trim());
        book.centerStake=et_zh.getText().toString().trim();
        book.placeOrRiverName=et_dmhm.getText().toString().trim();
        book.angle=Util.valueLong(et_jiaodu.getText().toString().trim());
        book.initAperture=Util.valueDouble(et_cnkj.getText().toString().trim());
        book.designElevation=Util.valueDouble(et_sjbg.getText().toString().trim());
        book.bottomElevation=Util.valueDouble(et_gdbg.getText().toString().trim());
        book.mainPurpose= DictUtil.getDictCodeByLabel(getActivity(), DictUtil.SMALL_BRIDGE_CULVERT_STRUCTURE, tv_jgwyt.getText().toString().trim());
        book.culvertLength=Util.valueDouble(et_hcqc.getText().toString().trim());
        book.culvertRoofHeight=Util.valueDouble(et_hdttgd.getText().toString().trim());
        book.riverBedSlope=Util.valueLong(et_hcpd.getText().toString().trim());
        book.bedSoilVegetation=et_hctrzb.getText().toString().trim();
        book.measuringDepth=Util.valueDouble(et_csss.getText().toString().trim());
        book.channelsIntersectedPipelinesDesc=et_hdjlms.getText().toString().trim();
        book.catchmentLandformGeologyDesc=et_hsdxdmms.getText().toString().trim();
        book.riverRoadModificationDesc=et_ghglfa.getText().toString().trim();

        DBUtil.useExtraDbByProjectName(getActivity(), projectName);
        for(int i=0;i<mListFormInfo.size();i++){
            SmallBridgeCulvertFormInfo formInfo = mListFormInfo.get(i);
            formInfo.recordBookId=book.id;
            formInfo.saveOrUpdate("id = ?", formInfo.id + "");
        }
        for(int i=0;i<mListFormInfoDel.size();i++){
            mListFormInfoDel.get(i).delete();
        }
        for(int i=0;i<mListFloodSurvey.size();i++){
            SmallBridgeCulvertFloodSurvey survey = mListFloodSurvey.get(i);
            survey.recordBookId=book.id;
            survey.saveOrUpdate("id = ?", survey.id + "");
        }
        for(int i=0;i<mListFloodSurveyDel.size();i++){
            mListFloodSurveyDel.get(i).delete();
        }
        return book;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SmallBridgeCulvertInitialActivity.RESULT_CODE_INITIAL) {
            SmallBridgeCulvertFormInfo smallBridgeCulvertFormInfo = (SmallBridgeCulvertFormInfo) data.getSerializableExtra(SMALL_BRIDGE_CULVERT_FORM_INFO);
            switch (requestCode) {
                case REQUEST_CODE_CREATE:
                    String cnjck = DictUtil.getDictLabelByCode(getActivity(), DictUtil.INIT_IMPORT_EXPORT_FORM, smallBridgeCulvertFormInfo.initImportExportForm);
                    smallBridgeCulvertFormInfo.initImportExportFormStr=cnjck;
                    mListFormInfo.add(smallBridgeCulvertFormInfo);
                    mInitialAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_CODE_EDIT:
                    mListFormInfo.remove(formInfoPosition);
                    mListFormInfo.add(formInfoPosition, smallBridgeCulvertFormInfo);
                    mInitialAdapter.notifyDataSetChanged();
                    break;
            }
        }
        if (resultCode == SmallBridgeCulvertFloodSurveyActivity.RESULT_CODE_HISTORY_FLOOD) {
            SmallBridgeCulvertFloodSurvey smallBridgeCulvertFloodSurvey = (SmallBridgeCulvertFloodSurvey) data.getSerializableExtra(SMALL_BRIDGE_CULVERT_FLOOD_SURVEY);
            switch (requestCode) {
                case REQUEST_CODE_CREATE:
                    mListFloodSurvey.add(smallBridgeCulvertFloodSurvey);
                    mFloodSurveyAdapter.notifyDataSetChanged();
                    break;
                case REQUEST_CODE_EDIT:
                    mListFloodSurvey.remove(floodSurveyPosition);
                    mListFloodSurvey.add(floodSurveyPosition, smallBridgeCulvertFloodSurvey);
                    mFloodSurveyAdapter.notifyDataSetChanged();
                    break;
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.iv_add_cn)
    public void addInitial() {
        Intent intent = new Intent(getActivity(), SmallBridgeCulvertInitialActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CREATE);
    }

    @OnClick(R.id.iv_add_lshsw)
    public void addLshsw() {
        Intent intent = new Intent(getActivity(), SmallBridgeCulvertFloodSurveyActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CREATE);
    }

}
