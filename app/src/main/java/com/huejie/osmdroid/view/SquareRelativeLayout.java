package com.huejie.osmdroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**正方形LinearLayout*/
public class SquareRelativeLayout extends RelativeLayout {

    public SquareRelativeLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    public SquareRelativeLayout(Context context, AttributeSet attrs) {
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
