package com.huejie.osmdroid.project.recordbook.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.model.books.FloodLevelRecordSurveyInfo;
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

public class FloodLevelSurveyRecordsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_age)
    EditText et_age;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_address_detail)
    EditText et_address_detail;
    @BindView(R.id.et_fsny)
    EditText et_fsny;
    @BindView(R.id.et_gaocheng)
    EditText et_gaocheng;

    private FloodLevelRecordSurveyInfo info;
    @BindView(R.id.tv_kkd)
    CheckedTextView tv_kkd;

    //可靠度
    @OnClick(R.id.tv_kkd)
    public void kkd() {
        Util.showPopwindow(this, tv_kkd, DictUtil.getDictLabels(this, DictUtil.PERSON_RELIABILITY), "");
    }


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void confirm() {
        info.personName = et_name.getText().toString().trim();
        info.personAge = Util.valueLong(et_age.getText().toString().trim());
        String personReliability = tv_kkd.getText().toString().trim();
        if (!TextUtils.isEmpty(personReliability)) {
            info.personReliability = DictUtil.getDictCodeByLabel(this, DictUtil.PERSON_RELIABILITY, personReliability);
        }
        info.detailRecord = et_address_detail.getText().toString().trim();
        info.floodElevation = Util.valueDouble(et_gaocheng.getText().toString().trim());
        info.personAddress = et_address.getText().toString().trim();
        info.floodYear = Util.valueLong(et_fsny.getText().toString().trim());
        info.updateTime = Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S);
        info.updateUser = AppContext.sp.getLong(Config.SP.CURRENT_USER_ID);
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
        setContentView(R.layout.activity_flood_level_survey_records);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("洪水位调查记录");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("确定");
        info = (FloodLevelRecordSurveyInfo) getIntent().getSerializableExtra(INTENT_DATA);
        et_name.setText(info.personName);
        et_age.setText(Util.valueString(info.personAge));
        et_address.setText(info.personAddress);
        et_address_detail.setText(info.detailRecord);
        et_fsny.setText(Util.valueString(info.floodYear));
        et_gaocheng.setText(Util.valueString(info.floodElevation));
//        et_fwdcqk.setText(info.d);
//        et_clsj.setText(info.personName);

    }
}
