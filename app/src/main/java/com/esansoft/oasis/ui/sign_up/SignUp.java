package com.esansoft.oasis.ui.sign_up;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.base.base_header.BaseHeader;
import com.esansoft.base.util.BaseAlert;
import com.esansoft.oasis.R;
import com.esansoft.oasis.ui.main.Main;

import java.util.regex.Pattern;

public class SignUp extends BaseActivity {
    //========================================
    // Layout
    //========================================
    private BaseHeader header;

    private EditText etLastName;
    private EditText etFirstName;
    private EditText etMobile;
    private EditText etPassword;
    private EditText etBusinessID;

    private ImageView imgShowPassword;

    private CheckBox ckbAll;
    private CheckBox ckbTerms;
    private CheckBox ckbPrivacy;
    private CheckBox ckbMarketing;

    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initLayout();
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initLayout() {
        header = findViewById(R.id.header);
        header.btnHeaderLeft.setOnClickListener(v -> {
            finish();
        });


        etLastName = findViewById(R.id.etLastName);
        etFirstName = findViewById(R.id.etFirstName);
        etMobile = findViewById(R.id.etMobile);
        etMobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etPassword = findViewById(R.id.etPassword);
        etBusinessID = findViewById(R.id.etBusinessID);

        imgShowPassword = findViewById(R.id.imgShowPassword);
        imgShowPassword.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    etPassword.setInputType(InputType.TYPE_NULL);
                    imgShowPassword.setSelected(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    imgShowPassword.setSelected(false);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    break;
            }

            return false;
        });

        ckbAll = findViewById(R.id.ckbAll);
        ckbAll.setOnClickListener(v -> setAllCheck(ckbAll.isChecked()));
        ckbTerms = findViewById(R.id.ckbTerms);
        ckbTerms.setOnClickListener(v -> checkAllCheck());
        ckbPrivacy = findViewById(R.id.ckbPrivacy);
        ckbPrivacy.setOnClickListener(v -> checkAllCheck());
        ckbMarketing = findViewById(R.id.ckbMarketing);
        ckbMarketing.setOnClickListener(v -> checkAllCheck());

        btnNext = findViewById(R.id.btnNext);
        btnNext.setEnabled(false);
        btnNext.setOnClickListener(v -> validate());
    }

    /**
     * 휴대폰 유효성 체크
     */
    private void validate() {
        // 성 체크
        if (!isEmptyItem(etLastName, "성을 입력하세요."))
            return;

        // 이름 체크
        if (!isEmptyItem(etFirstName, "이름을 입력하세요."))
            return;

        // 휴대폰 번호 체크
        if (!isEmptyItem(etMobile, "휴대폰 번호를 입력하세요."))
            return;

        String strMobile = etMobile.getText().toString().trim();

        if (strMobile.indexOf("010") != 0) {
            etMobile.requestFocus();
            BaseAlert.show("휴대폰 번호를 확인하세요.");
            return;
        }


        // 비밀 번호 체크
        if (!isEmptyItem(etPassword, "비밀 번호를 입력하세요."))
            return;

        String strPassword = etPassword.getText().toString().trim();

        if (!isValidPassword(strPassword)) {
            etPassword.requestFocus();
            BaseAlert.show("영문, 숫자를 포함한 7-30자 입력");
            return;
        }

        requestSignUp();
    }

    /**
     * 비밀번호 유효성 체크
     *
     * @param password
     * @return
     */
    private boolean isValidPassword(String password) {
        int nPasswordPatternCount = 0;

        // 정규 표현식 소문자 영어가 사용되었는지
        boolean bUseAlpha = Pattern.matches(".*[a-zA-Z].*", password);

        // 정규 표현식 숫자가 사용되었는지
        boolean bUseNumber = Pattern.matches(".*[0-9].*", password);

        if (bUseAlpha)
            nPasswordPatternCount++;

        if (bUseNumber)
            nPasswordPatternCount++;

        if (nPasswordPatternCount < 2 || password.length() < 7)
            return false;

        return true;
    }

    /**
     * 회원가입 요청
     */
    private void requestSignUp() {

        openLoadingBar();

        new Handler().postDelayed(() -> {
            closeLoadingBar();

            BaseAlert.show("가입되었습니다.", (dialog, which) -> {
                goMain();
            });
        }, 2000);

    }

    /**
     * 메인화면으로 이동한다.
     */
    private void goMain() {
        Intent intent = new Intent(mContext, Main.class);
        mContext.startActivity(intent);
        finish();
    }

    /**
     * 체크 에디트박스
     *
     * @param editText
     * @param message
     */
    private boolean isEmptyItem(EditText editText, String message) {
        String strEmpty = editText.getText().toString().trim();

        if (strEmpty.isEmpty()) {
            BaseAlert.show(message);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            return false;
        }

        return true;
    }

    /**
     * 체크박스 확인
     */
    private void checkAllCheck() {
        if (ckbTerms.isChecked() && ckbPrivacy.isChecked() && ckbMarketing.isChecked())
            ckbAll.setChecked(true);
        else
            ckbAll.setChecked(false);

        if (ckbTerms.isChecked() && ckbPrivacy.isChecked())
            btnNext.setEnabled(true);
        else
            btnNext.setEnabled(false);
    }

    /**
     * 전체 체크
     *
     * @param checked
     */
    private void setAllCheck(boolean checked) {
        ckbTerms.setChecked(checked);
        ckbPrivacy.setChecked(checked);
        ckbMarketing.setChecked(checked);

        checkAllCheck();
    }

    @Override
    protected void initialize() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (etLastName.isFocused()) {
                mgr.hideSoftInputFromWindow(etLastName.getWindowToken(), 0);
                etLastName.clearFocus();
            } else if (etFirstName.isFocused()) {
                mgr.hideSoftInputFromWindow(etFirstName.getWindowToken(), 0);
                etFirstName.clearFocus();
            } else if (etMobile.isFocused()) {
                mgr.hideSoftInputFromWindow(etMobile.getWindowToken(), 0);
                etMobile.clearFocus();
            } else if (etPassword.isFocused()) {
                mgr.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                etPassword.clearFocus();
            } else if (etBusinessID.isFocused()) {
                mgr.hideSoftInputFromWindow(etBusinessID.getWindowToken(), 0);
                etBusinessID.clearFocus();
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}
