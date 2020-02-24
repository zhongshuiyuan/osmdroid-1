package com.huejie.osmdroid.project.recordbook.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.activity.BaseActivity;
import com.huejie.osmdroid.util.DBUtil;
import com.huejie.osmdroid.view.PaintView;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class PaintActivity extends BaseActivity implements View.OnClickListener {

    private PaintView paintView;
    private ImageView iv_recovery;
    private ImageView iv_revoke;
    private ImageView iv_eraser;
    private PopupWindow pop_pen;
    private ImageView iv_brush;

    private ImageView iv_blue;
    private ImageView iv_red;
    private ImageView iv_green;
    private ImageView iv_small;
    private ImageView iv_middle;
    private ImageView iv_big;
    private ImageView iv_black;
    private PaintView.EnumPaintSize paintSize;
    private PaintView.EnumPaintColor paintColor;
    private ImageView iv_black_select;
    private ImageView iv_blue_select;
    private ImageView iv_red_select;
    private ImageView iv_green_select;
    private PopupWindow pop_clear;
    private TextView tv_clear;
    private LinearLayout rl_layout;
    private LinearLayout rl_top;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.iv_clean)
    public void clean() {
        paintView.removeAllPaint();
    }

    @OnClick(R.id.tv_complete)
    public void complete() {
        dialog();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    public void initView() {

        rl_layout = (LinearLayout) findViewById(R.id.rl_layout);
        rl_top = (LinearLayout) findViewById(R.id.rl_top);
        iv_recovery = (ImageView) findViewById(R.id.iv_recovery);
        iv_revoke = (ImageView) findViewById(R.id.iv_revoke);
        iv_eraser = (ImageView) findViewById(R.id.iv_eraser);
        iv_brush = (ImageView) findViewById(R.id.iv_brush);


        iv_recovery.setOnClickListener(this);
        iv_revoke.setOnClickListener(this);
        iv_eraser.setOnClickListener(this);
        iv_brush.setOnClickListener(this);


    }

    private String project_id;

    public void initData() {
//获取照片地址
        String path = getIntent().getStringExtra("mediaPath");
        project_id = getIntent().getStringExtra("projectId");

        if (path != null) {                                //当传入照片地址时，以照片为画布，否则就创建空白画布
            paintView = new PaintView(this, path);
        } else {
            paintView = new PaintView(this, "");
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paintView.setLayoutParams(layoutParams);
        rl_layout.addView(paintView, 1);

        /**
         * 监听是否撤销、恢复做的，然后改变撤销恢复按钮状态
         */
        paintView.setOnMoveLitener(new PaintView.OnMoveLitener() {
            @Override
            public void onMove(int savePahtSize, int deletePathSize) {
                if (deletePathSize > 0) {
                    iv_recovery.setImageResource(R.mipmap.recovery_has);
                } else {
                    iv_recovery.setImageResource(R.mipmap.recovery);
                }

                if (savePahtSize > 0) {
                    iv_revoke.setImageResource(R.mipmap.revoke_has);
                } else {
                    iv_revoke.setImageResource(R.mipmap.revoke);
                }
            }
        });

        ViewTreeObserver vto = rl_top.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl_top.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                System.out.println(rl_top.getHeight()+"  bb");
                rl_top.getWidth();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择画笔样式
            case R.id.iv_brush:
                paintView.selectPaintStyle(0);
                iv_eraser.setImageResource(R.mipmap.eraser_normal);
                showPenPopwindow();
                break;
            //撤销
            case R.id.iv_recovery:
                paintView.redo();
                break;
            //恢复
            case R.id.iv_revoke:
                paintView.undo();
                break;
            //清空
            case R.id.iv_eraser:
                iv_eraser.setImageResource(R.mipmap.eraser_select);
                PaintView.EnumPaintStyle paintStyle = paintView.getPaintStyle();
                if (paintStyle == PaintView.EnumPaintStyle.BRUSH) {         //当是画笔模式时，变为橡皮擦，并设置画笔按钮状态
                    paintView.selectPaintStyle(1);
                    paintSize = paintView.getPaintSize();
                    if (paintSize == PaintView.EnumPaintSize.SMALL) {
                        iv_brush.setImageResource(R.mipmap.brush_unselected_small);
                    } else if (paintSize == PaintView.EnumPaintSize.MIDDLE) {
                        iv_brush.setImageResource(R.mipmap.brush_unselected_mid);
                    } else if (paintSize == PaintView.EnumPaintSize.BIG) {
                        iv_brush.setImageResource(R.mipmap.brush_unselected_big);
                    }
                }
                break;

            case R.id.iv_black:                                           //选择黑色按钮，画笔状态改变
                paintView.selectPaintColor(0);
                paintSize = paintView.getPaintSize();
                if (paintSize == PaintView.EnumPaintSize.SMALL) {
                    iv_brush.setImageResource(R.mipmap.brush_black_small);
                    iv_small.setImageResource(R.mipmap.pen_black_small_select);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintSize == PaintView.EnumPaintSize.MIDDLE) {
                    iv_brush.setImageResource(R.mipmap.brush_black_mid);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_black_mid_select);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintSize == PaintView.EnumPaintSize.BIG) {
                    iv_brush.setImageResource(R.mipmap.brush_black_big);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_black_big_select);
                }
                iv_black_select.setVisibility(View.VISIBLE);
                iv_blue_select.setVisibility(View.INVISIBLE);
                iv_red_select.setVisibility(View.INVISIBLE);
                iv_green_select.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_blue:                                      //选择蓝色按钮，画笔状态改变
                paintView.selectPaintColor(1);
                paintSize = paintView.getPaintSize();
                if (paintSize == PaintView.EnumPaintSize.SMALL) {
                    iv_brush.setImageResource(R.mipmap.brush_blue_small);
                    iv_small.setImageResource(R.mipmap.pen_blue_small_select);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintSize == PaintView.EnumPaintSize.MIDDLE) {
                    iv_brush.setImageResource(R.mipmap.brush_blue_mid);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_blue_mid_select);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintSize == PaintView.EnumPaintSize.BIG) {
                    iv_brush.setImageResource(R.mipmap.brush_blue_big);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_blue_big_select);

                }
                iv_black_select.setVisibility(View.INVISIBLE);
                iv_blue_select.setVisibility(View.VISIBLE);
                iv_red_select.setVisibility(View.INVISIBLE);
                iv_green_select.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_red:                                      //选择红色按钮，画笔状态改变
                paintView.selectPaintColor(2);
                paintSize = paintView.getPaintSize();
                if (paintSize == PaintView.EnumPaintSize.SMALL) {
                    iv_brush.setImageResource(R.mipmap.brush_red_small);
                    iv_small.setImageResource(R.mipmap.pen_red_small_select);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintSize == PaintView.EnumPaintSize.MIDDLE) {
                    iv_brush.setImageResource(R.mipmap.brush_red_mid);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_red_mid_select);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintSize == PaintView.EnumPaintSize.BIG) {
                    iv_brush.setImageResource(R.mipmap.brush_red_big);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_red_big_select);
                }
                iv_black_select.setVisibility(View.INVISIBLE);
                iv_blue_select.setVisibility(View.INVISIBLE);
                iv_red_select.setVisibility(View.VISIBLE);
                iv_green_select.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_green:                                   //选择绿色按钮，画笔状态改变
                paintView.selectPaintColor(3);
                paintSize = paintView.getPaintSize();
                if (paintSize == PaintView.EnumPaintSize.SMALL) {
                    iv_brush.setImageResource(R.mipmap.brush_green_small);
                    iv_small.setImageResource(R.mipmap.pen_green_small_select);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintSize == PaintView.EnumPaintSize.MIDDLE) {
                    iv_brush.setImageResource(R.mipmap.brush_green_mid);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_green_mid_select);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintSize == PaintView.EnumPaintSize.BIG) {
                    iv_brush.setImageResource(R.mipmap.brush_green_big);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_green_big_select);
                }
                iv_black_select.setVisibility(View.INVISIBLE);
                iv_blue_select.setVisibility(View.INVISIBLE);
                iv_red_select.setVisibility(View.INVISIBLE);
                iv_green_select.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_small:                                   //选择细线时，画笔状态改变
                paintView.selectPaintSize(0);
                paintColor = paintView.getPaintColor();
                if (paintColor == PaintView.EnumPaintColor.BLACK) {
                    iv_brush.setImageResource(R.mipmap.brush_black_small);
                    iv_small.setImageResource(R.mipmap.pen_black_small_select);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintColor == PaintView.EnumPaintColor.BLUE) {
                    iv_brush.setImageResource(R.mipmap.brush_blue_small);
                    iv_small.setImageResource(R.mipmap.pen_blue_small_select);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintColor == PaintView.EnumPaintColor.RED) {
                    iv_brush.setImageResource(R.mipmap.brush_red_small);
                    iv_small.setImageResource(R.mipmap.pen_red_small_select);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintColor == PaintView.EnumPaintColor.GREEN) {
                    iv_brush.setImageResource(R.mipmap.brush_green_small);
                    iv_small.setImageResource(R.mipmap.pen_green_small_select);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                }
                break;
            case R.id.iv_middle:                                 //选择中线时，画笔状态改变
                paintView.selectPaintSize(1);
                paintColor = paintView.getPaintColor();
                if (paintColor == PaintView.EnumPaintColor.BLACK) {
                    iv_brush.setImageResource(R.mipmap.brush_black_mid);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_black_mid_select);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintColor == PaintView.EnumPaintColor.BLUE) {
                    iv_brush.setImageResource(R.mipmap.brush_blue_mid);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_blue_mid_select);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintColor == PaintView.EnumPaintColor.RED) {
                    iv_brush.setImageResource(R.mipmap.brush_red_mid);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_red_mid_select);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                } else if (paintColor == PaintView.EnumPaintColor.GREEN) {
                    iv_brush.setImageResource(R.mipmap.brush_green_mid);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_green_mid_select);
                    iv_big.setImageResource(R.mipmap.pen_big_normal);
                }
                break;
            case R.id.iv_big:                                   //选择粗线时，画笔状态改变
                paintView.selectPaintSize(2);
                paintColor = paintView.getPaintColor();
                if (paintColor == PaintView.EnumPaintColor.BLACK) {
                    iv_brush.setImageResource(R.mipmap.brush_black_big);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_black_big_select);
                } else if (paintColor == PaintView.EnumPaintColor.BLUE) {
                    iv_brush.setImageResource(R.mipmap.brush_blue_big);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_blue_big_select);
                } else if (paintColor == PaintView.EnumPaintColor.RED) {
                    iv_brush.setImageResource(R.mipmap.brush_red_big);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_red_big_select);
                } else if (paintColor == PaintView.EnumPaintColor.GREEN) {
                    iv_brush.setImageResource(R.mipmap.brush_green_big);
                    iv_small.setImageResource(R.mipmap.pen_small_normal);
                    iv_middle.setImageResource(R.mipmap.pen_middle_normal);
                    iv_big.setImageResource(R.mipmap.pen_green_big_select);
                }
                break;

        }
    }

    /**
     * 显示画板颜色画笔弹框
     */
    private void showPenPopwindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_pen, null);
        pop_pen = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  //设置popwindow为布局大小
        pop_pen.setBackgroundDrawable(new BitmapDrawable());
        pop_pen.setOutsideTouchable(true);
        pop_pen.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        iv_black = (ImageView) contentView.findViewById(R.id.iv_black);
        iv_blue = (ImageView) contentView.findViewById(R.id.iv_blue);
        iv_red = (ImageView) contentView.findViewById(R.id.iv_red);
        iv_green = (ImageView) contentView.findViewById(R.id.iv_green);
        iv_small = (ImageView) contentView.findViewById(R.id.iv_small);
        iv_middle = (ImageView) contentView.findViewById(R.id.iv_middle);
        iv_big = (ImageView) contentView.findViewById(R.id.iv_big);
        iv_black_select = (ImageView) contentView.findViewById(R.id.iv_black_select);
        iv_blue_select = (ImageView) contentView.findViewById(R.id.iv_blue_select);
        iv_red_select = (ImageView) contentView.findViewById(R.id.iv_red_select);
        iv_green_select = (ImageView) contentView.findViewById(R.id.iv_green_select);

        paintColor = paintView.getPaintColor();                                   //根据画笔颜色，来选择画板上状态
        paintSize = paintView.getPaintSize();
        if (paintColor == PaintView.EnumPaintColor.BLACK) {
            iv_black_select.setVisibility(View.VISIBLE);
            if (paintSize == PaintView.EnumPaintSize.SMALL) {
                iv_small.setImageResource(R.mipmap.pen_black_small_select);
                iv_brush.setImageResource(R.mipmap.brush_black_small);
            } else if (paintSize == PaintView.EnumPaintSize.MIDDLE) {
                iv_middle.setImageResource(R.mipmap.pen_black_mid_select);
                iv_brush.setImageResource(R.mipmap.brush_black_mid);
            } else if (paintSize == PaintView.EnumPaintSize.BIG) {
                iv_big.setImageResource(R.mipmap.pen_black_big_select);
                iv_brush.setImageResource(R.mipmap.brush_black_big);
            }
        } else if (paintColor == PaintView.EnumPaintColor.BLUE) {
            iv_blue_select.setVisibility(View.VISIBLE);
            if (paintSize == PaintView.EnumPaintSize.SMALL) {
                iv_small.setImageResource(R.mipmap.pen_blue_small_select);
                iv_brush.setImageResource(R.mipmap.brush_blue_small);
            } else if (paintSize == PaintView.EnumPaintSize.MIDDLE) {
                iv_middle.setImageResource(R.mipmap.pen_blue_mid_select);
                iv_brush.setImageResource(R.mipmap.brush_blue_mid);
            } else if (paintSize == PaintView.EnumPaintSize.BIG) {
                iv_big.setImageResource(R.mipmap.pen_blue_big_select);
                iv_brush.setImageResource(R.mipmap.brush_blue_big);
            }
        } else if (paintColor == PaintView.EnumPaintColor.RED) {
            iv_red_select.setVisibility(View.VISIBLE);
            if (paintSize == PaintView.EnumPaintSize.SMALL) {
                iv_small.setImageResource(R.mipmap.pen_red_small_select);
                iv_brush.setImageResource(R.mipmap.brush_red_small);
            } else if (paintSize == PaintView.EnumPaintSize.MIDDLE) {
                iv_middle.setImageResource(R.mipmap.pen_red_mid_select);
                iv_brush.setImageResource(R.mipmap.brush_red_mid);
            } else if (paintSize == PaintView.EnumPaintSize.BIG) {
                iv_big.setImageResource(R.mipmap.pen_red_big_select);
                iv_brush.setImageResource(R.mipmap.brush_red_big);
            }
        } else if (paintColor == PaintView.EnumPaintColor.GREEN) {
            iv_green_select.setVisibility(View.VISIBLE);
            if (paintSize == PaintView.EnumPaintSize.SMALL) {
                iv_small.setImageResource(R.mipmap.pen_green_small_select);
                iv_brush.setImageResource(R.mipmap.brush_green_small);
            } else if (paintSize == PaintView.EnumPaintSize.MIDDLE) {
                iv_middle.setImageResource(R.mipmap.pen_green_mid_select);
                iv_brush.setImageResource(R.mipmap.brush_green_mid);
            } else if (paintSize == PaintView.EnumPaintSize.BIG) {
                iv_big.setImageResource(R.mipmap.pen_green_big_select);
                iv_brush.setImageResource(R.mipmap.brush_green_big);
            }
        }

        iv_black.setOnClickListener(this);
        iv_blue.setOnClickListener(this);
        iv_red.setOnClickListener(this);
        iv_green.setOnClickListener(this);
        iv_small.setOnClickListener(this);
        iv_middle.setOnClickListener(this);
        iv_big.setOnClickListener(this);

        pop_pen.showAsDropDown(iv_brush);    //popwindow显示在控件下方
    }

    /**
     * 显示保存弹框
     */
    private void dialog() {
        AlertDialog dialog = new AlertDialog.Builder(PaintActivity.this).setMessage("确认保存图片吗？").setTitle("警告").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = paintView.saveBitmap(DBUtil.getProjectNameById(PaintActivity.this, project_id));
                Intent intent = new Intent();
                intent.putExtra("mediaPath", path);
                setResult(RESULT_OK, intent);
                finish();
            }
        }).create();
        dialog.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog();
        }
        return super.onKeyDown(keyCode, event);
    }


}
