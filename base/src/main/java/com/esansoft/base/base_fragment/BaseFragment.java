package com.esansoft.base.base_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    protected Context mContext;
    protected Activity mActivity;

    public BaseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        mActivity = getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mContext = context;
            mActivity = (Activity) context;
        }
    }

    //=========================
    // 로딩바 구현
    //=========================
    public interface CallLoadingDialog {
        void openLoadingDialog();

        void closeLoadingDialog();
    }

    // Loading Callback
    protected CallLoadingDialog callLoadingDialog;

    protected void loadingDialogOpen() {
        if (callLoadingDialog != null)
            callLoadingDialog.openLoadingDialog();
    }

    protected void loadingDialogClose() {
        if (callLoadingDialog != null)
            callLoadingDialog.closeLoadingDialog();
    }
}
