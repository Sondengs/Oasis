package com.esansoft.oasis.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.esansoft.base.base_fragment.BaseFragment;
import com.esansoft.oasis.R;
import com.esansoft.oasis.ui.work_state.WorkStateAdapter;
import com.esansoft.oasis.ui.work_state.WorkStateVO;

import java.util.ArrayList;

public class WorkFragment extends BaseFragment {
    //===================================
    // Layout
    //===================================
    private View view;
    private ListView listView;


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

        return view;
    }

    private void initLayout() {
        listView = view.findViewById(R.id.listView);
    }


    protected void initialize() {
        mList = new ArrayList<>();

        mList.add(new WorkStateVO("https://techcrunch.com/wp-content/uploads/2018/07/logo-2.png", "조예라", "FULL_TYPE", "10:18", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/mcdonalds.png", "김지호", "FULL_TYPE", "10:15", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/pepsi.png", "김윤규", "PART_TYPE", "09:48", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/yc.png", "진성훈", "FULL_TYPE", "11:11", "WORKING"));
        mList.add(new WorkStateVO("", "전형근", "PART_TYPE", "10:25", "WORKING"));
        mList.add(new WorkStateVO("", "한순모", "FULL_TYPE", "09:44", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/snap.png", "최보람", "FULL_TYPE", "09:39", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/snap.png", "김철영", "PART_TYPE", "10:00", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/snap.png", "이상근", "PART_TYPE", "10:18", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/snap.png", "박지훈", "FULL_TYPE", "10:00", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/apple.png", "조예라", "FULL_TYPE", "10:18", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/apple.png", "김지호", "FULL_TYPE", "10:15", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/apple.png", "김윤규", "PART_TYPE", "09:48", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/random/apple.png", "진성훈", "FULL_TYPE", "11:11", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/wool.png", "전형근", "PART_TYPE", "10:25", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/wool.png", "한순모", "FULL_TYPE", "09:44", "WORKING"));
        mList.add(new WorkStateVO("https://brandmark.io/logo-rank/wool.png", "최보람", "FULL_TYPE", "09:39", "WORKING"));
        mList.add(new WorkStateVO("", "김철영", "PART_TYPE", "10:00", "WORKING"));
        mList.add(new WorkStateVO("", "이상근", "PART_TYPE", "10:18", "WORKING"));
        mList.add(new WorkStateVO("", "박지훈", "FULL_TYPE", "10:00", "WORKING"));


        mAdapter = new WorkStateAdapter(mContext, mList);
        listView.setAdapter(mAdapter);
    }
}
