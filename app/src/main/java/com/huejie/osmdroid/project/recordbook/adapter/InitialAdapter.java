package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.books.SmallBridgeCulvertFormInfo;
import com.huejie.osmdroid.util.DictUtil;

import java.util.List;

/**
 * @author twj
 *
 */
public class InitialAdapter extends BaseAdapter {
    private Context context;
    private List<SmallBridgeCulvertFormInfo> smallBridgeCulvertList;
    private LayoutInflater inflater;

    public InitialAdapter(Context context, List<SmallBridgeCulvertFormInfo> smallBridgeCulvertList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.smallBridgeCulvertList = smallBridgeCulvertList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return null == smallBridgeCulvertList ? 0 : smallBridgeCulvertList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return smallBridgeCulvertList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_initial, null);
            holder.tv_cnjck = convertView.findViewById(R.id.tv_cnjck);
            holder.tv_cnjg = convertView.findViewById(R.id.tv_cnjg);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SmallBridgeCulvertFormInfo smallBridgeCulvertFormInfo = smallBridgeCulvertList.get(i);
        String cnjck =smallBridgeCulvertFormInfo.initImportExportFormStr;
        holder.tv_cnjck.setText(cnjck);
        holder.tv_cnjg.setText(smallBridgeCulvertFormInfo.initSubstructureInfrastructureForm);
        
        return convertView;
    }

    class ViewHolder {
        public TextView tv_cnjck;
        public TextView tv_cnjg;
    }

}
