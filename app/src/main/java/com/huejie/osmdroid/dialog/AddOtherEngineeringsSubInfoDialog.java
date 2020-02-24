package com.huejie.osmdroid.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.SimpleChooseModel;
import com.huejie.osmdroid.model.books.OtherEngineeringSubInfo;
import com.huejie.osmdroid.util.Util;

import java.util.List;


/**
 * Created by guchao on 2016/4/13.
 */
public class AddOtherEngineeringsSubInfoDialog extends BaseDialog {
    private Context context;

    private EditText et_jczh;
    private TextView tv_cancel;
    private TextView tv_title;
    private TextView tv_confirm;
    private String title;
    private EditText et_gygcmc;
    private EditText et_jcjd;

    private AddOtherEngineeringsSubInfoDialog(Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    private OtherEngineeringSubInfo subInfo;

    public void setData(OtherEngineeringSubInfo subInfo) {
        this.subInfo = subInfo;
    }


    private static AddOtherEngineeringsSubInfoDialog dialog;

    public static AddOtherEngineeringsSubInfoDialog getInstance(Context context) {
        if (null == dialog) {
            dialog = new AddOtherEngineeringsSubInfoDialog(context);
        }
        return dialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog = null;
    }

    private List<SimpleChooseModel> list;
    private long relocationType = -1;

    @Override
    public void onCreateDialog(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_other_engineerings_sub_info);
        et_jczh = findViewById(R.id.et_jczh);
        et_gygcmc = findViewById(R.id.et_gygcmc);
        et_jcjd = findViewById(R.id.et_jcjd);
        tv_title = findViewById(R.id.tv_title);
        tv_cancel = findViewById(R.id.btn_cancel);
        tv_confirm = findViewById(R.id.btn_confirm);
        tv_title.setText(title);
        if (null != subInfo) {
            et_jczh.setText(subInfo.crossStake);
            et_gygcmc.setText(subInfo.relocationName);
            et_jcjd.setText(Util.valueString(subInfo.crossAngle));
        }

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qdzh = et_jczh.getText().toString().trim();
                if (TextUtils.isEmpty(qdzh)) {
                    ToastUtils.showShortToast("交叉桩号不能为空");
                    return;
                }
                if (null != onClick) {
                    onClick.onConfirm(qdzh, et_gygcmc.getText().toString().trim(), Util.valueLong(et_jcjd.getText().toString().trim()));
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClick) {
                    onClick.onCancel();
                }
            }
        });


    }


    private OnClick onClick;

    public void setOnclick(OnClick onClick) {
        this.onClick = onClick;
    }

    /***/
    public static interface OnClick {
        /**
         * 打开操作
         */
        void onCancel();

        /**
         * 确定操作
         */
        void onConfirm(String startStake, String relocationName, long crossAngle);

    }


}
