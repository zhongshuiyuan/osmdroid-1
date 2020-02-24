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
import com.huejie.osmdroid.model.basic.SysDictData;
import com.huejie.osmdroid.util.DictUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guchao on 2016/4/13.
 */
public class AddOtherEngineeringsDialog extends BaseDialog {
    private Context context;

    private EditText et_qdzh;
    private TextView tv_cancel;
    private TextView tv_title;
    private TextView tv_confirm;
    private TextView tv_choose;
    private GridView gridView;
    private View rl_choose;
    private String title;
    private boolean showType;
    private PopListAdapter adapter;

    private AddOtherEngineeringsDialog(Context context) {
        super(context, R.style.add_dialog);
        this.context = context;
    }

    public void showChooseBookType(boolean showType) {
        this.showType = showType;
    }

    private static AddOtherEngineeringsDialog dialog;

    public static AddOtherEngineeringsDialog getInstance(Context context) {
        if (null == dialog) {
            dialog = new AddOtherEngineeringsDialog(context);
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
    private long relocationType = -1;

    @Override
    public void onCreateDialog(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_other_engineerings);
        et_qdzh = findViewById(R.id.et_qdzh);
        tv_title = findViewById(R.id.tv_title);
        tv_cancel = findViewById(R.id.btn_cancel);
        tv_confirm = findViewById(R.id.btn_confirm);
        rl_choose = findViewById(R.id.rl_choose);
        tv_choose = findViewById(R.id.tv_choose);
        gridView = findViewById(R.id.gridView);
        tv_title.setText(title);
        tv_choose.setText(recordBookName);
        List<SysDictData> data = DictUtil.getDictDatas(context, DictUtil.RELOCATION_TYPE_GROUP);
        list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            list.add(new SimpleChooseModel(data.get(i).dictLabel, data.get(i).id));
        }
        adapter = new PopListAdapter(context, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).isChoose = position == i;
                }
                relocationType = list.get(position).code;
                adapter.notifyDataSetChanged();

            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qdzh = et_qdzh.getText().toString().trim();
                if (TextUtils.isEmpty(qdzh)) {
                    ToastUtils.showShortToast("交叉桩号不能为空");
                    return;
                }

                if (relocationType < 0) {
                    ToastUtils.showShortToast("请选择改移工程项目");
                    return;
                }
                if (null != onClick) {
                    onClick.onConfirm(qdzh, relocationType);
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
        void onConfirm(String startStake, long relocationType);

        void onChoose();


    }


}
