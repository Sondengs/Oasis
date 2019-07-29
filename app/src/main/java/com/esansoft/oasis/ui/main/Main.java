package com.esansoft.oasis.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.base.base_header.BaseHeader;
import com.esansoft.base.base_view_pager.BaseViewPager;
import com.esansoft.base.base_view_pager.ViewPagerAdapter;
import com.esansoft.base.settings.SettingsKey;
import com.esansoft.base.util.BaseAlert;
import com.esansoft.base_resource.broadcast_action.ClsBroadCast;
import com.esansoft.oasis.R;
import com.esansoft.oasis.ui.member_login.Login;
import com.esansoft.oasis.ui.scanner.ScanBarcode;
import com.esansoft.oasis.ui.settings_main.SettingFragment;
import com.esansoft.oasis.ui.work_place_search.FindWorkPlace;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main extends BaseActivity {
    //=========================================
    // Final
    //=========================================
    // GPS 사용 시 회사 인식 갭차이
    private final double GPS_GAP = 0.001;
    // GPS 를 통해 출퇴근 확인 단위(분)
    private final int WORK_CHECK_TIME = 1;

    private final int TAB_PAGE_HOME = 0;
    private final int TAB_PAGE_WORK = 1;
    private final int TAB_PAGE_NOTI = 2;
    private final int TAB_PAGE_SETTING = 3;

    //=========================================
    // Layout
    //=========================================
    private BaseHeader header;
    private TextView tvMainHome;
    private TextView tvMainWork;
    private TextView tvMainNoti;
    private TextView tvMainSetting;

    //=========================================
    // Variable
    //=========================================
    private CommuteFragment fragmentHome;
    private WorkFragment fragmentWork;
    private NotiFragment fragmentNoti;
    private SettingFragment fragmentSetting;

    private BaseViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> mListFragment = new ArrayList<>();

    //=========================================
    // Location
    //=========================================

    private long mGPSOutTime = 0;
    /**
     * GPS 출퇴근
     */
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            String strDate = sdf.format(cal.getTime());

            // 새벽 1시~5시 사이는 체크하지 않는다.
            int nHour = cal.get(Calendar.HOUR_OF_DAY);

            if (nHour > 1 && nHour < 5) {
                mSettings.Value.GPSCount = 0;
                mSettings.putIntItem(SettingsKey.GPSCount, mSettings.Value.GPSCount);
                mSettings.Value.GPSTime = 0;
                mSettings.putLongItem(SettingsKey.GPSTime, mSettings.Value.GPSTime);
                return;
            }

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            Log.d("GPS", "위도 : " + latitude + " / 경도 : " + longitude);

            double dblWorkLatitude = Double.parseDouble(mSettings.Value.WorkLatitude);
            double dblWorkLongitude = Double.parseDouble(mSettings.Value.WorkLongitude);

            // 금일 출퇴근 여부 확인
            // API 호출하여 금일 출퇴근 여부 확인 필요
            long now = System.currentTimeMillis();

            if (!mSettings.Value.WorkStart.equals(strDate)) {
                Log.d("GPS", "출근 확인");
                // 출근
                if (dblWorkLatitude - GPS_GAP < latitude && latitude < dblWorkLatitude + GPS_GAP &&         // 위도 체크
                        dblWorkLongitude - GPS_GAP < longitude && longitude < dblWorkLongitude + GPS_GAP) { // 경도 체크
                    Log.d("GPS", "출근 확인- 회사 안");

                    //회사안에 있다고 판단
                    if (mSettings.Value.GPSCount == 0) {
                        mSettings.Value.GPSTime = now;
                        mSettings.Value.GPSCount++;
                    } else {
                        Log.d("GPS", "출근 확인 - 회사 안 : " + ((now - mSettings.Value.GPSTime) / 1000) + "초");

                        if (now - mSettings.Value.GPSTime < 1000 * 60 * WORK_CHECK_TIME) {
                            setDialogWorkStart();
                        }
                    }
                } else {
                    Log.d("GPS", "출근 확인 - 회사 밖");

                    // 회사 밖인 경우
                    if (mSettings.Value.GPSCount == 0) {
                        mGPSOutTime = 0;
                    } else {
                        // 회사안에 출근 처리하지 않고 나간 경우

                        // GPS 오류인지 아닌지 시간을 체크한다.
                        if (mGPSOutTime == 0) {
                            mGPSOutTime = now;
                        } else {
                            // 1분이상 밖으로 나가있으면 출근 GPS 타이머를 초기화
                            if (now - mGPSOutTime < 1000 * 60 * WORK_CHECK_TIME) {
                                mSettings.Value.GPSCount = 0;
                                mSettings.putIntItem(SettingsKey.GPSCount, mSettings.Value.GPSCount);
                                mSettings.Value.GPSTime = 0;
                                mSettings.putLongItem(SettingsKey.GPSTime, mSettings.Value.GPSTime);
                            }
                        }
                    }
                }
            } else if (!mSettings.Value.WorkEnd.equals(strDate)) {
                // 퇴근
            } else {
                mSettings.Value.GPSCount = 0;
                mSettings.Value.GPSTime = 0;
            }

            mSettings.putIntItem(SettingsKey.GPSCount, mSettings.Value.GPSCount);
            mSettings.putLongItem(SettingsKey.GPSTime, mSettings.Value.GPSTime);

            if (mSettings.Value.GPSCount == 0 || mSettings.Value.GPSTime == 0)
                return;

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    AlertDialog.Builder builderCheckWork;
    AlertDialog dialogCheckWork;

    /**
     * 출근 여부 다이얼로그
     */
    private void setDialogWorkStart() {
        if (dialogCheckWork == null || !dialogCheckWork.isShowing()) {
            builderCheckWork = new AlertDialog.Builder(BaseActivity.BaseContext);
            dialogCheckWork = builderCheckWork.setCancelable(false)
                    .setMessage("출근 처리 하시겠습니까?")
                    .setTitle("출근 확인")
                    .setNegativeButton(com.esansoft.base.R.string.common_no, null)
                    .setPositiveButton(com.esansoft.base.R.string.common_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewPager.setCurrentItem(TAB_PAGE_HOME);
                            fragmentHome.setWorkStart();
                        }
                    })
                    .create();
            dialogCheckWork.show();
        }
    }

    /**
     * 퇴근 여부 다이얼로그
     */
    private void setDialogWorkEnd() {
    }

    //=========================================
    // Broadcast
    //=========================================
    private BroadcastReceiver mBroadcastLogout = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ClsBroadCast.BROAD_CAST_ACTION_LOGOUT)) {
                goLogin();
                finish();
            }
        }
    };

    //=========================================
    // Initialize
    //=========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        initLayout();

        initialize();

        testLocation();
    }

    private void testLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsLocationListener);
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


    public void clearAppCache(File dir) {
        if (dir == null)
            dir = getCacheDir();
        if (dir == null)
            return;

        File[] caches = dir.listFiles();

        for (File aCache : caches) {
            if (aCache.isDirectory()) {
                clearAppCache(aCache);
            } else {
                aCache.delete();
            }
        }
    }

    //=============================
    // Event
    //=============================
    /**
     * 백키 두번 연속 누름을 체크하기 위해 선언
     */
    protected long lBackPressedTime;

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();

        if ((now - lBackPressedTime) < 2 * 1000) {
            unregisterReceiver();

            clearAppCache(null);

            this.finishAffinity();

            this.overridePendingTransition(0, 0);
        } else {
            lBackPressedTime = now;
            Toast.makeText(getBaseContext(), (String) getText(R.string.main_0),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

}
