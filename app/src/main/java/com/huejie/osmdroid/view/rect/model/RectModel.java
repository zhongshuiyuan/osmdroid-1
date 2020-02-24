package com.huejie.osmdroid.view.rect.model;

/**
 * Description 绘制矩形实体类
 */
public class RectModel extends DrawBaseModel {

    private float left,top,right,bottom ;

    public void startRectF(float x,float y){
        left=x;
        top=y;
    }

    public void toRectF(float x,float y){
        right=x;
        bottom=y;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }
}
