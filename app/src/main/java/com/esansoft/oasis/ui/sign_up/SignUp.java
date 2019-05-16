package com.esansoft.oasis.ui.sign_up;

import android.os.Bundle;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.base.base_header.BaseHeader;
import com.esansoft.oasis.R;

public class SignUp extends BaseActivity {
    //========================================
    // Layout
    //========================================
    private BaseHeader header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initLayout();
    }

    private void initLayout() {
        header = findViewById(R.id.header);
        header.btnHeaderLeft.setOnClickListener(v -> {
            finish();
        });
    }
}
