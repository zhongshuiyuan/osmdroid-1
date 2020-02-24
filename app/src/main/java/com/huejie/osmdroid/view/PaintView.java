package com.huejie.osmdroid.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.utils.ImageUtils;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public class PaintView extends View {

    private Context mContext;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private Paint mPaint;

    private ArrayList<DrawPath> savePath;
    private ArrayList<DrawPath> deletePath;
    private DrawPath dp;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private int bitmapWidth;
    private int bitmapHeight;

    private int currentColor = Color.BLACK;
    private int currentSize = 10;
    private EnumPaintStyle currentStyle = EnumPaintStyle.BRUSH;

    private String mFilePath;

    private int[] paintSize = {5, 10, 15};
    private int[] paintColor = {Color.BLACK, Color.BLUE, Color.RED, Color.GREEN};

    public static enum EnumPaintSize {
        SMALL, MIDDLE, BIG;
    }

    public static enum EnumPaintStyle {
        BRUSH, ERASER;
    }

    public static enum EnumPaintColor {
        BLACK, BLUE, RED, GREEN;
    }


    public PaintView(Context c, String path) {
        super(c);
        mContext = c;
        //得到屏幕的分辨率
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);

        bitmapWidth = dm.widthPixels;
        bitmapHeight = dm.heightPixels - 2 * 45;

        init(path);
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();

    }

    public PaintView(Context c, AttributeSet attrs) {
        super(c, attrs);
        mContext = c;
        //得到屏幕的分辨率
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);

        bitmapWidth = dm.widthPixels;
        bitmapHeight = dm.heightPixels - 2 * 45;

        //init();
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }

    public int calculateInSampleSize(BitmapFactory.Options op, int reqWidth,
                                     int reqheight) {
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;
        int inSampleSize = 1;
        if (originalWidth > reqWidth || originalHeight > reqheight) {
            int halfWidth = originalWidth / 2;
            int halfHeight = originalHeight / 2;
            while ((halfWidth / inSampleSize > reqWidth)
                    && (halfHeight / inSampleSize > reqheight)) {
                inSampleSize *= 2;

            }
        }
        return inSampleSize;
    }

    public void init(String path) {
        mFilePath = path;
        initCanvas();
    }

    //初始化画布
    public void initCanvas() {
        setPaintStyle();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        if (TextUtils.isEmpty(mFilePath)) {
            mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
            mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
            mCanvas.drawColor(Color.WHITE);
        } else {
            mBitmap = BitmapFactory.decodeFile(mFilePath).copy(Bitmap.Config.RGB_565, true);     //经过网上的搜索说是不允许直接修改res里面的图片，只要在后面加上.copy(Bitmap.Config.ARGB_8888, true); 不然会报错
            mBitmap = resizeBitmap(mBitmap, bitmapWidth, bitmapHeight);
            mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中

        }

//        Bitmap bitmap = BitmapFactory.decodeResource(rs, R.drawable.a2,options);

        //画布大小
        //mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight,
        //        Bitmap.Config.RGB_565);
//        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中

//        mCanvas.drawColor(Color.WHITE);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = w;
            int newHeight = h;
            float scaleWight = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWight, scaleHeight);
            Bitmap res = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return res;
        } else {
            return null;
        }
    }

    //设置画笔样式
    public void setPaintStyle() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(currentSize);
        if (currentStyle == EnumPaintStyle.BRUSH) {
            mPaint.setColor(currentColor);
        } else {
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(20);
        }
    }

    //设置画笔样式
    public void selectPaintStyle(int which) {

        if (which == 0) {
            currentStyle = EnumPaintStyle.BRUSH;
            setPaintStyle();
        }
        //当选择的是橡皮擦时，设置颜色为白色
        if (which == 1) {
            currentStyle = EnumPaintStyle.ERASER;
            setPaintStyle();
            mPaint.setStrokeWidth(20);
        }
    }

    //获取画板颜色
    public EnumPaintStyle getPaintStyle() {
        EnumPaintStyle paintStyle = EnumPaintStyle.BRUSH;
        if (currentStyle == EnumPaintStyle.BRUSH) {
            paintStyle = EnumPaintStyle.BRUSH;
        } else {
            paintStyle = EnumPaintStyle.ERASER;
        }
        return paintStyle;
    }

    //选择画笔大小
    public void selectPaintSize(int which) {
        int size = paintSize[which];
        currentSize = size;
        setPaintStyle();
    }

    //获取画板大小
    public EnumPaintSize getPaintSize() {
        EnumPaintSize paintSiee = EnumPaintSize.SMALL;
        switch (currentSize) {
            case 5:
                paintSiee = EnumPaintSize.SMALL;
                break;
            case 10:
                paintSiee = EnumPaintSize.MIDDLE;
                break;
            case 15:
                paintSiee = EnumPaintSize.BIG;
                break;
        }
        return paintSiee;
    }

    //设置画笔颜色
    public void selectPaintColor(int which) {

        currentColor = paintColor[which];
        setPaintStyle();
    }

    //获取画板颜色
    public EnumPaintColor getPaintColor() {
        EnumPaintColor paintColor = EnumPaintColor.BLACK;
        switch (currentColor) {
            case Color.BLACK:
                paintColor = EnumPaintColor.BLACK;
                break;
            case Color.BLUE:
                paintColor = EnumPaintColor.BLUE;
                break;
            case Color.RED:
                paintColor = EnumPaintColor.RED;
                break;
            case Color.GREEN:
                paintColor = EnumPaintColor.GREEN;
                break;
        }
        return paintColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);     //显示旧的画布
        if (mPath != null) {
            // 实时的显示
            canvas.drawPath(mPath, mPaint);
        }
