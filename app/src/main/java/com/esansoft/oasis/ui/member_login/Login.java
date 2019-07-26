package com.esansoft.oasis.ui.member_login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.base.network.ClsNetworkCheck;
import com.esansoft.base.settings.SettingsKey;
import com.esansoft.base.util.BaseAlert;
import com.esansoft.oasis.R;
import com.esansoft.oasis.model.LoginModel;
import com.esansoft.oasis.network.BaseConst;
import com.esansoft.oasis.network.Http;
import com.esansoft.oasis.network.HttpBaseService;
import com.esansoft.oasis.ui.main.Main;
import com.esansoft.oasis.ui.member_sign_up.SignUp;
import com.esansoft.oasis.ui.scanner.ScanBarcode;
import com.esansoft.oasis.value_object.EmployeeVO;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends BaseActivity {
    //========================================
    // Layout
    //========================================
    private EditText etAccount;
    private EditText etPassword;
    private TextView tvSignUp;
    private TextView tvFindIdPassword;
    private Switch switchAutoLogin;
    private Button btnLogin;

    //========================================
    // Initialize
    //========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLayout();

        initialize();
    }

    @Override
    protected void initialize() {
        if (mSettings.Value.AutoLogin) {
            etAccount.setText(mSettings.Value.LoginID);
            etPassword.setText(mSettings.Value.LoginPW);
            btnLogin.performClick();
        }
    }

    @Override
    protected void initLayout() {
        etAccount = findViewById(R.id.etAccount);
        etAccount.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        InputFilter[] filter = new InputFilter[1];
        filter[0] = new InputFilter.LengthFilter(50);
        etAccount.setFilters(filter);
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkLoginButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPassword = findViewById(R.id.etPassword);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkLoginButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> goSignUp());

        tvFindIdPassword = findViewById(R.id.tvFindIdPassword);
        tvFindIdPassword.setOnClickListener(v -> goScan());

        switchAutoLogin = findViewById(R.id.switchAutoLogin);
        switchAutoLogin.setChecked(mSettings.Value.AutoLogin);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setEnabled(false);
        btnLogin.setOnClickListener(v -> requestEMPVIEW());
    }

    /**
     * 로그인 버튼 활성화 체크
     */
    private void checkLoginButton() {
        if (etAccount.getText().toString().trim().length() > 0 &&
                etPassword.getText().toString().trim().length() > 0) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }

    /**
     * 로그인
     */
    private void requestEMPVIEW() {
        if (!validateEmail()) {
            etAccount.requestFocus();
            BaseAlert.show(getString(R.string.login_0));
            return;
        }

        // 인터넷 연결 여부 확인
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(getString(R.string.common_network_error));
            return;
        }

        String strEmail = etAccount.getText().toString();
        String strPassword = etPassword.getText().toString();

        if (switchAutoLogin.isChecked()) {
            mSettings.Value.AutoLogin = true;
            mSettings.putBooleanItem(SettingsKey.AutoLogin, mSettings.Value.AutoLogin);

            mSettings.Value.LoginID = strEmail;
            mSettings.putStringItem(SettingsKey.LoginID, mSettings.Value.LoginID);

            mSettings.Value.LoginPW = strPassword;
            mSettings.putStringItem(SettingsKey.LoginPW, mSettings.Value.LoginPW);
        } else {
            mSettings.Value.AutoLogin = false;
            mSettings.putBooleanItem(SettingsKey.AutoLogin, mSettings.Value.AutoLogin);

            mSettings.Value.LoginID = "";
            mSettings.putStringItem(SettingsKey.LoginID, mSettings.Value.LoginID);

            mSettings.Value.LoginPW = "";
            mSettings.putStringItem(SettingsKey.LoginPW, mSettings.Value.LoginPW);
        }

        // API 호출 시 로딩바
        openLoadingBar();

        // API URL및 Param 설정
        // 여기서 <SampleModel> 은 받을 데이터의 형태(Json format)
        Call<LoginModel> call = Http.member(HttpBaseService.TYPE.POST).login(
                BaseConst.URL_HOST,
                "LOGIN",
                "2",
                strPassword,
                strEmail
        );

        // Api 호출 후 response 받을 위치
        call.enqueue(new Callback<LoginModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
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

                            Response<LoginModel> response = (Response<LoginModel>) msg.obj;

                            if (response.body().Data.get(0).Validation) {
                                // UserInterface 연결
                                setSesstion(response.body().Data.get(0));

                                goMain();
                            } else {
                                // ErrorMsg
                                BaseAlert.show(getString(R.string.login_1));
                            }
                        }
                    }
                }.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Log.d("Test", t.getMessage());
                closeLoadingBar();
            }
        });
    }

    /**
     * @param employeeVO
     */
    private void setSesstion(EmployeeVO employeeVO) {
        mUser.Value.CDO_01 = employeeVO.CDO_01;
        mUser.Value.CDO_02 = employeeVO.CDO_02;
        mUser.Value.CDO_04 = employeeVO.CDO_04;
        mUser.Value.CDO_12 = employeeVO.CDO_12;
        mUser.Value.CDO_20 = employeeVO.CDO_20;
        mUser.Value.CDO_23 = employeeVO.CDO_23;
        mUser.Value.CDO_ID = employeeVO.CDO_ID;
    }

    /**
     * 이메일 체크
     *
     * @return
     */
    private boolean validateEmail() {
        String email = etAccount.getText().toString();

        boolean isValid = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());

        return isValid;
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
