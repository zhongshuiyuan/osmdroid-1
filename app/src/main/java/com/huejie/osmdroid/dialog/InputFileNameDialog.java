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
public class InputFileNameDialog extends BaseDialog {
    private Context context;

    private EditText et_name;

    private TextView tv_cancel;
    private TextView tv_title;
    private TextView tv_confirm;
    private String title;

    private InputFileNameDialog(Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    private static InputFileNameDialog dialog;

    public static InputFileNameDialog getInstance(Context context) {
        if (null == dialog) {
            dialog = new InputFileNameDialog(context);
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

    @Override
    public void onCreateDialog(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_input_filename);

        et_name = findViewById(R.id.et_name);
        tv_title = findViewById(R.id.tv_title);
        tv_cancel = findViewById(R.id.btn_cancel);
        tv_confirm = findViewById(R.id.btn_confirm);
        tv_title.setText(title);

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showShortToast("名称不能为空");
                    return;
                }
                if (null != onClick) {
                    onClick.onConfirm(name);
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
        void onConfirm(String name);


    }


}
