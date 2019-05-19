package com.esansoft.base;

import android.app.Application;
import android.content.Context;

public class OasisApp extends Application {

    public static Context BaseContext;

    @Override
    public void onCreate() {
        super.onCreate();

        BaseContext = this;
    }
}
