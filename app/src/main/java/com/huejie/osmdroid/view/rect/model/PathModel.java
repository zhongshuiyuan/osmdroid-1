package com.huejie.osmdroid.view.rect.model;

import android.graphics.Path;

/**
 * Description 绘制轨迹实体类
 */
public class PathModel extends DrawBaseModel {

    private Path path;

    public PathModel(){
        this.path=new Path();
    }

    public Path getPath() {
        return path;
    }

    public void startPath(float x,float y){
        path.reset();
        path.moveTo(x,y);
    }

    public void movePath(float x,float y){
        path.lineTo(x,y);
    }
}
