package com.huejie.osmdroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**正方形LinearLayout*/
public class SquareLinearLayout extends LinearLayout {

    public SquareLinearLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    public SquareLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, w, oldw, oldw);
    }
}
