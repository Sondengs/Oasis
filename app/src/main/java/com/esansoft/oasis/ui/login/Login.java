package com.esansoft.oasis.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.oasis.R;
import com.esansoft.oasis.ui.sign_up.SignUp;

public class Login extends BaseActivity {
    //========================================
    // Layout
    //========================================
    private TextView tvSignUp;

    //========================================
    // Initialize
    //========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLayout();
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignUp();
            }
        });
    }

    /**
     * 회원가입 화면으로 이동한다.
     */
    private void goSignUp() {
        Intent intent = new Intent(mActivity, SignUp.class);
        mActivity.startActivity(intent);
    }

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
}
