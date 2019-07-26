package com.esansoft.oasis.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.esansoft.base.base_fragment.BaseFragment;
import com.esansoft.base.network.ClsNetworkCheck;
import com.esansoft.base.util.BaseAlert;
import com.esansoft.base.util.ClsDateTime;
import com.esansoft.oasis.R;
import com.esansoft.oasis.model.ATD_LIST_Model;
import com.esansoft.oasis.network.BaseConst;
import com.esansoft.oasis.network.Http;
import com.esansoft.oasis.network.HttpBaseService;
import com.esansoft.oasis.ui.work_state.WorkStateAdapter;
import com.esansoft.oasis.value_object.WorkStateVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkFragment extends BaseFragment {
    //===================================
    // Layout
    //===================================
    private View view;
    private ListView listView;
    private SwipeRefreshLayout swipeRefresh;

    //===================================
    // Variable
    //===================================
    private WorkStateAdapter mAdapter;
    private ArrayList<WorkStateVO> mList;

    public WorkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_work, container, false);

        initLayout();

        initialize();

        requestATDVIEW();

        return view;
    }

    private void initLayout() {
        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goWorkRecord();
                Toast.makeText(mContext, "Pos:" + position, Toast.LENGTH_LONG).show();
            }
        });

        // 아래로 당겨서 갱신
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> requestATDVIEW());
    }

    /**
     * 근무정보 상세 페이지로 이동
     */
    private void goWorkRecord() {
    }


    protected void initialize() {
        mList = new ArrayList<>();

        mAdapter = new WorkStateAdapter(mContext, mList);
        listView.setAdapter(mAdapter);
    }

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
                "LIST",
                mUser.Value.CDO_ID,
                "20190611",
                ""
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
                Log.d("Test", t.getMessage());
                closeLoadingBar();
            }
        });
    }
}
