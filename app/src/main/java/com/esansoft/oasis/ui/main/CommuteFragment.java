package com.esansoft.oasis.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esansoft.base.base_fragment.BaseFragment;
import com.esansoft.oasis.R;
import com.esansoft.oasis.ui.work_place_search.FindWorkPlace;

public class CommuteFragment extends BaseFragment {
    //========================
    // Layout
    //========================
    private View view;

    private TextView tvGoToWork;
    private TextView tvLeaveWork;
    private Button btnFineWork;


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

        initLayout();

        return view;
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        tvGoToWork = view.findViewById(R.id.tvGoToWork);
        tvGoToWork.setSelected(true);
        tvGoToWork.setOnClickListener(v -> setCommute(v));
        tvLeaveWork = view.findViewById(R.id.tvLeaveWork);
        tvLeaveWork.setOnClickListener(v -> setCommute(v));

        btnFineWork = view.findViewById(R.id.btnFineWork);
        btnFineWork.setOnClickListener(v -> goFindWorkPlace());
    }

    /**
     * 근무지 찾기
     */
    private void goFindWorkPlace() {
        Intent intent = new Intent(mContext, FindWorkPlace.class);
        mActivity.startActivityForResult(intent, FindWorkPlace.REQUEST_CODE);
    }

    /**
     * 출퇴근 체크
     *
     * @param v
     */
    private void setCommute(View v) {
        v.setSelected(!v.isSelected());
    }

    //================================
    // Event
    //================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FindWorkPlace.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(mContext, "서버 호출하여 근무지 확인", Toast.LENGTH_LONG).show();
        }
    }
}
