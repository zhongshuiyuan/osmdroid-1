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
import com.huejie.osmdroid.model.books.ExistingBridgeCulvertRecordBook;
import com.huejie.osmdroid.model.books.ExistingRelatedRiverEngineeringSurveyInfo;
import com.huejie.osmdroid.model.books.FloodLevelRecordSurveyInfo;
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
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


public class ExistingBridgeAndCulvertRecordBookFragment extends BookBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.et_jgwmc)
    EditText etJgwmc;
    @BindView(R.id.et_dmhm)
    EditText etDmhm;
    @BindView(R.id.et_xzxzh)
    EditText etXzxzh;
    @BindView(R.id.et_yzxzh)
    EditText etYzxzh;
    @BindView(R.id.et_hlzxzh)
    EditText etHlzxzh;
    @BindView(R.id.et_lxhljj)
    EditText etLxhljj;
    @BindView(R.id.et_hsmj)
    EditText etHsmj;
    @BindView(R.id.tv_slfx)
    CheckedTextView tvSlfx;
    @BindView(R.id.et_sws)
    EditText etSws;
    @BindView(R.id.et_sjhsw)
    EditText etSjhsw;
    @BindView(R.id.et_zgthsw)
    EditText etZgthsw;
    @BindView(R.id.et_zdthsw)
    EditText etZdthsw;
    @BindView(R.id.tv_hkdjjk)
    CheckedTextView tvHkdjjk;
    @BindView(R.id.tv_sfmzbz)
    CheckedTextView tvSfmzbz;
    @BindView(R.id.et_bjlmc)
    EditText etBjlmc;
    @BindView(R.id.et_jxjd)
    EditText etJxjd;
    @BindView(R.id.tv_djxz)
    CheckedTextView tvDjxz;
    @BindView(R.id.tv_djgh)
    CheckedTextView tvDjgh;
    @BindView(R.id.et_widthSituation)
    EditText etWidthSituation;
    @BindView(R.id.et_widthPlanning)
    EditText etWidthPlanning;
    @BindView(R.id.tv_mainlineCrossover)
    CheckedTextView tvMainlineCrossover;
    @BindView(R.id.et_jkxz)
    EditText etJkxz;
    @BindView(R.id.et_jkgh)
    EditText etJkgh;
    @BindView(R.id.tv_qiaom)
    CheckedTextView tvQiaom;
    @BindView(R.id.tv_ssf)
    CheckedTextView tvSsf;
    @BindView(R.id.tv_xsg)
    CheckedTextView tvXsg;
    @BindView(R.id.tv_zhiz)
    CheckedTextView tvZhiz;
    @BindView(R.id.tv_sbjg)
    CheckedTextView tvSbjg;
    @BindView(R.id.tv_xbjg)
    CheckedTextView tvXbjg;
    @BindView(R.id.tv_sygn)
    CheckedTextView tvSygn;
    @BindView(R.id.et_bcms)
    EditText etBcms;
    @BindView(R.id.tv_dongk)
    CheckedTextView tvDongk;
    @BindView(R.id.tv_hans)
    CheckedTextView tvHans;
    @BindView(R.id.tv_cjf)
    CheckedTextView tvCjf;
    @BindView(R.id.tv_gnsy)
    CheckedTextView tvGnsy;
    @BindView(R.id.et_hd_bcms)
    EditText etHdBcms;
    @BindView(R.id.et_jgxs)
    EditText etJgxs;
    @BindView(R.id.et_holeNumber)
    EditText etHoleNumber;
    @BindView(R.id.et_holeSize)
    EditText etHoleSize;
    @BindView(R.id.tv_gzwfa)
    CheckedTextView tvGzwfa;
    @BindView(R.id.et_jcjd)
    EditText etJcjd;
    @BindView(R.id.et_cyfaly)
    EditText etCyfaly;
    @BindView(R.id.et_hd_bjl_jcgxms)
    EditText etHdBjlJcgxms;
    @BindView(R.id.et_dxdmdzgk_qxfa)
    EditText etDxdmdzgkQxfa;
    @BindView(R.id.et_ghglfa)
    EditText etGhglfa;

    @BindView(R.id.riverListView)
    ListView riverListView;
    @BindView(R.id.floodListView)
    ListView floodListView;

    Unbinder unbinder;
    private BookSimple bookSimple;

    private LargeMediumBridgeRiverAdapter riverAdapter;
    private LargeMediumBridgeFloodAdapter floodAdapter;
    private List<ExistingRelatedRiverEngineeringSurveyInfo> riverList;
    private List<FloodLevelRecordSurveyInfo> floodList;
    private static final String FROM_TAG = "from_tag";
    private static final String INTENT_DATA = "intent_data";
    private static final String TAG_ADD = "tag_add";
    private static final String TAG_MODIFY = "tag_modify";

    //水流方向
    @OnClick(R.id.tv_slfx)
    public void slfx() {
        Util.showPopwindow(getActivity(), tvSlfx, DictUtil.getDictLabels(getActivity(), DictUtil.FLOW_DIRECTION), "");
    }

    //通航等级及净空
    @OnClick(R.id.tv_hkdjjk)
    public void hkdjjk() {
        Util.showPopwindow(getActivity(), tvHkdjjk, DictUtil.getDictLabels(getActivity(), DictUtil.NAVIGATION_LEVEL), "");
    }

    //通航净空是否满足现行通航标准
    @OnClick(R.id.tv_sfmzbz)
    public void sfmzbz() {
        Util.showPopwindow(getActivity(), tvSfmzbz, DictUtil.getDictLabels(getActivity(), DictUtil.SYS_IS_SYSTEM), "");
    }

    //被交叉道路等级现状
    @OnClick(R.id.tv_djxz)
    public void djxz() {
        Util.showPopwindow(getActivity(), tvDjxz, DictUtil.getDictLabels(getActivity(), DictUtil.CROSS_ROAD_LEVEL), "");
    }

    //被交叉道路等级规划
    @OnClick(R.id.tv_djgh)
    public void djgh() {
        Util.showPopwindow(getActivity(), tvDjgh, DictUtil.getDictLabels(getActivity(), DictUtil.CROSS_ROAD_LEVEL), "");
    }

    //主线交叉方式
    @OnClick(R.id.tv_mainlineCrossover)
    public void mainlineCrossover() {
        Util.showPopwindow(getActivity(), tvMainlineCrossover, DictUtil.getDictLabels(getActivity(), DictUtil.MAIN_INTERSECT_WAY), "");
    }

    //桥面
    @OnClick(R.id.tv_qiaom)
    public void qiaom() {
        Util.showPopwindow(getActivity(), tvQiaom, DictUtil.getDictLabels(getActivity(), DictUtil.BRIDGE_FLOOR_DISEASE), "");
    }

    //伸缩缝
    @OnClick(R.id.tv_ssf)
    public void ssf() {
        Util.showPopwindow(getActivity(), tvSsf, DictUtil.getDictLabels(getActivity(), DictUtil.EXPANSION_JOINTS_DISEASE), "");
    }

    //泄水管
    @OnClick(R.id.tv_xsg)
    public void xsg() {
        Util.showPopwindow(getActivity(), tvXsg, DictUtil.getDictLabels(getActivity(), DictUtil.DRAIN_PIPE_DISEASE), "");
    }

    //支座
    @OnClick(R.id.tv_zhiz)
    public void zhiz() {
        Util.showPopwindow(getActivity(), tvZhiz, DictUtil.getDictLabels(getActivity(), DictUtil.BEARING_DISEASE), "");
    }

    //上部结构
    @OnClick(R.id.tv_sbjg)
    public void sbjg() {
        Util.showPopwindow(getActivity(), tvSbjg, DictUtil.getDictLabels(getActivity(), DictUtil.SUPERSTRUCTURE_DISEASE), "");
    }

    //下部结构
    @OnClick(R.id.tv_xbjg)
    public void xbjg() {
        Util.showPopwindow(getActivity(), tvXbjg, DictUtil.getDictLabels(getActivity(), DictUtil.SUBSTRUCTURE_DISEASE), "");
    }

    //使用功能
    @OnClick(R.id.tv_sygn)
    public void sygn() {
        Util.showPopwindow(getActivity(), tvSygn, DictUtil.getDictLabels(getActivity(), DictUtil.BRIDGE_FUNCTIONAL_DEPLETION), "");
    }

    //洞口
    @OnClick(R.id.tv_dongk)
    public void dongk() {
        Util.showPopwindow(getActivity(), tvDongk, DictUtil.getDictLabels(getActivity(), DictUtil.HOLE_DISEASE), "");
    }

    //涵身
    @OnClick(R.id.tv_hans)
    public void hans() {
        Util.showPopwindow(getActivity(), tvHans, DictUtil.getDictLabels(getActivity(), DictUtil.CULVERT_BODY_DISEASE), "");
    }

    //沉降缝
    @OnClick(R.id.tv_cjf)
    public void cjf() {
        Util.showPopwindow(getActivity(), tvCjf, DictUtil.getDictLabels(getActivity(), DictUtil.SETTLEMENT_JOINT_DISEASE), "");
    }

    //功能使用
    @OnClick(R.id.tv_gnsy)
    public void gnsy() {
        Util.showPopwindow(getActivity(), tvGnsy, DictUtil.getDictLabels(getActivity(), DictUtil.CULVERT_FUNCTIONAL_DEPLETION), "");
    }

    //初拟构造物方案
    @OnClick(R.id.tv_gzwfa)
    public void gzwfa() {
        Util.showPopwindow(getActivity(), tvGzwfa, DictUtil.getDictLabels(getActivity(), DictUtil.PRELIMINARY_CONSTRUCTION_PLAN), "");
    }


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

    public ExistingBridgeAndCulvertRecordBookFragment() {
        // Required empty public constructor
    }

    public static ExistingBridgeAndCulvertRecordBookFragment newInstance(BookSimple param1, String param2) {
        ExistingBridgeAndCulvertRecordBookFragment fragment = new ExistingBridgeAndCulvertRecordBookFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private String projectName;
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
        View view = inflater.inflate(R.layout.fragment_existing_bridge_culvert_recordbook, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private ExistingBridgeCulvertRecordBook book;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        book = LitePal.find(ExistingBridgeCulvertRecordBook.class, bookSimple.id);
        riverList = LitePal.where("record_book_id = ? and record_book_type_id = ?", book.id + "", book.recordBookTypeId + "").find(ExistingRelatedRiverEngineeringSurveyInfo.class);
        floodList = LitePal.where("record_book_id = ? and record_book_type_id = ?", book.id + "", book.recordBookTypeId + "").find(FloodLevelRecordSurveyInfo.class);

        etJgwmc.setText(book.structureName);
        etDmhm.setText(book.placeName);
        etXzxzh.setText(book.nowCenterStake);
        etYzxzh.setText(book.originalCenterStake);
        etHlzxzh.setText(book.riverCenterStake);
        etLxhljj.setText(Util.valueString(book.roadRiverAngle));
        etHsmj.setText(Util.valueString(book.catchmentArea));
        tvSlfx.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.FLOW_DIRECTION, book.flowDirection));
        etSws.setText(Util.valueString(book.riverDepth));
        etSjhsw.setText(Util.valueString(book.desginFloodLevel));
        etZgthsw.setText(Util.valueString(book.highestNavigationLevel));
        etZdthsw.setText(Util.valueString(book.lowestNavigationLevel));
        tvHkdjjk.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.NAVIGATION_LEVEL, book.navigationLevelHeadroom));
        tvSfmzbz.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.SYS_IS_SYSTEM, book.meetsStandards));
        etBjlmc.setText(book.intersectName);
        etJxjd.setText(Util.valueString(book.intersectCrossAngle));
        tvDjxz.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.CROSS_ROAD_LEVEL, book.intersectNowLevel));
        tvDjgh.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.CROSS_ROAD_LEVEL, book.intersectPlanLevel));
        etWidthSituation.setText(Util.valueString(book.intersectNowWidth));
        etWidthPlanning.setText(Util.valueString(book.intersectPlanWidth));
        tvMainlineCrossover.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.MAIN_INTERSECT_WAY, book.mainIntersectWay));
        etJkxz.setText(Util.valueString(book.headroomNow));
        etJkgh.setText(Util.valueString(book.headroomPlan));

        tvQiaom.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.BRIDGE_FLOOR_DISEASE, book.bridgeFloorDisease));
        tvSsf.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.EXPANSION_JOINTS_DISEASE, book.expansionJointsDisease));
        tvXsg.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.DRAIN_PIPE_DISEASE, book.drainPipeDisease));
        tvZhiz.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.BEARING_DISEASE, book.bearingDisease));
        tvSbjg.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.SUPERSTRUCTURE_DISEASE, book.superstructureDisease));
        tvXbjg.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.SUBSTRUCTURE_DISEASE, book.substructureDisease));
        tvSygn.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.BRIDGE_FUNCTIONAL_DEPLETION, book.bridgeFunctionalDepletion));
        etBcms.setText(book.bridgeAdditionalDesc);

        tvDongk.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.HOLE_DISEASE, book.holeDisease));
        tvHans.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.CULVERT_BODY_DISEASE, book.culvertBodyDisease));
        tvCjf.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.SETTLEMENT_JOINT_DISEASE, book.settlementJointDisease));
        tvGnsy.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.CULVERT_FUNCTIONAL_DEPLETION, book.culvertFunctionalDepletion));
        etHdBcms.setText(book.culvertAdditionalDesc);

        etJgxs.setText(book.constructionStructuralForm);
        etHoleNumber.setText(Util.valueString(book.structureHoleNumber));
        etHoleSize.setText(Util.valueString(book.structureHoleAperture));
        tvGzwfa.setText(DictUtil.getDictLabelByCode(getActivity(), DictUtil.PRELIMINARY_CONSTRUCTION_PLAN, book.preliminaryConstructionPlan));
        etJcjd.setText(Util.valueString(book.constructionCrossAngle));
        etCyfaly.setText(book.adoptingReasons);
        etHdBjlJcgxms.setText(book.crossLineDesc);
        etDxdmdzgkQxfa.setText(book.controlFactorsDesc);
        etGhglfa.setText(book.changeRiverDesc);


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
        book.structureName=etJgwmc.getText().toString().trim();
        book.placeName=etDmhm.getText().toString().trim();
        book.nowCenterStake=etXzxzh.getText().toString().trim();
        book.originalCenterStake=etYzxzh.getText().toString().trim();
        book.riverCenterStake=etHlzxzh.getText().toString().trim();
        book.roadRiverAngle=Util.valueLong(etLxhljj.getText().toString().trim());
        book.catchmentArea=Util.valueDouble(etHsmj.getText().toString().trim());
        book.flowDirection=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.FLOW_DIRECTION, tvSlfx.getText().toString().trim());
        book.riverDepth=Util.valueDouble(etSws.getText().toString().trim());
        book.desginFloodLevel=Util.valueDouble(etSjhsw.getText().toString().trim());
        book.highestNavigationLevel=Util.valueDouble(etZgthsw.getText().toString().trim());
        book.lowestNavigationLevel=Util.valueDouble(etZdthsw.getText().toString().trim());
        book.navigationLevelHeadroom=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.NAVIGATION_LEVEL, tvHkdjjk.getText().toString().trim());
        book.meetsStandards=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.SYS_IS_SYSTEM, tvSfmzbz.getText().toString().trim());
        book.intersectName=etBjlmc.getText().toString().trim();
        book.intersectCrossAngle=Util.valueLong(etJxjd.getText().toString().trim());
        book.intersectNowLevel=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.CROSS_ROAD_LEVEL, tvDjxz.getText().toString().trim());
        book.intersectPlanLevel=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.CROSS_ROAD_LEVEL, tvDjgh.getText().toString().trim());
        book.intersectNowWidth=Util.valueDouble(etWidthSituation.getText().toString().trim());
        book.intersectPlanWidth=Util.valueDouble(etWidthPlanning.getText().toString().trim());
        book.mainIntersectWay=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.MAIN_INTERSECT_WAY, tvMainlineCrossover.getText().toString().trim());
        book.headroomNow=Util.valueDouble(etJkxz.getText().toString().trim());
        book.headroomPlan=Util.valueDouble(etJkgh.getText().toString().trim());
        book.bridgeFloorDisease=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.BRIDGE_FLOOR_DISEASE, tvQiaom.getText().toString().trim());
        book.expansionJointsDisease=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.EXPANSION_JOINTS_DISEASE, tvSsf.getText().toString().trim());
        book.drainPipeDisease=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.DRAIN_PIPE_DISEASE, tvXsg.getText().toString().trim());
        book.bearingDisease=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.BEARING_DISEASE, tvZhiz.getText().toString().trim());
        book.superstructureDisease=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.SUPERSTRUCTURE_DISEASE, tvSbjg.getText().toString().trim());
        book.substructureDisease=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.SUBSTRUCTURE_DISEASE, tvXbjg.getText().toString().trim());
        book.bridgeFunctionalDepletion=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.BRIDGE_FUNCTIONAL_DEPLETION, tvSygn.getText().toString().trim());
        book.bridgeAdditionalDesc=etBcms.getText().toString().trim();

        book.holeDisease=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.HOLE_DISEASE, tvDongk.getText().toString().trim());
        book.culvertBodyDisease=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.CULVERT_BODY_DISEASE, tvHans.getText().toString().trim());
        book.settlementJointDisease=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.SETTLEMENT_JOINT_DISEASE, tvCjf.getText().toString().trim());
        book.culvertFunctionalDepletion=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.CULVERT_FUNCTIONAL_DEPLETION, tvGnsy.getText().toString().trim());
        book.culvertAdditionalDesc=etHdBcms.getText().toString().trim();

        book.constructionStructuralForm=etJgxs.getText().toString().trim();
        book.structureHoleNumber=Util.valueLong(etHoleNumber.getText().toString().trim());
        book.structureHoleAperture=Util.valueDouble(etHoleSize.getText().toString().trim());
        book.preliminaryConstructionPlan=DictUtil.getDictCodeByLabel(getActivity(), DictUtil.PRELIMINARY_CONSTRUCTION_PLAN, tvGzwfa.getText().toString().trim());
        book.constructionCrossAngle=Util.valueLong(etJcjd.getText().toString().trim());
        book.adoptingReasons=etCyfaly.getText().toString().trim();
        book.crossLineDesc=etHdBjlJcgxms.getText().toString().trim();
        book.controlFactorsDesc=etDxdmdzgkQxfa.getText().toString().trim();
        book.changeRiverDesc=etGhglfa.getText().toString().trim();

        return book;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
