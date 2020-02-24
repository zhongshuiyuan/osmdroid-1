package com.huejie.osmdroid.project.recordbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.model.books.SmallBridgeCulvertFormInfo;
import com.huejie.osmdroid.util.DictUtil;
import com.huejie.osmdroid.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huejie.osmdroid.project.recordbook.bookfragment.SmallBridgeAndCulvertRecordBookFragment.SMALL_BRIDGE_CULVERT_FORM_INFO;

/**
 * @author: TongWeiJie
 * @date: 2019/6/24 11:49
 * @description: 小桥涵-初拟
 */
public class SmallBridgeCulvertInitialActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    RadioButton tvRight;
    @BindView(R.id.tv_hd)
    CheckedTextView tvHd;
    @BindView(R.id.et_jgxsxq)
    EditText etJgxsxq;//结构形式-小桥

    public static final int RESULT_CODE_INITIAL = 200;
    private SmallBridgeCulvertFormInfo mSmallBridgeCulvertFormInfo;

    @OnClick(R.id.tv_hd)
    public void hd() {
        Util.showPopwindow(this, tvHd, DictUtil.getDictLabels(this, DictUtil.INIT_IMPORT_EXPORT_FORM), "");
    }


    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void right() {
        String jgxsxq = etJgxsxq.getText().toString().trim();
        String hd = tvHd.getText().toString().trim();
        long sort = DictUtil.getDictCodeByLabel(this, DictUtil.INIT_IMPORT_EXPORT_FORM, hd);

        if (TextUtils.isEmpty(hd)) {
            ToastUtils.showShortToast("进出口形式不能为空");
            return;
        }
        if (TextUtils.isEmpty(jgxsxq)) {
            ToastUtils.showShortToast("基础结构形式不能为空");
            return;
        }
        Intent intent = new Intent();
        if(mSmallBridgeCulvertFormInfo!=null){
            mSmallBridgeCulvertFormInfo.initImportExportForm = sort;
            mSmallBridgeCulvertFormInfo.initSubstructureInfrastructureForm = jgxsxq;
            mSmallBridgeCulvertFormInfo.updateTime = System.currentTimeMillis();
            intent.putExtra(SMALL_BRIDGE_CULVERT_FORM_INFO, mSmallBridgeCulvertFormInfo);
        }else{
            SmallBridgeCulvertFormInfo smallBridgeCulvertFormInfo = new SmallBridgeCulvertFormInfo();
            smallBridgeCulvertFormInfo.initImportExportForm = sort;
            smallBridgeCulvertFormInfo.initSubstructureInfrastructureForm = jgxsxq;
            smallBridgeCulvertFormInfo.updateTime = System.currentTimeMillis();
            intent.putExtra(SMALL_BRIDGE_CULVERT_FORM_INFO, smallBridgeCulvertFormInfo);
        }
        setResult(RESULT_CODE_INITIAL, intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_birdge_culvert_initial);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("保存");

        mSmallBridgeCulvertFormInfo = (SmallBridgeCulvertFormInfo) getIntent().getSerializableExtra(SMALL_BRIDGE_CULVERT_FORM_INFO);
        if(mSmallBridgeCulvertFormInfo !=null){
            tvTitle.setText("修改初拟");
            String hd = DictUtil.getDictLabelByCode(this, DictUtil.INIT_IMPORT_EXPORT_FORM, mSmallBridgeCulvertFormInfo.initImportExportForm);
            tvHd.setText(hd);
            etJgxsxq.setText(mSmallBridgeCulvertFormInfo.initSubstructureInfrastructureForm);
        }else{
            tvTitle.setText("新增初拟");
        }
    }

}
