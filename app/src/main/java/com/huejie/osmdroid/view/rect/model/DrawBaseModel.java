package com.huejie.osmdroid.view.rect.model;

import android.graphics.Paint;

import java.io.Serializable;

/**
 * Description 图形基础实体类
 */
public class DrawBaseModel implements Serializable {

    public static final int TPEY_LINE=0;
    public static final int TPEY_PATH=1;
    public static final int TPEY_CIRCLE=2;
    public static final int TPEY_RECT=3;

    private int type;//绘制类型 0直线 1轨迹 2圆 3矩形
    private int color;//画笔颜色
    private int paintSize;//画笔宽度
    private Paint.Style paintStyle;//画笔style

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPaintSize() {
        return paintSize;
    }

    public void setPaintSize(int paintSize) {
        this.paintSize = paintSize;
    }

    public Paint.Style getPaintStyle() {
        return paintStyle;
    }

    public void setPaintStyle(Paint.Style paintStyle) {
        this.paintStyle = paintStyle;
    }
}
