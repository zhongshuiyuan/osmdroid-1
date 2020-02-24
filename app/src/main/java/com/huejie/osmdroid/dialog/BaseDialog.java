package com.huejie.osmdroid.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.huejie.osmdroid.R;


/**
 * Created by guchao on 2016/4/13.
 */
public abstract class BaseDialog extends Dialog {
    private Context context;

    public BaseDialog(Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    /**
     * 设置对话宽度是否包含内容
     */
    private boolean wrapContent = false;

    public void setWrapContent(boolean wrapContent) {
        this.wrapContent = wrapContent;
    }

    private int gravity = Gravity.CENTER;
    private float marginWidth = 0.85f;

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setMarginWidth(float marginWidth) {
        this.marginWidth = marginWidth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDialog(savedInstanceState);
        Window dialogWindow = getWindow();
        //  WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(gravity);
        if (!wrapContent) {
            WindowManager m = ((Activity) context).getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            //  p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
            p.width = (int) (d.getWidth() * marginWidth); // 宽度设置为屏幕的0.85
            dialogWindow.setAttributes(p);
        }


    }

    public abstract void onCreateDialog(Bundle savedInstanceState);

}
