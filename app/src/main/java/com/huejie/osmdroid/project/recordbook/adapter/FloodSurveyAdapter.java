package com.huejie.osmdroid.project.recordbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.model.books.SmallBridgeCulvertFloodSurvey;
import com.huejie.osmdroid.util.Util;

import java.util.List;

/**
 * @author twj
 *
 */
public class FloodSurveyAdapter extends BaseAdapter {
    private Context context;
    private List<SmallBridgeCulvertFloodSurvey> smallBridgeCulvertList;
    private LayoutInflater inflater;

    public FloodSurveyAdapter(Context context, List<SmallBridgeCulvertFloodSurvey> smallBridgeCulvertList) {
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
            convertView = inflater.inflate(R.layout.item_flood_survey, null);
            holder.tv_nf = convertView.findViewById(R.id.tv_nf);
            holder.tv_bg = convertView.findViewById(R.id.tv_bg);
            holder.tv_wz = convertView.findViewById(R.id.tv_wz);
            holder.tv_zq = convertView.findViewById(R.id.tv_zq);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SmallBridgeCulvertFloodSurvey survey = smallBridgeCulvertList.get(i);
        holder.tv_nf.setText(Util.valueString(survey.historicalFloodYear));
        holder.tv_bg.setText(Util.valueString(survey.historicalFloodElevation));
        holder.tv_wz.setText(Util.valueString(survey.historicalFloodPosition));
        holder.tv_zq.setText(Util.valueString(survey.historicalFloodPeriod));
        
        return convertView;
    }

    class ViewHolder {
        public TextView tv_nf;
        public TextView tv_bg;
        public TextView tv_wz;
        public TextView tv_zq;
    }

}
