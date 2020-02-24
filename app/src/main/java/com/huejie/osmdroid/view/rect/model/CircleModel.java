package com.huejie.osmdroid.view.rect.model;

/**
 * Description 绘制圆实体类
 */
public class CircleModel extends DrawBaseModel {

    private float centerx;
    private float centery;
    private float radis;
    private float movex;
    private float movey;

    public float getCenterx() {
        return centerx;
    }

    public void setCenterx(float centerx) {
        this.centerx = centerx;
    }

    public float getCentery() {
        return centery;
    }

    public void setCentery(float centery) {
        this.centery = centery;
    }

    public float getRadis() {
        return radis;
    }

    public void setRadis(float radis) {
        this.radis = radis;
    }

    public float getMovex() {
        return movex;
    }

    public void setMovex(float movex) {
        this.movex = movex;
    }

    public float getMovey() {
        return movey;
    }

    public void setMovey(float movey) {
        this.movey = movey;
    }
}
