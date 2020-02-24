package com.huejie.osmdroid.view.rect.model;

/**
 * Description 绘制直线实体类
 */
public class LineModel extends DrawBaseModel {

    private float startx;
    private float starty;
    private float endx;
    private float endy;

    public float getStartx() {
        return startx;
    }

    public void setStartx(float startx) {
        this.startx = startx;
    }

    public float getStarty() {
        return starty;
    }

    public void setStarty(float starty) {
        this.starty = starty;
    }

    public float getEndx() {
        return endx;
    }

    public void setEndx(float endx) {
        this.endx = endx;
    }

    public float getEndy() {
        return endy;
    }

    public void setEndy(float endy) {
        this.endy = endy;
    }

}
