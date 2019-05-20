package com.esansoft.oasis.ui.find_work_place;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.oasis.R;

import java.util.ArrayList;

public class FindWorkPlace extends BaseActivity {
    public static final int REQUEST_CODE = 1553;

    //===================================
    // Layout
    //===================================
    private Spinner spinnerCity;
    private Spinner spinnerStreet;

    //===================================
    // Variable
    //===================================
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> listCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_work_place);

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        spinnerCity = findViewById(R.id.spinnerCity);

    }

    @Override
    protected void initialize() {
    }
}
