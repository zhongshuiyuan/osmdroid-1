package com.huejie.osmdroid.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huejie.osmdroid.R;


/**
 * Created by guchao on 2016/4/13.
 */
public class DownloadSettingDialog extends BaseDialog {
    private Context context;

    private TextView tv_total;
    private TextView tv_cache;
    private TextView tv_left;
    private TextView tv_cancel;
    private TextView tv_confirm;

    private DownloadSettingDialog(Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    private static DownloadSettingDialog dialog;

    public static DownloadSettingDialog getInstance(Context context) {
        if (null == dialog) {
            dialog = new DownloadSettingDialog(context);
        }
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog = null;
    }

    private String total;
    private String cache;
    private String left;

    public DownloadSettingDialog setSeekBarProgress(String total,  String left) {

        this.total = total;

        this.left = left;
        return dialog;
    }


    @Override
    public void onCreateDialog(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_download_setting);

        tv_total = findViewById(R.id.tv_total);
        tv_cache = findViewById(R.id.tv_cache);
        tv_left = findViewById(R.id.tv_left);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);

        tv_total.setText(total);
//        tv_cache.setText(cache);
        tv_left.setText(left);

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClick) {
                    onClick.onConfirm();
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

    private int currentZoomLevel;

    public int getCurrentZoomLevel() {
        return currentZoomLevel;
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
        void onConfirm();


    }


}
