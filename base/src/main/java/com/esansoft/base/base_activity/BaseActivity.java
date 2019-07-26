package com.esansoft.base.base_activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.esansoft.base.R;
import com.esansoft.base.base_fragment.BaseFragment;
import com.esansoft.base.settings.InterfaceSettings;
import com.esansoft.base.user_interface.InterfaceUser;
import com.esansoft.base.util.BaseLoadingBar;
import com.esansoft.base.util.ClsUtil;

public abstract class BaseActivity extends FragmentActivity {
    public static Context BaseContext;
    protected Context mContext;
    protected Activity mActivity;
    protected InterfaceSettings mSettings;
    protected InterfaceUser mUser;

    private BaseLoadingBar mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
        BaseContext = this;
        mContext = this;
        mActivity = this;
        mSettings = InterfaceSettings.getInstance(this);
        mUser = InterfaceUser.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mUser == null || mUser.Value == null || mUser.Value.CDO_ID == null || mUser.Value.CDO_ID.isEmpty()) {
            if (!getClass().getSimpleName().equals("Intro") && !getClass().getSimpleName().equals("Login") && !getClass().getSimpleName().equals("SignUp"))
                ClsUtil.forceRestartAppforActivity(mActivity);
        }
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

    protected BaseFragment.CallLoadingBar callLoadingBar = new BaseFragment.CallLoadingBar() {
        @Override
        public void callOpenLoadingBar() {
            openLoadingBar();
        }

        @Override
        public void callCloseLoadingBar() {
            closeLoadingBar();
        }
    };
}
