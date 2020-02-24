package com.huejie.osmdroid.view.rect.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.huejie.osmdroid.view.rect.model.CircleModel;
import com.huejie.osmdroid.view.rect.model.DrawBaseModel;
import com.huejie.osmdroid.view.rect.model.LineModel;
import com.huejie.osmdroid.view.rect.model.PathModel;
import com.huejie.osmdroid.view.rect.model.RectModel;
import com.huejie.osmdroid.view.rect.tools.MathHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义view，绘制
 */
public class DrawPicView extends View {

    //点击直线位置
    private enum Line {
        START, //开始
        CENTRE, //中心
        END  //结尾
    }

    //声明路径
    private Path path;
    //声明画笔
    private Paint paint;
    private PointF startPoint;
    private PointF endPoint;

    //记录所有画过的轨迹
    private List<List<PointF>> linePath = new ArrayList<>();
    //记录画过的所有的内容
    private List<DrawBaseModel> list = new ArrayList<>();
    //记录撤销过的所有的内容
    private List<DrawBaseModel> delectList = new ArrayList<>();
    //现在所画的图形是什么
    private DrawBaseModel drawBaseModel;
    //记录画过的所有的直线
    private List<LineModel> lineList = new ArrayList<>();
    //记录画过的所有的圆
    private List<CircleModel> circleList = new ArrayList<>();
    //记录画过的所有的矩形
    private List<RectModel> RectList = new ArrayList<>();
    //记录画过的所有的轨迹
    private List<DrawBaseModel> pathList = new ArrayList<>();
    //绘制图形的Type
    private int type;
    private boolean isPointUp = true;
    private int downState;
    private int moveState;
    private PointF cenPoint;
    private MotionEvent downEvent;
    private MotionEvent upEvent;
    private Line lineStatue;//点中直线位置

    private RectF lineRectf;//给直线一个范围，如果手指落入这个范围，则判定用户点击了直线
    private int precision = 50;//直线范围精度，值越大精度越差
    private int chickLine = -1;//用户是否点中了直线，点中直线则对直线进行操作，未点中则绘制图形，-1 未点中，正整数 点中第几条

    private Context context;

    public DrawPicView(Context context) {
        super(context);
        initView(context);
    }

    public DrawPicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        path = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        startPoint = new PointF();
        endPoint = new PointF();
        drawBaseModel = new DrawBaseModel();
        lineRectf = new RectF();
        type = DrawBaseModel.TPEY_PATH;

        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.save();
        //绘制之前的图形
        for (DrawBaseModel model : list) {
            drawGraph(canvas, model);
        }
        //绘制当前的图形
        if (!isPointUp) {
            drawGraph(canvas, drawBaseModel);
        }
        canvas.restore();
        Log.i("PaintView", "OnDraw");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        //获取拖动事件发生的位置
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastPoint.x = event.getX();
                lastPoint.y = event.getY();
                isClickLine(event);
                if (chickLine == -1)//没有点击到直线上，才去绘制图形
                {
                    down(event);
                }

