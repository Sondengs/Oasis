package com.esansoft.oasis.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esansoft.base.base_fragment.BaseFragment;
import com.esansoft.oasis.R;

public class CommuteFragment extends BaseFragment {
    //========================
    // Layout
    //========================
    private View view;

    private TextView tvGoToWork;
    private TextView tvLeaveWork;


    public CommuteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commute, container, false);

        initLayout(view);

        return view;
    }

    /**
     * 레이아웃 초기화
     *
     * @param view
     */
    private void initLayout(View view) {
        tvGoToWork = view.findViewById(R.id.tvGoToWork);
        tvGoToWork.setSelected(true);
        tvGoToWork.setOnClickListener(v -> setCommute(v));
        tvLeaveWork = view.findViewById(R.id.tvLeaveWork);
        tvLeaveWork.setOnClickListener(v -> setCommute(v));
    }

    /**
     * 출퇴근 체크
     *
     * @param v
     */
    private void setCommute(View v) {
        v.setSelected(!v.isSelected());
    }
}
