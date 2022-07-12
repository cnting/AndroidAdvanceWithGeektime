package com.sample.battery.hook;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by cnting on 2022/7/12
 */
public abstract class ProxyHook extends Hook implements InvocationHandler {

    /**
     * 要代理的真实对象
     */
    private Object proxyObj;

    public ProxyHook(Context context) {
        super(context);
    }

    public void setProxyObj(Object proxyObj) {
        this.proxyObj = proxyObj;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Log.d("===>","invoke method:"+method.getName());
        HookMethodHandler hookMethodHandler = baseHookHandle.getHookedMethodHandler(method);
        if (hookMethodHandler != null) {
            return hookMethodHandler.doHookInner(proxyObj, method, args);
        }
        return method.invoke(proxyObj, args);
    }
}
