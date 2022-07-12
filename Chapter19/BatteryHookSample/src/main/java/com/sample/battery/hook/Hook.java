package com.sample.battery.hook;

import android.content.Context;

/**
 * Created by cnting on 2022/7/12
 */
public abstract class Hook {
    protected Context context;
    protected BaseHookHandle baseHookHandle;

    public Hook(Context context) {
        this.context = context;
        baseHookHandle = createBaseHookHandle();
    }

    public abstract BaseHookHandle createBaseHookHandle();

    public abstract void onInstall();
}
