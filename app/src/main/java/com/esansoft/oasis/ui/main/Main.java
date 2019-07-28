package com.esansoft.oasis.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.base.base_header.BaseHeader;
import com.esansoft.base.base_view_pager.BaseViewPager;
import com.esansoft.base.base_view_pager.ViewPagerAdapter;
import com.esansoft.base.util.BaseAlert;
import com.esansoft.base_resource.broadcast_action.ClsBroadCast;
import com.esansoft.oasis.R;
import com.esansoft.oasis.ui.member_login.Login;
import com.esansoft.oasis.ui.scanner.ScanBarcode;
import com.esansoft.oasis.ui.settings_main.SettingFragment;
import com.esansoft.oasis.ui.work_place_search.FindWorkPlace;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class Main extends BaseActivity {
    private final int TAB_PAGE_HOME = 0;
    private final int TAB_PAGE_WORK = 1;
    private final int TAB_PAGE_NOTI = 2;
    private final int TAB_PAGE_SETTING = 3;
    //=========================
    private BaseViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> mListFragment = new ArrayList<>();


    private CommuteFragment fragmentHome;
    private WorkFragment fragmentWork;
    private NotiFragment fragmentNoti;
    private SettingFragment fragmentSetting;


    private BaseHeader header;

    private TextView tvMainHome;
    private TextView tvMainWork;
    private TextView tvMainNoti;
    private TextView tvMainSetting;

    private BroadcastReceiver mBroadcastLogout = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ClsBroadCast.BROAD_CAST_ACTION_LOGOUT)) {
                goLogin();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        header = findViewById(R.id.header);
        header.btnHeaderRight1.setOnClickListener(v -> goScan());

        tvMainHome = findViewById(R.id.tvMainHome);
        tvMainHome.setOnClickListener(v -> setCurrentViewPager(TAB_PAGE_HOME));
        tvMainWork = findViewById(R.id.tvMainWork);
        tvMainWork.setOnClickListener(v -> setCurrentViewPager(TAB_PAGE_WORK));
        tvMainNoti = findViewById(R.id.tvMainNoti);
        tvMainNoti.setOnClickListener(v -> setCurrentViewPager(TAB_PAGE_NOTI));
        tvMainSetting = findViewById(R.id.tvMainSetting);
        tvMainSetting.setOnClickListener(v -> setCurrentViewPager(TAB_PAGE_SETTING));

        initViewPager();

        viewPager.setCurrentItem(1, false);
    }

    @Override
    protected void initialize() {
        registerReceiver();
        setTag();
    }

    private void registerReceiver() {
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastLogout,
                new IntentFilter(ClsBroadCast.BROAD_CAST_ACTION_LOGOUT));
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastLogout);
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPagerMain);

        fragmentHome = new CommuteFragment();
        fragmentWork = new WorkFragment();
        fragmentNoti = new NotiFragment();
        fragmentSetting = new SettingFragment();

        fragmentHome.setOnLoadingDialog(callLoadingBar);
        fragmentWork.setOnLoadingDialog(callLoadingBar);
        fragmentNoti.setOnLoadingDialog(callLoadingBar);
        fragmentSetting.setOnLoadingDialog(callLoadingBar);

        mListFragment.add(fragmentHome);
        mListFragment.add(fragmentWork);
        mListFragment.add(fragmentNoti);
        mListFragment.add(fragmentSetting);

        mViewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager(), mListFragment);
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.setOffscreenPageLimit(mListFragment.size() - 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTag();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 화면 탭 설정
     */
    private void setTag() {
        tvMainHome.setSelected(false);
        tvMainWork.setSelected(false);
        tvMainNoti.setSelected(false);
        tvMainSetting.setSelected(false);

        switch (viewPager.getCurrentItem()) {
            case TAB_PAGE_HOME:
                tvMainHome.setSelected(true);
                header.tvHeaderTitle.setText("출퇴근체크");
                header.btnHeaderRight1.setVisibility(View.VISIBLE);
                break;
            case TAB_PAGE_WORK:
                tvMainWork.setSelected(true);
                header.tvHeaderTitle.setText("근무기록");
                header.btnHeaderRight1.setVisibility(View.GONE);

                if (fragmentWork != null && fragmentWork.getContext() != null)
                    fragmentWork.requestATDVIEW();
                break;
            case TAB_PAGE_NOTI:
                tvMainNoti.setSelected(true);
                header.tvHeaderTitle.setText("알림");
                header.btnHeaderRight1.setVisibility(View.GONE);
                break;
            case TAB_PAGE_SETTING:
                tvMainSetting.setSelected(true);
                header.tvHeaderTitle.setText("설정");
                header.btnHeaderRight1.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 로그인으로 이동한다.
     */
    private void goLogin() {
        Intent intent = new Intent(mContext, Login.class);
        mContext.startActivity(intent);
    }

    /**
     * 바코드를 스캔한다.
     */
    private void goScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScanBarcode.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    /**
     * ViewPager 이동시킨다.
     *
     * @param page
     */
    private void setCurrentViewPager(int page) {
        viewPager.setCurrentItem(page);
    }

    //================================
    // Event
    //================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FindWorkPlace.REQUEST_CODE:
                if (fragmentHome != null)
                    fragmentHome.onActivityResult(requestCode, resultCode, data);
                break;
            case IntentIntegrator.REQUEST_CODE:
                // QR 코드/ 바코드를 스캔한 결과
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (result.getFormatName() != null)
                    BaseAlert.show("Scan Type : " + result.getFormatName() + " / Data : " + result.getContents());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}
