package com.esansoft.base.base_activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.esansoft.base.R;
import com.esansoft.base.util.BaseLoadingBar;

public abstract class BaseActivity extends FragmentActivity {
    public static Context BaseContext;
    protected Context mContext;
    protected Activity mActivity;

    private BaseLoadingBar mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
        BaseContext = this;
        mContext = this;
        mActivity = this;
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
    }

    /**
     * 레이아웃 초기화
     */
    protected abstract void initLayout();

    /**
     * 데이터 초기화
     */
    protected abstract void initialize();

    protected void openLoadingBar() {
        if (mLoadingBar == null)
            mLoadingBar = new BaseLoadingBar();

        mLoadingBar.show();
    }

    protected void closeLoadingBar() {
        if (mLoadingBar != null) {
            mLoadingBar.dismiss();
        }
    }
}
