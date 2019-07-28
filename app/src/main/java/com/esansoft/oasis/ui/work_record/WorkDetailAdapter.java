package com.esansoft.oasis.ui.work_record;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esansoft.oasis.R;
import com.esansoft.oasis.value_object.WorkStateVO;

import java.util.ArrayList;

public class WorkDetailAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<WorkStateVO> mList;
    private LayoutInflater mInflater;


    public WorkDetailAdapter(Context context, ArrayList<WorkStateVO> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        finalHolder.tvDate.setText(mList.get(position).LED_03);
        finalHolder.tvStart.setText(mList.get(position).LED_07);
        finalHolder.tvEnd.setText(mList.get(position).LED_08);
        finalHolder.tvWorkTime.setText(mList.get(position).WORKTIME);
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
