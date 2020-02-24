package com.huejie.osmdroid.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.huejie.osmdroid.R;


/**
 * Created by guchao on 2016/4/13.
 */
public class LodingDialog extends BaseDialog {
    private static Context mContext;

    private TextView tv_tip;


    private LodingDialog(Context context) {
        super(context, R.style.add_dialog);
    }

    public static void init(Context context) {
        mContext = context;
    }

    private static LodingDialog dialog;

    public static LodingDialog getInstance() {
        if (null == dialog) {
            dialog = new LodingDialog(mContext);
        }
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog = null;
    }

    public void setTip(String tip) {
        tv_tip.setText(tip);
    }


    @Override
    public void onCreateDialog(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_loding);
        tv_tip = findViewById(R.id.tv_tip);
    }

}
