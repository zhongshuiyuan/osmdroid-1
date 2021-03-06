package com.huejie.osmdroid.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.adapter.PopListAdapter;
import com.huejie.osmdroid.model.SimpleChooseModel;
import com.huejie.osmdroid.util.DictUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guchao on 2016/4/13.
 */
public class AddSeparateTypeBridgeDialog extends BaseDialog {
    private Context context;
    private EditText et_zh;
    private TextView tv_cancel;
    private TextView tv_title;
    private TextView tv_confirm;
    private TextView tv_choose;
    private GridView gridView;
    private View rl_choose;
    private String title;
    private boolean showType;
    private PopListAdapter adapter;

    private AddSeparateTypeBridgeDialog(Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    public void showChooseBookType(boolean showType) {
        this.showType = showType;
    }

    private static AddSeparateTypeBridgeDialog dialog;

    public static AddSeparateTypeBridgeDialog getInstance(Context context) {
        if (null == dialog) {
            dialog = new AddSeparateTypeBridgeDialog(context);
        }
        return dialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String recordBookName;

    public void setChoose(String recordBookName) {
        this.recordBookName = recordBookName;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog = null;
    }

    private List<SimpleChooseModel> list;
    private String wallType;

    @Override
    public void onCreateDialog(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_add_sepatate_type_bridge);
        et_zh = findViewById(R.id.et_zh);
        tv_title = findViewById(R.id.tv_title);
        tv_cancel = findViewById(R.id.btn_cancel);
        tv_confirm = findViewById(R.id.btn_confirm);
        rl_choose = findViewById(R.id.rl_choose);
        tv_choose = findViewById(R.id.tv_choose);
        gridView = findViewById(R.id.gridView);
        tv_title.setText(title);
        tv_choose.setText(recordBookName);
        String[] data = DictUtil.getDictLabels(context, DictUtil.STRUCTURAL_TYPES);
        list = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            list.add(new SimpleChooseModel(data[i]));
        }
        adapter = new PopListAdapter(context, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).isChoose = position == i;
                }
                wallType = list.get(position).name;
                adapter.notifyDataSetChanged();

            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qdzh = et_zh.getText().toString().trim();
                if (TextUtils.isEmpty(qdzh)) {
                    ToastUtils.showShortToast("交叉桩号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(wallType)) {
                    ToastUtils.showShortToast("结构物类型不能为空");
                    return;
                }
                if (null != onClick) {
                    onClick.onConfirm(qdzh, wallType);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClick) {
                    onClick.onCancel();
                }
            }
        });
        if (showType) {
            rl_choose.setVisibility(View.VISIBLE);
            rl_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClick) {
                        onClick.onChoose();
                    }
                }
            });
        } else {
            rl_choose.setVisibility(View.GONE);
        }


    }


    private OnClick onClick;

    public void setOnclick(OnClick onClick) {
        this.onClick = onClick;
    }

    /***/
    public static interface OnClick {
        /**
         * 打开操作
         */
        void onCancel();

        /**
         * 确定操作
         */
        void onConfirm(String crossStake, String wallType);

        void onChoose();


    }


}