                if (null != onStartAndCompletionListener) {
                    onStartAndCompletionListener.onStart(new PointF(event.getX(), event.getY()));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (chickLine == -1)//没有点击到直线上，才去绘制图形
                {
                    move(event);
                } else              //点击到直线上，对直线进行操作
                {
                    operateLine(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (chickLine == -1)//没有点击到直线上，才将图形加到列表中
                {
                    up(event);
                }

                if (null != onStartAndCompletionListener) {
                    onStartAndCompletionListener.onComplete(new PointF(event.getX(), event.getY()));
                }
                break;
        }
        //返回true表明处理方法已经处理该事件
        return true;
    }

    //判断是否点中直线
    private void isClickLine(MotionEvent event) {
        chickLine = -1;//每次点击重置点击位置,防止记录上传点击位置
        //先判断是否在精度范围内点中直线
        for (int i = 0; i < lineList.size(); i++) {
            LineModel model = lineList.get(i);
            //以按下点坐标为中心，如果点距离直线最小距离小于精度，则判断用户点到直线上
            if (precision >= MathHelper.getInstanse().pointToLine(model.getStartx(), model.getStarty()
                    , model.getEndx(), model.getEndy(), event.getX(), event.getY())) {
                //如果点击到了直线上，判断点击位置 如果点击坐标距离起点距离小于精度，则判断点击到起始位置
                //如果点击坐标距离结束点距离小于精度，则判断点击到结束位置 否则则判定点击到直线上
                if (MathHelper.getInstanse().lineSpace(model.getStartx(), model.getStarty()
                        , event.getX(), event.getY()) <= precision) {
                    lineStatue = Line.START;
                } else if (MathHelper.getInstanse().lineSpace(model.getEndx(), model.getEndy()
                        , event.getX(), event.getY()) <= precision) {
                    lineStatue = Line.END;
                } else {
                    lineStatue = Line.CENTRE;
                }
                chickLine = i;
                break;
            }
        }
    }

    private PointF lastPoint = new PointF();//记录上次点击位置

    //对直线进行操作
    private void operateLine(MotionEvent event) {
        drawBaseModel = lineList.get(chickLine);//将当前绘制model更改为要操作的直线
        type = DrawBaseModel.TPEY_LINE;//绘制类型更改为直线
        switch (lineStatue) {
            case START://点击到直线头部,更改起点
                ((LineModel) drawBaseModel).setStartx(event.getX());
                ((LineModel) drawBaseModel).setStarty(event.getY());
                break;
            case END://点击到直线尾部,更改结束点
                ((LineModel) drawBaseModel).setEndx(event.getX());
                ((LineModel) drawBaseModel).setEndy(event.getY());
                break;
            case CENTRE://点击到直线中间，移动直线
                //获取每次 x,y平移量
                float x = event.getX() - lastPoint.x;
                float y = event.getY() - lastPoint.y;
                //直线起点终点平移，使直线平移
                ((LineModel) drawBaseModel).setStartx(((LineModel) drawBaseModel).getStartx() + x);
                ((LineModel) drawBaseModel).setStarty(((LineModel) drawBaseModel).getStarty() + y);
                ((LineModel) drawBaseModel).setEndx(((LineModel) drawBaseModel).getEndx() + x);
                ((LineModel) drawBaseModel).setEndy(((LineModel) drawBaseModel).getEndy() + y);
                lastPoint.x = event.getX();
                lastPoint.y = event.getY();
                break;
        }
        postInvalidate();
    }

    //手指按下操作
    private void down(MotionEvent event) {
        isPointUp = false;
        switch (type) {
            case DrawBaseModel.TPEY_LINE:
//                LineIsDownOn(event);
                drawBaseModel = new LineModel();
                ((LineModel) drawBaseModel).setStartx(event.getX());
                ((LineModel) drawBaseModel).setStarty(event.getY());
                break;
            case DrawBaseModel.TPEY_PATH:
                drawBaseModel = new PathModel();
                ((PathModel) drawBaseModel).startPath(event.getX(), event.getY());
                break;
            case DrawBaseModel.TPEY_CIRCLE:
                drawBaseModel = new CircleModel();
                ((CircleModel) drawBaseModel).setCenterx(event.getX());
                ((CircleModel) drawBaseModel).setCentery(event.getY());
                break;
            case DrawBaseModel.TPEY_RECT:
                drawBaseModel = new RectModel();
                ((RectModel) drawBaseModel).startRectF(event.getX(), event.getY());
                break;
        }
        drawBaseModel.setType(type);
        drawBaseModel.setColor(Color.RED);
        Log.i("PaintView", "down");
    }


    //判断是否点在了直线上
    private void LineIsDownOn(MotionEvent event) {
        downEvent = event;
        for (int i = 0; i < lineList.size(); i++) {
            startPoint.x = lineList.get(i).getStartx();
            startPoint.y = lineList.get(i).getStarty();
            endPoint.x = lineList.get(i).getEndx();
            endPoint.y = lineList.get(i).getEndy();
            //如果点击起点
            if (startPoint.equals(event)) {
                downState = 1;
                break;
                //如果点击终点
            } else if (endPoint.equals(event)) {
                Log.i("onTouchDown", "downState = 2");
                downState = 2;
                break;
                //如果在直线上
            } else if (panduan(event)) {
                Log.i("onTouchDown", "downState = 3");
                downState = 3;
                break;
                //在直线外
            } else {
                downState = 0;
            }
        }
    }

    private boolean panduan(MotionEvent event) {
        double lDis = Math.sqrt((startPoint.x - endPoint.x)
                * (startPoint.x - endPoint.x) + (startPoint.y - endPoint.y)
                * (startPoint.y - endPoint.y));

        double lDis1 = Math.sqrt((event.getX() - startPoint.x) * (event.getX() - startPoint.x)
                + (event.getY() - startPoint.y) * (event.getY() - startPoint.y));
        double lDis2 = Math.sqrt((event.getX() - endPoint.x) * (event.getX() - endPoint.x)
                + (event.getY() - endPoint.y) * (event.getY() - endPoint.y));


        if (lDis1 + lDis2 >= lDis + 0.00f && lDis1 + lDis2 <= lDis + 5.00f) {
            return true;
        } else {
            return false;
        }
    }

    //手指移动时的操作
    private void move(MotionEvent event) {
        switch (type) {
            case DrawBaseModel.TPEY_LINE:
                ((LineModel) drawBaseModel).setEndx(event.getX());
                ((LineModel) drawBaseModel).setEndy(event.getY());
//                IsChangePoint(downState,event);
                break;
            case DrawBaseModel.TPEY_PATH:
                ((PathModel) drawBaseModel).movePath(event.getX(), event.getY());
                break;
            case DrawBaseModel.TPEY_CIRCLE:
                float x = ((CircleModel) drawBaseModel).getCenterx();
                float y = ((CircleModel) drawBaseModel).getCentery();
                ((CircleModel) drawBaseModel).setRadis((float) Math.sqrt(
                        (x - event.getX()) * (x - event.getX())
                                + (y - event.getY()) * (y - event.getY())));
                ((CircleModel) drawBaseModel).setMovex(event.getX());
                ((CircleModel) drawBaseModel).setMovey(event.getY());
                break;
            case DrawBaseModel.TPEY_RECT:
                ((RectModel) drawBaseModel).toRectF(event.getX(), event.getY());
                break;
        }
        Log.i("PaintView", "move");
        postInvalidate();
    }

    //手指抬起的操作
    private void up(MotionEvent event) {
        move(event);
        switch (type) {
            case DrawBaseModel.TPEY_LINE:
                lineList.add((LineModel) drawBaseModel);
                break;
            case DrawBaseModel.TPEY_CIRCLE:
                circleList.add((CircleModel) drawBaseModel);
                break;
            case DrawBaseModel.TPEY_PATH:
                pathList.add(drawBaseModel);
                break;
            case DrawBaseModel.TPEY_RECT:
                RectList.add((RectModel) drawBaseModel);
                break;

            default:
                break;
        }
        list.add(drawBaseModel);
        isPointUp = true;
        Log.i("PaintView", "up");
    }


    //绘制图形
    private void drawGraph(Canvas canvas, DrawBaseModel model) {
        paint.setColor(model.getColor());

        switch (model.getType()) {
            case DrawBaseModel.TPEY_LINE:
                drawLine(canvas, (LineModel) model);
                break;
            case DrawBaseModel.TPEY_PATH:
                drawPath(canvas, (PathModel) model);
                break;
            case DrawBaseModel.TPEY_CIRCLE:
                drawCircle(canvas, (CircleModel) model);
                break;
            case DrawBaseModel.TPEY_RECT:
                drawRectangle(canvas, (RectModel) model);
                break;
        }

    }

    //画直线
    private void drawLine(Canvas canvas, LineModel model) {

        //对直线进行操作时，不再绘制该直线
        /*if (chickLine!=-1&&model.equals((LineModel)drawBaseModel)){
            return;
        }*/
        canvas.drawLine(model.getStartx(), model.getStarty()
                , model.getEndx(), model.getEndy(), paint);
    }

    //画圆
    private void drawCircle(Canvas canvas, CircleModel model) {
        float x = model.getCenterx() - (model.getCenterx() - model.getMovex()) / 2;
        float y = model.getCentery() - (model.getCentery() - model.getMovey()) / 2;
        canvas.drawCircle(x, y, model.getRadis() / 2, paint);
    }

    //画矩形
    private void drawRectangle(Canvas canvas, RectModel model) {
        // TODO Auto-generated method stub
        float left = Math.min(model.getLeft(), model.getRight());
        float right = Math.max(model.getLeft(), model.getRight());
        float top = Math.min(model.getTop(), model.getBottom());
        float bottom = Math.max(model.getTop(), model.getBottom());
        canvas.drawRect(left, top, right, bottom, paint);

    }

    //画轨迹
    private void drawPath(Canvas canvas, PathModel model) {
        canvas.drawPath(model.getPath(), paint);
    }

    //清空
    public void clean() {
        path.reset();
        list.clear();
        delectList.clear();
//        mediaType = drawBaseModel.TPEY_CIRCLE;
        postInvalidate();
    }

    //撤销
    public void cancel() {
        if (list.size() > 0) {
            DrawBaseModel delect = list.get(list.size() - 1);
            list.remove(delect);
            delectList.add(delect);
            postInvalidate();
        } else {
            Toast.makeText(context, "无内容可撤销！", Toast.LENGTH_SHORT).show();
        }

    }

    //设置绘制图形类型
    public void setType(int type) {
        this.type = type;
    }

    //还原
    public void restore() {
        // TODO Auto-generated method stub
        if (delectList.size() > 0) {
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawBaseModel dp = delectList.get(delectList.size() - 1);
            list.add(dp);
            delectList.remove(delectList.size() - 1);
            invalidate();
        } else {
            Toast.makeText(context, "无内容可还原！", Toast.LENGTH_SHORT).show();
        }
    }

    private OnStartAndCompletionListener onStartAndCompletionListener;

    public void setOnStartAndCompletionListener(OnStartAndCompletionListener onStartAndCompletionListener) {
        this.onStartAndCompletionListener = onStartAndCompletionListener;
    }

    public static interface OnStartAndCompletionListener {
        void onComplete(PointF pointF);

        void onStart(PointF pointF);

    }

}
