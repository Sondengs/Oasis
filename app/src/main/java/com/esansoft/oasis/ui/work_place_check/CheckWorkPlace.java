package com.esansoft.oasis.ui.work_place_check;

import android.os.Bundle;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.base.base_header.BaseHeader;
import com.esansoft.oasis.R;

public class CheckWorkPlace extends BaseActivity {
    public static final int REQUEST_CODE = 1553;

    //===================================
    // Layout
    //===================================
    private BaseHeader header;

    //===================================
    // Variable
    //===================================


    //===================================
    // Initialize
    //===================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_work_place_check);

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        header = findViewById(R.id.header);
        header.btnHeaderLeft.setOnClickListener(v -> finish());


    }

    @Override
    protected void initialize() {

    }
}
