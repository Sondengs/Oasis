package com.esansoft.oasis.ui.member_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.oasis.R;
import com.esansoft.oasis.ui.scanner.ScanBarcode;
import com.esansoft.oasis.ui.member_sign_up.SignUp;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Login extends BaseActivity {
    //========================================
    // Layout
    //========================================
    private TextView tvSignUp;
    private TextView tvFindIdPassword;


    //========================================
    // Initialize
    //========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLayout();
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void initLayout() {
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> goSignUp());

        tvFindIdPassword = findViewById(R.id.tvFindIdPassword);
        tvFindIdPassword.setOnClickListener(v -> goScan());
    }


    //========================
    // Methods
    //========================

    /**
     * 스캔화면으로 이동한다.
     */
    private void goScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScanBarcode.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    /**
     * 회원가입 화면으로 이동한다.
     */
    private void goSignUp() {
        Intent intent = new Intent(mActivity, SignUp.class);
        mActivity.startActivity(intent);
    }

    //========================
    // Events
    //========================
    /**
     * 앱종료 시
     */
    private long finalBackTime;

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();

        if ((now - finalBackTime) < 2000) {
            this.finishAffinity();
        } else {
            finalBackTime = now;
            Toast.makeText(this, "이전 버튼을 한번더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            // QR 코드/ 바코드를 스캔한 결과
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            // result.getFormatName() : 바코드 종류
            // result.getContents() : 바코드 값
            Log.d("Test", "Scan Type : " + result.getFormatName() + " / Data : " + result.getContents());
        }
    }
}
