package com.huejie.osmdroid.project.recordbook.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.model.books.ExistingRelatedRiverEngineeringSurveyInfo;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.util.DictUtil;
import com.huejie.osmdroid.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huejie.osmdroid.project.recordbook.bookfragment.LargeMediumBridgeRecordBookFragment.FROM_TAG;
import static com.huejie.osmdroid.project.recordbook.bookfragment.LargeMediumBridgeRecordBookFragment.INTENT_DATA;
import static com.huejie.osmdroid.project.recordbook.bookfragment.LargeMediumBridgeRecordBookFragment.TAG_ADD;
import static com.huejie.osmdroid.project.recordbook.bookfragment.LargeMediumBridgeRecordBookFragment.TAG_MODIFY;

public class ExistingRiverProjectActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.et_hm)
    EditText et_hm;
    @BindView(R.id.et_kuaj)
    EditText et_kuaj;
    @BindView(R.id.et_kongj)
    EditText et_kongj;
    @BindView(R.id.et_jznd)
    EditText et_jznd;
    @BindView(R.id.et_djqk)
    EditText et_djqk;
    @BindView(R.id.et_qc)
    EditText et_qc;
    @BindView(R.id.et_sbxs)
    EditText et_sbxs;
    @BindView(R.id.et_xbxs)
    EditText et_xbxs;
    @BindView(R.id.et_ysljj)
    EditText et_ysljj;
    @BindView(R.id.et_fsnd)
    EditText et_fsnd;
    @BindView(R.id.et_hsgc)
    EditText et_hsgc;

    @BindView(R.id.tv_xqxdwz)
    CheckedTextView tv_xqxdwz;

    //与新桥相对位置
    @OnClick(R.id.tv_xqxdwz)
    public void xqxdwz() {
        Util.showPopwindow(this, tv_xqxdwz, DictUtil.getDictLabels(this, DictUtil.REFER_RELATIVE_POSITION), "");
    }

    @BindView(R.id.tv_syqk)
    CheckedTextView tv_syqk;

    //使用情况
    @OnClick(R.id.tv_syqk)
    public void syqk() {
        Util.showPopwindow(this, tv_syqk, DictUtil.getDictLabels(this, DictUtil.REFER_CONDITION), "");
    }

    @BindView(R.id.tv_dzqk)
    CheckedTextView tv_dzqk;

    //地质情况
    @OnClick(R.id.tv_dzqk)
    public void dzqk() {
        Util.showPopwindow(this, tv_dzqk, DictUtil.getDictLabels(this, DictUtil.REFER_GEOLOGY), "");
    }

    @BindView(R.id.tv_cyqk)
    CheckedTextView tv_cyqk;

    //冲淤情况
    @OnClick(R.id.tv_cyqk)
    public void cyqk() {
        Util.showPopwindow(this, tv_cyqk, DictUtil.getDictLabels(this, DictUtil.REFER_SEDIMENT), "");
    }


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void confirm() {
        String placeName = et_hm.getText().toString().trim();
        if (TextUtils.isEmpty(placeName)) {
            ToastUtils.showShortToast("请输入河名（桥名）");
            return;
        }
        info.placeName = placeName;
        info.span = Util.valueDouble(et_kuaj.getText().toString().trim());
        info.aperture = Util.valueDouble(et_kongj.getText().toString().trim());
        info.buildYear = Util.valueLong(et_jznd.getText().toString().trim());
        info.levelWidth = et_djqk.getText().toString().trim();
        info.bridgeLength = Util.valueDouble(et_qc.getText().toString().trim());
        info.superstructure = et_sbxs.getText().toString().trim();
        info.substructure = et_xbxs.getText().toString().trim();
        info.crossRiverAngle = Util.valueLong(et_ysljj.getText().toString().trim());
        info.floodTime = Util.valueLong(et_fsnd.getText().toString().trim());
        info.floodElevation = Util.valueDouble(et_hsgc.getText().toString().trim());
        info.updateTime = Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S);
        info.updateUser = AppContext.sp.getLong(Config.SP.CURRENT_USER_ID);
        String relativePosition = tv_xqxdwz.getText().toString().trim();
        if (!TextUtils.isEmpty(relativePosition)) {
            info.relativePosition = DictUtil.getDictCodeByLabel(this, DictUtil.REFER_RELATIVE_POSITION, relativePosition);
        }

        String usedCondition = tv_syqk.getText().toString().trim();
        if (!TextUtils.isEmpty(usedCondition)) {
            info.usedCondition = DictUtil.getDictCodeByLabel(this, DictUtil.REFER_CONDITION, usedCondition);
        }

        String geology = tv_dzqk.getText().toString().trim();
        if (!TextUtils.isEmpty(geology)) {
            info.geology = DictUtil.getDictCodeByLabel(this, DictUtil.REFER_GEOLOGY, geology);
        }
        String sediment = tv_cyqk.getText().toString().trim();
        if (!TextUtils.isEmpty(sediment)) {
            info.sediment = DictUtil.getDictCodeByLabel(this, DictUtil.REFER_SEDIMENT, sediment);
        }
        String tag = getIntent().getStringExtra(FROM_TAG);
        DBUtil.useExtraDbByProjectName(this, getIntent().getStringExtra("projectName"));
        if (TAG_MODIFY.equals(tag)) {
            info.update(info.id);
        } else if (TAG_ADD.equals(tag)) {
            info.save();
        }
        setResult(RESULT_OK);
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_river_project);
        ButterKnife.bind(this);
        initView();
    }

    private ExistingRelatedRiverEngineeringSurveyInfo info;

    private void initView() {
        tv_title.setText("既有涉河工程调查");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("确定");
        info = (ExistingRelatedRiverEngineeringSurveyInfo) getIntent().getSerializableExtra(INTENT_DATA);
        et_hm.setText(info.placeName);
        et_kuaj.setText(Util.valueString(info.span));
        et_kongj.setText(Util.valueString(info.aperture));
        et_jznd.setText(Util.valueString(info.buildYear));
        et_djqk.setText(info.levelWidth);
        et_qc.setText(Util.valueString(info.bridgeLength));
        et_sbxs.setText(info.superstructure);
        et_xbxs.setText(info.substructure);
        et_ysljj.setText(Util.valueString(info.crossRiverAngle));
        et_fsnd.setText(Util.valueString(info.floodTime));
        et_hsgc.setText(Util.valueString(info.floodElevation));
        tv_xqxdwz.setText(DictUtil.getDictLabelByCode(this, DictUtil.REFER_RELATIVE_POSITION, info.relativePosition));
        tv_syqk.setText(DictUtil.getDictLabelByCode(this, DictUtil.REFER_CONDITION, info.usedCondition));
        tv_dzqk.setText(DictUtil.getDictLabelByCode(this, DictUtil.REFER_GEOLOGY, info.geology));
        tv_cyqk.setText(DictUtil.getDictLabelByCode(this, DictUtil.REFER_SEDIMENT, info.sediment));
    }
}
