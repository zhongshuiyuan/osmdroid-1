package com.huejie.osmdroid.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.adapter.PopListAdapter;
import com.huejie.osmdroid.model.SimpleChooseModel;
import com.huejie.osmdroid.util.DictUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guchao on 2016/4/13.
 */
public class AddExistingBridgeAndCulvertDialog extends BaseDialog {
    private Context context;

    private EditText et_xzxzh;
    private EditText et_jgwmc;

    private TextView tv_cancel;
    private TextView tv_title;
    private TextView tv_confirm;
    private TextView tv_choose;
    private View rl_choose;
    private String title;
    private boolean showType;


    private AddExistingBridgeAndCulvertDialog(Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    public void showChooseBookType(boolean showType) {
        this.showType = showType;
    }

    private static AddExistingBridgeAndCulvertDialog dialog;

    public static AddExistingBridgeAndCulvertDialog getInstance(Context context) {
        if (null == dialog) {
            dialog = new AddExistingBridgeAndCulvertDialog(context);
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
        setContentView(R.layout.dialog_add_existing_bridge_culvert);
        et_xzxzh = findViewById(R.id.et_xzxzh);
        et_jgwmc = findViewById(R.id.et_jgwmc);
        tv_title = findViewById(R.id.tv_title);
        tv_cancel = findViewById(R.id.btn_cancel);
        tv_confirm = findViewById(R.id.btn_confirm);
        rl_choose = findViewById(R.id.rl_choose);
        tv_choose = findViewById(R.id.tv_choose);
        tv_title.setText(title);
        tv_choose.setText(recordBookName);

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jczh = et_xzxzh.getText().toString().trim();
                if (TextUtils.isEmpty(jczh)) {
                    ToastUtils.showShortToast("现中心桩号不能为空");
                    return;
                }
                String jgwmc = et_jgwmc.getText().toString().trim();
                if (TextUtils.isEmpty(jgwmc)) {
                    ToastUtils.showShortToast("结构物名称不能为空");
                    return;
                }
                if (null != onClick) {
                    onClick.onConfirm(jczh, jgwmc);
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
