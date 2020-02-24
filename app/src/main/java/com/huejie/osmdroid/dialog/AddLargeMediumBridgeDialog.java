package com.huejie.osmdroid.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;


/**
 * Created by guchao on 2016/4/13.
 */
public class AddLargeMediumBridgeDialog extends BaseDialog {
    private Context context;

    private EditText et_jczh;
    private EditText et_name;


    private TextView tv_cancel;
    private TextView tv_title;
    private TextView tv_confirm;
    private TextView tv_choose;
    private View rl_choose;
    private String title;
    private boolean showType;


    private AddLargeMediumBridgeDialog(Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    public void showChooseBookType(boolean showType) {
        this.showType = showType;
    }

    private static AddLargeMediumBridgeDialog dialog;

    public static AddLargeMediumBridgeDialog getInstance(Context context) {
        if (null == dialog) {
            dialog = new AddLargeMediumBridgeDialog(context);
        }
        return dialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String recordBookName;

    public void setChoose(String recordBookName) {
        this.recordBookName = recordBookName;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog = null;
    }

    @Override
    public void onCreateDialog(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_large_medium_bridge);
        et_jczh = findViewById(R.id.et_jczh);
        tv_title = findViewById(R.id.tv_title);
        et_name = findViewById(R.id.et_name);
        tv_cancel = findViewById(R.id.btn_cancel);
        tv_confirm = findViewById(R.id.btn_confirm);
        rl_choose = findViewById(R.id.rl_choose);
        tv_choose = findViewById(R.id.tv_choose);
        tv_title.setText(title);
        tv_choose.setText(recordBookName);

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jczh = et_jczh.getText().toString().trim();
                if (TextUtils.isEmpty(jczh)) {
                    ToastUtils.showShortToast("桥梁中心桩号不能为空");
                    return;
                }
                String name = et_name.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showShortToast("桥梁名称不能为空");
                    return;
                }
                if (null != onClick) {
                    onClick.onConfirm(jczh, name);
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
        if (showType) {
            rl_choose.setVisibility(View.VISIBLE);
            rl_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClick) {
                        onClick.onChoose();
                    }
                }
            });
        } else {
            rl_choose.setVisibility(View.GONE);
        }


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
        void onConfirm(String crossStake, String name);

        void onChoose();


    }


}
