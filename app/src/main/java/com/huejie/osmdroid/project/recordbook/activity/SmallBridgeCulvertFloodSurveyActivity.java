package com.huejie.osmdroid.project.recordbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.model.books.SmallBridgeCulvertFloodSurvey;
import com.huejie.osmdroid.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huejie.osmdroid.project.recordbook.bookfragment.SmallBridgeAndCulvertRecordBookFragment.SMALL_BRIDGE_CULVERT_FLOOD_SURVEY;

/**
 * @author: TongWeiJie
 * @date: 2019/6/24 17:10
 * @description: 小桥涵-历史洪水位
 */
public class SmallBridgeCulvertFloodSurveyActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    RadioButton tvRight;
    @BindView(R.id.et_nf)
    EditText etNf;
    @BindView(R.id.et_bg)
    EditText etBg;
    @BindView(R.id.et_wz)
    EditText etWz;
    @BindView(R.id.et_zq)
    EditText etZq;

    public static final int RESULT_CODE_HISTORY_FLOOD = 201;
    private SmallBridgeCulvertFloodSurvey mSmallBridgeCulvertFloodSurvey;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void right() {

        String nf = etNf.getText().toString().trim();
        String bg = etBg.getText().toString().trim();
        String wz = etWz.getText().toString().trim();
        String zq = etZq.getText().toString().trim();

        if (TextUtils.isEmpty(nf)) {
            ToastUtils.showShortToast("年份不能为空");
            return;
        }
        if (TextUtils.isEmpty(bg)) {
            ToastUtils.showShortToast("标高不能为空");
            return;
        }
        if (TextUtils.isEmpty(wz)) {
            ToastUtils.showShortToast("位置不能为空");
            return;
        }
        if (TextUtils.isEmpty(zq)) {
            ToastUtils.showShortToast("周期不能为空");
            return;
        }

        Intent intent = new Intent();
        if(mSmallBridgeCulvertFloodSurvey!=null){
            mSmallBridgeCulvertFloodSurvey.historicalFloodYear = Util.valueLong(nf);
            mSmallBridgeCulvertFloodSurvey.historicalFloodElevation = Util.valueDouble(bg);
            mSmallBridgeCulvertFloodSurvey.historicalFloodPosition = Util.valueDouble(wz);
            mSmallBridgeCulvertFloodSurvey.historicalFloodPeriod = Util.valueDouble(zq);

            mSmallBridgeCulvertFloodSurvey.updateTime = System.currentTimeMillis();
            intent.putExtra(SMALL_BRIDGE_CULVERT_FLOOD_SURVEY, mSmallBridgeCulvertFloodSurvey);
        }else{
            SmallBridgeCulvertFloodSurvey smallBridgeCulvertFormInfo = new SmallBridgeCulvertFloodSurvey();
            smallBridgeCulvertFormInfo.historicalFloodYear = Util.valueLong(nf);
            smallBridgeCulvertFormInfo.historicalFloodElevation = Util.valueDouble(bg);
            smallBridgeCulvertFormInfo.historicalFloodPosition = Util.valueDouble(wz);
            smallBridgeCulvertFormInfo.historicalFloodPeriod = Util.valueDouble(zq);
            smallBridgeCulvertFormInfo.updateTime = System.currentTimeMillis();
            intent.putExtra(SMALL_BRIDGE_CULVERT_FLOOD_SURVEY, smallBridgeCulvertFormInfo);
        }
        setResult(RESULT_CODE_HISTORY_FLOOD, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_birdge_culvert_flood_survey);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("保存");

        mSmallBridgeCulvertFloodSurvey = (SmallBridgeCulvertFloodSurvey) getIntent().getSerializableExtra(SMALL_BRIDGE_CULVERT_FLOOD_SURVEY);
        if(mSmallBridgeCulvertFloodSurvey !=null){
            tvTitle.setText("修改历史洪水位");
            etNf.setText(Util.valueString(mSmallBridgeCulvertFloodSurvey.historicalFloodYear));
            etBg.setText(Util.valueString(mSmallBridgeCulvertFloodSurvey.historicalFloodElevation));
            etWz.setText(Util.valueString(mSmallBridgeCulvertFloodSurvey.historicalFloodPosition));
            etZq.setText(Util.valueString(mSmallBridgeCulvertFloodSurvey.historicalFloodPeriod));
        }else{
            tvTitle.setText("新增历史洪水位");
        }
    }

}
