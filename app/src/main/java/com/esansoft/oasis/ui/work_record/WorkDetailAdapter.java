package com.esansoft.oasis.ui.work_record;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esansoft.oasis.R;
import com.esansoft.oasis.value_object.WorkStateVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WorkDetailAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<WorkStateVO> mList;
    private LayoutInflater mInflater;
    private SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfFormat;

    public WorkDetailAdapter(Context context, ArrayList<WorkStateVO> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.sdfFormat = new SimpleDateFormat(mContext.getString(R.string.commute_10));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = mInflater.inflate(R.layout.listitem_work_record_detail, parent, false);

        RecyclerView.ViewHolder holder = new ViewHolder(convertView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= mList.size())
            return;

        WorkStateVO vo = mList.get(position);

        final ViewHolder finalHolder = (ViewHolder) holder;

        String strDate = mList.get(position).LED_03;
        try {
            strDate = sdfFormat.format(sdfParse.parse(strDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        finalHolder.tvDate.setText(strDate);

        StringBuilder sb;

        try {
            sb = new StringBuilder(mList.get(position).LED_07);
            finalHolder.tvStart.setText(sb.insert(2, ":"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sb = new StringBuilder(mList.get(position).LED_08);
            finalHolder.tvEnd.setText(sb.insert(2, ":"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sb = new StringBuilder(mList.get(position).WORKTIME);
            finalHolder.tvWorkTime.setText(sb.insert(2, ":"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(ArrayList<WorkStateVO> list) {
        mList = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvStart;
        TextView tvEnd;
        TextView tvWorkTime;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvStart = itemView.findViewById(R.id.tvStart);
            tvEnd = itemView.findViewById(R.id.tvEnd);
            tvWorkTime = itemView.findViewById(R.id.tvWorkTime);

            itemView.setOnClickListener(view -> {
            });
        }
    }
}
