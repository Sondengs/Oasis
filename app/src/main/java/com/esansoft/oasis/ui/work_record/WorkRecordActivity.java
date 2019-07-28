package com.esansoft.oasis.ui.work_record;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.base.base_header.BaseHeader;
import com.esansoft.base.network.ClsNetworkCheck;
import com.esansoft.base.util.BaseAlert;
import com.esansoft.base.util.ClsDateTime;
import com.esansoft.oasis.R;
import com.esansoft.oasis.model.ATD_LIST_Model;
import com.esansoft.oasis.network.BaseConst;
import com.esansoft.oasis.network.Http;
import com.esansoft.oasis.network.HttpBaseService;
import com.esansoft.oasis.value_object.WorkStateVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkRecordActivity extends BaseActivity {
    public static final String WORK_STATE = "WORK_STATE";

    //============================
    // Layout
    //============================
    BaseHeader header;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeRefresh;
    ImageView imgUserPhoto;
    TextView tvUserName;
    TextView tvWorkType;
    TextView tvWorkTime;
    TextView tvWorkState;


    //============================
    // Variable
    //============================
    private WorkStateVO mWorkDetailVO;

    private WorkDetailAdapter mAdapter;
    private ArrayList<WorkStateVO> mList;

    //============================
    // Initialize
    //============================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_record);

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        header = findViewById(R.id.header);
        header.btnHeaderLeft.setOnClickListener(v -> finish());

        tvUserName = findViewById(R.id.tvUserName);
        tvWorkType = findViewById(R.id.tvWorkType);
        tvWorkTime = findViewById(R.id.tvWorkTime);
        tvWorkTime.setVisibility(View.GONE);
        tvWorkState = findViewById(R.id.tvWorkState);
        tvWorkState.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> requestATDVIEW());
    }

    @Override
    protected void initialize() {
        mWorkDetailVO = (WorkStateVO) getIntent().getSerializableExtra(WORK_STATE);

        tvUserName.setText(mWorkDetailVO.LED_04_NM);
        tvWorkType.setText(mWorkDetailVO.LED_01);
        tvWorkTime.setText(mWorkDetailVO.WORKTIME);
        tvWorkState.setText(mWorkDetailVO.STAT);

        mList = new ArrayList<>();
        mAdapter = new WorkDetailAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);

        requestATDVIEW();
    }


    /**
     * 근무 상태 API 호출
     */
    public void requestATDVIEW() {
        // 인터넷 연결 여부 확인
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(getString(R.string.common_network_error));
            return;
        }

        // API 호출 시 로딩바
        openLoadingBar();

        String strToday = ClsDateTime.getNow("yyyyMMdd");

        // API URL및 Param 설정
        // 여기서 <SampleModel> 은 받을 데이터의 형태(Json format)
        Call<ATD_LIST_Model> call = Http.work(HttpBaseService.TYPE.POST).getWorkStateData(
                BaseConst.URL_HOST,
                "DETAIL",
                mWorkDetailVO.LED_ID,
                "20190611",
                mWorkDetailVO.LED_04
        );

        // Api 호출 후 response 받을 위치
        call.enqueue(new Callback<ATD_LIST_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ATD_LIST_Model> call, Response<ATD_LIST_Model> response) {
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

                            Response<ATD_LIST_Model> response = (Response<ATD_LIST_Model>) msg.obj;

                            mList = response.body().Data;
                            if (mList == null)
                                mList = new ArrayList<>();

                            mAdapter.updateData(mList);
                            mAdapter.notifyDataSetChanged();
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                }.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<ATD_LIST_Model> call, Throwable t) {
                closeLoadingBar();
            }
        });
    }
}
