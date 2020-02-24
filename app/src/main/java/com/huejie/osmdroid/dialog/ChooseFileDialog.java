package com.huejie.osmdroid.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.huejie.osmdroid.R;


/**
 * Created by guchao on 2016/4/13.
 */
public class ChooseFileDialog extends BaseDialog {
    private static Context mContext;

    private ChooseFileDialog(Context context) {
        super(context, R.style.add_dialog);
    }

    public static void init(Context context) {
        mContext = context;
    }

    private static ChooseFileDialog dialog;

    public static ChooseFileDialog getInstance() {
        if (null == dialog) {
            dialog = new ChooseFileDialog(mContext);
        }
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog = null;
    }


    @Override
    public void onCreateDialog(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_choose_fj);
        findViewById(R.id.btn_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClick) {
                    onClick.fromFile();
                }
            }
        });
        findViewById(R.id.btn_fjk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClick) {
                    onClick.fromFjLib();
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
        void fromFile();

        /**
         * 确定操作
         */
        void fromFjLib();


    }

}
