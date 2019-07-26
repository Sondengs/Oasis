package com.esansoft.oasis.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esansoft.base.base_fragment.BaseFragment;
import com.esansoft.base.network.ClsNetworkCheck;
import com.esansoft.base.util.BaseAlert;
import com.esansoft.base.util.ClsDateTime;
import com.esansoft.base.util.ClsUtil;
import com.esansoft.oasis.R;
import com.esansoft.oasis.model.ATD_INPUT_Model;
import com.esansoft.oasis.network.BaseConst;
import com.esansoft.oasis.network.Http;
import com.esansoft.oasis.network.HttpBaseService;
import com.esansoft.oasis.ui.work_place_search.FindWorkPlace;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        tvGoToWork.setOnClickListener(v -> setCommute(v));
        tvLeaveWork = view.findViewById(R.id.tvLeaveWork);
        tvLeaveWork.setOnClickListener(v -> setCommute(v));

        btnFineWork = view.findViewById(R.id.btnFineWork);
        btnFineWork.setOnClickListener(v -> goFindWorkPlace());
    }

    @Override
    public void onResume() {
        super.onResume();

        // 출퇴근 조회 필요
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
        if (v.getId() == R.id.tvGoToWork) {
            if (v.isSelected()) {
                Toast.makeText(mContext, R.string.commute_5, Toast.LENGTH_LONG).show();
            } else {
                requestATDVIEW("WORK", v);
            }
        } else if (v.getId() == R.id.tvLeaveWork) {
            if (v.isSelected()) {
                Toast.makeText(mContext, R.string.commute_6, Toast.LENGTH_LONG).show();
            } else {
                requestATDVIEW("END", v);
            }
        }
        //v.setSelected(!v.isSelected());
    }

    //================================
    // API 호출
    //================================

    /**
     * 출퇴근 입력
     */
    private void requestATDVIEW(String type, View v) {
        String strWorTime = "";
        String strEndTime = "";

        if (type.equals("WORK")) {
            strWorTime = ClsDateTime.getNow("HHmm");
        } else {
            strEndTime = ClsDateTime.getNow("HHmm");
        }

        // 인터넷 연결 여부 확인
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(getString(R.string.common_network_error));
            return;
        }

        openLoadingBar();

        Call<ATD_INPUT_Model> call = Http.work(HttpBaseService.TYPE.POST).setCommute(
                BaseConst.URL_HOST,
                type,
                mUser.Value.CDO_ID,
                "",
                ClsDateTime.getNow("yyyyMMdd"),
                mUser.Value.CDO_02,
                strWorTime,
                strEndTime
        );

        // Api 호출 후 response 받을 위치
        call.enqueue(new Callback<ATD_INPUT_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ATD_INPUT_Model> call, Response<ATD_INPUT_Model> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            // 로딩바 닫음
                            closeLoadingBar();

                            Response<ATD_INPUT_Model> response = (Response<ATD_INPUT_Model>) msg.obj;

                            // API Validation 추가 후에 체크 코드 삽입하세요.
                            v.setSelected(true);
//                            if (response.body().Data.get(0).Validation) {
//
//                            } else {
//                                // ErrorMsg
//                                BaseAlert.show(getString(R.string.login_1));
//                            }


                        }
                    }
                }.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<ATD_INPUT_Model> call, Throwable t) {
                Log.d("Test", t.getMessage());
                closeLoadingBar();
            }
        });

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
