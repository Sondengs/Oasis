package com.esansoft.oasis.ui.member_login;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.base.network.ClsNetworkCheck;
import com.esansoft.base.util.BaseAlert;
import com.esansoft.oasis.R;
import com.esansoft.oasis.model.BaseModel;
import com.esansoft.oasis.model.SampleModel;
import com.esansoft.oasis.network.BaseConst;
import com.esansoft.oasis.network.Http;
import com.esansoft.oasis.network.HttpBaseService;
import com.esansoft.oasis.ui.main.Main;
import com.esansoft.oasis.ui.member_sign_up.SignUp;
import com.esansoft.oasis.ui.scanner.ScanBarcode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends BaseActivity {
    //========================================
    // Layout
    //========================================
    private EditText etAccount;
    private TextView tvSignUp;
    private TextView tvFindIdPassword;
    private Button btnLogin;

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
        etAccount.setText(mSettings.Value.LoginID);
    }

    @Override
    protected void initLayout() {
        etAccount = findViewById(R.id.etAccount);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> goSignUp());

        tvFindIdPassword = findViewById(R.id.tvFindIdPassword);
        tvFindIdPassword.setOnClickListener(v -> goScan());

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEMPVIEW();
            }
        });
    }

    /**
     * 로그인
     */
    private void requestEMPVIEW() {
        // 인터넷 연결 여부 확인
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show("Check internet connection\nthen try again.");
            return;
        }

        // API 호출 시 로딩바
        openLoadingBar();

        // API URL및 Param 설정
        // 여기서 <SampleModel> 은 받을 데이터의 형태(Json format)
        Call<SampleModel> call = Http.member(HttpBaseService.TYPE.POST).login(
                BaseConst.URL_HOST,
                "LOGIN",
                "2",
                "1234",
                "test@esansoft.co.kr"
        );

        // Api 호출 후 response 받을 위치
        call.enqueue(new Callback<SampleModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<SampleModel> call, Response<SampleModel> response) {
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

                            // SampleModel 형태로 response를 받음
                            Response<SampleModel> response = (Response<SampleModel>) msg.obj;

                            // 원래 IsSuccess값을 보고 API 호출이 정상적으로 이뤄졌는지 판단
//                            boolean success = response.body().IsSuccess;
                            // API 호출이 제대로 안된 경우 ErrorMsg를 보고 판단
//                            String message = response.body().ErrorMsg;

                            BaseAlert.show("CDO_01 :" + response.body().Data.get(0).CDO_01 + "\n" +
                                    "CDO_02 :" + response.body().Data.get(0).CDO_02 + "\n" +
                                    "CDO_04 :" + response.body().Data.get(0).CDO_04 + "\n" +
                                    "CDO_12 :" + response.body().Data.get(0).CDO_12 + "\n" +
                                    "CDO_17 :" + response.body().Data.get(0).CDO_17 + "\n" +
                                    "CDO_18 :" + response.body().Data.get(0).CDO_18 + "\n" +
                                    "CDO_19 :" + response.body().Data.get(0).CDO_19 + "\n" +
                                    "CDO_20 :" + response.body().Data.get(0).CDO_20 + "\n" +
                                    "CDO_23 :" + response.body().Data.get(0).CDO_23, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    goMain();
                                }
                            });
                        }
                    }
                }.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<SampleModel> call, Throwable t) {
                Log.d("Test", t.getMessage());
                closeLoadingBar();
            }
        });
    }

    /**
     * 메인으로 이동한다.
     */
    private void goMain() {
        Intent intent = new Intent(mContext, Main.class);
        mContext.startActivity(intent);
        finish();
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

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            // QR 코드/ 바코드를 스캔한 결과
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            // result.getFormatName() : 바코드 종류
            // result.getContents() : 바코드 값
            Log.d("Test", "Scan Type : " + result.getFormatName() + " / Data : " + result.getContents());
        }
    }
}