//        //移动时，显示画笔图标
//        if(this.isMoving && currentColor != Color.WHITE){
//            //设置画笔的图标
//            Bitmap pen = BitmapFactory.decodeResource(this.getResources(),
//                    R.drawable.pen);
//            canvas.drawBitmap(pen, this.mX, this.mY - pen.getHeight(),
//                    new Paint(Paint.DITHER_FLAG));
//        }
    }

    //路径对象
    class DrawPath {
        Path path;
        Paint paint;
    }

    /**
     * 撤销的核心思想就是将画布清空，
     * 将保存下来的Path路径最后一个移除掉，
     * 重新将路径画在画布上面。
     */
    public void undo() {
        if (savePath != null && savePath.size() > 0) {
            //调用初始化画布函数以清空画布
            initCanvas();

            //将路径保存列表中的最后一个元素删除 ,并将其保存在路径删除列表中
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);

            //将路径保存列表中的路径重绘在画布上
            Iterator<DrawPath> iter = savePath.iterator();        //重复保存
            while (iter.hasNext()) {
                DrawPath dp = iter.next();
                mCanvas.drawPath(dp.path, dp.paint);

            }
            invalidate();// 刷新
            mOnMoveLitener.onMove(savePath.size(), deletePath.size());
        }
    }

    /**
     * 恢复的核心思想就是将撤销的路径保存到另外一个列表里面(栈)，
     * 然后从redo的列表里面取出最顶端对象，
     * 画在画布上面即可
     */
    public void redo() {
        if (deletePath.size() > 0) {
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            //将取出的路径重绘在画布上
            mCanvas.drawPath(dp.path, dp.paint);
            //将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);
            invalidate();
            mOnMoveLitener.onMove(savePath.size(), deletePath.size());
        }
    }

    /*
     * 清空的主要思想就是初始化画布
     * 将保存路径的两个List清空
     * */
    public void removeAllPaint() {
        //调用初始化画布函数以清空画布
        initCanvas();
        invalidate();//刷新
        savePath.clear();
        deletePath.clear();
        mOnMoveLitener.onMove(savePath.size(), deletePath.size());
    }

    /*
     * 保存所绘图形
     * 返回绘图文件的存储路径
     * */
    public String saveBitmap(String projectName) {
        File file = new File(Util.getMediaPathByProjectName(mContext, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR), projectName), Util.getFromatDate(System.currentTimeMillis(), Util.Y_M_D_H_M_S) + ".jpg");
        ImageUtils.save(mBitmap, file, Bitmap.CompressFormat.JPEG);

        return file.getPath();
    }

    private void touch_start(float x, float y) {
        mPath.reset();//清空path
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            //mPath.quadTo(mX, mY, x, y);
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);//源代码是这样写的，可是我没有弄明白，为什么要这样？
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        savePath.add(dp);
        mPath = null;
        deletePath.clear();
        mOnMoveLitener.onMove(savePath.size(), deletePath.size());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mPath = new Path();
                dp = new DrawPath();
                dp.path = mPath;
                dp.paint = mPaint;

                touch_start(x, y);
                invalidate(); //清屏
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public interface OnMoveLitener {
        void onMove(int savePahtSize, int deletePathSize);
    }

    private OnMoveLitener mOnMoveLitener;

    public void setOnMoveLitener(OnMoveLitener mOnMoveLitener) {
        this.mOnMoveLitener = mOnMoveLitener;
    }

}
