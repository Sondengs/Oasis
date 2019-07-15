package com.esansoft.oasis.ui.work_state;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esansoft.oasis.R;
import com.esansoft.oasis.value_object.WorkStateVO;

import java.util.ArrayList;

public class WorkStateAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<WorkStateVO> mList;
    private LayoutInflater mInflater;

    public WorkStateAdapter(Context context, ArrayList<WorkStateVO> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_work_record, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgUserPhoto = convertView.findViewById(R.id.imgUserPhoto);
            if (Build.VERSION.SDK_INT >= 21) {
                viewHolder.imgUserPhoto.setClipToOutline(true);
            }

            viewHolder.tvUserName = convertView.findViewById(R.id.tvUserName);
            viewHolder.tvWorkType = convertView.findViewById(R.id.tvWorkType);
            viewHolder.tvWorkTime = convertView.findViewById(R.id.tvWorkTime);
            viewHolder.tvWorkState = convertView.findViewById(R.id.tvWorkState);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 사용자 이미지 가져올때
//        Glide.with(mContext).load(mList.get(position).UserPhoto) // Url주소
//                .placeholder(R.drawable.main_profile_no_image)
//                .error(R.drawable.main_profile_no_image)
//                .into(viewHolder.imgUserPhoto);

        viewHolder.tvUserName.setText(mList.get(position).LED_04_NM);
        viewHolder.tvWorkType.setText(mList.get(position).LED_01);
        viewHolder.tvWorkTime.setText(mList.get(position).WORKTIME);
        viewHolder.tvWorkState.setText(mList.get(position).STAT);

        return convertView;
    }

    public void updateData(ArrayList<WorkStateVO> list) {
        mList = list;
    }

    static class ViewHolder {
        ImageView imgUserPhoto;
        TextView tvUserName;
        TextView tvWorkType;
        TextView tvWorkTime;
        TextView tvWorkState;
    }
}
